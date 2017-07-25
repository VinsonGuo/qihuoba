package com.yjjr.yjfutures.model.biz;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2017/7/25.
 */

public class ChargeResult {
    @SerializedName("201")
    private Info alipay;

    public Info getAlipay() {
        return alipay;
    }

    public void setAlipay(Info alipay) {
        this.alipay = alipay;
    }

    @Override
    public String toString() {
        return "ChargeResult{" +
                "alipay=" + alipay +
                '}';
    }
}
