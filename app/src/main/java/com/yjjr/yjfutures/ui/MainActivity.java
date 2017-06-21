package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.found.FoundFragment;
import com.yjjr.yjfutures.ui.mine.MineFragment;
import com.yjjr.yjfutures.ui.home.HomePageFragment;
import com.yjjr.yjfutures.ui.trade.TradeFragment;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class MainActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        final NoTouchScrollViewpager viewPager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = {new HomePageFragment(), TradeFragment.newInstance(false), new FoundFragment(), new MineFragment()};
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_one:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.tab_two:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.tab_three:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.tab_four:
                        viewPager.setCurrentItem(3, false);
                        break;
                }
            }
        });
    }
}
