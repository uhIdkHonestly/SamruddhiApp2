package com.samruddhi.trading.equities.domain;

public class OptionTradeAction {
    public static String getOptionTradeAction(String tradeAction) {

        if(tradeAction.equals("BUY")) {
            return "BUYTOOPEN";
        }
        else if(tradeAction.equals("SELL")) {
            return "SELLTOCLOSE";
        } else return "";
    }
}
