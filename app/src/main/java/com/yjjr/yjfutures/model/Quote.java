package com.yjjr.yjfutures.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.instacart.library.truetime.TrueTime;
import com.yjjr.yjfutures.utils.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by dell on 2017/7/6.
 */

public class Quote implements Parcelable {
    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
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
    private double LastSize;
    private double LastPrice;
    private double BidPrice;
    private double high;
    private int Multiple;
    private double low;
    private String Currency;
    /**
     * 是否持仓，根据需求加的字段
     */
    private boolean isHolding;

    public Quote() {
    }

    protected Quote(Parcel in) {
        this.askSize = in.readInt();
        this.Lastclose = in.readDouble();
        this.openinterest = in.readDouble();
        this.Change = in.readDouble();
        this.SecurityType = in.readString();
        this.TradingTime = in.readString();
        this.Vol = in.readInt();
        this.Tick = in.readDouble();
        this.open = in.readDouble();
        this.ChangeRate = in.readDouble();
        this.Exchange = in.readString();
        this.Symbolname = in.readString();
        this.AskPrice = in.readDouble();
        this.ExchangeRate = in.readDouble();
        this.BidSize = in.readInt();
        this.Symbol = in.readString();
        this.LastSize = in.readDouble();
        this.LastPrice = in.readDouble();
        this.BidPrice = in.readDouble();
        this.high = in.readDouble();
        this.Multiple = in.readInt();
        this.low = in.readDouble();
        this.Currency = in.readString();
        this.isHolding = in.readByte() != 0;
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

    public double getLastSize() {
        return LastSize;
    }

    public void setLastSize(double lastSize) {
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

    public boolean isRest() {
//        return AskPrice == -1 && BidPrice == -1;
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
    }

    /**
     * 判断是否休市，如果休市返回true
     */
    private boolean testIsRest(String timeRange) {
        DateTime now = DateUtils.nowDateTime();
        String[] times = timeRange.split("-");
        DateTime startTime = new DateTime(DateUtils.parseTime(times[0])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        DateTime endTime = new DateTime(DateUtils.parseTime(times[1])).withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).withDayOfMonth(now.getDayOfMonth());
        // 如果结束时间在开始时间之前，就加一天
        if (endTime.isBefore(startTime)) {
            endTime = endTime.plusDays(1);
        }
        return !(now.isAfter(startTime) && now.isBefore(endTime));
    }

    @Override
    public String toString() {
        return "Quote{" +
                "Symbol='" + Symbol + '\'' +
                ", Symbolname='" + Symbolname + '\'' +
                ", AskPrice=" + AskPrice +
                ", ExchangeRate=" + ExchangeRate +
                ", BidSize=" + BidSize +
                ", LastSize=" + LastSize +
                ", LastPrice=" + LastPrice +
                ", BidPrice=" + BidPrice +
                ", askSize=" + askSize +
                ", Lastclose=" + Lastclose +
                ", openinterest=" + openinterest +
                ", Change=" + Change +
                ", SecurityType='" + SecurityType + '\'' +
                ", TradingTime='" + TradingTime + '\'' +
                ", Vol=" + Vol +
                ", Tick=" + Tick +
                ", open=" + open +
                ", ChangeRate=" + ChangeRate +
                ", Exchange=" + Exchange +
                ", high=" + high +
                ", Multiple=" + Multiple +
                ", low=" + low +
                ", Currency='" + Currency + '\'' +
                ", isHolding=" + isHolding +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.askSize);
        dest.writeDouble(this.Lastclose);
        dest.writeDouble(this.openinterest);
        dest.writeDouble(this.Change);
        dest.writeString(this.SecurityType);
        dest.writeString(this.TradingTime);
        dest.writeInt(this.Vol);
        dest.writeDouble(this.Tick);
        dest.writeDouble(this.open);
        dest.writeDouble(this.ChangeRate);
        dest.writeString(this.Exchange);
        dest.writeString(this.Symbolname);
        dest.writeDouble(this.AskPrice);
        dest.writeDouble(this.ExchangeRate);
        dest.writeInt(this.BidSize);
        dest.writeString(this.Symbol);
        dest.writeDouble(this.LastSize);
        dest.writeDouble(this.LastPrice);
        dest.writeDouble(this.BidPrice);
        dest.writeDouble(this.high);
        dest.writeInt(this.Multiple);
        dest.writeDouble(this.low);
        dest.writeString(this.Currency);
        dest.writeByte(this.isHolding ? (byte) 1 : (byte) 0);
    }
}