package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/11.
 */

public class Exchange {

    /**
     * Account : test001
     * Currency : EUR
     * CnyExchangeRate : 8.0
     * UsdExchangeRate : 1.09589
     */

    private String Account;
    private String Currency;
    private double CnyExchangeRate;
    private double UsdExchangeRate;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public double getCnyExchangeRate() {
        return CnyExchangeRate;
    }

    public void setCnyExchangeRate(double CnyExchangeRate) {
        this.CnyExchangeRate = CnyExchangeRate;
    }

    public double getUsdExchangeRate() {
        return UsdExchangeRate;
    }

    public void setUsdExchangeRate(double UsdExchangeRate) {
        this.UsdExchangeRate = UsdExchangeRate;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "Account='" + Account + '\'' +
                ", Currency='" + Currency + '\'' +
                ", CnyExchangeRate=" + CnyExchangeRate +
                ", UsdExchangeRate=" + UsdExchangeRate +
                '}';
    }
}
