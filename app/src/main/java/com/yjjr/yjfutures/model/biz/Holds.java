package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/8/10.
 */

public class Holds {

    /**
     * Account : gzw
     * ProfitPriceLine : 2000.0
     * Symbol : CLU7
     * OpenDate : 1502326485267
     * Currency : USD
     * Qty : 1
     * BuySell : 买入
     * LossPriceLine : 400.0
     * AvgPrice : 49.61
     * OrderId : 104137
     */

    private String Account;
    private double ProfitPriceLine;
    private String Symbol;
    private long OpenDate;
    private String Currency;
    private int Qty;
    private String BuySell;
    private double LossPriceLine;
    private double AvgPrice;
    private String OrderId;
    /**
     * ProfitPriceLine : 2000.0
     * MarketPrice : 49.73
     * UnrealizedPL : 519.9999999999818
     * LossPriceLine : 400.0
     * AvgPrice : 49.6
     */

    private double MarketPrice;
    private double UnrealizedPL;
    /**
     * ProfitPriceLine : 1000.0
     * SumUnrealizedPL : 330.0
     * SumQty : 3
     * MarketPrice : 1292.5
     * RivalPrice : 1291.4
     * UnrealizedPL : 110.0
     * LossPriceLine : 200.0
     * AvgPrice : 1291.4
     */

    private double RivalPrice;

    private int statue;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public double getProfitPriceLine() {
        return ProfitPriceLine;
    }

    public void setProfitPriceLine(double ProfitPriceLine) {
        this.ProfitPriceLine = ProfitPriceLine;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String Symbol) {
        this.Symbol = Symbol;
    }

    public long getOpenDate() {
        return OpenDate;
    }

    public void setOpenDate(long OpenDate) {
        this.OpenDate = OpenDate;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public String getBuySell() {
        return BuySell;
    }

    public void setBuySell(String BuySell) {
        this.BuySell = BuySell;
    }

    public double getLossPriceLine() {
        return LossPriceLine;
    }

    public void setLossPriceLine(double LossPriceLine) {
        this.LossPriceLine = LossPriceLine;
    }

    public double getAvgPrice() {
        return AvgPrice;
    }

    public void setAvgPrice(double AvgPrice) {
        this.AvgPrice = AvgPrice;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public double getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(double MarketPrice) {
        this.MarketPrice = MarketPrice;
    }

    public double getUnrealizedPL() {
        return UnrealizedPL;
    }

    public void setUnrealizedPL(double UnrealizedPL) {
        this.UnrealizedPL = UnrealizedPL;
    }

    public double getRivalPrice() {
        return RivalPrice;
    }

    public void setRivalPrice(double RivalPrice) {
        this.RivalPrice = RivalPrice;
    }

    @Override
    public String toString() {
        return "Holds{" +
                "Account='" + Account + '\'' +
                ", ProfitPriceLine=" + ProfitPriceLine +
                ", Symbol='" + Symbol + '\'' +
                ", OpenDate=" + OpenDate +
                ", Currency='" + Currency + '\'' +
                ", Qty=" + Qty +
                ", BuySell='" + BuySell + '\'' +
                ", LossPriceLine=" + LossPriceLine +
                ", AvgPrice=" + AvgPrice +
                ", OrderId='" + OrderId + '\'' +
                ", MarketPrice=" + MarketPrice +
                ", UnrealizedPL=" + UnrealizedPL +
                ", RivalPrice=" + RivalPrice +
                '}';
    }

    /**
     *  -1已失效 0正常 1已触发 2平仓中
     */
    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }
}
