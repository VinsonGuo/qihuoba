package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class TakeOrderActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TakeOrderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
    }
}
