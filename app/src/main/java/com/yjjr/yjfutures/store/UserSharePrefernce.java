package com.yjjr.yjfutures.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yjjr.yjfutures.model.FastTakeOrderConfig;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;

/**
 * 存储用户信息用户的sp
 * Created by guoziwei on 2015/8/31.
 */
public class UserSharePrefernce {

    public static final String TOKEN_SHAREPREF_NAME = "user";
    public static final String IS_LOGIN = "is_login";
    public static final String ACCOUNT = "account";
    public static final String IS_REAL_ACCOUNT = "is_real_account";
    public static final String TOKEN = "token";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_INFO = "account_info";
    public static final String FAST_TAKE_ORDER = "fast_take_order";
    private static Gson sGson = new Gson();

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
    }

    public static void setLogin(Context ctx, boolean token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, token);
        editor.apply();
    }

    public static boolean isLogin(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public static String getAccount(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCOUNT, "");
    }

    public static void setAccount(Context ctx, String token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT, token);
        editor.commit();
    }

    public static void setRealAccount(Context ctx, boolean token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_REAL_ACCOUNT, token);
        editor.apply();
    }

    public static boolean isRealAccount(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_REAL_ACCOUNT, false);
    }


    public static void setToken(Context ctx, String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static String getToken(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN, "");
    }


    public static void setPassword(Context ctx, String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, token);
        editor.apply();
    }

    public static String getPassword(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD, "");
    }


   /* public static void setAccountInfo(Context ctx, AccountInfoContent token) {
        if (token == null) return;
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT_INFO, sGson.toJson(token));
        editor.commit();
    }

    public static AccountInfoContent getAccountInfo(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(ACCOUNT_INFO, "");
        AccountInfoContent accountInfoContent = null;
        try {
            if (!TextUtils.isEmpty(json)) {
                accountInfoContent = sGson.fromJson(json, AccountInfoContent.class);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return accountInfoContent;
    }*/


    public static void clearCache() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(TOKEN_SHAREPREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }

}
