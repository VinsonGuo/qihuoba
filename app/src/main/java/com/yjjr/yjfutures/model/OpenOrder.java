package com.yjjr.yjfutures.model;

/**
 * <a:OpenOrder>
 * <a:Account>test001</a:Account>
 * <a:BuySell>买入</a:BuySell>
 * <a:FilledQty>1</a:FilledQty>
 * <a:OrderID>100004</a:OrderID>
 * <a:OrderType>市价</a:OrderType>
 * <a:Price>0</a:Price>
 * <a:Qty>1</a:Qty>
 * <a:RemoteID i:nil="true"/>
 * <a:SendTime>2017/7/10 10:05:02</a:SendTime>
 * <a:Status>已成交</a:Status>
 * <a:Symbol>CLQ7</a:Symbol>
 * <a:memo/>
 * <a:offsetFlag> </a:offsetFlag>
 * </a:OpenOrder>
 * Created by guoziwei on 2017/7/10.
 */

public class OpenOrder {
    private String Account;
    private String BuySell;
    private int FilledQty;
    private String OrderID;
    private String OrderType;
    private double Price;
    private int Qty;
    private String SendTime;
    private String Status;
    private String Symbol;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getBuySell() {
        return BuySell;
    }

    public void setBuySell(String buySell) {
        BuySell = buySell;
    }

    public int getFilledQty() {
        return FilledQty;
    }

    public void setFilledQty(int filledQty) {
        FilledQty = filledQty;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    @Override
    public String toString() {
        return "OpenOrder{" +
                "Account='" + Account + '\'' +
                ", BuySell='" + BuySell + '\'' +
                ", FilledQty=" + FilledQty +
                ", OrderID='" + OrderID + '\'' +
                ", OrderType='" + OrderType + '\'' +
                ", Price=" + Price +
                ", Qty=" + Qty +
                ", SendTime='" + SendTime + '\'' +
                ", Status='" + Status + '\'' +
                ", Symbol='" + Symbol + '\'' +
                '}';
    }
}
