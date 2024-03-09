package com.samruddhi.trading.equities.domain.updateorder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateOrderResponse {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("OrderID")
    private String orderID;


    public UpdateOrderResponse(String message, String orderID) {
        this.message = message;
        this.orderID = orderID;
    }

    public UpdateOrderResponse() {
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return "UpdateOrderResponse{" +
                "Message='" + message + '\'' +
                ", OrderID='" + orderID + '\'' +
                '}';
    }
}
