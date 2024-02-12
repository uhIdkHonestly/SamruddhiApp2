package com.samruddhi.trading.equities.domain;

public class NextStrikePrice {
    String fullOptionTicker;
    String dateWithStrike;

    public NextStrikePrice(String fullOptionTicker, String dateWithStrike) {
        this.fullOptionTicker = fullOptionTicker;
        this.dateWithStrike = dateWithStrike;
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
