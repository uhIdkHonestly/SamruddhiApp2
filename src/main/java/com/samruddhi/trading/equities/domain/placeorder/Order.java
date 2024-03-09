package com.samruddhi.trading.equities.domain.placeorder;


import com.fasterxml.jackson.databind.ObjectMapper;

        import java.io.File;
        import java.io.IOException;
        import java.util.List;

// Order class
public class Order {
    private String Message;
    private String OrderID;

    public Order(String message, String orderID) {
        Message = message;
        OrderID = orderID;
    }

    // Getters and Setters
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }
}

