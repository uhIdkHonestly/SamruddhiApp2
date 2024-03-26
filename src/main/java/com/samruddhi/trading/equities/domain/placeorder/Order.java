package com.samruddhi.trading.equities.domain.placeorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

        import java.io.File;
        import java.io.IOException;
        import java.util.List;

// Order class
public class Order {

    public Order(String message, String orderID) {
        this.message = message;
        this.orderId = orderID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("Message")
    private String message;

    @JsonProperty("OrderID")
    private String orderId;

    public Order() {

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

