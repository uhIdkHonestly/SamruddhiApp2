package com.samruddhi.trading.equities.domain.placeorder;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlaceOrderResponse {
    @JsonProperty("Orders")
    private List<Order> Orders;
    @JsonProperty("Errors")
    private List<Error> Errors;

    public PlaceOrderResponse() {

    }

    // Getters and Setters
    public List<Order> getOrders() {
        return Orders;
    }

    public void setOrders(List<Order> orders) {
        Orders = orders;
    }

    public List<Error> getErrors() {
        return Errors;
    }

    public void setErrors(List<Error> errors) {
        Errors = errors;
    }
}
