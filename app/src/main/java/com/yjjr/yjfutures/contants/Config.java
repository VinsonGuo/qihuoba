package com.yjjr.yjfutures.contants;

import android.os.Environment;


import com.yjjr.yjfutures.ui.BaseApplication;

import java.io.File;

/**
 * 常量的配置
 * Created by guoziwei on 2016/11/23.
 */

public class Config {

    public static final String HOST = "http://192.168.9.156:80";
//    public static final String HOST = "http://ap.leoset.hk";
    /**
     * baseurl最后要以/结尾
     */
    public static final String BASE_URL = HOST + "/api/";

    /**
     * 在线交易的url
     */
    public static final String ONLINE_TRADE_URL = "http://10.50.0.3:9938/";
    //   public static String ONLINE_TRADE_URL = "http://139.219.232.193:9938/";

    /**
     * 手机号码的正则
     */
    public static final String REG_PHONE = "^0?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 身份证号码验证
     */
    public static final String REG_IDCARD = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";

    public static final String DES_KEY = "KVBAx159";
    public static final String DES_IV = "KVBBn159";

    /**
     * 上传照片的最大大小 512M
     */
    public static int UPLOAD_PHOTO_MAX_SIZE = 512 * 1000;

    /**
     * 发送短信验证码的间隔
     */
    public static int SMS_SECOND = 60;

    public static String getDir() {
        String dir = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/yjjr" :
                BaseApplication.getInstance().getFilesDir().getAbsolutePath() + "/yjjr";
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir;
    }


    public static String getPicDir(String fileName) {
        String dir = getDir() + "/pic";
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir + "/" + fileName;
    }

    public static String getLogDir(String fileName) {
        String dir = getDir() + "/log";
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir + "/" + fileName;
    }
}
