package com.samruddhi.trading.equities.logic;

import com.samruddhi.pnl.InMemoryPnlTracker;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;

import java.util.concurrent.Callable;

public class BaseTradeWorker implements Callable<TradeWorkerStatus> {

    final InMemoryPnlTracker inMemoryPnlTracker = InMemoryPnlTracker.getInstance();
    @Override
    public TradeWorkerStatus call() throws Exception {
        return null;
    }
}