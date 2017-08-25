package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AlterLoginPwdActivity extends BaseActivity {

    public static final int TYPE_LOGIN_PWD = 0;
    public static final int TYPE_TRADE_PWD = 1;

    private int mType = 0;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, AlterLoginPwdActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_login_pwd);
        EventBus.getDefault().register(this);
        mType = getIntent().getIntExtra(Constants.CONTENT_PARAMETER, 0);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPwd = (RegisterInput) findViewById(R.id.ri_pwd);
        headerView.bindActivity(mContext);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        EditText etPassword = riPwd.getEtInput();
        if (mType == TYPE_TRADE_PWD) {
            headerView.setMainTitle(R.string.alter_trade_password);
            riPwd.setName("输入密码");
            etPassword.setHint("请输入原交易密码");
            etPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        etPassword.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                btnConfirm.setSelected(!TextUtils.isEmpty(s));
            }
        });

        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        tvInfo.setText(TextUtils.concat("如遇到问题，请", SpannableUtil.getStringByColor(mContext, "联系客服",R.color.main_color)));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnConfirm.isSelected()) {
                    if (mType == AlterLoginPwdActivity.TYPE_TRADE_PWD) {
                        // 交易密码必须为6位
                        if (riPwd.getValue().length() != 6) {
                            ToastUtils.show(mContext, "交易密码必须为6位");
                            return;
                        }
                        if (StringUtils.isInValidTradePwd(riPwd.getValue())) {
                            ToastUtils.show(mContext, R.string.trade_pwd_wrong);
                            return;
                        }
                        AlterLoginPwdActivity2.startActivity(mContext, riPwd.getValue(), mType);
                    }else {
                        if (TextUtils.equals(riPwd.getValue(), UserSharePrefernce.getPassword(mContext))) {
                            AlterLoginPwdActivity2.startActivity(mContext, riPwd.getValue(), mType);
                        } else {
                            ToastUtils.show(mContext, "输入的密码不正确，请重试");
                        }
                    }
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
