package com.yjjr.yjfutures.model;

/**
 * <a:Holding>
 * <a:Account>test001</a:Account>
 * <a:BuySell>买入</a:BuySell>
 * <a:Currency>USD</a:Currency>
 * <a:Margin>3522.61</a:Margin>
 * <a:MarketPrice>44.59</a:MarketPrice>
 * <a:Qty>1</a:Qty>
 * <a:RealizedPL>150</a:RealizedPL>
 * <a:Symbol>CLQ7</a:Symbol>
 * <a:UnrealizedPL>110.00000000000654</a:UnrealizedPL>
 * <a:avgPrice>44.48</a:avgPrice>
 * </a:Holding>
 * <p>
 * Public Property Account() As String 账号
 * Public Property Symbol() As String 合约代码
 * Public Property avgPrice() As Double 持仓均价
 * Public Property Qty() As Long 数量 >0 买持仓  <0 卖持仓
 * Public Property BuySell() As String 买卖
 * Public Property UnrealizedPL() As Double 浮动盈亏
 * Public Property RealizedPL() As Double 平仓盈亏
 * Public Property Margin() As Double 保证金
 * Public Property MarketPrice() As Double 市场价
 * Public Property Currency() As String 货币
 * <p>
 * Created by dell on 2017/7/10.
 */

public class Holding {
    private String Account;
    private String Symbol;
    private double avgPrice;
    private int Qty;
    private String BuySell;
    private double UnrealizedPL;
    private double RealizedPL;
    private double Margin;
    private double MarketPrice;
    private String Currency;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getBuySell() {
        return BuySell;
    }

    public void setBuySell(String buySell) {
        BuySell = buySell;
    }

    public double getUnrealizedPL() {
        return UnrealizedPL;
    }

    public void setUnrealizedPL(double unrealizedPL) {
        UnrealizedPL = unrealizedPL;
    }

    public double getRealizedPL() {
        return RealizedPL;
    }

    public void setRealizedPL(double realizedPL) {
        RealizedPL = realizedPL;
    }

    public double getMargin() {
        return Margin;
    }

    public void setMargin(double margin) {
        Margin = margin;
    }

    public double getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    @Override
    public String toString() {
        return "Holding{" +
                "Account='" + Account + '\'' +
                ", Symbol='" + Symbol + '\'' +
                ", avgPrice=" + avgPrice +
                ", Qty=" + Qty +
                ", BuySell='" + BuySell + '\'' +
                ", UnrealizedPL=" + UnrealizedPL +
                ", RealizedPL=" + RealizedPL +
                ", Margin=" + Margin +
                ", MarketPrice=" + MarketPrice +
                ", Currency='" + Currency + '\'' +
                '}';
    }
}
