package com.yjjr.yjfutures.contants;

import android.os.Environment;


import com.yjjr.yjfutures.ui.BaseApplication;

import java.io.File;

/**
 * 常量的配置
 * Created by guoziwei on 2016/11/23.
 */

public class Config {


    /**
     * 手机号码的正则
     */
    public static final String REG_PHONE = "^0?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 身份证号码验证
     */
    public static final String REG_IDCARD = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";

    public static final String DES_KEY = "pR3t8Bp16BeDAMErGwnditpFhf9ZY4a1";
    public static final String DES_IV = "12345678";

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

}
