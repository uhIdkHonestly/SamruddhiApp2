package com.samruddhi.trading.equities.services;

import java.util.Set;

public class CallOrderContrctSizeService {

    private static Set<String> dailyExpiryOptionsTickers = Set.of("SPY", "QQQ");

    public int getCallOrderSizeForTicker(int ticker, double price, boolean isEarningsPlay) {
        if(dailyExpiryOptionsTickers.contains(ticker)) {
            return 2;
        } else if(isEarningsPlay) {
            return 1;
        } else if(price > 100) {
            return 1;
        } else
            return 2;
    }
}
