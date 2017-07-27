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
    private String mobileNo;
    private String idcard;
    private boolean identityAuth;
    private boolean existPayPwd;


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

    public boolean isIdentityAuth() {
        return identityAuth;
    }

    public void setIdentityAuth(boolean identityAuth) {
        this.identityAuth = identityAuth;
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", idcard='" + idcard + '\'' +
                ", identityAuth=" + identityAuth +
                ", existPayPwd=" + existPayPwd +
                '}';
    }
}
