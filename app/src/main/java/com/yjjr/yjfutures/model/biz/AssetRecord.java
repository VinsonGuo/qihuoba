package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/27.
 */

public class AssetRecord {


    /**
     * tradeTime : 1503478436457
     * money : 10000000
     * assetName : 充值
     * account : 13632922343
     */

    private long tradeTime;
    private double money;
    private String assetName;
    private String account;

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AssetRecord{" +
                "tradeTime=" + tradeTime +
                ", money=" + money +
                ", assetName='" + assetName + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
