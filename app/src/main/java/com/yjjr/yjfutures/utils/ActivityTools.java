package com.yjjr.yjfutures.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.telephony.TelephonyManager;
import android.view.WindowManager;


/**
 * Created by hou on 2015/9/11.
 */
public class ActivityTools {

    private static MediaPlayer mMediaPlayer;
    /**
     * 打开后60s不提示声音
     */
    private static boolean is60Second = false;


   /* public static void playSound(Context context, @RawRes int res) {
        try {
            if (SettingSharePrefernce.isTraderSound(context)) {
                MediaPlayer.create(context, res).start();
                AudioPlayer player = new AudioPlayer();
                player.play(context, res);
//                int id = soundPool.load(context, res, 1);
//                soundPool.play(id, 1, 1, 0, 5, 1);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }*/

    static {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                is60Second = true;
            }
        }, 60 * 1000);
    }

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

    private static void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static void playSound(Context c, @RawRes int rid) {
        stop();
        if (c == null || !is60Second) return;
        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
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
}
