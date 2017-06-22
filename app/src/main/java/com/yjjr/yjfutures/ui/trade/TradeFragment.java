package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.EditTextWithControl;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsNeedBack;
    private RadioButton rbChart1;
    private RadioButton rbChart2;
    private RadioButton rbChart3;
    private RadioButton rbChart4;
    private NoTouchScrollViewpager viewpager;
    private ProgressBar pbLeft;
    private ProgressBar pbRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvCenter;
    private TextView tvBottom;




    public TradeFragment() {
        // Required empty public constructor
    }

    public static TradeFragment newInstance(boolean isNeedBack) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, isNeedBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsNeedBack = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade, container, false);
        RadioGroup rgNav = (RadioGroup) v.findViewById(R.id.rg_nav);
        rbChart1 = (RadioButton)v.findViewById( R.id.rb_chart1 );
        rbChart2 = (RadioButton)v.findViewById( R.id.rb_chart2 );
        rbChart3 = (RadioButton)v.findViewById( R.id.rb_chart3 );
        rbChart4 = (RadioButton)v.findViewById( R.id.rb_chart4 );
        viewpager = (NoTouchScrollViewpager)v.findViewById( R.id.viewpager );
        pbLeft = (ProgressBar)v.findViewById( R.id.pb_left );
        pbRight = (ProgressBar)v.findViewById( R.id.pb_right );
        tvLeft = (TextView)v.findViewById( R.id.tv_left );
        tvRight = (TextView)v.findViewById( R.id.tv_right );
        tvCenter = (TextView)v.findViewById( R.id.tv_center );
        tvBottom = (TextView)v.findViewById( R.id.tv_bottom );
        final NoTouchScrollViewpager viewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);
        Fragment[] fragments = {new LineChartFragment(), new LineChartFragment(), new CombinedChartFragment(), new CandleStickChartFragment()};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), fragments));
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
        pbLeft.setRotation(180);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
            case R.id.tv_right:
                TakeOrderActivity.startActivity(mContext);
                break;
        }
    }
}
