package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/9/14.
 */

public class Active {

    /**
     * clickUrl : https://www.baidu.com
     * createdDate : 1505188213243
     * activityName : 试运行活动
     * htmlUrl : /app/banner/001.png
     * statue : 0
     * acode : pilotRun2
     * type : SYX
     */

    private String clickUrl;
    private long createdDate;
    private String activityName;
    private String htmlUrl;
    private int statue;
    private String acode;
    private String type;

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Active{" +
                "clickUrl='" + clickUrl + '\'' +
                ", createdDate=" + createdDate +
                ", activityName='" + activityName + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", statue=" + statue +
                ", acode='" + acode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
