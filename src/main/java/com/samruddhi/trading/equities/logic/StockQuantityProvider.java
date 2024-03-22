package com.samruddhi.trading.equities.logic;

import static com.samruddhi.trading.equities.orderlimits.NearestOptionStrikePrice.getNearestOptionCallStrikePrice;

public class StockQuantityProvider {

    public static int getStockBuySellQuantityByTicker(String ticker, double price) {
        int quantity  = 0;

        // ETFs are rounded down to the nearest number, other logic based on Pricing needs fixing
        if (OptionExpiryPeriod.hasDailyOptions(ticker)) {
            quantity = 5;
        } else {
            int priceInt = (int) Math.floor(price);
            if (priceInt < 30) {
                quantity = 50;
            } else if (priceInt < 120) {
                quantity = 25;
            } else if (priceInt < 200) {
                quantity = 15;
            } else if (priceInt < 500) {
                quantity = 6;
            } else {
                quantity = 3;
            }
        }
        return quantity;
    }
}
