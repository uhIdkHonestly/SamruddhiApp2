package com.samruddhi.trading.equities.logic;

//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_MINUTE;
//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_DAILY;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.FinishedTrade;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.orderlimits.OptionTickerProvider;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import com.samruddhi.trading.equities.studies.EMACalculator;
import com.samruddhi.trading.equities.studies.MACDCalculator;
import com.samruddhi.trading.equities.studies.RSICalculator;

import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.*;


public class TradeWorker implements Callable<TradeWorkerStatus> {

    private static int PER_INTERVAL_SLEEP_TIME = 100; // In milli seconds
    // We allow upto 3 exceptions per Ticker in the TradeWorker
    private final int MAX_ALLOWED_EXCEPTION_COUNT = 3;
    private int currentExceptionCount = 0;

    private enum CurrentStatus {
        UPTREND, DOWNTREND, CALL_HELD, PUT_HELD, NO_STATUS;
    }

    private static final Logger logger = LoggerFactory.getLogger(com.samruddhi.trading.equities.logic.TradeWorker.class);

    private boolean isTerminated = false;
    private MarketDataService marketDataService;
    /** This ticker is injected to the COnstructor,  TradeWorker can only work with one Stock like AAPL at a time */

    private String ticker;
    /**
     * Holds all the Bars since a buy or Call or Put is initiated until Sold
     */
    private List<Bar> barsSincePurchase;

    /**
     * Holds all the Bars in the last 3 intervals (3 minutes) to see a green uptrend or a red downtrend
     */
    private List<Bar> barsInCurrentTrend;

    /**
     * just the last minute's EMAs
     */
    private PreviousEmas previousEmas;

    /**
     * What's my curent status ie UPTREND, DOWNTREND, CALL_HELD, PUT_HELD, NO_STATUS, storeed for chgecking worker status easily
     */
    private CurrentStatus currentStatus;

    /**
     * Details of order fill status that came from ORDER
     */
    private OrderFillStatus recentBuyFillStatus;

    /**
     * Keeps track of all Finished trades in this worker and also worker status
     */
    private TradeWorkerStatus tradeWorkerStatus;

    private OptionOrderProcessor optionOrderProcessor;

    private StreamingOptionQuoteService streamingOptionQuoteService;

