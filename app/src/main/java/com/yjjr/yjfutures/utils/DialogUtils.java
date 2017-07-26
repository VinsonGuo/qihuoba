package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

/**
 * Created by dell on 2017/7/26.
 */

public class DialogUtils {
    public static CustomPromptDialog createCustomServiceDialog(final Context context) {
        return new CustomPromptDialog.Builder(context)
                .isShowClose(true)
                .setMessage(HttpConfig.SERVICE_PHONE)
                .setMessageDrawableId(R.drawable.ic_found_service)
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTools.toDialer(context, HttpConfig.SERVICE_PHONE);
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
