package com.samruddhi.trading.equities.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Bar{
    @JsonProperty("High")
    public String high;
    @JsonProperty("Low")
    public String low;
    @JsonProperty("Open")
    public String open;
    @JsonProperty("Close")
    public String close;
    @JsonProperty("TimeStamp")
    public Date timeStamp;
    @JsonProperty("TotalVolume")
    public String totalVolume;
    @JsonProperty("DownTicks")
    public int downTicks;
    @JsonProperty("DownVolume")
    public int downVolume;
    @JsonProperty("OpenInterest")
    public String openInterest;
    @JsonProperty("IsRealtime")
    public boolean isRealtime;
    @JsonProperty("IsEndOfHistory")
    public boolean isEndOfHistory;
    @JsonProperty("TotalTicks")
    public int totalTicks;
    @JsonProperty("UnchangedTicks")
    public int unchangedTicks;
    @JsonProperty("UnchangedVolume")
    public int unchangedVolume;
    @JsonProperty("UpTicks")
    public int upTicks;
    @JsonProperty("UpVolume")
    public int upVolume;
    @JsonProperty("Epoch")
    public Object epoch;
    @JsonProperty("BarStatus")
    public String barStatus;

    public Bar(double price) {
      this.closeNumeric = closeNumeric;
    }

    public double closeNumeric = 0.0;

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

   public void setOpen(String open) {
        this.open = open;
    }

    public double getClose() {
        if(closeNumeric == 0.0)
            closeNumeric = Double.parseDouble(close);
        return closeNumeric;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getDownTicks() {
        return downTicks;
    }

    public void setDownTicks(int downTicks) {
        this.downTicks = downTicks;
    }

    public int getDownVolume() {
        return downVolume;
    }

    public void setDownVolume(int downVolume) {
        this.downVolume = downVolume;
    }

    public String getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(String openInterest) {
        this.openInterest = openInterest;
    }

    public boolean isRealtime() {
        return isRealtime;
    }

    public void setRealtime(boolean realtime) {
        isRealtime = realtime;
    }

    public boolean isEndOfHistory() {
        return isEndOfHistory;
    }

    public void setEndOfHistory(boolean endOfHistory) {
        isEndOfHistory = endOfHistory;
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }

    public int getUnchangedTicks() {
        return unchangedTicks;
    }

    public void setUnchangedTicks(int unchangedTicks) {
        this.unchangedTicks = unchangedTicks;
    }

    public int getUnchangedVolume() {
        return unchangedVolume;
    }

    public void setUnchangedVolume(int unchangedVolume) {
        this.unchangedVolume = unchangedVolume;
    }

    public int getUpTicks() {
        return upTicks;
    }

    public void setUpTicks(int upTicks) {
        this.upTicks = upTicks;
    }

    public int getUpVolume() {
        return upVolume;
    }

    public void setUpVolume(int upVolume) {
        this.upVolume = upVolume;
    }

    public Object getEpoch() {
        return epoch;
    }

    public void setEpoch(Object epoch) {
        this.epoch = epoch;
    }

    public String getBarStatus() {
        return barStatus;
    }

    public void setBarStatus(String barStatus) {
        this.barStatus = barStatus;
    }
}


