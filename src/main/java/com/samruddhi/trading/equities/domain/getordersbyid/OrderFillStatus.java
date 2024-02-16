package com.samruddhi.trading.equities.domain.getordersbyid;

public class OrderFillStatus {

    String status;
    double fillPrice;
    int fillQuantity;
    String ticker;

    public OrderFillStatus(String status, double fillPrice, int fillQuantity, String ticker) {
        this.status = status;
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
        this.ticker = ticker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFillPrice() {
        return fillPrice;
    }

    public void setFillPrice(double fillPrice) {
        this.fillPrice = fillPrice;
    }

    public int getFillQuantity() {
        return fillQuantity;
    }

    public void setFillQuantity(int fillQuantity) {
        this.fillQuantity = fillQuantity;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
