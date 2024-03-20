package com.samruddhi.trading.equities.dummy;

import core.TradingApp;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DummyTradingApp implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TradingApp.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static boolean shutdownNow = false;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Entered DummyTradingApp");
        List<String> tickers = List.of("AAPL", "AMZN", "MSFT", "PLTR");
        List<Callable<String>> workers = tickers.stream().map(ticker -> new SimpleCallable<>(ticker)
        ).collect(Collectors.toList());
        try {
            executorService.invokeAll(workers);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        } finally {
            shutdownHook();
        }
        logger.info("Exiting DummyTradingApp");
    }

    private void shutdownHook() {

        // Keep job alive until a condition is met (e.g., time-based)
        while (!shutdownNow) {
            try {
                Thread.sleep(2 * 60000); // Sleep for 2 minute
                logger.info("In shutdownHook");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class SimpleCallable<String> implements Callable<java.lang.String> {


        String ticker;

        boolean breakLoop = false;

        SimpleCallable(String ticker) {
            this.ticker = ticker;
        }

        @Override
        public java.lang.String call() throws Exception {
            while (!breakLoop) {
                logger.info("SimpleCallable Call for {} at {}", ticker, LocalDateTime.now());
                Thread.sleep(4000);
            }
            return "Processing ticker" + ticker;
        }
    }
}
