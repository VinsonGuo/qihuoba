package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/31.
 */

public class Update {

    /**
     * android : -1
     * updateUrl : http://www.qihuofa.com:9300/app/android/laset
     * androidDesc : 1.新增场外期权
     * verNo : 2.0.01
     */

    private int android;
    private String updateUrl;
    private String androidDesc;
    private String verNo;

    public int getAndroid() {
        return android;
    }

    public void setAndroid(int android) {
        this.android = android;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getAndroidDesc() {
        return androidDesc;
    }

    public void setAndroidDesc(String androidDesc) {
        this.androidDesc = androidDesc;
    }

    public String getVerNo() {
        return verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo;
    }

    @Override
    public String toString() {
        return "Update{" +
                "android=" + android +
                ", updateUrl='" + updateUrl + '\'' +
                ", androidDesc='" + androidDesc + '\'' +
                ", verNo='" + verNo + '\'' +
                '}';
    }
}
