package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/5.
 */

public class UserLoginResponse {
    private int ReturnCode;
    private String Message;
    private String ForceUpdate;
    private String URL;
    private String HQfreshTime;
    private String Updates;
    private String SilentInstall;
    private String Version;
    private String ServerName;
    private String CompanyName;
    private String Name;
    private String cid;
    private String FailTime;
    private String FirstLoginDate;
    private int ValidDays ;
    private String ExpiredDate;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int returnCode) {
        ReturnCode = returnCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getForceUpdate() {
        return ForceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        ForceUpdate = forceUpdate;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getHQfreshTime() {
        return HQfreshTime;
    }

    public void setHQfreshTime(String HQfreshTime) {
        this.HQfreshTime = HQfreshTime;
    }

    public String getUpdates() {
        return Updates;
    }

    public void setUpdates(String updates) {
        Updates = updates;
    }

    public String getSilentInstall() {
        return SilentInstall;
    }

    public void setSilentInstall(String silentInstall) {
        SilentInstall = silentInstall;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFailTime() {
        return FailTime;
    }

    public void setFailTime(String failTime) {
        FailTime = failTime;
    }

    public String getFirstLoginDate() {
        return FirstLoginDate;
    }

    public void setFirstLoginDate(String firstLoginDate) {
        FirstLoginDate = firstLoginDate;
    }

    public int getValidDays() {
        return ValidDays;
    }

    public void setValidDays(int validDays) {
        ValidDays = validDays;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    @Override
    public String toString() {
        return "UserLoginResponse{" +
                "ReturnCode=" + ReturnCode +
                ", Message='" + Message + '\'' +
                ", ForceUpdate='" + ForceUpdate + '\'' +
                ", URL='" + URL + '\'' +
                ", HQfreshTime='" + HQfreshTime + '\'' +
                ", Updates='" + Updates + '\'' +
                ", SilentInstall='" + SilentInstall + '\'' +
                ", Version='" + Version + '\'' +
                ", ServerName='" + ServerName + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", Name='" + Name + '\'' +
                ", cid='" + cid + '\'' +
                ", FailTime='" + FailTime + '\'' +
                ", FirstLoginDate='" + FirstLoginDate + '\'' +
                ", ValidDays=" + ValidDays +
                ", ExpiredDate='" + ExpiredDate + '\'' +
                '}';
    }
}
