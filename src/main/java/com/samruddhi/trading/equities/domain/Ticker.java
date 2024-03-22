package com.samruddhi.trading.equities.domain;

import core.TickertMaster;

public class Ticker {

    private String name;
    private OptionType optionType;
    /**
     * tradeUsingOnlyStocks - true - trades stocks, only Uptrend based buys and then sell, no shorting yet
     * tradeUsingOnlyStocks - false - trades Options , calls and pust
     */
    private boolean tradeUsingOnlyStocks;

    public Ticker(String name, OptionType optionType, boolean tradeUsingOnlyStocks) {
        this.name = name;
        this.optionType = optionType;
        this.tradeUsingOnlyStocks = tradeUsingOnlyStocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public boolean isTradeUsingOnlyStocks() {
        return tradeUsingOnlyStocks;
    }

    public void setTradeUsingOnlyStocks(boolean tradeUsingOnlyStocks) {
        this.tradeUsingOnlyStocks = tradeUsingOnlyStocks;
    }
}