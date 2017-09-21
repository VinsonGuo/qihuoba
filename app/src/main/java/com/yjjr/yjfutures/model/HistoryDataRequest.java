package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/9/21.
 */

public class HistoryDataRequest {
    /** 合约代码 **/
    private String symbol;

    /** 交易所 **/
    private String exchange;

    /** 开始时间 **/
    private String starttime;

    /** 时间类型 **/
    private String datatype;

    public HistoryDataRequest() {
    }

    public HistoryDataRequest(String symbol, String exchange, String starttime, String datatype) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.starttime = starttime;
        this.datatype = datatype;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    @Override
    public String toString() {
        return "HistoryDataRequest{" +
                "symbol='" + symbol + '\'' +
                ", exchange='" + exchange + '\'' +
                ", starttime='" + starttime + '\'' +
                ", datatype='" + datatype + '\'' +
                '}';
    }
}
