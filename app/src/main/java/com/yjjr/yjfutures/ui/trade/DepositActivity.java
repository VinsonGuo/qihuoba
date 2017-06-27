package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

import java.net.Socket;

public class DepositActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DepositActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);

    }
}
