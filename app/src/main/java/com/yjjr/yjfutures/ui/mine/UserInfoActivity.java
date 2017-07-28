package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.widget.HeaderView;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvName;
    private TextView mTvCard;
    private TextView mTvPwd;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvCard = (TextView) findViewById(R.id.tv_card);
        mTvPwd = (TextView) findViewById(R.id.tv_pwd);
        findViewById(R.id.tv_pay_pwd).setOnClickListener(this);
        findViewById(R.id.tv_auth).setOnClickListener(this);
        findViewById(R.id.tv_deposit).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo == null) return;
        if(userInfo.isExistPayPwd()) {
            mTvPwd.setText(R.string.modify);
        }
        if(!TextUtils.isEmpty(userInfo.getIdcard())) {
            mTvName.setText(userInfo.getName());
        }
        if(!TextUtils.isEmpty(userInfo.getAlipay())) {
            mTvCard.setText(R.string.modify);
        }
    }

    @Override
    public void onClick(View v) {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo == null) return;
        switch (v.getId()) {
            case R.id.tv_auth:
                if(!TextUtils.isEmpty(userInfo.getIdcard())) {
                    AuthInfoActivity.startActivity(mContext);
                }else {
                    AuthActivity.startActivity(mContext);
                }
                break;
            case R.id.tv_pay_pwd:
                if(userInfo.isExistPayPwd()) {
                    AlterLoginPwdActivity.startActivity(mContext, AlterLoginPwdActivity.TYPE_TRADE_PWD);
                }else {
                    SetTradePwdActivity.startActivity(mContext);
                }
                break;
            case R.id.tv_deposit:
                BindCardActivity.startActivity(mContext);
                break;
        }
    }
}
