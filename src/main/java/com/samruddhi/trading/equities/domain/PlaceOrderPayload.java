package com.samruddhi.trading.equities.domain;

import java.util.Objects;

/**
 * Pojo for Place Order Request payload
 */
public class PlaceOrderPayload {
    String underlyingTicker;

    String AccountID;
    /** If option trading thsi reflects Option Ticker */
    String symbol;
    int quantity;
    String orderType = "Limit";
    double limitPrice;
    String tradeAction = "BUY";
    String timeInForce = " { \"Duration\": \"DAY\"  }";
    String route = "\"Route\": \"Intelligent\"";

    String legs = """
            [ {
                "Quantity":%s,
                "Symbol":"%s",
                "TradeAction": "%s"
                }
            ]""";

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getTradeAction() {
        return tradeAction;
    }

    public void setTradeAction(String tradeAction) {
        this.tradeAction = tradeAction;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }


    public String getLegs() {
        return String.format(legs, quantity, symbol, OptionTradeAction.getOptionTradeAction(tradeAction));
    }

    public void setLegs(String legs) {
        this.legs = legs;
    }

    @Override
    public String toString() {
        return "PlaceOrderPayload{" +
                "underlyingTicker='" + underlyingTicker + '\'' +
                ", AccountID='" + AccountID + '\'' +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", orderType='" + orderType + '\'' +
                ", limitPrice=" + limitPrice +
                ", tradeAction='" + tradeAction + '\'' +
                ", timeInForce='" + timeInForce + '\'' +
                ", route='" + route + '\'' +
                ", legs='" + legs + '\'' +
                '}';
    }

    public String getUnderlyingTicker() {
        return underlyingTicker;
    }

    public void setUnderlyingTicker(String underlyingTicker) {
        this.underlyingTicker = underlyingTicker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceOrderPayload payload)) return false;
        return getQuantity() == payload.getQuantity() && Double.compare(payload.getLimitPrice(), getLimitPrice()) == 0 && Objects.equals(getAccountID(), payload.getAccountID()) && Objects.equals(getSymbol(), payload.getSymbol()) && Objects.equals(getOrderType(), payload.getOrderType()) && Objects.equals(getTradeAction(), payload.getTradeAction()) && Objects.equals(getTimeInForce(), payload.getTimeInForce()) && Objects.equals(getRoute(), payload.getRoute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountID(), getSymbol(), getQuantity(), getOrderType(), getLimitPrice(), getTradeAction(), getTimeInForce(), getRoute());
    }


}
