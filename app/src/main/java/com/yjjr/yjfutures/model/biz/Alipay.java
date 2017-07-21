package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/21.
 */

public class Alipay {

    /**
     * existPayPwd : false
     */

    private boolean existPayPwd;

    public boolean isExistPayPwd() {
        return existPayPwd;
    }

    public void setExistPayPwd(boolean existPayPwd) {
        this.existPayPwd = existPayPwd;
    }

    @Override
    public String toString() {
        return "Alipay{" +
                "existPayPwd=" + existPayPwd +
                '}';
    }
}
