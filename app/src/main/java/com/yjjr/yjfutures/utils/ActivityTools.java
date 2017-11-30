package com.yjjr.yjfutures.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.IpResponse;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.ConfigSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.mine.AuthActivity;
import com.yjjr.yjfutures.ui.mine.BindCardActivity;
import com.yjjr.yjfutures.ui.mine.SetTradePwdActivity;
import com.yjjr.yjfutures.ui.mine.UserInfoActivity;
import com.yjjr.yjfutures.ui.mine.WithdrawActivity;
import com.yjjr.yjfutures.ui.trade.DepositActivity;
import com.yjjr.yjfutures.utils.http.HttpManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import retrofit2.Call;


/**
 * Created by hou on 2015/9/11.
 */
public class ActivityTools {


    public static void setFullscreen(Activity a, boolean fullscreen) {
        WindowManager.LayoutParams attrs = a.getWindow().getAttributes();
        if (fullscreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        a.getWindow().setAttributes(attrs);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static float getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static void toDialer(Context context, String phone) {
        String uri = "tel:" + phone.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

    public static void share(Context context, String content, String url) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, content);
        i.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(i, content));
    }

    public static void toDeposit(Context context) {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo == null) {
            ToastUtils.show(context, R.string.please_finish_user_info);
            UserInfoActivity.startActivity(context);
            return;
        }
        if (TextUtils.isEmpty(userInfo.getIdcard())) {
            ToastUtils.show(context, R.string.please_finish_user_info);
//            UserInfoActivity.startActivity(context);
            AuthActivity.startActivity(context);
        } else if (TextUtils.isEmpty(userInfo.getAlipay())) {
//            ToastUtils.show(context, R.string.please_finish_alipay);
//            UserInfoActivity.startActivity(context);
//            BindCardActivity.startActivity(context);
            DialogUtils.createToBindAlipayDialog(context).show();
        } else {
            DepositActivity.startActivity(context);
        }
    }

    public static void toWithdraw(Context context) {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo == null) {
            ToastUtils.show(context, R.string.please_finish_user_info);
            UserInfoActivity.startActivity(context);
            return;
        }
        if (TextUtils.isEmpty(userInfo.getIdcard())) {
            ToastUtils.show(context, R.string.please_finish_user_info);
//            UserInfoActivity.startActivity(context);
            AuthActivity.startActivity(context);
        } else if (TextUtils.isEmpty(userInfo.getAlipay())) {
            ToastUtils.show(context, R.string.please_finish_alipay);
//            UserInfoActivity.startActivity(context);
            BindCardActivity.startActivity(context);
        } else if (!userInfo.isExistPayPwd()) {
            ToastUtils.show(context, R.string.please_finish_trade_pwd);
//            UserInfoActivity.startActivity(context);
            SetTradePwdActivity.startActivity(context);
        } else {
            WithdrawActivity.startActivity(context);
        }
    }

    /**
     * 通过网络获取ip，同步请求，只能从子线程调用
     */
    public static String getIpByNetwork() {
        String ip = getIpAddressString();
        Call<IpResponse> call = HttpManager.getHttpService().getIp();
        try {
            IpResponse body = call.execute().body();
            if (body != null && body.getCode() == 0) {
                ip = body.getData().getIp();
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return ip;
    }

    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isNeedShowGuide(Context context) {
        return BuildConfig.VERSION_CODE > ConfigSharePrefernce.getVersionCode(context);
    }

    public static String getDeviceAndVerson() {
        return Build.MODEL + "," + BuildConfig.VERSION_NAME + "," + BuildConfig.APPLICATION_ID;
    }

    /**
     * 获取友盟渠道名
     *
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Activity ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData.get("UMENG_CHANNEL") + "";
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.get(key) + "";
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}
