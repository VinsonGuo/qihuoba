package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SmsCountDownTimer;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    @com.mobsandgeeks.saripaar.annotation.Pattern(regex = HttpConfig.REG_PHONE, messageResId = R.string.phone_num_illegal)
    private EditText mEtPhone;

    @com.mobsandgeeks.saripaar.annotation.Pattern(regex = "[0-9]{6}", messageResId = R.string.verification_type_error)
    private EditText mEtSmsCode;

    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.password_too_simple)
    private EditText mEtPassword;

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
        mOperaButton = riSmscode.getOperaButton();
        mCountDownTimer = new SmsCountDownTimer(mOperaButton);
        mEtPhone = riPhone.getEtInput();
        mEtSmsCode = riSmscode.getEtInput();
        mEtPassword = riPassword.getEtInput();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                mValidator.validate();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        mBtnConfirm.setEnabled(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        if (errors.size() > 0) {
            String message = errors.get(0).getCollatedErrorMessage(mContext);
            ToastUtils.show(mContext, message);
        }
        mBtnConfirm.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }
}
