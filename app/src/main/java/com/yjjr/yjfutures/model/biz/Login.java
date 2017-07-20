package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/20.
 */

public class Login {

    /**
     * account : gzw
     * name : 郭子维
     * password : null
     */

    private String account;
    private String name;
    private String password;

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

    @Override
    public String toString() {
        return "Login{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
