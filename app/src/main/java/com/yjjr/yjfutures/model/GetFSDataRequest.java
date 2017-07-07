package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/7.
 */

public class GetFSDataRequest {
    private String Symbol;
    private String Exchange;
    private String StartTime;

    public GetFSDataRequest() {
    }

    public GetFSDataRequest(String symbol, String exchange, String startTime) {
        Symbol = symbol;
        Exchange = exchange;
        StartTime = startTime;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String exchange) {
        Exchange = exchange;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    @Override
    public String toString() {
        return "GetFSDataRequest{" +
                "Symbol='" + Symbol + '\'' +
                ", Exchange='" + Exchange + '\'' +
                ", StartTime='" + StartTime + '\'' +
                '}';
    }
}
