package com.samruddhi.trading.equities.domain.getordersbyid;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetOrdersByOrderIdResponse {

    @JsonProperty("Orders")
    private List<Order> orders;
    @JsonProperty("Errors")
    private List<Object> errors; // Assuming type Object, adjust as needed

    // Getters and Setters
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}