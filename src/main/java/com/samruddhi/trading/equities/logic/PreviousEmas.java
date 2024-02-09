package com.samruddhi.trading.equities.logic;

public class PreviousEmas {

    double ema5day;
    double ema13day;
    double ema50day;

    public PreviousEmas(double ema5day, double ema13day, double ema50day) {
        this.ema5day = ema5day;
        this.ema13day = ema13day;
        this.ema50day = ema50day;
    }
}
