package com.samruddhi.trading.equities.domain.getordersbyid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConditionalOrder {
    @JsonProperty("Relationship")
    private String relationship;
    @JsonProperty("OrderID")
    private String orderId;

    // Getters and Setters
}
