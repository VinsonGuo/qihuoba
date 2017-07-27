package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.HeaderView;

public class HelpCenterActivity extends BaseActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HelpCenterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
        findViewById(R.id.tv5).setOnClickListener(this);
        findViewById(R.id.tv6).setOnClickListener(this);
        findViewById(R.id.tv7).setOnClickListener(this);
        findViewById(R.id.tv8).setOnClickListener(this);
        findViewById(R.id.tv9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT);
                break;
            case R.id.tv2:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT1);
                break;
            case R.id.tv3:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT2);
                break;
            case R.id.tv4:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT3);
                break;
            case R.id.tv5:
                WebActivity.startActivity(mContext, HttpConfig.URL_DISCLAIMER);
                break;
            case R.id.tv6:
                WebActivity.startActivity(mContext, HttpConfig.URL_DISCLOSURE);
                break;
            case R.id.tv7:
                WebActivity.startActivity(mContext, HttpConfig.URL_POLICY);
                break;
            case R.id.tv8:
                WebActivity.startActivity(mContext, HttpConfig.URL_SERVICE);
                break;
            case R.id.tv9:
                WebActivity.startActivity(mContext, HttpConfig.URL_SUPERVISE);
                break;


        }
    }
}
