package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class AboutUsActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(this);
        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(String.format("V%s%s", BuildConfig.VERSION_NAME, BuildConfig.DEBUG ? "(测试版)" : ""));
    }
}
