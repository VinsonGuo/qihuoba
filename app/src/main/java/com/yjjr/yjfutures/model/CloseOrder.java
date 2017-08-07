package com.yjjr.yjfutures.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/7/12.
 */

public class CloseOrder implements Parcelable {

    /**
     * Account : track003
     * OpenDate : 1501817697000
     * Symbol : CLU7
     * OpenBuySell : 买入
     * RealizedPL : -5
     * fee : 140
     * ClosePrice : 48.94
     * CloseDate : 1501818680707
     * Exchange : NYMEX
     * RealizedPL_CNY : -35
     * OpenPrice : 48.9425
     * Qty : 2
     * Currency : USD
     */

    private String Account;
    private long OpenDate;
    private String Symbol;
    private String OpenBuySell;
    private double RealizedPL;
    private double fee;
    private double ClosePrice;
    private long CloseDate;
    private String Exchange;
    private double RealizedPL_CNY;
    private double OpenPrice;
    private int Qty;
    private String Currency;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public long getOpenDate() {
        return OpenDate;
    }

    public void setOpenDate(long OpenDate) {
        this.OpenDate = OpenDate;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    public String getOpenBuySell() {
        return OpenBuySell;
    }

    public void setOpenBuySell(String OpenBuySell) {
        this.OpenBuySell = OpenBuySell;
    }

    public double getRealizedPL() {
        return RealizedPL;
    }

    public void setRealizedPL(double RealizedPL) {
        this.RealizedPL = RealizedPL;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getClosePrice() {
        return ClosePrice;
    }

    public void setClosePrice(double ClosePrice) {
        this.ClosePrice = ClosePrice;
    }

    public long getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(long CloseDate) {
        this.CloseDate = CloseDate;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String Exchange) {
        this.Exchange = Exchange;
    }

    public double getRealizedPL_CNY() {
        return RealizedPL_CNY;
    }

    public void setRealizedPL_CNY(double RealizedPL_CNY) {
        this.RealizedPL_CNY = RealizedPL_CNY;
    }

    public double getOpenPrice() {
        return OpenPrice;
    }

    public void setOpenPrice(double OpenPrice) {
        this.OpenPrice = OpenPrice;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Account);
        dest.writeLong(this.OpenDate);
        dest.writeString(this.Symbol);
        dest.writeString(this.OpenBuySell);
        dest.writeDouble(this.RealizedPL);
        dest.writeDouble(this.fee);
        dest.writeDouble(this.ClosePrice);
        dest.writeLong(this.CloseDate);
        dest.writeString(this.Exchange);
        dest.writeDouble(this.RealizedPL_CNY);
        dest.writeDouble(this.OpenPrice);
        dest.writeInt(this.Qty);
        dest.writeString(this.Currency);
    }

    public CloseOrder() {
    }

    protected CloseOrder(Parcel in) {
        this.Account = in.readString();
        this.OpenDate = in.readLong();
        this.Symbol = in.readString();
        this.OpenBuySell = in.readString();
        this.RealizedPL = in.readDouble();
        this.fee = in.readDouble();
        this.ClosePrice = in.readDouble();
        this.CloseDate = in.readLong();
        this.Exchange = in.readString();
        this.RealizedPL_CNY = in.readDouble();
        this.OpenPrice = in.readDouble();
        this.Qty = in.readInt();
        this.Currency = in.readString();
    }

    public static final Creator<CloseOrder> CREATOR = new Creator<CloseOrder>() {
        @Override
        public CloseOrder createFromParcel(Parcel source) {
            return new CloseOrder(source);
        }

        @Override
        public CloseOrder[] newArray(int size) {
            return new CloseOrder[size];
        }
    };
}
