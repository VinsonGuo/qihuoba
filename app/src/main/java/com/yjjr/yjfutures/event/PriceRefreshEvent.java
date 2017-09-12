package com.yjjr.yjfutures.event;

/**
 * 报价刷新的event
 * Created by dell on 2017/7/11.
 */

public class PriceRefreshEvent {
    private String symbol;

    public PriceRefreshEvent(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "PollRefreshEvent{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
