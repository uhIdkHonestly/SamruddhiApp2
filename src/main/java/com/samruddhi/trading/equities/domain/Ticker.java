package com.samruddhi.trading.equities.domain;

import core.TickertMaster;

public class Ticker {

    String name;
    OptionType optionType;

    public Ticker(String name,  OptionType optionType) {
        this.name = name;
        this.optionType = optionType;
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


}