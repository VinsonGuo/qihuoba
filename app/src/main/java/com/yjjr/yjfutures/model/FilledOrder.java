package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/11.
 */

public class FilledOrder {

    /**
     * Account : test001
     * BuySell : 买入
     * Currency : USD
     * Direction : null
     * ExcuteID : null
     * Fee : 30
     * FeeSpecified : true
     * FilledID : 500059
     * FilledQty : 1
     * FilledQtySpecified : true
     * FilledTime : 2017/7/11 9:02:14
     * IBaccount : null
     * Memo :
     * OrderID : 100032
     * Price : 44.64
     * PriceSpecified : true
     * Symbol : CLQ7
     * Symbolname : null
     * cumQty : 0
     * cumQtySpecified : true
     * fromAccount : null
     * offsetFlag :
     */

    private String Account;
    private String BuySell;
    private String Currency;
    private double Fee;
    private String FilledID;
    private int FilledQty;
    private String FilledTime;
    private String Memo;
    private String OrderID;
    private double Price;
    private String Symbol;
    private String Symbolname;
    private int cumQty;
    private String fromAccount;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getBuySell() {
        return BuySell;
    }

    public void setBuySell(String BuySell) {
        this.BuySell = BuySell;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double Fee) {
        this.Fee = Fee;
    }

    public String getFilledID() {
        return FilledID;
    }

    public void setFilledID(String FilledID) {
        this.FilledID = FilledID;
    }

    public int getFilledQty() {
        return FilledQty;
    }

    public void setFilledQty(int FilledQty) {
        this.FilledQty = FilledQty;
    }

    public String getFilledTime() {
        return FilledTime;
    }

    public void setFilledTime(String FilledTime) {
        this.FilledTime = FilledTime;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String Memo) {
        this.Memo = Memo;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }


    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    public String getSymbolname() {
        return Symbolname;
    }

    public void setSymbolname(String Symbolname) {
        this.Symbolname = Symbolname;
    }

    public int getCumQty() {
        return cumQty;
    }

    public void setCumQty(int cumQty) {
        this.cumQty = cumQty;
    }


    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    @Override
    public String toString() {
        return "FilledOrder{" +
                "Account='" + Account + '\'' +
                ", BuySell='" + BuySell + '\'' +
                ", Currency='" + Currency + '\'' +
                ", Fee=" + Fee +
                ", FilledID='" + FilledID + '\'' +
                ", FilledQty=" + FilledQty +
                ", FilledTime='" + FilledTime + '\'' +
                ", Memo='" + Memo + '\'' +
                ", OrderID='" + OrderID + '\'' +
                ", Price=" + Price +
                ", Symbol='" + Symbol + '\'' +
                ", Symbolname='" + Symbolname + '\'' +
                ", cumQty=" + cumQty +
                ", fromAccount='" + fromAccount + '\'' +
                '}';
    }
}
