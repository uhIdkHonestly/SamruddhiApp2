package com.samruddhi.trading.equities.logic;

import java.time.LocalTime;

public class CurrentActiveTrade {

    String ticker;
    String optionTicker;
    // Quantity of calls and puts bought
    int quantity;
    double pricePerCall;
    double totalPrice;
    LocalTime tradeTime;

    public CurrentActiveTrade(String ticker, String optionTicker, int quantity, double pricePerCall, double totalPrice, LocalTime tradeTime) {
        this.ticker = ticker;
        this.optionTicker = optionTicker;
        this.quantity = quantity;
        this.pricePerCall = pricePerCall;
        this.totalPrice = totalPrice;
        this.tradeTime = tradeTime;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getOptionTicker() {
        return optionTicker;
    }

    public void setOptionTicker(String optionTicker) {
        this.optionTicker = optionTicker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerCall() {
        return pricePerCall;
    }

    public void setPricePerCall(double pricePerCall) {
        this.pricePerCall = pricePerCall;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalTime getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(LocalTime tradeTime) {
        this.tradeTime = tradeTime;
    }
}
