package com.samruddhi.trading.equities.logic;

//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_MINUTE;
//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_DAILY;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
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


public class TradeWorker implements Callable<TradeWorkerStatus> {

    private enum CurrentStatus {
        UPTREND, DOWNTREND, CALL_HELD, PUT_HELD, NO_STATUS;
    }

    private static final Logger logger = LoggerFactory.getLogger(com.samruddhi.trading.equities.logic.TradeWorker.class);

    private boolean isTerminated = false;
    private MarketDataService marketDataService;
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

    private CurrentStatus currentStatus;

    private TradeWorkerStatus tradeWorkerStatus;

    private OptionOrderProcessor optionOrderProcessor;

    private StreamingOptionQuoteService streamingOptionQuoteService;

    public TradeWorker(MarketDataService marketDataService, String ticker) {
        this.marketDataService = marketDataService;
        this.barsSincePurchase = new ArrayList<>();
        this.barsInCurrentTrend = new ArrayList<>();
        this.currentStatus = currentStatus;
        this.tradeWorkerStatus = tradeWorkerStatus;
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
                        case NO_STATUS, UPTREND, DOWNTREND -> checkAndPlaceOrder(minuteBars, dailyBars);
                        case CALL_HELD -> determineCallSellPoint(minuteBars, dailyBars);
                        case PUT_HELD -> determinePutsSellPoint(minuteBars, dailyBars);
                    }

                    // Sleep for 1/2 minute, some import issue in J 21
                    //TimeUnit.MILLISECONDS.sleep(500);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.info("Task interrupted.");
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                } catch (Exception e) {
                    logger.info("Error during task execution: " + e.getMessage());
                    // Optional: Decide if you want to interrupt the thread and stop the task
                }
            }
        }
        return null;
    }

    private void checkAndPlaceOrder(List<Bar> minuteBars, List<Bar> dailyBars) {

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

        if (ema5 > ema50 && ema5 > ema13 && previousEmas != null) {
            // Probable Buy call scenario
            currentStatus = CurrentStatus.UPTREND;
            // check if first time
            boolean isBuyCallBasedOnPastMinute = previousEmas.ema5day > previousEmas.ema50day && previousEmas.ema5day > previousEmas.ema13day;


            // Initiate an Option buy order if all criteria met
            if (isBuyCallBasedOnPastMinute && isMacdBullish && isRsiBullish) {
                initiateCallOrPutBuying(ticker, dailyBars.get(dailyBars.size()-1).getClose(), 'C');
            }
        } else if (ema5 < ema50 && ema5 < ema13 && previousEmas != null) {
            // Probable Buy put scenario
            currentStatus = CurrentStatus.DOWNTREND;
            // check if first time
            boolean isSellPutBasedOnPastMinute = previousEmas.ema5day < previousEmas.ema50day && previousEmas.ema5day < previousEmas.ema13day;
            if (isSellPutBasedOnPastMinute && !isMacdBullish && !isRsiBullish) {
                //initiatePutBuying(ticker, price);
                initiateCallOrPutBuying(ticker, dailyBars.get(dailyBars.size()-1).getClose(), 'P');
            }
        }
    }

    /** 1) determine option ticker
     *  2) get Bid / Ask and limit we want to place
     *  3) Place Order for desired Option quantity (say 2 to 5)
     *  4) track status
     *
     * @param ticker
     */
    private void initiateCallOrPutBuying(String ticker, double price, char callOrPut) {
        try {
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, price, callOrPut);
            switch (callOrPut) {
                case 'C' -> optionOrderProcessor.processCallBuyOrder(nextStrikePrice, ticker, price);
                case 'P' -> optionOrderProcessor.processPutBuyOrder(nextStrikePrice, ticker, price);
            }
        } catch (Exception e) {
            // TO DO fix me
        }

    }

    /** id not result if the call BUY order did not result in FILL we need to cancel order
     *
     * @param nextStrikePrice
     * @param ticker
     * @param price
     * @throws Exception
     */
    private void processCallBuyAndCancel(NextStrikePrice nextStrikePrice  , String ticker, double price) throws Exception {
        OrderFillStatus orderFillStatus = optionOrderProcessor.processCallBuyOrder(nextStrikePrice, ticker, price);

        //process cancel order that did not fill or resubvmit next minute??
        //if(orderFillStatus.getStatus().equals())
    }

    private void determineCallSellPoint(List<Bar> minuteBars, List<Bar> dailyBars) {

    }

    private void determinePutsSellPoint(List<Bar> minuteBars, List<Bar> dailyBars) {

    }
}
