package com.yjjr.yjfutures.ui.mine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.exception.UserNotExistException;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.MainActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.RegisterInput;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginActivity extends BaseActivity {

    public static final int REQUEST_REGISTER = 10;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressDialog mLoginDialog;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginDialog = new ProgressDialog(mContext);
        mLoginDialog.setMessage(getString(R.string.login_in));
        mLoginDialog.setCancelable(false);
        findViews();
    }

    private void findViews() {
        RegisterInput riUsername = (RegisterInput) findViewById(R.id.ri_phone);
        RegisterInput riPassword = (RegisterInput) findViewById(R.id.ri_password);
        etUsername = riUsername.getEtInput();
        etPassword = riPassword.getEtInput();
        etUsername.setInputType(InputType.TYPE_CLASS_PHONE);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnLogin = (Button) findViewById(R.id.btn_login);
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
                FindPwdActivity.startActivity(mContext);
            }
        });
        findViewById(R.id.tv_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
            }
        });

    }

    private void login() {
        if (!btnLogin.isSelected()) {
            return;
        }
        btnLogin.setSelected(false);
        mLoginDialog.show();
        final String account = etUsername.getText().toString().trim();
//        LastInputSharePrefernce.setLastAccount(mContext, account);
        final String password = etPassword.getText().toString().trim();
//        final String password = StringUtils.encodePassword(etPassword.getText().toString().trim());
        HttpManager.getBizService().login(account, password)
                .flatMap(new Function<BizResponse<UserInfo>, ObservableSource<UserLoginResponse>>() {
                    @Override
                    public ObservableSource<UserLoginResponse> apply(@NonNull BizResponse<UserInfo> loginBizResponse) throws Exception {
                        if (loginBizResponse.getRcode() != 0) {
                            if (loginBizResponse.getRcode() == 90) { // 用户不存在
                                throw new UserNotExistException(loginBizResponse.getRmsg());
                            }
                            throw new RuntimeException(loginBizResponse.getRmsg());
                        }
                        BaseApplication.getInstance().setUserInfo(loginBizResponse.getResult());
                        return HttpManager.getHttpService().userLogin(account, password, ActivityTools.getIpAddressString());
                    }
                })
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
                        mLoginDialog.dismiss();
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
                        mLoginDialog.dismiss();
                        LogUtils.e(throwable);
                        btnLogin.setSelected(true);
                        if(throwable instanceof UserNotExistException) {
                            DialogUtils.createToRegisterDialog(mContext, account).show();
                        }else {
                            ToastUtils.show(mContext, throwable.getMessage());
                        }
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
