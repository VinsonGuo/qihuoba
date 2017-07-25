package com.yjjr.yjfutures.model.biz;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2017/7/25.
 */

public class NumberResult {
    @SerializedName("101")
    private Info servicePhone;
    @SerializedName("102")
    private Info qq;
    @SerializedName("103")
    private Info complaintPhone;

    public Info getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(Info servicePhone) {
        this.servicePhone = servicePhone;
    }

    public Info getQq() {
        return qq;
    }

    public void setQq(Info qq) {
        this.qq = qq;
    }

    public Info getComplaintPhone() {
        return complaintPhone;
    }

    public void setComplaintPhone(Info complaintPhone) {
        this.complaintPhone = complaintPhone;
    }

    @Override
    public String toString() {
        return "NumberResult{" +
                "servicePhone=" + servicePhone +
                ", qq=" + qq +
                ", complaintPhone=" + complaintPhone +
                '}';
    }
}
