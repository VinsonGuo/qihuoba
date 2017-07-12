package com.yjjr.yjfutures.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/7/12.
 */

public class CloseOrder implements Parcelable {

    /**
     * Account : gzw
     * OpenDate : 2017-07-12T13:54:41
     * OpenPrice : 45.86
     * Symbol : CLQ7
     * OpenBuySell : 买入
     * Qty : 1
     * CloseDate : 2017-07-12T14:01:49
     * ClosePrice : 45.87
     * RealizedPL : 9.99999999999801
     * FilledID : null
     * Symbolname : null
     * Currency : USD
     * Exchange : NYMEX
     */

    private String Account;
    private String OpenDate;
    private double OpenPrice;
    private String Symbol;
    private String OpenBuySell;
    private int Qty;
    private String CloseDate;
    private double ClosePrice;
    private double RealizedPL;
    private String FilledID;
    private String Symbolname;
    private String Currency;
    private String Exchange;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getOpenDate() {
        return OpenDate;
    }

    public void setOpenDate(String OpenDate) {
        this.OpenDate = OpenDate;
    }

    public double getOpenPrice() {
        return OpenPrice;
    }

    public void setOpenPrice(double OpenPrice) {
        this.OpenPrice = OpenPrice;
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

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public String getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(String CloseDate) {
        this.CloseDate = CloseDate;
    }

    public double getClosePrice() {
        return ClosePrice;
    }

    public void setClosePrice(double ClosePrice) {
        this.ClosePrice = ClosePrice;
    }

    public double getRealizedPL() {
        return RealizedPL;
    }

    public void setRealizedPL(double RealizedPL) {
        this.RealizedPL = RealizedPL;
    }

    public String getFilledID() {
        return FilledID;
    }

    public void setFilledID(String FilledID) {
        this.FilledID = FilledID;
    }

    public String getSymbolname() {
        return Symbolname;
    }

    public void setSymbolname(String Symbolname) {
        this.Symbolname = Symbolname;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String Exchange) {
        this.Exchange = Exchange;
    }

    @Override
    public String toString() {
        return "CloseOrder{" +
                "Account='" + Account + '\'' +
                ", OpenDate='" + OpenDate + '\'' +
                ", OpenPrice=" + OpenPrice +
                ", Symbol='" + Symbol + '\'' +
                ", OpenBuySell='" + OpenBuySell + '\'' +
                ", Qty=" + Qty +
                ", CloseDate='" + CloseDate + '\'' +
                ", ClosePrice=" + ClosePrice +
                ", RealizedPL=" + RealizedPL +
                ", FilledID='" + FilledID + '\'' +
                ", Symbolname='" + Symbolname + '\'' +
                ", Currency='" + Currency + '\'' +
                ", Exchange='" + Exchange + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Account);
        dest.writeString(this.OpenDate);
        dest.writeDouble(this.OpenPrice);
        dest.writeString(this.Symbol);
        dest.writeString(this.OpenBuySell);
        dest.writeInt(this.Qty);
        dest.writeString(this.CloseDate);
        dest.writeDouble(this.ClosePrice);
        dest.writeDouble(this.RealizedPL);
        dest.writeString(this.FilledID);
        dest.writeString(this.Symbolname);
        dest.writeString(this.Currency);
        dest.writeString(this.Exchange);
    }

    public CloseOrder() {
    }

    protected CloseOrder(Parcel in) {
        this.Account = in.readString();
        this.OpenDate = in.readString();
        this.OpenPrice = in.readDouble();
        this.Symbol = in.readString();
        this.OpenBuySell = in.readString();
        this.Qty = in.readInt();
        this.CloseDate = in.readString();
        this.ClosePrice = in.readDouble();
        this.RealizedPL = in.readDouble();
        this.FilledID = in.readString();
        this.Symbolname = in.readString();
        this.Currency = in.readString();
        this.Exchange = in.readString();
    }

    public static final Parcelable.Creator<CloseOrder> CREATOR = new Parcelable.Creator<CloseOrder>() {
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
