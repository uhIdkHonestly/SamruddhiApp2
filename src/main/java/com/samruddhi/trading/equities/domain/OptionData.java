package com.samruddhi.trading.equities.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OptionData {
    @JsonProperty("Delta")
    private String delta;
    @JsonProperty("Theta")
    private String theta;
    @JsonProperty("Gamma")
    private String gamma;
    @JsonProperty("Rho")
    private String rho;
    @JsonProperty("Vega")
    private String vega;
    @JsonProperty("ImpliedVolatility")
    private String impliedVolatility;
    @JsonProperty("IntrinsicValue")
    private String intrinsicValue;
    @JsonProperty("ExtrinsicValue")
    private String extrinsicValue;
    @JsonProperty("TheoreticalValue")
    private String theoreticalValue;
    @JsonProperty("ProbabilityITM")
    private String probabilityITM;
    @JsonProperty("ProbabilityOTM")
    private String probabilityOTM;
    @JsonProperty("ProbabilityBE")
    private String probabilityBE;
    @JsonProperty("ProbabilityITM_IV")
    private String probabilityITM_IV;
    @JsonProperty("ProbabilityOTM_IV")
    private String probabilityOTM_IV;
    @JsonProperty("ProbabilityBE_IV")
    private String probabilityBE_IV;
    @JsonProperty("TheoreticalValue_IV")
    private String theoreticalValue_IV;
    @JsonProperty("DailyOpenInterest")
    private int dailyOpenInterest;
    @JsonProperty("Ask")
    private String ask;
    @JsonProperty("Bid")
    private String bid;
    @JsonProperty("Mid")
    private String mid;
    @JsonProperty("AskSize")
    private int askSize;
    @JsonProperty("BidSize")
    private int bidSize;
    @JsonProperty("Close")
    private String close;
    @JsonProperty("High")
    private String high;
    @JsonProperty("Last")
    private String last;
    @JsonProperty("Low")
    private String low;
    @JsonProperty("NetChange")
    private String netChange;
    @JsonProperty("NetChangePct")
    private String netChangePct;
    @JsonProperty("Open")
    private String open;
    @JsonProperty("PreviousClose")
    private String previousClose;
    @JsonProperty("Volume")
    private int volume;
    @JsonProperty("Side")
    private String side;
    @JsonProperty("Strikes")
    private List<String> strikes;
    @JsonProperty("Legs")
    private List<Object> legs; // Assuming empty object for simplicity, define a class if structure is known

    // Getters and setters


    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getTheta() {
        return theta;
    }

    public void setTheta(String theta) {
        this.theta = theta;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getRho() {
        return rho;
    }

    public void setRho(String rho) {
        this.rho = rho;
    }

    public String getVega() {
        return vega;
    }

    public void setVega(String vega) {
        this.vega = vega;
    }

    public String getImpliedVolatility() {
        return impliedVolatility;
    }

    public void setImpliedVolatility(String impliedVolatility) {
        this.impliedVolatility = impliedVolatility;
    }

    public String getIntrinsicValue() {
        return intrinsicValue;
    }

    public void setIntrinsicValue(String intrinsicValue) {
        this.intrinsicValue = intrinsicValue;
    }

    public String getExtrinsicValue() {
        return extrinsicValue;
    }

    public void setExtrinsicValue(String extrinsicValue) {
        this.extrinsicValue = extrinsicValue;
    }

    public String getTheoreticalValue() {
        return theoreticalValue;
    }

    public void setTheoreticalValue(String theoreticalValue) {
        this.theoreticalValue = theoreticalValue;
    }

    public String getProbabilityITM() {
        return probabilityITM;
    }

    public void setProbabilityITM(String probabilityITM) {
        this.probabilityITM = probabilityITM;
    }

    public String getProbabilityOTM() {
        return probabilityOTM;
    }

    public void setProbabilityOTM(String probabilityOTM) {
        this.probabilityOTM = probabilityOTM;
    }

    public String getProbabilityBE() {
        return probabilityBE;
    }

    public void setProbabilityBE(String probabilityBE) {
        this.probabilityBE = probabilityBE;
    }

    public String getProbabilityITM_IV() {
        return probabilityITM_IV;
    }

    public void setProbabilityITM_IV(String probabilityITM_IV) {
        this.probabilityITM_IV = probabilityITM_IV;
    }

    public String getProbabilityOTM_IV() {
        return probabilityOTM_IV;
    }

    public void setProbabilityOTM_IV(String probabilityOTM_IV) {
        this.probabilityOTM_IV = probabilityOTM_IV;
    }

    public String getProbabilityBE_IV() {
        return probabilityBE_IV;
    }

    public void setProbabilityBE_IV(String probabilityBE_IV) {
        this.probabilityBE_IV = probabilityBE_IV;
    }

    public String getTheoreticalValue_IV() {
        return theoreticalValue_IV;
    }

    public void setTheoreticalValue_IV(String theoreticalValue_IV) {
        this.theoreticalValue_IV = theoreticalValue_IV;
    }

    public int getDailyOpenInterest() {
        return dailyOpenInterest;
    }

    public void setDailyOpenInterest(int dailyOpenInterest) {
        this.dailyOpenInterest = dailyOpenInterest;
    }

    public double getAsk() {
        return Double.parseDouble(ask);
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public double getBid() {
        return Double.parseDouble(bid);
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getNetChange() {
        return netChange;
    }

    public void setNetChange(String netChange) {
        this.netChange = netChange;
    }

    public String getNetChangePct() {
        return netChangePct;
    }

    public void setNetChangePct(String netChangePct) {
        this.netChangePct = netChangePct;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public List<String> getStrikes() {
        return strikes;
    }

    public void setStrikes(List<String> strikes) {
        this.strikes = strikes;
    }

    public List<Object> getLegs() {
        return legs;
    }

    public void setLegs(List<Object> legs) {
        this.legs = legs;
    }
}

