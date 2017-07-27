package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/11.
 */

public class AccountInfo {

    /**
     * Account : test001
     * AccountType : 孙账户
     * AvailableFund : 1144629.315
     * AvailableFundSpecified : true
     * ClearLine : 0.02
     * ClearLineSpecified : true
     * Credit : 1369863.014
     * CreditSpecified : true
     * Currency : CNY
     * Deposite : 0.0
     * DepositeSpecified : true
     * DepositeCredit : 0.0
     * DepositeCreditSpecified : true
     * Equity : 1144629.315
     * EquitySpecified : true
     * ExchangeRate : 7.0
     * ExchangeRateSpecified : true
     * Fee : 1200.0
     * FeeSpecified : true
     * FeeRate : 1.0
     * FeeRateSpecified : true
     * FrozenMargin : 0.0
     * FrozenMarginSpecified : true
     * IP : 139.224.8.133
     * LastdayEquity : 1506019.315
     * LastdayEquitySpecified : true
     * UserInfo : Yes
     * Margin : 0.0
     * MarginSpecified : true
     * MarginRate : 1.0
     * MarginRateSpecified : true
     * Name : 1
     * NetReturn : -362220.0
     * NetReturnSpecified : true
     * PreWithdraw : 0.0
     * PreWithdrawSpecified : true
     * RealizedPL : -360190.0
     * RealizedPLSpecified : true
     * Route :
     * Status : 冻结
     * TpLine : 0.0
     * TpLineSpecified : true
     * UnrealizedPL : 0.0
     * UnrealizedPLSpecified : true
     * Winlost :
     */

    private String Account;
    private String AccountType;
    private double AvailableFund;
    private double ClearLine;
    private double Credit;
    private String Currency;
    private double Deposite;
    private double DepositeCredit;
    private double Equity;
    private double ExchangeRate;
    private double Fee;
    private double FeeRate;
    private double FrozenMargin;
    private String IP;
    private double LastdayEquity;
    private String Login;
    private double Margin;
    private double MarginRate;
    private String Name;
    private double NetReturn;
    private double PreWithdraw;
    private double RealizedPL;
    private String Route;
    private String Status;
    private double TpLine;
    private double UnrealizedPL;
    private String Winlost;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public double getAvailableFund() {
        return AvailableFund;
    }

    public void setAvailableFund(double availableFund) {
        AvailableFund = availableFund;
    }

    public double getClearLine() {
        return ClearLine;
    }

    public void setClearLine(double clearLine) {
        ClearLine = clearLine;
    }

    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public double getDeposite() {
        return Deposite;
    }

    public void setDeposite(double deposite) {
        Deposite = deposite;
    }

    public double getDepositeCredit() {
        return DepositeCredit;
    }

    public void setDepositeCredit(double depositeCredit) {
        DepositeCredit = depositeCredit;
    }

    public double getEquity() {
        return Equity;
    }

    public void setEquity(double equity) {
        Equity = equity;
    }

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double fee) {
        Fee = fee;
    }

    public double getFeeRate() {
        return FeeRate;
    }

    public void setFeeRate(double feeRate) {
        FeeRate = feeRate;
    }

    public double getFrozenMargin() {
        return FrozenMargin;
    }

    public void setFrozenMargin(double frozenMargin) {
        FrozenMargin = frozenMargin;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public double getLastdayEquity() {
        return LastdayEquity;
    }

    public void setLastdayEquity(double lastdayEquity) {
        LastdayEquity = lastdayEquity;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public double getMargin() {
        return Margin;
    }

    public void setMargin(double margin) {
        Margin = margin;
    }

    public double getMarginRate() {
        return MarginRate;
    }

    public void setMarginRate(double marginRate) {
        MarginRate = marginRate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getNetReturn() {
        return NetReturn;
    }

    public void setNetReturn(double netReturn) {
        NetReturn = netReturn;
    }

    public double getPreWithdraw() {
        return PreWithdraw;
    }

    public void setPreWithdraw(double preWithdraw) {
        PreWithdraw = preWithdraw;
    }

    public double getRealizedPL() {
        return RealizedPL;
    }

    public void setRealizedPL(double realizedPL) {
        RealizedPL = realizedPL;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getTpLine() {
        return TpLine;
    }

    public void setTpLine(double tpLine) {
        TpLine = tpLine;
    }

    public double getUnrealizedPL() {
        return UnrealizedPL;
    }

    public void setUnrealizedPL(double unrealizedPL) {
        UnrealizedPL = unrealizedPL;
    }

    public String getWinlost() {
        return Winlost;
    }

    public void setWinlost(String winlost) {
        Winlost = winlost;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "Account='" + Account + '\'' +
                ", AccountType='" + AccountType + '\'' +
                ", AvailableFund=" + AvailableFund +
                ", ClearLine=" + ClearLine +
                ", Credit=" + Credit +
                ", Currency='" + Currency + '\'' +
                ", Deposite=" + Deposite +
                ", DepositeCredit=" + DepositeCredit +
                ", Equity=" + Equity +
                ", ExchangeRate=" + ExchangeRate +
                ", Fee=" + Fee +
                ", FeeRate=" + FeeRate +
                ", FrozenMargin=" + FrozenMargin +
                ", IP='" + IP + '\'' +
                ", LastdayEquity=" + LastdayEquity +
                ", UserInfo='" + Login + '\'' +
                ", Margin=" + Margin +
                ", MarginRate=" + MarginRate +
                ", Name='" + Name + '\'' +
                ", NetReturn=" + NetReturn +
                ", PreWithdraw=" + PreWithdraw +
                ", RealizedPL=" + RealizedPL +
                ", Route='" + Route + '\'' +
                ", Status='" + Status + '\'' +
                ", TpLine=" + TpLine +
                ", UnrealizedPL=" + UnrealizedPL +
                ", Winlost='" + Winlost + '\'' +
                '}';
    }
}
