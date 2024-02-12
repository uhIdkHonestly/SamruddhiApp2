package com.samruddhi.trading.equities.logic;

import java.util.Set;

public class OptionExpiryPeriod {

    private static Set<String> dailyExpiryOptionsTickers = Set.of("SPY", "QQQ");

    public static boolean hasDailyOptions(String ticker) {
        return dailyExpiryOptionsTickers.contains(ticker);
    }
}

