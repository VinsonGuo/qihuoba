package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/27.
 */

public class AssetRecord {

    /**
     * PAGE_ROW_NUMBER : 1
     * Money : 300
     * Account : lj
     * TradeTime : 1499237751000
     * ID : 4
     * AssetName : 记录2
     */

    private int Money;
    private String Account;
    private long TradeTime;
    private int ID;
    private String AssetName;

    public int getMoney() {
        return Money;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public long getTradeTime() {
        return TradeTime;
    }

    public void setTradeTime(long TradeTime) {
        this.TradeTime = TradeTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAssetName() {
        return AssetName;
    }

    public void setAssetName(String AssetName) {
        this.AssetName = AssetName;
    }
}
