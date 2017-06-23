package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class OrderActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OrderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        NoTouchScrollViewpager viewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        headerView.bindActivity(mContext);
        Fragment[] fragments = {new PositionListFragment(), new CandleStickChartFragment()};
        String[] titles = {getString(R.string.cc), getString(R.string.js)};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
    }
}
