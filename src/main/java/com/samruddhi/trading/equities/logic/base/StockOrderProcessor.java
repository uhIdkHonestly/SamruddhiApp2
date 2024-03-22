package com.samruddhi.trading.equities.logic.base;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;

public interface StockOrderProcessor {

    // Covers going long on stocks
    public OrderFillStatus createStockBuyOrder(String ticker, double price) throws Exception;

    // Sell the stock you are long on
    public OrderFillStatus createStockSellOrder(String ticker, double price) throws Exception;

    public OrderFillStatus replaceStockSellOrder(String orderId, String ticker) throws Exception;

    public void cancelOrder(String orderId) throws Exception;

    /**
     We need to cover 1 ) Sell Stocks short
     2 Cover stock short by buying it back
     */
}
