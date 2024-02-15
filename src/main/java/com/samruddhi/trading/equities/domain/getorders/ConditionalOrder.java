package com.samruddhi.trading.equities.domain.getorders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConditionalOrder {
    @JsonProperty("Relationship")
    private String relationship;
    @JsonProperty("OrderID")
    private String orderId;

    // Getters and Setters
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

