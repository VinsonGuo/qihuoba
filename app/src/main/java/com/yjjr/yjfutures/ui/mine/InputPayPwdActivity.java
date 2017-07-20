package com.yjjr.yjfutures.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.RxActivity;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class InputPayPwdActivity extends RxActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, InputPayPwdActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pay_pwd);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(this);
    }
}
