package com.yjjr.yjfutures.widget.listener;

import android.app.Activity;

import com.hyphenate.EMCallBack;
import com.yjjr.yjfutures.utils.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Created by dell on 2017/9/11.
 */

public class YJChat implements EMCallBack {

    private WeakReference<Activity> mReference;
    private CallBack mCallBack;

    public YJChat(Activity activity, CallBack callBack) {
        mReference = new WeakReference<>(activity);
        mCallBack = callBack;
    }

    @Override
    public void onSuccess() {
        if (mReference.get() != null && !mReference.get().isFinishing()) {
            mReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onSuccess();
                }
            });
        }
    }

    @Override
    public void onError(final int code, final String error) {
        LogUtils.e("环信错误：" + code + error);
        if (mReference.get() != null && !mReference.get().isFinishing()) {
            mReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onError(code, error);
                }
            });
        }
    }

    @Override
    public void onProgress(final int progress, final String status) {
        if (mReference.get() != null && !mReference.get().isFinishing()) {
            mReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallBack.onProgress(progress, status);
                }
            });
        }
    }

    public interface CallBack {
        public void onSuccess();

        public void onError(int code, String error);

        public void onProgress(int progress, String status);
    }
}
