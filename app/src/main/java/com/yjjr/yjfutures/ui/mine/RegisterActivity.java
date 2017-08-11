package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SmsCountDownTimer;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    @com.mobsandgeeks.saripaar.annotation.Pattern(regex = HttpConfig.REG_PHONE, messageResId = R.string.phone_num_illegal)
    private EditText mEtPhone;

    @com.mobsandgeeks.saripaar.annotation.Pattern(regex = "[0-9]{4}", messageResId = R.string.verification_type_error)
    private EditText mEtSmsCode;

    @Password(min = 6, scheme = Password.Scheme.ANY, messageResId = R.string.password_too_simple)
    private EditText mEtPassword;

    @Checked(messageResId = R.string.please_agree_and_select_protocal)
    private AppCompatCheckBox mCheckBox;

    private Validator mValidator;
    private Button mBtnConfirm;
    private TextView mOperaButton;

    private CountDownTimer mCountDownTimer;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mValidator = new Validator(mContext);
        mValidator.setValidationListener(this);
        RegisterInput riPhone = (RegisterInput) findViewById(R.id.ri_phone);
        RegisterInput riSmscode = (RegisterInput) findViewById(R.id.ri_smscode);
        RegisterInput riPassword = (RegisterInput) findViewById(R.id.ri_password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        tvInfo.setText(TextUtils.concat("如收不到验证码，请拨打", SpannableUtil.getStringByColor(mContext, "客服热线", R.color.main_color), "索取"));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
            }
        });
        mOperaButton = riSmscode.getOperaButton();
        mCountDownTimer = new SmsCountDownTimer(mOperaButton);
        mEtPhone = riPhone.getEtInput();
        mEtSmsCode = riSmscode.getEtInput();
        mEtPassword = riPassword.getEtInput();
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_check);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mValidator.validate();
            }
        });
        mEtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        mEtSmsCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final Pattern pattern = Pattern.compile(HttpConfig.REG_PHONE);
        mEtPhone.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                boolean matches = pattern.matcher(s.toString()).matches();
                mOperaButton.setEnabled(matches);

            }
        });
        findViewById(R.id.iv_back).setOnClickListener(this);
        mOperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxUtils.handleSendSms(mContext, mOperaButton, mCountDownTimer, mEtPhone.getText().toString().trim());
            }
        });
        mOperaButton.setEnabled(false);
        mBtnConfirm.setOnClickListener(this);
        findViewById(R.id.tv_agreement).setOnClickListener(this);
        Observable.merge(RxTextView.textChanges(mEtPhone), RxTextView.textChanges(mEtSmsCode), RxTextView.textChanges(mEtPassword))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        mValidator.validate();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                register();
                break;
            case R.id.tv_agreement:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT);
                break;
        }
    }

    private void register() {
        if (mBtnConfirm.isSelected()) {
            mBtnConfirm.setSelected(false);
            HttpManager.getBizService().register(mEtPhone.getText().toString(), mEtPassword.getText().toString(), mEtSmsCode.getText().toString())
                    .compose(RxUtils.applyBizSchedulers())
                    .compose(mContext.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<BizResponse>() {
                        @Override
                        public void accept(@NonNull BizResponse response) throws Exception {
                            ToastUtils.show(mContext, response.getRmsg());
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                            mBtnConfirm.setSelected(true);
                            ToastUtils.show(mContext, throwable.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onValidationSucceeded() {
        mBtnConfirm.setSelected(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        if (errors.size() > 0) {
            String message = errors.get(0).getCollatedErrorMessage(mContext);
//            ToastUtils.show(mContext, message);
        }
        mBtnConfirm.setSelected(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }
}
