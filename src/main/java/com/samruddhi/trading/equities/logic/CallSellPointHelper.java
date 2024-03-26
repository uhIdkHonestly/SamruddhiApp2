package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.base.BaseSellPointHelper;
import com.samruddhi.trading.equities.studies.EMACalculator;
import com.samruddhi.trading.equities.studies.MACDCalculator;
import com.samruddhi.trading.equities.studies.RSICalculator;

import java.util.List;

public class CallSellPointHelper extends BaseSellPointHelper {

    private final String ticker;

    public CallSellPointHelper(String ticker) {
        this.ticker = ticker;
    }

    private final double ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150 = 0.01;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150 = 0.02;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50 = 0.03;

    public double getAcceptablePriceDropPercent(double priceOfUnderlying, String ticker) {
        if(priceOfUnderlying < 50)
            return  ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50 ;
        else if( priceOfUnderlying < 150)
            return ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150;
        else
            return ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150;
    }
}
