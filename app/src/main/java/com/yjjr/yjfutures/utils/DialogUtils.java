package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.Update;
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

    public static CustomPromptDialog createUpdateDialog(final Context context, final Update update) {
        int android = update.getAndroid();
        boolean isForceUpdate = android == -1;
        CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .isShowClose(!isForceUpdate)
                .setMessage(update.getAndroidDesc())
                .setMessageDrawableId(R.drawable.ic_info)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(HttpConfig.BIZ_HOST + update.getUpdateUrl()));
                        context.startActivity(i);
                    }
                })
                .create();
        dialog.setCancelable(!isForceUpdate);
        return dialog;
    }
}
