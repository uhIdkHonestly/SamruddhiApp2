package com.samruddhi.trading.equities.domain.getordersbyid;

import com.samruddhi.trading.equities.logic.OptionOrderFillStatus;

import java.time.LocalTime;

public class OrderFillStatus {

    public static final OrderFillStatus ORDER_FILL_STATUS_FAILED = new OrderFillStatus("0", "FAILED", 0.0, 0, "");
    /** we abort as contrat price is not right */
    public static final OrderFillStatus ORDER_FILL_STATUS_ABORTED = new OrderFillStatus("0", "ABORTED", 0.0, 0, "");

    String orderId;
    OptionOrderFillStatus status;
    double fillPrice;
    int fillQuantity;
    /** This wil be actual option ticker */
    String optionTicker;

    LocalTime executionTime = LocalTime.now();

    public OrderFillStatus(String orderId, String status, double fillPrice, int fillQuantity, String ticker) {
        this.orderId = orderId;
        this.status = OptionOrderFillStatus.fromString(status);
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
        this.optionTicker = ticker;
    }

    public OrderFillStatus(String orderId, OptionOrderFillStatus status, double fillPrice, int fillQuantity, String ticker) {
        this.orderId = orderId;
        this.status = status;
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
        this.optionTicker = ticker;
    }

    public OptionOrderFillStatus getStatus() {
        return status;
    }

    public void setStatus(OptionOrderFillStatus status) {
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
        return optionTicker;
    }

    public void setTicker(String ticker) {
        this.optionTicker = ticker;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public LocalTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(LocalTime executionTime) {
        this.executionTime = executionTime;
    }
}
