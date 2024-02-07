package com.samruddhi.trading.equities.logic;

//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_MINUTE;
//import static com.samruddhi.trading.equities.services.MarketDataServiceImpl.TIME_UNIT_DAILY;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import com.samruddhi.trading.equities.studies.EMACalculator;
import core.TradingApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.WindowFocusListener;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TradeWorker implements Callable<TradeWorkerStatus> {

    private static class PreviousEmas {
        double ema5day;
        double ema13day;
        double ema50day;
    }

    private static final Logger logger = LoggerFactory.getLogger(com.samruddhi.trading.equities.logic.TradeWorker.class);

    private boolean isTerminated = false;
    private MarketDataService marketDataService;
    private String ticker;
    private PreviousEmas previousEmas;

    private enum CurrentSTatus {
        UPTREND, DOWNTREND, CALL_HELP, PUT_HELD, NO_STATUS;
    }

    public TradeWorker(MarketDataService marketDataService, String ticker) {
        this.marketDataService = marketDataService;
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
                    //String quotes = fetchQuotes(); // Simulate fetching quotes
                    //String result = processQuotes(quotes); // Process the quotes
                    //System.out.println("Processed result: " + result);

                    // get daily minute data , tick - 2 (just last 2)
                    // marketDataService.getStockDataBars(1, MarketDataServiceImpl.TIME_UNIT_MINUTE, 2);
                    List<Bar> minuteBars = marketDataService.getStockDataBars(ticker,  "Minute", 1, 2);

                    List<Bar> dailyBars = marketDataService.getStockDataBars(ticker,  "Daily", 1, 50);


                    double ema5 = EMACalculator.calculateEMAs(dailyBars, 5);
                    double ema13 = EMACalculator.calculateEMAs(dailyBars, 5);
                    double ema50 = EMACalculator.calculateEMAs(dailyBars, 5);

                    // do the math
                    // get 50 day daily data for 50 days
                    if(ema5 > ema50 && ema5 > ema13 ) {
                        // check if first time
                        boolean isBuyPastMinute = previousEmas.ema5day > previousEmas.ema50day && previousEmas.ema5day > previousEmas.ema13day;
                        // Validate RSI and MACD  and VWAP
                        // Initiate an Option buy order at -1 $ or -2 Dollar in the money
                    }
                    // Sleep for 1/2 minute
                    TimeUnit.MILLISECONDS.sleep(500);
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
}
