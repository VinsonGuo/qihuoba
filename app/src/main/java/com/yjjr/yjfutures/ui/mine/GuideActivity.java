package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.SystemBarHelper;

public class GuideActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager = new ViewPager(mContext);
        viewPager.setId(R.id.viewpager);
        resetStatusBar();
        SystemBarHelper.immersiveStatusBar(this, 0);
        setContentView(viewPager);
        Fragment[] fragments = {
                GuideFragment.newInstance(1),
                GuideFragment.newInstance(2),
                GuideFragment.newInstance(3)};
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
    }
}
