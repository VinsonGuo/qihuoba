package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.RegisterInput;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class AuthInfoActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AuthInfoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_info);
        EventBus.getDefault().register(this);
        final RegisterInput riName = (RegisterInput) findViewById(R.id.ri_name);
        final RegisterInput riCard = (RegisterInput) findViewById(R.id.ri_idcard);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        Observable.merge(RxTextView.textChanges(riName.getEtInput()), RxTextView.textChanges(riCard.getEtInput()))
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        btnConfirm.setSelected(!TextUtils.isEmpty(riCard.getValue()) && !TextUtils.isEmpty(riName.getValue()));
                    }
                });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnConfirm.isSelected()) {
                    btnConfirm.setSelected(false);
                    HttpManager.getBizService().auth(riName.getValue(), riCard.getValue())
                            .compose(RxUtils.applyBizSchedulers())
                            .compose(mContext.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe(new Consumer<BizResponse>() {
                                @Override
                                public void accept(@NonNull BizResponse bizResponse) throws Exception {
                                    AuthSuccessActivity.startActivity(mContext);
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
