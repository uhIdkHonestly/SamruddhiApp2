package com.samruddhi.trading.equities.domain.getorders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeActivationRule {
    @JsonProperty("TimeUtc")
    private String timeUtc;

    // Getters and Setters
    public String getTimeUtc() {
        return timeUtc;
    }

    public void setTimeUtc(String timeUtc) {
        this.timeUtc = timeUtc;
    }
}
