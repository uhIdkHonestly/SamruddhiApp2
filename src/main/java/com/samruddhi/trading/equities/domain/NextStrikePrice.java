package com.samruddhi.trading.equities.domain;

public class NextStrikePrice {
    String fullOptionTicker;
    String dateWithStrike;

    String underlying;

    public NextStrikePrice(String fullOptionTicker, String dateWithStrike, String underlying) {
        this.fullOptionTicker = fullOptionTicker;
        this.dateWithStrike = dateWithStrike;
        this.underlying = underlying;
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
}
