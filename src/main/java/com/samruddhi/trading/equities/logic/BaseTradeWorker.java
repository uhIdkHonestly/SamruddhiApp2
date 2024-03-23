package com.samruddhi.trading.equities.logic;

import com.samruddhi.pnl.InMemoryPnlTracker;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;

import java.util.concurrent.Callable;

public class BaseTradeWorker implements Callable<TradeWorkerStatus> {

    final int ALLOWED_MAX_LOSS_PER_DAY =  -500;
    final static int PER_INTERVAL_SLEEP_TIME = 950; // In milliseconds for working with minute Candles

    final InMemoryPnlTracker inMemoryPnlTracker = InMemoryPnlTracker.getInstance();
    @Override
    public TradeWorkerStatus call() throws Exception {
        return null;
    }

    boolean maxPnlLossExceededPerDay() {
        return inMemoryPnlTracker.getTotalPNL() < ALLOWED_MAX_LOSS_PER_DAY;

    }
}
