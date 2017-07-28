package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private CustomPromptDialog mLogoutDialog;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            tvPhone.setText(userInfo.getMobileNo());
        }
        mLogoutDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("您确定退出当前账号吗？")
                .isShowClose(true)
                .setMessageDrawableId(R.drawable.ic_info)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BaseApplication.getInstance().logout(mContext);
                    }
                })
                .create();
        findViewById(R.id.tv_bind_phone).setOnClickListener(this);
        findViewById(R.id.tv_login_pwd).setOnClickListener(this);
        findViewById(R.id.tv_help).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bind_phone:
                AlterPhoneActivity.startActivity(mContext);
                break;
            case R.id.tv_login_pwd:
                AlterLoginPwdActivity.startActivity(mContext, AlterLoginPwdActivity.TYPE_LOGIN_PWD);
                break;
            case R.id.tv_help:
                HelpCenterActivity.startActivity(mContext);
                break;
            case R.id.tv_logout:
                mLogoutDialog.show();
                break;
        }
    }
}
