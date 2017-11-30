package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeActivity extends BaseActivity {

    public int mIndex;
    private NoTouchScrollViewpager mViewpager;

    public static void startActivity(Context context, String symbol, boolean isDemo) {
        Intent intent = new Intent(context, TradeActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, isDemo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        final String symbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        final boolean isDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER_2, false);
        mViewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = new Fragment[]{TradeFragment.newInstance(isDemo, symbol)};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragments[0] = TradeFragment.newInstance(isDemo, "CLU7");
                mAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
                mViewpager.setAdapter(mAdapter);
            }
        },3000);*/
    }

}
