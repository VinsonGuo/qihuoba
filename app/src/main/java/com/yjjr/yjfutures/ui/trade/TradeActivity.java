package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeActivity extends BaseActivity {

    public static void startActivity(Context context, String symbol) {
        Intent intent = new Intent(context, TradeActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        String symbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        final NoTouchScrollViewpager viewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = {TradeFragment.newInstance(false, symbol), new OrderFragment()};
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
