package com.samruddhi.trading.equities.domain;

import java.time.LocalTime;

public class FinishedTrade {
    private String ticker;
    private double buyPrice;
    private double sellPrice;
    private LocalTime entry;
    private LocalTime  exit;
    private long quantity;
    private double profitOrLoss;

    public FinishedTrade(String ticker, double buyPrice, double sellPrice, LocalTime entry, LocalTime exit, long quantity, double profitOrLoss) {
        this.ticker = ticker;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.entry = entry;
        this.exit = exit;
        this.quantity = quantity;
        this.profitOrLoss = profitOrLoss;
    }
}
