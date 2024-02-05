package com.samruddhi.trading.equities.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class BarRoot{
    @JsonProperty("Bars")
    public ArrayList<Bar> bars;
}