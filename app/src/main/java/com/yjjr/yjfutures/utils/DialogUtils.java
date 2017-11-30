package com.yjjr.yjfutures.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.utils.FileUtils;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.biz.Active;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.mine.BindCardActivity;
import com.yjjr.yjfutures.ui.mine.LoginActivity;
import com.yjjr.yjfutures.ui.mine.RegisterActivity;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/7/26.
 */

public class DialogUtils {

    public static CustomPromptDialog createImageDialog(final Context context, final Active active) {
        FrameLayout frameLayout = new FrameLayout(context);
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setAdjustViewBounds(true);
        frameLayout.addView(iv);
        View view = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DisplayUtils.dip2px(context, 50), DisplayUtils.dip2px(context, 50), Gravity.RIGHT);
        view.setLayoutParams(params);
        frameLayout.addView(view);

        String url = active.getHtmlUrl();
        ImageLoader.load(context, (URLUtil.isHttpsUrl(url) || URLUtil.isHttpsUrl(url)) ? url : HttpConfig.BIZ_HOST + url, iv);

        final CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .setContentView(frameLayout)
                .create();
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(active.getClickUrl())) {
                    String url = active.getClickUrl();
                    WebActivity.startActivity(context, (URLUtil.isHttpsUrl(url) || URLUtil.isHttpsUrl(url)) ? url : HttpConfig.BIZ_HOST + url);
                }
            }
        });
        return dialog;
    }

    /**
     * 去注册的对话框
     */
    public static CustomPromptDialog createToRegisterDialog(final Context context, final String phone) {
        return new CustomPromptDialog.Builder(context)
                .isShowClose(true)
                .setMessageDrawableId(R.drawable.ic_info)
                .setMessage("用户不存在!")
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RegisterActivity.startActivity(context, phone);
                    }
                })
                .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public static CustomPromptDialog createToBindAlipayDialog(final Context context) {
        return new CustomPromptDialog.Builder(context)
                .setMessageDrawableId(R.drawable.ic_info)
                .setMessage(TextUtils.concat("请添加支付宝账户\n", SpannableUtil.getStringByColor(context,SpannableUtil.getStringBySize("(添加支付宝账户不会扣款)",0.8f) ,R.color.color_666666)))
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BindCardActivity.startActivity(context);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public static CustomPromptDialog createCommonDialog(final Context context, String info) {
        return new CustomPromptDialog.Builder(context)
                .isShowClose(true)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }


    public static CustomPromptDialog createReloginDialog(final Context context) {
        CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .isShowClose(false)
                .setMessageDrawableId(R.drawable.ic_info)
                .setMessage("请重新登录账号！")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!(context instanceof LoginActivity)) {
                            BaseApplication.getInstance().logout(context);
                        }
                    }
                })
                .create();
        dialog.setCancelable(false);
        return dialog;
    }

    public static CustomPromptDialog createCustomServiceDialog(final Context context) {
        return new CustomPromptDialog.Builder(context)
                .isShowClose(true)
                .setMessage(HttpConfig. SERVICE_PHONE)
                .setMessageDrawableId(R.drawable.ic_dialog_service)
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
        int android = update.getUpdateOS();
        boolean isForceUpdate = android == -1;
        CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .isShowClose(!isForceUpdate)
                .setMessageGravity(Gravity.LEFT)
                .setMessage(update.getRemark())
                .setMessageDrawableId(R.drawable.ic_update)
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



    public static CustomPromptDialog createSettingOrderDialog(final Context context, final Holds holding, final boolean isDemo) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_setting_order, null);
        final RadioGroup rgSl = (RadioGroup) v.findViewById(R.id.rg_sl);
        final RadioGroup rgSw = (RadioGroup) v.findViewById(R.id.rg_sw);
        final Disposable subscribe = HttpManager.getBizService(isDemo).getContractInfo(holding.getSymbol())
                .compose(RxUtils.<BizResponse<ContractInfo>>applyBizSchedulers())
                .subscribe(new Consumer<BizResponse<ContractInfo>>() {
                    private RadioButton createRedRadioButton(String name, Double tag) {
                        RadioButton rb = new RadioButton(context);
                        rb.setId(View.generateViewId());
                        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(DisplayUtils.dip2px(context, 46), DisplayUtils.dip2px(context, 17));
                        lp.setMargins(0, 0, DisplayUtils.dip2px(context, 8), 0);
                        rb.setLayoutParams(lp);
                        rb.setBackgroundResource(R.drawable.selector_trade_rb_red_bg);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(name);
                        rb.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_trade_rb_text_red_color));
                        rb.setTextSize(12);
                        rb.setTag(tag);
                        return rb;
                    }

                    private RadioButton createGreenRadioButton(String name, Double tag) {
                        RadioButton rb = new RadioButton(context);
                        rb.setId(View.generateViewId());
                        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(DisplayUtils.dip2px(context, 46), DisplayUtils.dip2px(context, 17));
                        lp.setMargins(0, 0, DisplayUtils.dip2px(context, 8), 0);
                        rb.setLayoutParams(lp);
                        rb.setBackgroundResource(R.drawable.selector_trade_rb_green_bg);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(name);
                        rb.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_trade_rb_text_green_color));
                        rb.setTextSize(12);
                        rb.setTag(tag);
                        return rb;
                    }

                    @Override
                    public void accept(@NonNull BizResponse<ContractInfo> response) throws Exception {
                        ContractInfo mContractInfo = response.getResult();
                        Map<String, Double> map = mContractInfo.getLossLevel();
                        for (Map.Entry<String, Double> next : map.entrySet()) {
                            double value = Double.parseDouble(next.getKey());
                            RadioButton sl = createRedRadioButton(next.getKey(), value);
                            rgSl.addView(sl);
                            if (holding.getLossPriceLine() == value) {
                                sl.setChecked(true);
                            }
                            RadioButton sw = createGreenRadioButton(next.getKey(), value);
                            rgSw.addView(sw);
                            if (holding.getProfitPriceLine() == value) {
                                sw.setChecked(true);
                            }
                        }
                        if (rgSl.getCheckedRadioButtonId() == -1) {
                            ((RadioButton) rgSl.getChildAt(1)).setChecked(true);
                        }
                        if (rgSw.getCheckedRadioButtonId() == -1) {
                            ((RadioButton) rgSw.getChildAt(1)).setChecked(true);
                        }
                    }
                }, RxUtils.commonErrorConsumer());
        final CustomPromptDialog dialog = new CustomPromptDialog.Builder(context)
                .setContentView(v)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final double sl = (double) rgSl.findViewById(rgSl.getCheckedRadioButtonId()).getTag();
                        final double sw = (double) rgSw.findViewById(rgSw.getCheckedRadioButtonId()).getTag();
                        HttpManager.getBizService(isDemo).setRiskControlTrigger(holding.getOrderId(), sl, sw)
                                .compose(RxUtils.<BizResponse<CommonResponse>>applyBizSchedulers())
                                .subscribe(new Consumer<BizResponse<CommonResponse>>() {
                                    @Override
                                    public void accept(@NonNull BizResponse<CommonResponse> r) throws Exception {
                                        ToastUtils.show(context, r.getRmsg());
                                    }
                                }, RxUtils.commonErrorConsumer());
                        dialog.dismiss();
                    }
                })
                .isShowClose(true)
                .create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!subscribe.isDisposed()) {
                    subscribe.dispose();
                }
            }
        });
        v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
