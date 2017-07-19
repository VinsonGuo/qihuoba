package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;

public class AlterPhoneActivity extends BaseActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AlterPhoneActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_phone);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPhone = (RegisterInput) findViewById(R.id.ri_phone);
        headerView.bindActivity(mContext);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSuccessActivity.startActivity(mContext,"手机号码更改成功","手机号码",riPhone.getValue());
            }
        });
    }
}
