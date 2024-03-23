package com.samruddhi.trading.equities.logic;

import com.samruddhi.pnl.InMemoryPnlTracker;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;

import java.util.concurrent.Callable;

public class BaseTradeWorker implements Callable<TradeWorkerStatus> {

    final int ALLOWED_MAX_LOSS_PER_DAY =  -500;
    final static int PER_INTERVAL_SLEEP_TIME_REGULAR = 59 * 1000; //  59 seconds

    final static int PER_INTERVAL_SLEEP_TIME_AFTER_BUY = 30 * 1000; //  59 seconds

    final InMemoryPnlTracker inMemoryPnlTracker = InMemoryPnlTracker.getInstance();

    /**
     * What's my curent status ie UPTREND, DOWNTREND, CALL_HELD, PUT_HELD, NO_STATUS,
     * stored for checking worker status easily
     */
   protected CurrentStatus currentStatus;

    enum CurrentStatus {
        UPTREND, DOWNTREND, CALL_HELD, PUT_HELD, STOCKS_HELD, NO_STATUS;

    }

    @Override
    public TradeWorkerStatus call() throws Exception {
        return null;
    }

    boolean maxPnlLossExceededPerDay() {
        return inMemoryPnlTracker.getTotalPNL() < ALLOWED_MAX_LOSS_PER_DAY;
    }


    /** Before any buy we sleep for around a minute before we get next set of 50 minute Bars
     * However once we hold some stocks, we need to check price movement sooner ,current at each 30 seconds interval
     * @throws InterruptedException
     */
    protected void doSleep() throws InterruptedException{
        if(currentStatus != StockTradeWorker.CurrentStatus.STOCKS_HELD)
            Thread.sleep(PER_INTERVAL_SLEEP_TIME_REGULAR);
        else
            Thread.sleep(PER_INTERVAL_SLEEP_TIME_AFTER_BUY);
    }
}
