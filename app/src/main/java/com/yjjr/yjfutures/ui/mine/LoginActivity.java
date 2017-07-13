package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.UserLoginRequest;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.MainActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginActivity extends BaseActivity {

    public static final int REQUEST_REGISTER = 10;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        ImageView iv1 = (ImageView) findViewById(R.id.iv_del1);
        ImageView iv2 = (ImageView) findViewById(R.id.iv_del2);
        TextView tvRegister = (TextView) findViewById(R.id.tv_register);
        TextView tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
//        long account = LastInputSharePrefernce.getLastAccount(mContext);
//        etUsername.setText(account == 0 ? null : String.valueOf(account));
//        etUsername.setSelection(etUsername.getText().length());
        btnLogin.setSelected(false);
        Observable.merge(RxTextView.textChanges(etUsername), RxTextView.textChanges(etPassword))
                .map(new Function<CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence) throws Exception {
                        return !TextUtils.isEmpty(etUsername.getText()) && !TextUtils.isEmpty(etPassword.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxView.selected(btnLogin));
        RxTextView.textChanges(etUsername)
                .map(new Function<CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence) throws Exception {
                        return !TextUtils.isEmpty(etUsername.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxView.visibility(iv1));
        RxTextView.textChanges(etPassword)
                .map(new Function<CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence) throws Exception {
                        return !TextUtils.isEmpty(etPassword.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxView.visibility(iv2));
        RxView.focusChanges(etUsername)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                        return aBoolean && !TextUtils.isEmpty(etUsername.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxView.visibility(iv1));
        RxView.focusChanges(etPassword)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                        return aBoolean && !TextUtils.isEmpty(etPassword.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxView.visibility(iv2));
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText(null);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setText(null);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(mContext);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FindPwdCheckActivity.startActivity(mContext);
            }
        });

    }

    private void login() {
        if (!btnLogin.isSelected()) {
            return;
        }
        btnLogin.setSelected(false);
        final String account = etUsername.getText().toString().trim();
//        LastInputSharePrefernce.setLastAccount(mContext, account);
        final String password = etPassword.getText().toString().trim();
//        final String password = StringUtils.encodePassword(etPassword.getText().toString().trim());
        UserLoginRequest model = new UserLoginRequest(account, password, "Trader", "3.29");
//        RxUtils.createSoapObservable("UserLogin", model, UserLoginResponse.class)
        HttpManager.getHttpService().userLogin(account, password)
                .map(new Function<UserLoginResponse, UserLoginResponse>() {
                    @Override
                    public UserLoginResponse apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                        if (userLoginResponse.getReturnCode() != 1) {
                            throw new RuntimeException(userLoginResponse.getMessage());
                        }
                        return userLoginResponse;
                    }
                })
                .compose(RxUtils.<UserLoginResponse>applySchedulers())
                .compose(this.<UserLoginResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<UserLoginResponse>() {
                    @Override
                    public void accept(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                        UserSharePrefernce.setLogin(mContext, true);
                        UserSharePrefernce.setAccount(mContext, account);
                        UserSharePrefernce.setPassword(mContext, password);
                        BaseApplication.getInstance().setTradeToken(userLoginResponse.getCid());
                        MainActivity.startActivity(mContext);
                        finishDelay();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        btnLogin.setSelected(true);
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
//            long account = LastInputSharePrefernce.getLastAccount(mContext);
//            etUsername.setText(account == 0 ? null : String.valueOf(account));
//            etUsername.setSelection(etUsername.getText().length());
//            etPassword.setText(null);
        }
    }

}
