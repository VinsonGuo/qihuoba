package com.yjjr.yjfutures.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.FastOrderSharePrefernce;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.mine.LoginActivity;
import com.yjjr.yjfutures.utils.LogUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guoziwei on 2016/11/22.
 */

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static BaseApplication sInstance;
    private List<Activity> mActivities = new ArrayList<>();
    private boolean isBackground = false;
    /**
     * 真实用户的cid
     */
    private String mTradeToken = "";
    /**
     * 模拟用户的cid
     */
    private String mDemoTradeToken = "";
    private UserInfo mUserInfo;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public boolean isBackground() {
        return isBackground;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        LogUtils.init();
        initUmeng();
        JodaTimeAndroid.init(this);
        registerActivityLifecycleCallbacks(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private void initUmeng() {
        PlatformConfig.setWeixin("wx9f81e3beeffcd53c", "cb8cd620c673659ab700860dbc8b33c");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        UMShareAPI.get(this);


        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCheckDevice(false);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushCheck(true);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.onAppStart();
        MiPushRegistar.register(this, "2882303761517603946", "5871760360946");
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.d("device token -- %s", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e(s + "\n" + s1);
            }
        });
        HuaWeiRegister.register(this);
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
        if (isBackground) {
            isBackground = false;
        }
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

    @Override
    public void onTrimMemory(final int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // Get called every-time when application went to background.
            isBackground = true;
        }
    }

    public void closeApplication() {
        // 清除快速下单的信息
        FastOrderSharePrefernce.clearCache();
        for (Activity a : mActivities) {
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
        mActivities.clear();
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(UserSharePrefernce.getAccount(this)) && UserSharePrefernce.isLogin(this);
    }

    public void toLogin(Context a) {
        closeApplication();
        LoginActivity.startActivity(a);
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
        return UserSharePrefernce.getAccount(this);
    }

    public String getToken() {
        return UserSharePrefernce.getToken(this);
    }

    public String getTradeToken() {
        return mTradeToken;
    }

    public void setTradeToken(String tradeToken) {
        mTradeToken = tradeToken;
    }

    public String getTradeToken(boolean isDemo) {
        if (isDemo) {
            return mDemoTradeToken;
        }
        return mTradeToken;
    }

    public void setDemoTradeToken(String demoTradeToken) {
        mDemoTradeToken = demoTradeToken;
    }

    public void logout(Context a) {
        UserSharePrefernce.clearCache();
        mTradeToken = "";
        mDemoTradeToken = "";
        mUserInfo = null;
        toLogin(a);
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

}
