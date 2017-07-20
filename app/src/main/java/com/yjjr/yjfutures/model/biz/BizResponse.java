package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/20.
 */

public class BizResponse<T> {
    private int rcode;
    private T result;

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

    @Override
    public String toString() {
        return "BizResponse{" +
                "rcode=" + rcode +
                ", result=" + result +
                '}';
    }
}
