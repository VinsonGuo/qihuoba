package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/20.
 */

public class UserInfo {

    /**
     * account : gzw
     * name : 郭子维
     * password : null
     */

    private String account;
    private String name;
    private String password;
    private String alipay;
    private String mobileNo;
    private String idcard;
    private boolean existPayPwd;
    private boolean isExistUnreadNotice;
    private String emchatAccount;
    private String emchatPwd;
    private String yjEmchat;

    public String getEmchatAccount() {
        return emchatAccount;
    }

    public void setEmchatAccount(String emchatAccount) {
        this.emchatAccount = emchatAccount;
    }

    public String getEmchatPwd() {
        return emchatPwd;
    }

    public void setEmchatPwd(String emchatPwd) {
        this.emchatPwd = emchatPwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExistPayPwd() {
        return existPayPwd;
    }

    public void setExistPayPwd(boolean existPayPwd) {
        this.existPayPwd = existPayPwd;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public boolean isExistUnreadNotice() {
        return isExistUnreadNotice;
    }

    public void setExistUnreadNotice(boolean existUnreadNotice) {
        isExistUnreadNotice = existUnreadNotice;
    }

    public String getYjEmchat() {
        return yjEmchat;
    }

    public void setYjEmchat(String yjEmchat) {
        this.yjEmchat = yjEmchat;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", alipay='" + alipay + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", idcard='" + idcard + '\'' +
                ", existPayPwd=" + existPayPwd +
                ", isExistUnreadNotice=" + isExistUnreadNotice +
                ", emchatAccount='" + emchatAccount + '\'' +
                ", emchatPwd='" + emchatPwd + '\'' +
                ", yjEmchat='" + yjEmchat + '\'' +
                '}';
    }
}
