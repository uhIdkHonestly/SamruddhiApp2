package com.samruddhi.trading.equities.domain;

public class Order {
    String AccountID;
    String Symbol;
    String Quantity;
    String OrderType = "Limit";
    double limitPrice;
    String TradeAction = "BUY";
    String TimeInForce = " { \"Duration\": \"DAY\"  }";
    String route = "\"Route\": \"Intelligent\"";

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getTradeAction() {
        return TradeAction;
    }

    public void setTradeAction(String tradeAction) {
        TradeAction = tradeAction;
    }

    public String getTimeInForce() {
        return TimeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        TimeInForce = timeInForce;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
