package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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

    public static CustomPromptDialog createTakeOrderSuccessDialog(final Context context, String type) {
        return new CustomPromptDialog.Builder(context)
                .setMessage(type + "委托成功")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public static CustomPromptDialog createSettingOrderDialog(final Context context, final Holds holding) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_setting_order, null);
        final RadioGroup rgSl = (RadioGroup) v.findViewById(R.id.rg_sl);
        final RadioGroup rgSw = (RadioGroup) v.findViewById(R.id.rg_sw);
        final Disposable subscribe = HttpManager.getBizService().getContractInfo(holding.getSymbol())
                .compose(RxUtils.<BizResponse<ContractInfo>>applyBizSchedulers())
                .subscribe(new Consumer<BizResponse<ContractInfo>>() {
                    private RadioButton createRadioButton(String name, Double tag) {
                        RadioButton rb = new RadioButton(context);
                        rb.setId(View.generateViewId());
                        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(DisplayUtils.dip2px(context, 46), DisplayUtils.dip2px(context, 17));
                        lp.setMargins(DisplayUtils.dip2px(context, 8), 0, 0, 0);
                        rb.setLayoutParams(lp);
                        rb.setBackgroundResource(R.drawable.selector_trade_rb_bg);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(name);
                        rb.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_trade_rb_text_color));
                        rb.setTextSize(12);
                        rb.setTag(tag);
                        return rb;
                    }
                    @Override
                    public void accept(@NonNull BizResponse<ContractInfo> response) throws Exception {
                        ContractInfo mContractInfo = response.getResult();
                        Map<String, Double> map = mContractInfo.getLossLevel();
                        for (Map.Entry<String, Double> next : map.entrySet()) {
                            rgSl.addView(createRadioButton(next.getKey(), next.getValue()));
                            rgSw.addView(createRadioButton(next.getKey(), next.getValue()));
                        }
                        ((RadioButton) rgSl.getChildAt(1)).setChecked(true);
                        ((RadioButton) rgSw.getChildAt(1)).setChecked(true);
                    }
                }, RxUtils.commonErrorConsumer());
        CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .setContentView(v)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final double sl = (double) rgSl.findViewById(rgSl.getCheckedRadioButtonId()).getTag();
                        final double sw = (double) rgSw.findViewById(rgSw.getCheckedRadioButtonId()).getTag();
                        HttpManager.getBizService().setRiskControlTrigger(holding.getOrderId(), sl,sw)
                                .compose(RxUtils.<BizResponse<CommonResponse>>applyBizSchedulers())
                                .subscribe(new Consumer<BizResponse<CommonResponse>>() {
                                    @Override
                                    public void accept(@NonNull BizResponse<CommonResponse> r) throws Exception {
                                        ToastUtils.show(context, sl+"设置成功"+sw);
                                    }
                                },RxUtils.commonErrorConsumer());
                        dialog.dismiss();
                    }
                })
                .isShowClose(true)
                .create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(!subscribe.isDisposed()) {
                    subscribe.dispose();
                }
            }
        });
        return dialog;
    }
}
