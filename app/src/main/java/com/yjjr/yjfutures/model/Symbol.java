package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/6.
 */

public class Symbol {
    private String tick;

    private String xFeeRate;

    private String Symbolname;

    private String Exchange;

    private String xMargin;

    private String tradingtime;

    private String Symbol;

    private String minfee;

    private String feetype;

    private String Currency;

    private String multiple;

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

    public String getXFeeRate() {
        return xFeeRate;
    }

    public void setXFeeRate(String xFeeRate) {
        this.xFeeRate = xFeeRate;
    }

    public String getSymbolname() {
        return Symbolname;
    }

    public void setSymbolname(String Symbolname) {
        this.Symbolname = Symbolname;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String Exchange) {
        this.Exchange = Exchange;
    }

    public String getXMargin() {
        return xMargin;
    }

    public void setXMargin(String xMargin) {
        this.xMargin = xMargin;
    }

    public String getTradingtime() {
        return tradingtime;
    }

    public void setTradingtime(String tradingtime) {
        this.tradingtime = tradingtime;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    public String getMinfee() {
        return minfee;
    }

    public void setMinfee(String minfee) {
        this.minfee = minfee;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    @Override
    public String toString() {
        return "ClassPojo [tick = " + tick + ", xFeeRate = " + xFeeRate + ", Symbolname = " + Symbolname + ", Exchange = " + Exchange + ", xMargin = " + xMargin + ", tradingtime = " + tradingtime + ", Symbol = " + Symbol + ", minfee = " + minfee + ", feetype = " + feetype + ", Currency = " + Currency + ", multiple = " + multiple + "]";
    }
}
