package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.FinishedTrade;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.base.BaseTradeWorker;
import com.samruddhi.trading.equities.logic.base.StockOrderProcessor;
import com.samruddhi.trading.equities.orderlimits.OptionTickerProvider;
import com.samruddhi.trading.equities.quartz.ConcurrentCompletedTradeQueue;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import com.samruddhi.trading.equities.studies.EMACalculator;
import com.samruddhi.trading.equities.studies.MACDCalculator;
import com.samruddhi.trading.equities.studies.RSICalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.*;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;

/**
 * For a given Ticker this would trade using plain stocks
 */
public class StockTradeWorker extends BaseTradeWorker {

    private static final Logger logger = LoggerFactory.getLogger(StockTradeWorker.class);

    // We allow upto 3 exceptions per Ticker in the TradeWorker
    private final int MAX_ALLOWED_EXCEPTION_COUNT = 3;
    private int currentExceptionCount = 0;

    private boolean isTerminated = false;
    private MarketDataService marketDataService;
    /**
     * This ticker is injected to the Constructor,  TradeWorker can only work with one Stock like AAPL at a time
     */
    private String ticker;

    /**
     * just the last minute's EMAs
     */
    private PreviousEmas previousEmas;

    /**
     * last to last minutes EMAs
     */
    private PreviousEmas previousTwoMinuteAgoEmas;


    /**
     * Details of order fill status that came from ORDER
     */
    private OrderFillStatus recentBuyFillStatus;

    /**
     * Keeps track of all Finished trades in this worker and also worker status
     */
    private TradeWorkerStatus tradeWorkerStatus;

    private StockOrderProcessor stockOrderProcessor;

    private ConcurrentCompletedTradeQueue concurrentCompletedTradeQueue;

    public StockTradeWorker(MarketDataService marketDataService, String ticker) {
        this.currentStatus = CurrentStatus.NO_STATUS;
        this.marketDataService = marketDataService;
        this.tradeWorkerStatus = new TradeWorkerStatus("");
        this.previousEmas = new PreviousEmas(0, 0, 0);
        this.previousTwoMinuteAgoEmas = new PreviousEmas(0, 0, 0);
        this.ticker = ticker;
        this.stockOrderProcessor = new StockOrderProcessorImpl(marketDataService);
        this.concurrentCompletedTradeQueue = ConcurrentCompletedTradeQueue.getInstance();
        this.stockMarketCloseTimeChecker = stockMarketCloseTimeChecker;
    }

    public void triggerTermination() {
        this.isTerminated = true;
    }

