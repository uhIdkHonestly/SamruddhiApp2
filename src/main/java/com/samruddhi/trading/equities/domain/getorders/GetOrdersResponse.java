package com.samruddhi.trading.equities.domain.getorders;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetOrdersResponse {
    @JsonProperty("Orders")
    private List<Order> orders;
    @JsonProperty("Errors")
    private List<Object> errors; // Assuming type Object, adjust as needed
    @JsonProperty("NextToken")
    private String nextToken;
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

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
