package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yjjr.yjfutures.R;


/**
 * Created by guoziwei on 15/7/7.
 */
public class ToastUtils {
    private static Toast sToast;
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    private ToastUtils() {
    }

    public static void show(final Context context, final @StringRes int id) {
        show(context, context.getString(id), null);
    }

    public static void show(final Context context, final @StringRes int id, final @StringRes int descId) {
        show(context, context.getString(id), context.getString(descId));
    }

    public static void show(final Context context, final String content) {
        show(context, content, null);
    }

    public static void show(final Context context, final String content, final String desc) {
        sMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sToast == null) {
                    sToast = Toast.makeText(context,
                            content,
                            Toast.LENGTH_SHORT);
                    View toastView = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
                    TextView tv = (TextView) toastView.findViewById(R.id.tv_title);
                    TextView tvDesc = (TextView) toastView.findViewById(R.id.tv_desc);
                    tv.setText(content);
                    tvDesc.setVisibility(TextUtils.isEmpty(desc) ? View.GONE : View.VISIBLE);
                    tvDesc.setText(desc);
                    sToast.setView(toastView);
                    sToast.setGravity(Gravity.FILL, 0, 0);
//                    sToast.setGravity(Gravity.CENTER,0,0);
                } else {
                    View toastView = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
                    TextView tv = (TextView) toastView.findViewById(R.id.tv_title);
                    TextView tvDesc = (TextView) toastView.findViewById(R.id.tv_desc);
                    tv.setText(content);
                    tvDesc.setVisibility(TextUtils.isEmpty(desc) ? View.GONE : View.VISIBLE);
                    tvDesc.setText(desc);
                    sToast.setView(toastView);
                }
                sToast.show();
            }
        });
    }

    public static void dismiss() {
        try {
            if (sToast != null) {
                sToast.cancel();
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

}

