package com.yjjr.yjfutures.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.utils.ExceptionHandler;
import com.yjjr.yjfutures.utils.LogUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guoziwei on 2016/11/22.
 */

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static BaseApplication sInstance;
    private List<Activity> mActivities = new ArrayList<>();
    private String mTradeToken = "";

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        LogUtils.init();
        JodaTimeAndroid.init(this);
        registerActivityLifecycleCallbacks(this);
        CrashReport.initCrashReport(getApplicationContext(), "97d5703386", BuildConfig.DEBUG);
        Stetho.initializeWithDefaults(this);
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
//        MobclickAgent.enableEncrypt(true);
//        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
//        MobclickAgent.setCheckDevice(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.add(activity);
    }


    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }

    public void closeApplication() {
        for (Activity a : mActivities) {
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
        mActivities.clear();
    }

    public boolean isLogin() {
        return UserSharePrefernce.getAccount(this) != -1 && UserSharePrefernce.isLogin(this);
    }

    public void toLogin(Activity a) {
        closeApplication();
//        LoginActivity.startActivity(a);
    }


    public void toRegister(Activity a) {
        closeApplication();
//        LoginActivity.startActivity(a);
//        RegisterActivity.startActivity(a, LoginActivity.REQUEST_REGISTER);
    }

    public boolean isRealAccount() {
        return isLogin() && UserSharePrefernce.isRealAccount(this);
    }

    public String getAccount() {
//        return UserSharePrefernce.getAccount(this);
        return "582c5812-a757-4bcf-89bf-dc5d2be39fa0";
    }

    public String getToken() {
        return UserSharePrefernce.getToken(this);
    }

    public String getTradeToken() {
        return mTradeToken;
    }

    public void logout(Activity a) {
        UserSharePrefernce.clearCache();
        mTradeToken = "";
        toLogin(a);
    }

    public void setTradeToken(String tradeToken) {
        mTradeToken = tradeToken;
    }
}
