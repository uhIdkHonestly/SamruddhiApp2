package com.samruddhi.trading.equities.logic.base;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;

public interface OptionOrderProcessor {
    
    public OrderFillStatus createCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;
    public OrderFillStatus createCallSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;
    public OrderFillStatus replaceCallOrPutSellOrder(String orderId, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;

    public OrderFillStatus createPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;
    public OrderFillStatus createPutSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception;

    public void cancelOrder(String orderId) throws Exception;
}

