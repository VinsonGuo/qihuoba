package com.yjjr.yjfutures.utils;


import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.yjjr.yjfutures.BuildConfig;

public class LogUtils {
    public static void init() {
        Logger.init("qihuoba")                 // default PRETTYLOGGER or use just init()
                .methodCount(1)                 // default 2
//                .hideThreadInfo()
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(1);                // default 0
    }

    public static void e(String msg, Object... args) {
        Logger.e(msg, args);
    }

    public static void e(Throwable throwable, String msg, Object... args) {
        Logger.e(throwable, msg, args);
    }

    public static void e(Throwable throwable) {
        Logger.e(throwable, null);
    }

    public static void w(String msg, Object... args) {
        Logger.w(msg, args);
    }

    public static void d(String msg, Object... args) {
        Logger.d(msg, args);
    }

    public static void v(String msg, Object... args) {
        Logger.v(msg, args);
    }

    public static void i(String msg, Object... args) {
        Logger.i(msg, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

}