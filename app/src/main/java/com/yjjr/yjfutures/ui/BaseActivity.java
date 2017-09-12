package com.yjjr.yjfutures.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.ReloginDialogEvent;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.InputMethodUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SystemBarHelper;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Administrator on 2016/11/22.
 */

public class BaseActivity extends RxAppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected boolean isDestroy = false;
    protected BaseActivity mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private CustomPromptDialog mReloginDialog;

    /**
     * 是否设置默认的状态栏
     */
    private boolean mIsSetDefaultStatusBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mContext = this;
        LogUtils.d("onCreate (" + getClass().getSimpleName() + ".java:1)");
        mReloginDialog = DialogUtils.createReloginDialog(mContext);
//        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        // 在这里统一加了关闭键盘的逻辑，防止用户点击左上角返回键而键盘没有关闭
        InputMethodUtil.hiddenInputMethod(this);
    }

    public void finishDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    finish();
                }
            }
        }, 1000);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBarColor();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStatusBarColor();
    }

    /**
     * 重置statusbar，必须在setContentView之前调用
     */
    protected void resetStatusBar() {
        mIsSetDefaultStatusBar = false;
    }

    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsSetDefaultStatusBar) {
            SystemBarHelper.tintStatusBar(this, ContextCompat.getColor(this, R.color.background_dark), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReloginDialogEvent event) {
        if (mReloginDialog != null && !mReloginDialog.isShowing()) {
            BaseApplication.getInstance().clearCache();
            mReloginDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        isDestroy = true;
    }

}
