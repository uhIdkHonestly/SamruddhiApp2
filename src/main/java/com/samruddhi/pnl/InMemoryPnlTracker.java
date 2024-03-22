package com.samruddhi.pnl;

import com.samruddhi.trading.equities.logic.StockTradeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPnlTracker {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryPnlTracker.class);

    // AtomicLong to ensure thread-safe updates to the total PNL
    private final AtomicLong totalPNL = new AtomicLong(0);

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
        tracker.updateTotalPNL(100);
        tracker.updateTotalPNL(-50);
        tracker.updateTotalPNL(200);

        logger.info("Total PNL: " + tracker.getTotalPNL());
    }
}
