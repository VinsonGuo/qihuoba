package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/20.
 */

public class BizResponse<T> {
    private int rcode;
    private T result;
    private String rmsg;

    /**
     * @return 97  请重新登录
     * 98  有其他设备正在登录，请重新登录
     * 99 cid失效
     * 90 无此账号
     */
    public int getRcode() {
        return rcode;
    }

    public void setRcode(int rcode) {
        this.rcode = rcode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getRmsg() {
        return rmsg;
    }

    public void setRmsg(String rmsg) {
        this.rmsg = rmsg;
    }

    @Override
    public String toString() {
        return "BizResponse{" +
                "rcode=" + rcode +
                ", result=" + result +
                ", rmsg='" + rmsg + '\'' +
                '}';
    }
}
