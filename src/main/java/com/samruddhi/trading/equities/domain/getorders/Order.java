package com.samruddhi.trading.equities.domain.getorders;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {
    @JsonProperty("AccountID")
    private String accountId;
    @JsonProperty("CommissionFee")
    private String commissionFee;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("Duration")
    private String duration;
    @JsonProperty("GoodTillDate")
    private String goodTillDate;
    @JsonProperty("Legs")
    private List<Leg> legs;
    @JsonProperty("MarketActivationRules")
    private List<MarketActivationRule> marketActivationRules;
    @JsonProperty("OrderID")
    private String orderId;
    @JsonProperty("OpenedDateTime")
    private String openedDateTime;
    @JsonProperty("OrderType")
    private String orderType;
    @JsonProperty("PriceUsedForBuyingPower")
    private String priceUsedForBuyingPower;
    @JsonProperty("Routing")
    private String routing;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("StatusDescription")
    private String statusDescription;
    @JsonProperty("AdvancedOptions")
    private String advancedOptions;
    @JsonProperty("TimeActivationRules")
    private List<TimeActivationRule> timeActivationRules;
    @JsonProperty("UnbundledRouteFee")
    private String unbundledRouteFee;
    @JsonProperty("ClosedDateTime")
    private String closedDateTime;
    @JsonProperty("FilledPrice")
    private String filledPrice;
    @JsonProperty("ConditionalOrders")
    private List<ConditionalOrder> conditionalOrders;
    @JsonProperty("LimitPrice")
    private String limitPrice;
    @JsonProperty("StopPrice")
    private String stopPrice;
    @JsonProperty("GroupName")
    private String groupName;
    @JsonProperty("TrailingStop")
    private TrailingStop trailingStop;

    // Getters and Setters
}
