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
                '}';
    }
}
