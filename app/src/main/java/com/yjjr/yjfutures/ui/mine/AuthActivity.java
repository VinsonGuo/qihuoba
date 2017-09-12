package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AuthActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AuthActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setSelected(true);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthInfoActivity.startActivity(mContext);
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
    }
}
