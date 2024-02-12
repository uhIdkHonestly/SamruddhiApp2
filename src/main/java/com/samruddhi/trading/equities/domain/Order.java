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
}
