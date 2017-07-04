package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class OrderFragment extends BaseFragment {

    private Fragment[] mFragments;
    private NoTouchScrollViewpager mViewpager;
    private TabLayout mTabLayout;

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        mTabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        mViewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);

        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mFragments = new Fragment[]{new PositionListFragment(), new SettlementListFragment()};
        String[] titles = {getString(R.string.cc), getString(R.string.js)};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), mFragments, titles));
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
