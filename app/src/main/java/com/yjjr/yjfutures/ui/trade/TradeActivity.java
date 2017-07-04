package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TradeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        final NoTouchScrollViewpager viewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        Fragment[] fragments = {TradeFragment.newInstance(false), new OrderFragment()};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        RadioGroup rgNav = (RadioGroup) findViewById(R.id.rg_nav);
        rgNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_market:
                        viewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_order:
                        viewpager.setCurrentItem(1, false);
                        break;
                }
            }
        });
    }
}
