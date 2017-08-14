package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeActivity extends BaseActivity {

    private NoTouchScrollViewpager mViewpager;
    private RadioGroup mRgNav;
    public int mIndex;

    public static void startActivity(Context context, String symbol, boolean isDemo) {
        Intent intent = new Intent(context, TradeActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, isDemo);
        if(HttpConfig.IS_OPEN_TRADE) {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        final String symbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        final boolean isDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER_2, false);
        mViewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = new Fragment[]{TradeFragment.newInstance(isDemo, symbol), OrderFragment.newInstance(isDemo)};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        mRgNav = (RadioGroup) findViewById(R.id.rg_nav);
        mRgNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_market:
                        mIndex = 0;
                        mViewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_order:
                        mIndex = 1;
                        mViewpager.setCurrentItem(1, false);
                        break;
                }
            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragments[0] = TradeFragment.newInstance(isDemo, "CLU7");
                mAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
                mViewpager.setAdapter(mAdapter);
            }
        },3000);*/
    }

    @Override
    public void onBackPressed() {
        if(mViewpager.getCurrentItem() == 0) {
            super.onBackPressed();
        }else {
            ((RadioButton)mRgNav.getChildAt(0)).setChecked(true);
        }
    }
}
