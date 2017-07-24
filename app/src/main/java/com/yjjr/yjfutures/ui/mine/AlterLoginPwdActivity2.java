package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class AlterLoginPwdActivity2 extends BaseActivity {
    public static void startActivity(Context context, String oldPwd) {
        Intent intent = new Intent(context, AlterLoginPwdActivity2.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, oldPwd);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_login_pwd2);
        EventBus.getDefault().register(this);
        final String oldPwd = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPwd = (RegisterInput) findViewById(R.id.ri_pwd);
        headerView.bindActivity(mContext);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);

        riPwd.getEtInput().addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void afterTextChanged(Editable s) {
                btnConfirm.setSelected(!TextUtils.isEmpty(s));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnConfirm.isSelected()) {
                    return;
                }
                btnConfirm.setSelected(false);
                final String newPwd = riPwd.getValue();
                if(TextUtils.equals(oldPwd, newPwd)) {
                    ToastUtils.show(mContext, R.string.old_password_equals_new_password);
                    return;
                }
                HttpManager.getHttpService().changePassword(BaseApplication.getInstance().getTradeToken(), StringUtils.encodePassword(oldPwd), StringUtils.encodePassword(newPwd))
                        .map(new Function<CommonResponse, CommonResponse>() {
                            @Override
                            public CommonResponse apply(@NonNull CommonResponse commonResponse) throws Exception {
                                if(commonResponse.getReturnCode() != 1) {
                                    throw new RuntimeException(commonResponse.getMessage());
                                }
                                return commonResponse;
                            }
                        })
                        .compose(RxUtils.<CommonResponse>applySchedulers())
                        .compose(mContext.<CommonResponse>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<CommonResponse>() {
                            @Override
                            public void accept(@NonNull CommonResponse commonResponse) throws Exception {
                                CommonSuccessActivity.startActivity(mContext, "登录密码修改成功", "登录密码", newPwd);
                                // 更新密码
                                UserSharePrefernce.setPassword(mContext, newPwd);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(throwable);
                                ToastUtils.show(mContext,throwable.getMessage());
                                btnConfirm.setSelected(true);
                            }
                        });
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
