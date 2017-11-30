package com.yjjr.yjfutures.model;

import com.yjjr.yjfutures.utils.DateUtils;

import org.joda.time.DateTime;

/**
 * Created by dell on 2017/7/6.
 */

public class Quote {
    private int askSize;
    private double Lastclose;
    private double openinterest;
    private double Change;
    private String SecurityType;
    private String TradingTime;
    private int Vol;
    private double Tick;
    private double open;
    private double ChangeRate;
    private String Exchange;
    private String Symbolname;
    private double AskPrice;
    private double ExchangeRate;
    private int BidSize;
    private String Symbol;
    private int LastSize;
    private double LastPrice;
    private double BidPrice;
    private double high;
    private int Multiple;
    private double low;
    private String Currency;
    private int sort;
    private String lastTime;

    private int status;
    /**
     * 是否持仓，根据需求加的字段
     */
    private boolean isHolding;

    public Quote(Quote q) {
        this.askSize = q.askSize;
        this.Lastclose = q.Lastclose;
        this.openinterest = q.openinterest;
        this.Change = q.Change;
        this.SecurityType = q.SecurityType;
        this.TradingTime = q.TradingTime;
        this.Vol = q.Vol;
        this.Tick = q.Tick;
        this.open = q.open;
        this.ChangeRate = q.ChangeRate;
        this.Exchange = q.Exchange;
        this.Symbolname = q.Symbolname;
        this.AskPrice = q.AskPrice;
        this.ExchangeRate = q.ExchangeRate;
        this.BidSize = q.BidSize;
        this.Symbol = q.Symbol;
        this.LastSize = q.LastSize;
        this.LastPrice = q.LastPrice;
        this.BidPrice = q.BidPrice;
        this.high = q.high;
        this.Multiple = q.Multiple;
        this.low = q.low;
        this.Currency = q.Currency;
        this.sort = q.sort;
        this.lastTime = q.lastTime;
        this.status = q.status;
    }

    public Quote() {
    }

    public boolean isHolding() {
        return isHolding;
    }

