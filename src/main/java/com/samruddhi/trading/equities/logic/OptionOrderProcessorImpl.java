package com.samruddhi.trading.equities.logic;


import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;

/** Intelligently process Option order, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    @Override
    public void processCallBuyOrder() {

    }

    @Override
    public void processPutBuyOrder() {

    }
}
