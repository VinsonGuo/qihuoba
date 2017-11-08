package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/31.
 */

public class Update {


    private int updateOS;
    private String updateUrl;
    private String remark;
    private String verNo;
    private int statue;


    /**
     * -1 强制更新   1 可选更新 0 无更新提示
     */
    public int getUpdateOS() {
        return updateOS;
    }

    public void setUpdateOS(int updateOS) {
        this.updateOS = updateOS;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVerNo() {
        return verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo;
    }


    /**
     * 1的时候为审核状态
     */
    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    @Override
    public String toString() {
        return "Update{" +
                "updateOS=" + updateOS +
                ", updateUrl='" + updateUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", verNo='" + verNo + '\'' +
                ", statue=" + statue +
                '}';
    }
}
