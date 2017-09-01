package com.yjjr.yjfutures.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yjjr.yjfutures.model.FastTakeOrderConfig;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;

/**
 * 存储用户信息用户的sp
 * Created by guoziwei on 2015/8/31.
 */
public class FastOrderSharePrefernce {

    public static final String TOKEN_SHAREPREF_NAME = "fast_order";
    private static Gson sGson = new Gson();

    /**
     * 设置该品种快速平仓的信息
     * @param config  取消配置传null
     */
    public static void setFastTakeOrder(Context ctx, String symbol, FastTakeOrderConfig config) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(config != null) {
            editor.putString(symbol, sGson.toJson(config));
        }else {
            editor.putString(symbol, "");
        }
        editor.apply();
    }

    public static FastTakeOrderConfig getFastTakeOrder(Context ctx, String symbol) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(TOKEN_SHAREPREF_NAME,
                Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(symbol, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            FastTakeOrderConfig config = sGson.fromJson(json, FastTakeOrderConfig.class);
            return config;
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }


    public static void clearCache() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(TOKEN_SHAREPREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }

}
