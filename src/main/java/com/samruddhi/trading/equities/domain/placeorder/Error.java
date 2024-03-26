package com.samruddhi.trading.equities.domain.placeorder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    @JsonProperty("Error")
    private String Error;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("OrderID")
    private String OrderID;


    public Error() {

    }

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
