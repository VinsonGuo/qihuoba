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
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.mine.LoginActivity;
import com.yjjr.yjfutures.utils.SystemBarHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Subo on 15/10/12.
 */
public class SplashScreen extends BaseActivity {

    /**
     * Duration of wait
     **/
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        resetStatusBar();
        setContentView(R.layout.splash);
        SystemBarHelper.immersiveStatusBar(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        }, SPLASH_DISPLAY_LENGTH);

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
        if(BaseApplication.getInstance().isLogin()) {
            MainActivity.startActivity(mContext);
        }else {
            LoginActivity.startActivity(mContext);
        }
        finishDelay();
    }
}
