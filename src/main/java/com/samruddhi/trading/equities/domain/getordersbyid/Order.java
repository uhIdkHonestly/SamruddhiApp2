package com.samruddhi.trading.equities.domain.getordersbyid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samruddhi.trading.equities.logic.OptionOrderFillStatus;

import java.util.Date;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date goodTillDate;
    @JsonProperty("Legs")
    private List<Leg> legs;
    @JsonProperty("MarketActivationRules")
    private List<MarketActivationRule> marketActivationRules;
    @JsonProperty("OrderID")
    private String orderId;
    @JsonProperty("OpenedDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date openedDateTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date closedDateTime;
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
    // Getters and setters

    public String getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(String commissionFee) {
        this.commissionFee = commissionFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getGoodTillDate() {
        return goodTillDate;
    }

    public void setGoodTillDate(Date goodTillDate) {
        this.goodTillDate = goodTillDate;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public List<MarketActivationRule> getMarketActivationRules() {
        return marketActivationRules;
    }

    public void setMarketActivationRules(List<MarketActivationRule> marketActivationRules) {
        this.marketActivationRules = marketActivationRules;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOpenedDateTime() {
        return openedDateTime;
    }

    public void setOpenedDateTime(Date openedDateTime) {
        this.openedDateTime = openedDateTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPriceUsedForBuyingPower() {
        return priceUsedForBuyingPower;
    }

    public void setPriceUsedForBuyingPower(String priceUsedForBuyingPower) {
        this.priceUsedForBuyingPower = priceUsedForBuyingPower;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public OptionOrderFillStatus getStatus() {
        return OptionOrderFillStatus.fromString(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getAdvancedOptions() {
        return advancedOptions;
    }

    public void setAdvancedOptions(String advancedOptions) {
        this.advancedOptions = advancedOptions;
    }

    public List<TimeActivationRule> getTimeActivationRules() {
        return timeActivationRules;
    }

    public void setTimeActivationRules(List<TimeActivationRule> timeActivationRules) {
        this.timeActivationRules = timeActivationRules;
    }

    public String getUnbundledRouteFee() {
        return unbundledRouteFee;
    }

    public void setUnbundledRouteFee(String unbundledRouteFee) {
        this.unbundledRouteFee = unbundledRouteFee;
    }

    public Date getClosedDateTime() {
        return closedDateTime;
    }

    public void setClosedDateTime(Date closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public double getFilledPrice() {
        return Double.parseDouble(filledPrice);
    }

    public void setFilledPrice(String filledPrice) {
        this.filledPrice = filledPrice;
    }

    public List<ConditionalOrder> getConditionalOrders() {
        return conditionalOrders;
    }

    public void setConditionalOrders(List<ConditionalOrder> conditionalOrders) {
        this.conditionalOrders = conditionalOrders;
    }

    public String getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(String limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public TrailingStop getTrailingStop() {
        return trailingStop;
    }

    public void setTrailingStop(TrailingStop trailingStop) {
        this.trailingStop = trailingStop;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

