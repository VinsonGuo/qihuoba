package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;

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
        String oldPwd = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPwd = (RegisterInput) findViewById(R.id.ri_pwd);
        headerView.bindActivity(mContext);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSuccessActivity.startActivity(mContext, "登录密码修改成功", "登录密码", riPwd.getValue());
            }
        });
    }
}
