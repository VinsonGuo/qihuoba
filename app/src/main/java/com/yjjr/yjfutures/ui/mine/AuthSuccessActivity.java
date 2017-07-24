package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class AuthSuccessActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AuthSuccessActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_success);
        findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FinishEvent());
                finish();
            }
        });
    }
}
