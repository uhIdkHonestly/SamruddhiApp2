package com.samruddhi.trading.equities.domain.getordersbyid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Leg {
    @JsonProperty("AssetType")
    private String assetType;
    @JsonProperty("BuyOrSell")
    private String buyOrSell;
    @JsonProperty("ExecQuantity")
    private String execQuantity;
    @JsonProperty("ExecutionPrice")
    private String executionPrice;
    @JsonProperty("ExpirationDate")
    private String expirationDate;
    @JsonProperty("OpenOrClose")
    private String openOrClose;
    @JsonProperty("OptionType")
    private String optionType;
    @JsonProperty("QuantityOrdered")
    private String quantityOrdered;
    @JsonProperty("QuantityRemaining")
    private String quantityRemaining;
    @JsonProperty("StrikePrice")
    private String strikePrice;
    @JsonProperty("Symbol")
    private String symbol;
    @JsonProperty("Underlying")
    private String underlying;

    // Getters and Setters
}
