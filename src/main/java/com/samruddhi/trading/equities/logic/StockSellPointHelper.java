package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.logic.base.BaseSellPointHelper;

public class StockSellPointHelper extends BaseSellPointHelper {

    private final double ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150 = 0.01;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150 = 0.02;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50 = 0.03;

    private final String ticker;

    public StockSellPointHelper(String ticker) {
        this.ticker = ticker;
    }

    public double getAcceptablePriceChangePercent(double fillPrice, String ticker) {
        if(fillPrice < 50)
            return ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50;
        else if( fillPrice < 150)
            return ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150;
        else
            return ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150;
    }
}
