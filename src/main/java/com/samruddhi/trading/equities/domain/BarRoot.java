package com.samruddhi.trading.equities.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BarRoot{

    public List<Bar> bars;

    @JsonCreator
    public BarRoot(@JsonProperty("Bars") List<Bar> bars) {
        this.bars = bars;
    }
    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
    }
}