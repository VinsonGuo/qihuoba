package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.NestRadioGroup;
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
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), new Fragment[]{
                FullScreenLineChartFragment.newInstance(mSymbol, mIsDemo),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN5),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN15),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.HOUR),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.DAY),
                HandicapFragment.newInstance(mSymbol, mIsDemo)}));
        NestRadioGroup rgNav = (NestRadioGroup) findViewById(R.id.rg_nav);
        rgNav.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        mViewpager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_chart2:
                        mViewpager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_chart3:
                        mViewpager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_chart4:
                        mViewpager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_chart5:
                        mViewpager.setCurrentItem(4,false);
                        break;
                    case R.id.rb_chart6:
                        mViewpager.setCurrentItem(5,false);
                        break;
                    case R.id.rb_chart7:
                        mViewpager.setCurrentItem(6,false);
                        break;
                }
            }
        });
        ((RadioButton) rgNav.findViewById(R.id.rb_chart1)).setChecked(true);
    }

}
