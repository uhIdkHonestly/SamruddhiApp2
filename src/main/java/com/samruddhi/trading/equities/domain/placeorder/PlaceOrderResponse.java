package com.samruddhi.trading.equities.domain.placeorder;

import java.util.List;

public class PlaceOrderResponse {
    private List<Order> Orders;
    private List<Error> Errors;

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
