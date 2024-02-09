package com.samruddhi.trading.equities.domain;

import java.util.ArrayList;
import java.util.List;

public class TradeWorkerStatus {

    private String status;

    List<FinishedTrade> dailyTrades = new ArrayList<>();

    public TradeWorkerStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FinishedTrade> getDailyTrades() {
        return dailyTrades;
    }

    public void setDailyTrades(List<FinishedTrade> dailyTrades) {
        this.dailyTrades = dailyTrades;
    }

    public void addFinishedTrade(FinishedTrade finishedTrade) {
        dailyTrades.add(finishedTrade);
    }
}
