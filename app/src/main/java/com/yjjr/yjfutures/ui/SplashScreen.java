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
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.NumberResult;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.mine.GuideActivity;
import com.yjjr.yjfutures.ui.mine.LoginActivity;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SystemBarHelper;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;

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
        tvVersion.setText(String.format("V%s", BuildConfig.VERSION_NAME));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    checkPermissions();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
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
    }

    /**
     * android 6.0动态检查权限
     */
    private void checkPermissions() {
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
        if (UserSharePrefernce.isNeedShowGuide(mContext)) {
            GuideActivity.startActivity(mContext);
        } else {
            if (BaseApplication.getInstance().isLogin()) {
                MainActivity.startActivity(mContext);
            } else {
                LoginActivity.startActivity(mContext);
            }
        }
        finishDelay();
    }
}
