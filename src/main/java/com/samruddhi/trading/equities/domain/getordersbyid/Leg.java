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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public int getExecQuantity() {
        return Integer.parseInt(execQuantity);
    }

    public void setExecQuantity(String execQuantity) {
        this.execQuantity = execQuantity;
    }

    public String getExecutionPrice() {
        return executionPrice;
    }

    public void setExecutionPrice(String executionPrice) {
        this.executionPrice = executionPrice;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOpenOrClose() {
        return openOrClose;
    }

    public void setOpenOrClose(String openOrClose) {
        this.openOrClose = openOrClose;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(String quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public String getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(String quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public String getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(String strikePrice) {
        this.strikePrice = strikePrice;
    }

    // TO DO verify if symbol really returns Options symbol
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }
}
