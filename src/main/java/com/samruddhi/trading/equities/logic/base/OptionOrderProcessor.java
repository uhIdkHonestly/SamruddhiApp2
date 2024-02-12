package com.samruddhi.trading.equities.logic.base;

import com.samruddhi.trading.equities.domain.NextStrikePrice;

public interface OptionOrderProcessor {
    public void processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price);
    public void processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price);
}

