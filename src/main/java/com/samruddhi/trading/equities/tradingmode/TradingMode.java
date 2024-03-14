package com.samruddhi.trading.equities.tradingmode;

import com.samruddhi.trading.equities.config.ConfigManager;

public class TradingMode {

    public static TradingModeEnum getTradingMode() {
        //return TradingModeEnum.valueOf(ConfigManager.getInstance().getProperty("tradingmode"));
        return TradingModeEnum.DUMMY;
    }
}
