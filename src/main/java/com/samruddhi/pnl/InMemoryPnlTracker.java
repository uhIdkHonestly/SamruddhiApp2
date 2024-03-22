package com.samruddhi.pnl;

import com.samruddhi.trading.equities.logic.StockTradeWorker;
import core.TradingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPnlTracker {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryPnlTracker.class);

    // AtomicLong to ensure thread-safe updates to the total PNL
    private final AtomicLong totalPNL = new AtomicLong(0);

    private static InMemoryPnlTracker instance = null;

    // Private constructor to prevent instantiation from other classes
    private InMemoryPnlTracker() {
    }

    // Public static method to get the instance of the Singleton
    public static InMemoryPnlTracker getInstance() {
        if (instance == null) {
            synchronized (InMemoryPnlTracker.class) {
                if (instance == null) {
                    instance = new InMemoryPnlTracker();
                }
            }
        }
        return instance;
    }


    /**
     * Updates the total PNL with the PNL of a completed trade.
     * This method is thread-safe.
     *
     * @param tradePNL the PNL of the completed trade
     */
    public void updateTotalPNL(long tradePNL) {
        totalPNL.addAndGet(tradePNL);
    }

    /**
     * Retrieves the current total PNL.
     * This method is thread-safe.
     *
     * @return the total PNL
     */
    public long getTotalPNL() {
        return totalPNL.get();
    }

    public static void main(String[] args) {
        // Example usage
        InMemoryPnlTracker tracker = new InMemoryPnlTracker();

        // Simulate updates to PNL from different trades/threads
        tracker.updateTotalPNL(-300);
        tracker.updateTotalPNL(-50);
        tracker.updateTotalPNL(200);

        logger.info("Total PNL: " + tracker.getTotalPNL());
    }
}
