package com.samruddhi.trading.equities.domain;

/**
 * This class holds full Option ticker, but not the price
 */
public class NextStrikePrice {

    String underlyingTicker;

    String fullOptionTicker;
    String dateWithStrike;


    public NextStrikePrice(String fullOptionTicker, String dateWithStrike, String underlyingTicker) {
        this.fullOptionTicker = fullOptionTicker;
        this.dateWithStrike = dateWithStrike;
        this.underlyingTicker = underlyingTicker;
    }

    public String getFullOptionTicker() {
        return fullOptionTicker;
    }

    public void setFullOptionTicker(String fullOptionTicker) {
        this.fullOptionTicker = fullOptionTicker;
    }

    public String getDateWithStrike() {
        return dateWithStrike;
    }

    public void setDateWithStrike(String dateWithStrike) {
        this.dateWithStrike = dateWithStrike;
    }

    public String getUnderlyingTicker() {
        return underlyingTicker;
    }

    public void setUnderlyingTicker(String underlyingTicker) {
        this.underlyingTicker = underlyingTicker;
    }

    @Override
    public String toString() {
        return "NextStrikePrice{" +
                "underlyingTicker='" + underlyingTicker + '\'' +
                ", fullOptionTicker='" + fullOptionTicker + '\'' +
                ", dateWithStrike='" + dateWithStrike + '\'' +
                '}';
    }
}