    public void setHolding(boolean holding) {
        isHolding = holding;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public double getLastclose() {
        return Lastclose;
    }

    public void setLastclose(double lastclose) {
        Lastclose = lastclose;
    }

    public double getOpeninterest() {
        return openinterest;
    }

    public void setOpeninterest(double openinterest) {
        this.openinterest = openinterest;
    }

    public double getChange() {
        return Change;
    }

    public void setChange(double change) {
        Change = change;
    }

    public String getSecurityType() {
        return SecurityType;
    }

    public void setSecurityType(String securityType) {
        SecurityType = securityType;
    }

    public String getTradingTime() {
        return TradingTime;
    }

    public void setTradingTime(String tradingTime) {
        TradingTime = tradingTime;
    }

    public int getVol() {
        return Vol;
    }

    public void setVol(int vol) {
        Vol = vol;
    }

    public double getTick() {
        return Tick;
    }

    public void setTick(double tick) {
        Tick = tick;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getChangeRate() {
        return ChangeRate;
    }

    public void setChangeRate(double changeRate) {
        ChangeRate = changeRate;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String exchange) {
        Exchange = exchange;
    }

    public String getSymbolname() {
        return Symbolname;
    }

    public void setSymbolname(String symbolname) {
        Symbolname = symbolname;
    }

    public double getAskPrice() {
        return AskPrice;
    }

    public void setAskPrice(double askPrice) {
        AskPrice = askPrice;
    }

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public int getBidSize() {
        return BidSize;
    }

    public void setBidSize(int bidSize) {
        BidSize = bidSize;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public int getLastSize() {
        return LastSize;
    }

    public void setLastSize(int lastSize) {
        LastSize = lastSize;
    }

    public double getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(double lastPrice) {
        LastPrice = lastPrice;
    }

    public double getBidPrice() {
        return BidPrice;
    }

    public void setBidPrice(double bidPrice) {
        BidPrice = bidPrice;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public int getMultiple() {
        return Multiple;
    }

    public void setMultiple(int multiple) {
        Multiple = multiple;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public int getTradeTimeCount() {
        if (TradingTime.contains(",")) {
            int count = 0;
            String[] timeRanges = TradingTime.split(",");
            for (int i = 0; i < timeRanges.length; i++) {
                String timeRange = timeRanges[i];
                /*if (i == 0) {
                    DateTime now = DateUtils.nowDateTime();
                    String[] times = timeRange.split("-");
                    DateTime startTime = new DateTime(DateUtils.parseTime(times[0])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
                    if (startTime.getHourOfDay() < 6) {
                        timeRange = "06:00-" + times[1];
                    }
                }*/
                count += calcTradeTime(timeRange);
            }
            return count;
        } else {
            return calcTradeTime(TradingTime);
        }
    }

    private int calcTradeTime(String timeRange) {
        DateTime now = DateUtils.nowDateTime();
        String[] times = timeRange.split("-");
        DateTime startTime = new DateTime(DateUtils.parseTime(times[0])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        DateTime endTime = new DateTime(DateUtils.parseTime(times[1])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        // 如果结束时间在开始时间之前
        if (endTime.isBefore(startTime)) {
            if (now.isBefore(startTime)) {
                startTime = startTime.minusDays(1);
            } else {
                endTime = endTime.plusDays(1);
            }
        }

        return (int) ((endTime.getMillis() - startTime.getMillis()) / 1000 / 60);
    }

    /*public boolean isRest() {
        if (AskPrice == -1 && BidPrice == -1) {
            return true;
        }
        if (TextUtils.isEmpty(TradingTime)) {
            return true;
        }
        if (TradingTime.contains(",")) {
            String[] timeRanges = TradingTime.split(",");
            for (String timeRange : timeRanges) {
                // 如果在其中的一个时间段内没休市，返回false
                if (!testIsRest(timeRange)) {
                    return false;
                }
            }
            return true;
        } else {
            return testIsRest(TradingTime);
        }
    }*/

    public boolean isRest() {
        return status == 1;
    }

    /**
     * 判断是否休市，如果休市返回true
     */
    private boolean testIsRest(String timeRange) {
        DateTime now = DateUtils.nowDateTime();
        String[] times = timeRange.split("-");
        DateTime startTime = new DateTime(DateUtils.parseTime(times[0])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        DateTime endTime = new DateTime(DateUtils.parseTime(times[1])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        // 如果结束时间在开始时间之前
        if (endTime.isBefore(startTime)) {
            if (now.isBefore(startTime)) {
                startTime = startTime.minusDays(1);
            } else {
                endTime = endTime.plusDays(1);
            }
        }
        return !(now.isAfter(startTime) && now.isBefore(endTime));
    }


    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }


    /**
     * 0: 正常   1:休市   2:连接中断
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Quote{" +
                "askSize=" + askSize +
                ", Lastclose=" + Lastclose +
                ", openinterest=" + openinterest +
                ", Change=" + Change +
                ", SecurityType='" + SecurityType + '\'' +
                ", TradingTime='" + TradingTime + '\'' +
                ", Vol=" + Vol +
                ", Tick=" + Tick +
                ", open=" + open +
                ", ChangeRate=" + ChangeRate +
                ", Exchange='" + Exchange + '\'' +
                ", Symbolname='" + Symbolname + '\'' +
                ", AskPrice=" + AskPrice +
                ", ExchangeRate=" + ExchangeRate +
                ", BidSize=" + BidSize +
                ", Symbol='" + Symbol + '\'' +
                ", LastSize=" + LastSize +
                ", LastPrice=" + LastPrice +
                ", BidPrice=" + BidPrice +
                ", high=" + high +
                ", Multiple=" + Multiple +
                ", low=" + low +
                ", Currency='" + Currency + '\'' +
                ", sort=" + sort +
                ", lastTime='" + lastTime + '\'' +
                ", isHolding=" + isHolding +
                '}';
    }
}