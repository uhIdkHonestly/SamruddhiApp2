package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.TradeWorkerStatus;
import core.TradingApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.WindowFocusListener;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TradeWorker implements Callable<TradeWorkerStatus> {

    private static final Logger logger = LoggerFactory.getLogger(TradeWorker.class);

    private boolean isTerminated = false;

    public TradeWorker() {

    }

    public void triggerTermination() {
        this.isTerminated = true;
    }

    @Override
    public TradeWorkerStatus call() throws Exception {
        // this will run whole day
        while(!isTerminated) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //String quotes = fetchQuotes(); // Simulate fetching quotes
                    //String result = processQuotes(quotes); // Process the quotes
                    //System.out.println("Processed result: " + result);

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
