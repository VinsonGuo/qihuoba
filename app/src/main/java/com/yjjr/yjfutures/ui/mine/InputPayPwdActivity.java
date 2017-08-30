package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jungly.gridpasswordview.GridPasswordView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class InputPayPwdActivity extends RxActivity {

    private RxActivity mContext;
    private GridPasswordView mPasswordView;

    public static void startActivity(Context context, String money) {
        Intent intent = new Intent(context, InputPayPwdActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, money);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_input_pay_pwd);
        final String money = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(this);
        mPasswordView = (GridPasswordView) findViewById(R.id.pwdView);
        mPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                validPwd(psw, money);
            }
        });
    }

    private void validPwd(String pwd, final String money) {
        HttpManager.getBizService().validPayPwd(UserSharePrefernce.getAccount(mContext), pwd)
                .flatMap(new Function<BizResponse, ObservableSource<BizResponse>>() {
                    @Override
                    public ObservableSource<BizResponse> apply(@NonNull BizResponse response) throws Exception {
                        if (response.getRcode() != 0) {
                            throw new PayPwdException(response.getRmsg());
                        }
                        return HttpManager.getBizService().extractApply(money, "alipay");
                    }
                })
                .compose(RxUtils.applyBizSchedulers())
                .compose(this.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse>() {
                    @Override
                    public void accept(@NonNull BizResponse response) throws Exception {
                        CommonSuccessActivity.startActivity(mContext, "申请提款完成", "提款金额", money);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        ToastUtils.show(mContext, throwable.getMessage());
                        mPasswordView.clearPassword();
                        if (!(throwable instanceof PayPwdException)) {
                            finish();
                        }
                    }
                });
    }

    public static class PayPwdException extends RuntimeException {
        public PayPwdException(String msg) {
            super(msg);
        }
    }
}
