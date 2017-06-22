package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;

public class TradeActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TradeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout root = new FrameLayout(mContext);
        root.setId(R.id.root_view);
        setContentView(root);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(root.getId(), TradeFragment.newInstance(true))
                .commitAllowingStateLoss();
       /* RadioGroup rgNav = (RadioGroup) findViewById(R.id.rg_nav);
        final NoTouchScrollViewpager viewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = {new LineChartFragment(), new LineChartFragment(), new CombinedChartFragment(), new CandleStickChartFragment()};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewpager.setOffscreenPageLimit(fragments.length);
        rgNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        viewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_chart2:
                        viewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_chart3:
                        viewpager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_chart4:
                        viewpager.setCurrentItem(3, false);
                        break;
                }
            }
        });
        ((RadioButton) rgNav.getChildAt(0)).setChecked(true);
*/
    }
}
