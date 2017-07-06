package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/6.
 */

public class GetSymbolsRequest {
    private String Account;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public GetSymbolsRequest() {
    }

    public GetSymbolsRequest(String account) {

        Account = account;
    }

    @Override
    public String toString() {
        return "GetSymbolsRequest{" +
                "Account='" + Account + '\'' +
                '}';
    }
}
