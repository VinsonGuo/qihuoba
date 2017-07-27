package com.yjjr.yjfutures.model.biz;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by dell on 2017/7/27.
 */

public class ContractInfo {

    /**
     * symbol : CLU7
     * symbolName : 轻质原油1709
     * endTradeTime : 04:50
     * maxProfitMultiply : 5
     * lossLevel : {"1460.00":200,"2920.00":400,"5840.00":800,"11680.00":1600,"23360.00":3200}
     * cnyExchangeRate : 7.3
     * transactionFee : 0
     */

    private String symbol;
    private String symbolName;
    private String endTradeTime;
    private double maxProfitMultiply;
    private Map<String,Double> lossLevel;
    private double cnyExchangeRate;
    private double transactionFee;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getEndTradeTime() {
        return endTradeTime;
    }

    public void setEndTradeTime(String endTradeTime) {
        this.endTradeTime = endTradeTime;
    }

    public double getMaxProfitMultiply() {
        return maxProfitMultiply;
    }

    public void setMaxProfitMultiply(double maxProfitMultiply) {
        this.maxProfitMultiply = maxProfitMultiply;
    }

    public Map<String, Double> getLossLevel() {
        return lossLevel;
    }

    public void setLossLevel(Map<String, Double> lossLevel) {
        this.lossLevel = lossLevel;
    }

    public double getCnyExchangeRate() {
        return cnyExchangeRate;
    }

    public void setCnyExchangeRate(double cnyExchangeRate) {
        this.cnyExchangeRate = cnyExchangeRate;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    @Override
    public String toString() {
        return "ContractInfo{" +
                "symbol='" + symbol + '\'' +
                ", symbolName='" + symbolName + '\'' +
                ", endTradeTime='" + endTradeTime + '\'' +
                ", maxProfitMultiply=" + maxProfitMultiply +
                ", lossLevel=" + lossLevel +
                ", cnyExchangeRate=" + cnyExchangeRate +
                ", transactionFee=" + transactionFee +
                '}';
    }
}