    public TradeWorker(MarketDataService marketDataService, String ticker) {
        this.marketDataService = marketDataService;
        this.barsSincePurchase = new ArrayList<>();
        this.barsInCurrentTrend = new ArrayList<>();
        this.currentStatus = CurrentStatus.NO_STATUS;;
        this.tradeWorkerStatus = new TradeWorkerStatus("");
        this.previousEmas = new PreviousEmas(0, 0, 0);
        this.ticker = ticker;
        this.streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        this.optionOrderProcessor = new OptionOrderProcessorImpl();
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
                    // get  minute stock data , tick - 2 (just last 2) using the stock ticker
                    // marketDataService.getStockDataBars(1, MarketDataServiceImpl.TIME_UNIT_MINUTE, 2);
                    List<Bar> minuteBars = marketDataService.getStockDataBars(ticker, "Minute", 1, 2);

                    // get  daily stock data for past 50 days
                    List<Bar> dailyBars = marketDataService.getStockDataBars(ticker, "Daily", 1, 50);

                    switch (currentStatus) {
                        case NO_STATUS, UPTREND, DOWNTREND -> checkAndPlaceOptionBuyOrder(minuteBars, dailyBars);
                        case CALL_HELD -> checkAndPlaceCallSellPoint(minuteBars, dailyBars);
                        case PUT_HELD -> checkAndPlacePutsSellPoint(minuteBars, dailyBars);
                    }

                    // Sleep for 1/2 minute, some import issue in J 21 with TimeUnit.MILLISECONDS.sleep
                    Thread.sleep(PER_INTERVAL_SLEEP_TIME);
                } catch (InterruptedException e) {
                    logger.info("Task interrupted.");
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    currentExceptionCount++;
                } catch (Exception e) {
                    logger.info("Error during task execution: Perhaps we can see if things improve next minute" + e.getMessage());
                    currentExceptionCount++;
                    // Optional: Decide if you want to interrupt the thread and stop the task
                }
            }
            // Well if we get repetaed exceptions then we terminate this Thread and Ticker for the day!!!
            if(currentExceptionCount > MAX_ALLOWED_EXCEPTION_COUNT) {
                logger.info("Terminating the TradeWorker for {} due to repeated {} errors", ticker, currentExceptionCount);
                isTerminated = true;
            }
        }
        return null;
    }

    private void checkAndPlaceOptionBuyOrder(List<Bar> minuteBars, List<Bar> dailyBars) throws Exception {

        double ema5 = EMACalculator.calculateEMAs(dailyBars, 5);
        double ema13 = EMACalculator.calculateEMAs(dailyBars, 13);
        double ema50 = EMACalculator.calculateEMAs(dailyBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = dailyBars.subList(24, dailyBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP,
        double rsi = RSICalculator.calculateRSI(dailyBars.subList(36, dailyBars.size()), 14);
        boolean isRsiBullish = rsi > 40; // Fix me

        OrderFillStatus orderFillStatus = null;
        if (ema5 > ema50 && ema5 > ema13 && previousEmas != null) {
            // Probable Buy call scenario
            currentStatus = CurrentStatus.UPTREND;
            // check if first time 5 crossing above 13 and 50 DAY EMAs, if it was buyable last minute we don't want to Buy now as we may be bit late
            boolean isPastMinuteACallBuy = previousEmas.ema5day > previousEmas.ema50day && previousEmas.ema5day > previousEmas.ema13day;

            // Initiate an Option buy order if all criteria met
            if (!isPastMinuteACallBuy && isMacdBullish && isRsiBullish) {
                orderFillStatus = initiateCallOrPutBuying(ticker, dailyBars.get(dailyBars.size() - 1).getClose(), 'C');
                saveBuyStatus(orderFillStatus, true);
            }
        } else if (ema5 < ema50 && ema5 < ema13 && previousEmas != null) {
            // Probable Buy put scenario
            currentStatus = CurrentStatus.DOWNTREND;
            // check if first time 5 crossing below 13 and 50 DAY EMAs, if it was buyable last minute we don't want to Sell now as we may be bit late
            boolean isPastPastMinuteAPutBuy = previousEmas.ema5day < previousEmas.ema50day && previousEmas.ema5day < previousEmas.ema13day;
            if (!isPastPastMinuteAPutBuy && !isMacdBullish && !isRsiBullish) {
                //initiatePutBuying(ticker, price);
                orderFillStatus = initiateCallOrPutBuying(ticker, dailyBars.get(dailyBars.size() - 1).getClose(), 'P');
                saveBuyStatus(orderFillStatus, false);
            }
        }
        previousEmas = new PreviousEmas(ema5, ema13, ema50);
    }

    /**
     * Save status to indicate that something has been bought (call or put)
     */
    private void saveBuyStatus(OrderFillStatus orderFillStatus, boolean isCall) throws Exception {
        if (orderFillStatus.getStatus() != ORDER_STATUS_FILLED) {
            if (isCall)
                currentStatus = CurrentStatus.CALL_HELD;
            else
                currentStatus = CurrentStatus.PUT_HELD;

            recentBuyFillStatus = orderFillStatus;
        } else if (orderFillStatus.getStatus() != ORDER_STATUS_OPEN) {
            // TO DO -  optionOrderProcessor.cancelOrder(orderFillStatus.orderId);
            optionOrderProcessor.cancelOrder(orderFillStatus.getOrderId());
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
    private OrderFillStatus initiateCallOrPutBuying(String ticker, double price, char callOrPut) throws Exception {
        OrderFillStatus orderFillStatus = ORDER_FILL_STATUS_FAILED;
        try {
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, price, callOrPut);
            orderFillStatus = switch (callOrPut) {
                case 'C' -> optionOrderProcessor.createCallBuyOrder(nextStrikePrice, ticker, price);
                case 'P' -> optionOrderProcessor.createPutBuyOrder(nextStrikePrice, ticker, price);
                default -> ORDER_FILL_STATUS_FAILED;
            };
        } catch (Exception e) {
            logger.error("Error in initiateCallOrPutBuying {}", e.getMessage());
            throw e;
        }
        return orderFillStatus;
    }

    private OrderFillStatus initiateCallOrPutSelling(NextStrikePrice nextStrikePrice, String ticker, double price, char callOrPut) throws Exception {
        OrderFillStatus orderFillStatus = ORDER_FILL_STATUS_FAILED;
        try {
            orderFillStatus = switch (callOrPut) {
                case 'C' -> optionOrderProcessor.createCallSellOrder(nextStrikePrice, ticker, price);
                case 'P' -> optionOrderProcessor.createPutSellOrder(nextStrikePrice, ticker, price);
                default -> ORDER_FILL_STATUS_FAILED;
            };
        } catch (Exception e) {
            logger.error("Error in initiateCallOrPutBuying {}", e.getMessage());
            throw e;
        }
        return orderFillStatus;
    }

    /**
     * This when we already hold a call and need to sell it as trend is reversing or we need to trim due to meeting target price
     * Calls held will be sold if conditions are met.
     * @param minuteBars
     * @param dailyBars
     */
    private void checkAndPlaceCallSellPoint(List<Bar> minuteBars, List<Bar> dailyBars) throws Exception{

        // TO DO duplicated work fix me
        double ema5 = EMACalculator.calculateEMAs(dailyBars, 5);
        double ema13 = EMACalculator.calculateEMAs(dailyBars, 13);
        double ema50 = EMACalculator.calculateEMAs(dailyBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = dailyBars.subList(24, dailyBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP,
        double rsi = RSICalculator.calculateRSI(dailyBars.subList(36, dailyBars.size()), 14);
        boolean isRsiBullish = rsi > 40; // Fix me

        if ((ema5 < ema50 || ema5 < ema13 && !isMacdBullish) ||
                (TradeWorkerPriceHelper.hasDroppedByGivenPercentage(recentBuyFillStatus, minuteBars.get(minuteBars.size()-1), ConfigManager.getInstance().getAcceptablePriceDropPercent(recentBuyFillStatus.getTicker())))) {
            // TO DO We need to sell this call Asap
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, dailyBars.get(dailyBars.size() - 1).getClose(), 'C');
            // CHeck here and other places,  if using Daily bars last price is ok or we need to get price from latest minute bar...
            OrderFillStatus orderFillStatus = initiateCallOrPutSelling(nextStrikePrice, ticker, dailyBars.get(dailyBars.size() - 1).getClose(), 'C');
            // TO DO Store daily completed transactions in TradeWorkerStatus
            // place a sell order and wait for completion of Order
            // Need to do more work procssing status similar to saveBuyStatus
            if(orderFillStatus.getStatus() != ORDER_STATUS_OPEN) // To DO
                orderFillStatus = repeatUntilSold(orderFillStatus.getOrderId(), nextStrikePrice, ticker, dailyBars.get(dailyBars.size() - 1).getClose() );

            // Append internal DS that holds finished trades for the Day
            storeFinishedTrade(orderFillStatus);
            currentStatus = CurrentStatus.NO_STATUS;
        }
    }

    private OrderFillStatus  repeatUntilSold(String orderId, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        // TO DO
        return optionOrderProcessor.replaceCallSellOrder(orderId, nextStrikePrice, ticker, price);
    }

    private void checkAndPlacePutsSellPoint(List<Bar> minuteBars, List<Bar> dailyBars) {

    }

    //public FinishedTrade(String ticker, double buyPrice, double sellPrice, LocalTime entry, LocalTime exit, long quantity, double profitOrLoss) {
    private void storeFinishedTrade(OrderFillStatus sellFillStatus) {
        FinishedTrade finishedTrade = new FinishedTrade(sellFillStatus.getTicker(), recentBuyFillStatus.getFillPrice(), sellFillStatus.getFillPrice(),
                recentBuyFillStatus.getExecutionTime(), sellFillStatus.getExecutionTime(), sellFillStatus.getFillQuantity(), calculateProfit(sellFillStatus) ); // calculate profit
        tradeWorkerStatus.addFinishedTrade(finishedTrade);
    }

    private double calculateProfit(OrderFillStatus sellFillStatus) {
        if(recentBuyFillStatus.getFillPrice() <  sellFillStatus.getFillPrice()) {
            // We made a Profit
            return sellFillStatus.getFillPrice() - recentBuyFillStatus.getFillPrice() * recentBuyFillStatus.getFillQuantity();
        } else {
            // We made a Loss
            return sellFillStatus.getFillPrice() - recentBuyFillStatus.getFillPrice() * recentBuyFillStatus.getFillQuantity();
        }
    }
}
