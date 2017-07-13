package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/10.
 */

public class CommonResponse {
    private String Message;
    private int ReturnCode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int returnCode) {
        ReturnCode = returnCode;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "Message='" + Message + '\'' +
                ", ReturnCode=" + ReturnCode +
                '}';
    }
}
