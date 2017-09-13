package com.yjjr.yjfutures.exception;

/**
 * 用户不存在的异常
 * Created by dell on 2017/9/13.
 */

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message) {
        super(message);
    }
}
