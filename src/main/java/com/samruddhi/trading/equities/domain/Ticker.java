package com.samruddhi.trading.equities.domain;

import core.TickertMaster;

public class Ticker {
    String name;
    OptionType optionType;
    public Ticker(String name,  OptionType optionType) {
        this.name = name;
        this.optionType = optionType;
    }
}