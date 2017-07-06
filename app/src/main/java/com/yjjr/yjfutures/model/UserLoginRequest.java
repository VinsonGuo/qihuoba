package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/5.
 */
public class UserLoginRequest {

    public UserLoginRequest( String account, String password,String software, String version) {
        Software = software;
        Password = password;
        Account = account;
        Version = version;
    }

    public UserLoginRequest() {
    }

    private String Software;

    private String Password;

    private String Account;

    private String Version;

    public String getSoftware() {
        return Software;
    }

    public void setSoftware(String Software) {
        this.Software = Software;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }


    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    @Override
    public String toString() {
        return "ClassPojo [Software = " + Software + ", Password = " + Password + ", Account = " + Account + ", Version = " + Version + "]";
    }
}
