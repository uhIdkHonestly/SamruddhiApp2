package com.samruddhi.trading.equities.tradingmode;

import com.samruddhi.trading.equities.config.ConfigManager;

public class TradingMode {

    public static TradingModeEnum getTradingMode() {
        return TradingModeEnum.valueOf(ConfigManager.getInstance().getProperty("tradingmode"));
    }

    public static String getOrdersUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/brokerage/accounts/%s/orders/%s";
        } else {
            return "https://api.tradestation.com/v3/brokerage/accounts/%s/orders/%s";
        }
    }

    public static String getMarketDataUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/marketdata/barcharts/%s?interval=%s&unit=%s&barsback=%s&sessiontemplate=Default";
        } else {
            return "https://api.tradestation.com/v3/marketdata/barcharts/%s?interval=%s&unit=%s&barsback=%s&sessiontemplate=Default";
        }
    }

    public static String placeOrderUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/orderexecution/orders";
        } else {
            return "https://api.tradestation.com/v3/orderexecution/orders";
        }
    }

    public static String replaceOrderUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/orderexecution/orders/{orderID}";
        } else {
            return "https://api.tradestation.com/v3/orderexecution/orders/{orderID}";
        }
    }

    public static String cancelOrderUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/orderexecution/orders/%s";
        } else {
            return "https://api.tradestation.com/v3/orderexecution/orders/%s";
        }
    }

    public static String optionsQuoteUrl() {
        TradingModeEnum tradingModeEnum = getTradingMode();

        if (tradingModeEnum == TradingModeEnum.SIMULATED) {
            return   "https://sim-api.tradestation.com/v3/marketdata/stream/options/quotes";
        } else {
            return "https://api.tradestation.com/v3/marketdata/stream/options/quotes";
        }
    }
}
