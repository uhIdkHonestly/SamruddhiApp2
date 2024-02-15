package com.samruddhi.trading.equities.domain.placeorder;

public class Error {
    private String Error;
    private String Message;
    private String OrderID;

    // Getters and Setters
    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

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
