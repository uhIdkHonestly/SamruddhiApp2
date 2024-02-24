package com.samruddhi.trading.equities.logic.base;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;

public interface OptionOrderProcessor {
    public OrderFillStatus processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;
    public OrderFillStatus processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;

    public void cancelOrder(String orderId) throws Exception;
}

