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

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.biz.Active;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.WebActivity;
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
import zhy.com.highlight.HighLight;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;

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
                .setMessage(HttpConfig.SERVICE_PHONE)
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
                            rgSl.addView(createRedRadioButton(next.getKey(), Double.parseDouble(next.getKey())));
                            rgSw.addView(createGreenRadioButton(next.getKey(), Double.parseDouble(next.getKey())));
                        }
                        ((RadioButton) rgSl.getChildAt(1)).setChecked(true);
                        ((RadioButton) rgSw.getChildAt(1)).setChecked(true);
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

    public static void showGuideView(final Activity activity, final View view) {
        //test
      /*  final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(view)
//                .setFullingViewId(R.id.tv_title1)
                .setAlpha(150)
                .setHighTargetCorner(20)
//                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                view.performClick();
            }
        });

        builder1.addComponent(new SimpleComponent());
        final Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        view.post(new Runnable() {
            @Override
            public void run() {
                guide.show(activity);
            }
        });*/
    }

    public static void showGuideView(final Activity activity, final int... views) {
        final HighLight mHightLight = new HighLight(activity)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(false)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果 而且使下方点击回调失效
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        Toast.makeText(activity, "clicked and remove HightLight view by yourself", Toast.LENGTH_SHORT).show();
                    }
                });
        for (int v : views) {
            mHightLight.addHighLight(v, R.layout.item_market_price, new OnLeftPosCallback(45), new RectLightShape());
        }
        mHightLight.show();

//        //added by isanwenyu@163.com 设置监听器只有最后一个添加到HightLightView的knownView响应了事件
//        //优化在布局中声明onClick方法 {@link #clickKnown(view)}响应所有R.id.iv_known的控件的点击事件
//        View decorLayout = mHightLight.getHightLightView();
//        ImageView knownView = (ImageView) decorLayout.findViewById(R.id.iv_known);
//        knownView.setOnClickListener(new View.OnClickListener()
//          {
//            @Override
//            public void onClick(View view) {
//                remove(null);
//            }
//        });
    }
}
