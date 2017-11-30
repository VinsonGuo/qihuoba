package com.yjjr.yjfutures.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.NumberResult;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.ui.mine.GuideActivity;
import com.yjjr.yjfutures.ui.mine.RegisterActivity;
import com.yjjr.yjfutures.ui.publish.PublishActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SystemBarHelper;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Subo on 15/10/12.
 */
public class SplashScreen extends BaseActivity {

    /**
     * Duration of wait
     **/
    private static final int SPLASH_DISPLAY_LENGTH = 2000;
    private ImageView mIvSplash;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {
                checkPermissions();
            }
        }
    };
    private CustomPromptDialog mCustomPromptDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        resetStatusBar();
        SystemBarHelper.immersiveStatusBar(this, 0);
        setContentView(R.layout.splash);
        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(String.format("V%s%s", BuildConfig.VERSION_NAME, BuildConfig.DEBUG ? "(测试版)" : ""));
        mIvSplash = (ImageView) findViewById(R.id.iv_splash);
        mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH);
        mCustomPromptDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage(R.string.network_not_connect)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.show(mContext, "正在重试中...");
                        requestData();
                    }
                })
                .create();
        mCustomPromptDialog.setCancelable(false);
        requestData();

    }

    private void requestData() {
        HttpManager.getBizService().getSerivceInfo()
                .compose(RxUtils.<BizResponse<NumberResult>>applyBizSchedulers())
                .compose(this.<BizResponse<NumberResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<NumberResult>>() {
                    @Override
                    public void accept(@NonNull BizResponse<NumberResult> numberResultBizResponse) throws Exception {
                        NumberResult result = numberResultBizResponse.getResult();
                        HttpConfig.SERVICE_PHONE = result.getServicePhone().getName();
                        HttpConfig.QQ = result.getQq().getName();
                        HttpConfig.COMPLAINT_PHONE = result.getComplaintPhone().getName();
                    }
                }, RxUtils.commonErrorConsumer());
        /*HttpManager.getBizService().getWelcomImg()
                .compose(RxUtils.<BizResponse<List<Info>>>applyBizSchedulers())
                .compose(this.<BizResponse<List<Info>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<List<Info>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<List<Info>> listBizResponse) throws Exception {
                        Info info = listBizResponse.getResult().get(0);
//                        ImageLoader.load(mContext, HttpConfig.BIZ_HOST + info.getName(), mIvSplash);
                        if (!BuildConfig.DEBUG) {
                            HttpConfig.IS_OPEN_TRADE = Boolean.valueOf(info.getValue());
                        }
                    }
                }, RxUtils.commonErrorConsumer());*/

        HttpManager.getBizService().checkUpdate(BuildConfig.VERSION_NAME, BuildConfig.APPLICATION_ID + "," + ActivityTools.getChannelName(mContext))
                .compose(RxUtils.<BizResponse<Update>>applyBizSchedulers())
                .compose(this.<BizResponse<Update>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Update>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Update> response) throws Exception {
                        Update result = response.getResult();
                        if (mCustomPromptDialog != null && mCustomPromptDialog.isShowing()) {
                            mCustomPromptDialog.dismiss();
                        }
                        if (result.getStatue() == 1) {
                            mHandler.removeCallbacks(mRunnable);
                            PublishActivity.startActivity(mContext);
                            HttpConfig.IS_OPEN_TRADE = false;
                            return;
                        }
                        if (result.getUpdateOS() != 0) {
                            mHandler.removeCallbacks(mRunnable);
                            CustomPromptDialog updateDialog = DialogUtils.createUpdateDialog(mContext, result);
                            updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    startActivity();
                                }
                            });
                            updateDialog.show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mHandler.removeCallbacks(mRunnable);
                        mCustomPromptDialog.show();
                    }
                });
    }

    /**
     * android 6.0动态检查权限
     */
    private void checkPermissions() {
        if (isDestroy) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    .request(
                            Manifest.permission.READ_PHONE_STATE,/*推送、统计需要*/
                           /* Manifest.permission.CAMERA,*/
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                startActivity();
                            } else {
                                AlertDialog dialog = new AlertDialog.Builder(SplashScreen.this)
                                        .setMessage(getString(R.string.permission_deny_message))
                                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                                BaseApplication.getInstance().closeApplication();
                                            }
                                        })
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                if (!dialog.isShowing()) {
                                    dialog.show();
                                }
                            }

                        }
                    });
        } else {
            startActivity();
        }
    }

    private void startActivity() {
        if (isFinishing()) {
            return;
        }
        if (ActivityTools.isNeedShowGuide(mContext)) {
            GuideActivity.startActivity(mContext);
        } else {
            if (BaseApplication.getInstance().isLogin()) {
                MainActivity.startActivity(mContext);
            } else {
                //入口改成注册
//                LoginActivity.startActivity(mContext);
                RegisterActivity.startActivity(mContext);
            }
        }
        finishDelay();
    }
}
