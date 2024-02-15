package com.samruddhi.trading.equities.domain.getordersbyid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketActivationRule {
    @JsonProperty("RuleType")
    private String ruleType;
    @JsonProperty("Symbol")
    private String symbol;
    @JsonProperty("Predicate")
    private String predicate;
    @JsonProperty("TriggerKey")
    private String triggerKey;
    @JsonProperty("Price")
    private String price;

    // Getters and Setters
}
