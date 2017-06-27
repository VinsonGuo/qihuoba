package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TradeFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsNeedBack;
    private ProgressBar pbLeft;
    private ProgressBar pbRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvCenter;
    private TextView tvBottom;
    private CustomPromptDialog mTakeOrderDialog;
    private CustomPromptDialog mCloseOrderDialog;
    private ProgressDialog mProgressDialog;
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
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mIsNeedBack = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade, container, false);
        findViews(v);
        headerView.bindActivity(getActivity());
        Fragment[] fragments = {new TickChartFragment(), new TimeSharingplanFragment(), new CombinedChartFragment(), new HandicapListFragment()};
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
        tvCenter.setText(UserSharePrefernce.isFastTakeOrder(mContext) ? R.string.opened : R.string.closed);
        StringUtils.setOnlineTxTextStyle(tvLeft, "23.123", 1);
        StringUtils.setOnlineTxTextStyle(tvRight, "23.123", -1);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        v.findViewById(R.id.tv_order).setOnClickListener(this);
        v.findViewById(R.id.tv_center).setOnClickListener(this);
        v.findViewById(R.id.tv_close_order).setOnClickListener(this);
        v.findViewById(R.id.tv_deposit).setOnClickListener(this);
        return v;
    }

    private void findViews(View v) {
        headerView = (HeaderView) v.findViewById(R.id.header_view);
        rgNav = (RadioGroup) v.findViewById(R.id.rg_nav);
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
                        mProgressDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                mTakeOrderDialog.dismiss();
                                ToastUtils.show(mContext, R.string.online_transaction_open_success);
                            }
                        }, 3000);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        mCloseOrderDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("确定要卖出全部持仓么")
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
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.online_transaction_in_order));
        mProgressDialog.setCancelable(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FastTakeOrderEvent event) {
        tvCenter.setText(event.isOpened() ? R.string.opened : R.string.closed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_order:
                mCloseOrderDialog.show();
                break;
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
            case R.id.tv_deposit:
                DepositActivity.startActivity(mContext);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
