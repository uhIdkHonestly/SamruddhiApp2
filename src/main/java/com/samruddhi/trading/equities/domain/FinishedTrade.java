package com.samruddhi.trading.equities.domain;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "{" +
                "ticker='" + ticker + '\'' +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", entry=" + entry +
                ", exit=" + exit +
                ", quantity=" + quantity +
                ", profitOrLoss=" + profitOrLoss +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinishedTrade that)) return false;
        return Double.compare(that.buyPrice, buyPrice) == 0 && Double.compare(that.sellPrice, sellPrice) == 0 && quantity == that.quantity && Double.compare(that.profitOrLoss, profitOrLoss) == 0 && Objects.equals(ticker, that.ticker) && Objects.equals(entry, that.entry) && Objects.equals(exit, that.exit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, buyPrice, sellPrice, entry, exit, quantity, profitOrLoss);
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public LocalTime getEntry() {
        return entry;
    }

    public void setEntry(LocalTime entry) {
        this.entry = entry;
    }

    public LocalTime getExit() {
        return exit;
    }

    public void setExit(LocalTime exit) {
        this.exit = exit;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(double profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }
}