    @Override
    public TradeWorkerStatus call() throws Exception {
        // this will run whole day
        while (!isTerminated) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // get  daily stock data for past 50 days
                    List<Bar> pastMinuteBars = marketDataService.getStockDataBars(ticker, "Minute", 1, 50);
                    logger.info("Ticker {} Size of pastMinuteBars {}", ticker, pastMinuteBars.size());


                    switch (currentStatus) {
                        case NO_STATUS, UPTREND -> {
                            if (!maxPnlLossExceededPerDay())
                                checkAndPlaceStockBuyOrder(pastMinuteBars);
                        }
                        case STOCKS_HELD -> {
                            checkAndPlaceStockSellPoint(pastMinuteBars, pastMinuteBars);
                            if (maxPnlLossExceededPerDay()) {
                                isTerminated = true;
                            }
                        }
                    }

                    // terminating both while loops as our Daily MAX PNL loss exceeded
                    if (isTerminated)
                        break;

                    // Sleep for 1 or 1/2 minute, some import issue in J 21 with TimeUnit.MILLISECONDS.sleep
                    doSleep();
                } catch (InterruptedException e) {
                    logger.info("Task interrupted.");
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    currentExceptionCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Error during task execution: Perhaps we can see if things improve next minute" + e.getMessage());
                    currentExceptionCount++;
                    // Optional: Decide if you want to interrupt the thread and stop the task
                }
            }
            // Well if we get repetaed exceptions then we terminate this Thread and Ticker for the day!!!
            if (currentExceptionCount > MAX_ALLOWED_EXCEPTION_COUNT) {
                logger.info("Terminating the StockTradeWorker functioning for the Day {} due to repeated {} errors", ticker, currentExceptionCount);
                isTerminated = true;
            }
        }
        return null;
    }

    private void checkAndPlaceStockBuyOrder(List<Bar> allMinuteBars) throws Exception {

        double ema5 = EMACalculator.calculateEMAs(allMinuteBars, 5);
        double ema13 = EMACalculator.calculateEMAs(allMinuteBars, 13);
        double ema50 = EMACalculator.calculateEMAs(allMinuteBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = allMinuteBars.subList(24, allMinuteBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP,
        double rsi = RSICalculator.calculateRSI(allMinuteBars.subList(36, allMinuteBars.size()), 14);
        boolean isRsiBullish = rsi > 40; // Fix me

        OrderFillStatus orderFillStatus = null;
        if ((ema13 > ema50 || ema5 > ema50) && ema5 > ema13 && previousEmas != null) {
            // Probable Buy call scenario
            currentStatus = CurrentStatus.UPTREND;

            // Initiate an Option buy order if all criteria met
            if (!isStockBuyBasedOnPreviousEmas() && isMacdBullish && isRsiBullish) {
                orderFillStatus = initiateStockBuying(ticker, allMinuteBars.get(allMinuteBars.size() - 1).getClose());
                saveBuyStatus(orderFillStatus);
            }
        } else {
            logger.info("StockTradeWorker - Currently no uptrend detected {} {} {} ", ema5, ema13, ema50);
        }
        previousTwoMinuteAgoEmas = previousEmas;
        previousEmas = new PreviousEmas(ema5, ema13, ema50);
    }

    /**
     * checks If it was not a CALL buy signal in the past 2 minutes
     * check if first time 5 crossing above 13 and 50 DAY EMAs, if it was buyable last minute (0r last minus 1)  we don't want to Buy now as
     * we may be a bit late
     */
    private boolean isStockBuyBasedOnPreviousEmas() {
        if (previousEmas.ema13day == 0 || previousTwoMinuteAgoEmas.ema13day == 0) {
            // very beginning pf trading day, we ignore previous EMA check
            return false;
        } else {
            boolean isCallBuyPerPreviousEma = (previousEmas.ema13day > previousEmas.ema50day || previousEmas.ema5day > previousEmas.ema50day) && previousEmas.ema5day > previousEmas.ema13day;
            boolean isCallBuyPerPreTwoMinuteEma = (previousTwoMinuteAgoEmas.ema13day > previousTwoMinuteAgoEmas.ema50day || previousTwoMinuteAgoEmas.ema5day > previousTwoMinuteAgoEmas.ema50day) && previousTwoMinuteAgoEmas.ema5day > previousTwoMinuteAgoEmas.ema13day;

            // We need  a XOR as both should not be true ie shd not be a buy past 2 mins
            return isCallBuyPerPreviousEma ^ isCallBuyPerPreTwoMinuteEma;
        }
    }

    /**
     * checks If it was not a PUT buy signal in the past 2 minutes
     */
    private boolean isPutBuyBasedOnPreviousEmas() {
        if (previousEmas.ema13day == 0 || previousTwoMinuteAgoEmas.ema13day == 0) {
            // very beginning pf trading day, we ignore previous EMA check
            return false;
        } else {
            boolean isPastMinuteAPutBuy = (previousEmas.ema13day < previousEmas.ema50day || previousEmas.ema5day < previousEmas.ema50day) && previousEmas.ema5day < previousEmas.ema13day;
            boolean isPastTwoMinuteAPutBuy = (previousTwoMinuteAgoEmas.ema13day < previousTwoMinuteAgoEmas.ema50day || previousTwoMinuteAgoEmas.ema5day < previousTwoMinuteAgoEmas.ema50day) && previousTwoMinuteAgoEmas.ema5day < previousTwoMinuteAgoEmas.ema13day;

            // We need  a XOR as both should not be true ie shd not be a buy past 2 mins
            return isPastMinuteAPutBuy ^ isPastTwoMinuteAPutBuy;
        }
    }

    /**
     * Save status to indicate that something has been bought (call or put)
     */
    private void saveBuyStatus(OrderFillStatus orderFillStatus) throws Exception {
        if (orderFillStatus.getStatus() != ORDER_STATUS_FILLED) {
            currentStatus = CurrentStatus.STOCKS_HELD;
            recentBuyFillStatus = orderFillStatus;
        } else if (orderFillStatus.getStatus() != ORDER_STATUS_OPEN) {
            stockOrderProcessor.cancelOrder(orderFillStatus.getOrderId());
            currentStatus = CurrentStatus.NO_STATUS;
        } else if (orderFillStatus.getStatus() != ORDER_STATUS_FAILED) {
            logger.error("We have a failed buy Order {}, resetting CurrentStatus to NO_STATUS");
            currentStatus = CurrentStatus.NO_STATUS;
        }
    }

    /**
     * 1) determine option ticker
     * 2) get Bid / Ask and limit we want to place
     * 3) Place Order for desired Option quantity (say 2 to 5)
     * 4) track status
     *
     * @param ticker = Underlying ticker not Option ticker with strike
     */
    OrderFillStatus initiateStockBuying(String ticker, double price) throws Exception {
        OrderFillStatus orderFillStatus = ORDER_FILL_STATUS_FAILED;
        try {
            orderFillStatus = stockOrderProcessor.createStockBuyOrder(ticker, price);
        } catch (Exception e) {
            logger.error("Error in initiateStockBuying {}", e.getMessage());
            throw e;
        }
        return orderFillStatus;
    }

    OrderFillStatus initiateStockSelling(String ticker, double price) throws Exception {
        OrderFillStatus orderFillStatus = ORDER_FILL_STATUS_FAILED;
        try {
            orderFillStatus = stockOrderProcessor.createStockSellOrder(ticker, price);
        } catch (Exception e) {
            logger.error("Error in initiateStockSelling {}", e.getMessage());
            throw e;
        }
        return orderFillStatus;
    }

    /**
     * This when we already hold a call and need to sell it as trend is reversing or we need to trim due to meeting target price
     * Calls held will be sold if conditions are met.
     *
     * @param minuteBars
     * @param pastMinuteBars
     */
    private void checkAndPlaceStockSellPoint(List<Bar> minuteBars, List<Bar> pastMinuteBars) throws Exception {

        StockSellPointHelper stockSellPointHelper = new StockSellPointHelper(ticker);
        // Price drop or time past 3.45 earing stock market close for the day, needs Selling.
        boolean isStockSellPointReached = stockSellPointHelper.determineIfStockOrCallSellCriteriaMet(recentBuyFillStatus, pastMinuteBars) || stockMarketCloseTimeChecker.isCloseToMarketCloseTime();

        if (isStockSellPointReached) {
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, pastMinuteBars.get(pastMinuteBars.size() - 1).getClose(), 'C');
            // CHeck here and other places,  if using Daily bars last price is ok or we need to get price from latest minute bar...
            OrderFillStatus orderFillStatus = initiateStockSelling(ticker, pastMinuteBars.get(minuteBars.size() - 1).getClose());
            // Store daily completed transactions in TradeWorkerStatus
            if (orderFillStatus.getStatus() == ORDER_STATUS_OPEN)
                orderFillStatus = repeatUntilSold(orderFillStatus.getOrderId(), nextStrikePrice, ticker, pastMinuteBars.get(pastMinuteBars.size() - 1).getClose());

            // Append internal DS that holds finished trades for the Day
            storeFinishedTrade(orderFillStatus);
            currentStatus = CurrentStatus.NO_STATUS;
        }
    }

    /**
     * Repeat until the current Option ( can be either CALL or PUT) is Sold
     */
    private OrderFillStatus repeatUntilSold(String orderId, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        return stockOrderProcessor.replaceStockSellOrder(orderId, ticker);
    }


    //public FinishedTrade(String ticker, double buyPrice, double sellPrice, LocalTime entry, LocalTime exit, long quantity, double profitOrLoss) {
    private void storeFinishedTrade(OrderFillStatus sellFillStatus) {
        double profitOrLoss = calculateProfit(sellFillStatus); // calculate profit or loss
        FinishedTrade finishedTrade = new FinishedTrade(sellFillStatus.getTicker(), recentBuyFillStatus.getFillPrice(), sellFillStatus.getFillPrice(),
                recentBuyFillStatus.getExecutionTime(), sellFillStatus.getExecutionTime(), sellFillStatus.getFillQuantity(), profitOrLoss);
        tradeWorkerStatus.addFinishedTrade(finishedTrade);

        concurrentCompletedTradeQueue.addToQueue(finishedTrade.toString());
        inMemoryPnlTracker.updateTotalPNL((long) profitOrLoss);
    }

    private double calculateProfit(OrderFillStatus sellFillStatus) {
        if (recentBuyFillStatus.getFillPrice() < sellFillStatus.getFillPrice()) {
            // We made a Profit
            return sellFillStatus.getFillPrice() - recentBuyFillStatus.getFillPrice() * recentBuyFillStatus.getFillQuantity();
        } else {
            // We made a Loss
            return sellFillStatus.getFillPrice() - recentBuyFillStatus.getFillPrice() * recentBuyFillStatus.getFillQuantity();
        }
    }
}
