package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class AuthSuccessActivity extends BaseActivity {

    public static void startActivity(Context context, String name, String value) {
        Intent intent = new Intent(context, AuthSuccessActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, name);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_success);
        String name = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        String value = getIntent().getStringExtra(Constants.CONTENT_PARAMETER_2);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvValue = (TextView) findViewById(R.id.tv_card_number);
        tvName.setText(name);
        tvValue.setText(value);
        findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FinishEvent());
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new FinishEvent());
        super.onBackPressed();
    }
}
