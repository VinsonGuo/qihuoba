package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class FullScreenChartActivity extends BaseActivity {

    private String mSymbol;
    private boolean mIsDemo;
    private NoTouchScrollViewpager mViewpager;

    public static void startActivity(Context context, String symbol, boolean isDemo) {
        Intent intent = new Intent(context, FullScreenChartActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, isDemo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_chart);
        mSymbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        mIsDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER_2, false);
        mViewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), new Fragment[]{FullScreenLineChartFragment.newInstance(mSymbol, mIsDemo),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN)}));
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewpager.setCurrentItem(0);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewpager.setCurrentItem(1);
            }
        });
    }

}
