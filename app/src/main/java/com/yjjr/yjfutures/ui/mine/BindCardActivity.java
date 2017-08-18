package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.UpdateUserInfoEvent;
import com.yjjr.yjfutures.model.biz.Alipay;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/7/18.
 */

public class BindCardActivity extends BaseActivity {

    @Pattern(regex = "[0-9a-zA-Z@%\\+\\-\\u4e00-\\u9fa5]*", messageResId = R.string.user_name_not_legal)
    @Length(min = 2, max = 16, trim = true, messageResId = R.string.user_name_not_legal)
    private EditText mEtName;

    @Password(scheme = Password.Scheme.ANY, min = 1, message = "请输入支付宝账号")
    private EditText mEtNumber;
    @ConfirmPassword(message = "两次输入支付宝账号不一致")
    private EditText mEtConfirmNumber;

    private Validator mValidatorNoMessage;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BindCardActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);

        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        RegisterInput riName = (RegisterInput) findViewById(R.id.ri_name);
        RegisterInput riNumber = (RegisterInput) findViewById(R.id.ri_number);
        RegisterInput riConfirmNumber = (RegisterInput) findViewById(R.id.ri_confirm_number);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            riName.setText(userInfo.getName());
            riName.setEditable(false);
        }
        mEtName = riName.getEtInput();
        mEtNumber = riNumber.getEtInput();
        mEtConfirmNumber = riConfirmNumber.getEtInput();
        mValidatorNoMessage = new Validator(this);
        mValidatorNoMessage.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                btnConfirm.setSelected(true);
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                LogUtils.d(errors.toString());
                btnConfirm.setSelected(false);
            }
        });
        Observable.merge(RxTextView.textChanges(mEtName), RxTextView.textChanges(mEtNumber), RxTextView.textChanges(mEtConfirmNumber))
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        mValidatorNoMessage.validate();
                    }

                });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnConfirm.isSelected()) {
                    return;
                }
                btnConfirm.setSelected(false);
                HttpManager.getBizService().bindAlipay(UserSharePrefernce.getAccount(mContext), mEtNumber.getText().toString().trim())
                        .compose(RxUtils.<BizResponse<Alipay>>applyBizSchedulers())
                        .compose(mContext.<BizResponse<Alipay>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<BizResponse<Alipay>>() {
                            @Override
                            public void accept(@NonNull final BizResponse<Alipay> response) throws Exception {
                                new CustomPromptDialog.Builder(mContext)
                                        .setMessage("添加支付宝账号成功")
                                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                EventBus.getDefault().post(new UpdateUserInfoEvent());
                                                dialog.dismiss();
                                                finish();
                                                if (!response.getResult().isExistPayPwd()) {
                                                    SetTradePwdActivity.startActivity(mContext);
                                                }
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(throwable);
                                ToastUtils.show(mContext, "添加失败");
                                btnConfirm.setSelected(true);
                            }
                        });
            }
        });
    }
}
