package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jungly.gridpasswordview.GridPasswordView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SetTradePwdActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SetTradePwdActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_trade_pwd);
        EventBus.getDefault().register(this);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final GridPasswordView pwdView = (GridPasswordView) findViewById(R.id.pwdView);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        headerView.bindActivity(mContext);
        pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                btnConfirm.setSelected(psw.length() == 6);
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnConfirm.isSelected()) {
                    final String passWord = pwdView.getPassWord();
                    if (StringUtils.isInValidTradePwd(passWord)) {
                        ToastUtils.show(mContext, R.string.trade_pwd_wrong);
                        return;
                    }
                    btnConfirm.setSelected(false);
                    HttpManager.getBizService().setPayPwd(UserSharePrefernce.getAccount(mContext), passWord, null)
                            .compose(RxUtils.applyBizSchedulers())
                            .compose(mContext.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe(new Consumer<BizResponse>() {
                                @Override
                                public void accept(@NonNull BizResponse bizResponse) throws Exception {
                                    CommonSuccessActivity.startActivity(mContext, "交易密码设置成功", "交易密码", passWord);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    LogUtils.e(throwable);
                                    btnConfirm.setSelected(true);
                                    ToastUtils.show(mContext, throwable.getMessage());
                                }
                            });
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
