package com.yjjr.yjfutures.ui.trade;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

public class TradeFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsNeedBack;
    private RadioButton rbChart1;
    private RadioButton rbChart2;
    private RadioButton rbChart3;
    private RadioButton rbChart4;
    private ProgressBar pbLeft;
    private ProgressBar pbRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvCenter;
    private TextView tvBottom;
    private CustomPromptDialog mTakeOrderDialog;
    private NoTouchScrollViewpager mViewpager;
    private RadioGroup rgNav;
    private HeaderView headerView;


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
        findViews(v);
        headerView.bindActivity(getActivity());
        Fragment[] fragments = {new LineChartFragment(), new LineChartFragment(), new CombinedChartFragment(), new CandleStickChartFragment()};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), fragments));
        mViewpager.setOffscreenPageLimit(fragments.length);
        rgNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        mViewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_chart2:
                        mViewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_chart3:
                        mViewpager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_chart4:
                        mViewpager.setCurrentItem(3, false);
                        break;
                }
            }
        });
        ((RadioButton) rgNav.getChildAt(0)).setChecked(true);
        pbLeft.setRotation(180);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        v.findViewById(R.id.tv_order).setOnClickListener(this);
        v.findViewById(R.id.tv_center).setOnClickListener(this);
        return v;
    }

    private void findViews(View v) {
        headerView = (HeaderView) v.findViewById(R.id.header_view);
        rgNav = (RadioGroup) v.findViewById(R.id.rg_nav);
        rbChart1 = (RadioButton) v.findViewById(R.id.rb_chart1);
        rbChart2 = (RadioButton) v.findViewById(R.id.rb_chart2);
        rbChart3 = (RadioButton) v.findViewById(R.id.rb_chart3);
        rbChart4 = (RadioButton) v.findViewById(R.id.rb_chart4);
        pbLeft = (ProgressBar) v.findViewById(R.id.pb_left);
        pbRight = (ProgressBar) v.findViewById(R.id.pb_right);
        tvLeft = (TextView) v.findViewById(R.id.tv_left);
        tvRight = (TextView) v.findViewById(R.id.tv_right);
        tvCenter = (TextView) v.findViewById(R.id.tv_center);
        tvBottom = (TextView) v.findViewById(R.id.tv_bottom);
        mViewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);
        mTakeOrderDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("确定要下单么")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
            case R.id.tv_right:
                if (UserSharePrefernce.isFastTakeOrder(mContext)) {
                    mTakeOrderDialog.show();
                } else {
                    TakeOrderActivity.startActivity(mContext);
                }
                break;
            case R.id.tv_order:
                OrderActivity.startActivity(mContext);
                break;
            case R.id.tv_center:
                FastTakeOrderActivity.startActivity(mContext);
                break;
        }
    }

}
