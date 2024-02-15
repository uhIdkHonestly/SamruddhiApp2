package com.samruddhi.trading.equities.domain.getorders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrailingStop {
    @JsonProperty("Percent")
    private String percent;

    // Getters and Setters
    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}

