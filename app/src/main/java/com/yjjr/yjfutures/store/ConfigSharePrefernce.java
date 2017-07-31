package com.yjjr.yjfutures.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 存储用户信息用户的sp
 * Created by guoziwei on 2015/8/31.
 */
public class ConfigSharePrefernce {

    public static final String TOKEN_SHAREPREF_NAME = "config";
    public static final String VERSION_CODE = "version_code";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
    }

    public static void setVersionCode(Context ctx, int token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION_CODE, token);
        editor.apply();
    }

    public static int getVersionCode(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(VERSION_CODE, 0);
    }

}
