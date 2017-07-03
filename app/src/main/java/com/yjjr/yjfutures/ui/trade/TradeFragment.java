package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NestRadioGroup;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private NestRadioGroup rgNav;
    private HeaderView headerView;
    private TextView mTvKchart;
    private TopRightMenu mTopRightMenu;


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
        Fragment[] fragments = {new TickChartFragment(), new TimeSharingplanFragment(), new CombinedChartFragment(), new HandicapFragment()};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), fragments));
        mViewpager.setOffscreenPageLimit(fragments.length);
        rgNav.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        mViewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_chart2:
                        mViewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_chart4:
                        mViewpager.setCurrentItem(3, false);
                        break;
                }
                mTvKchart.setBackgroundResource(R.drawable.shape_trade_rb_bg_unchecked);
                mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.second_text_color));
            }
        });
        ((RadioButton) rgNav.getChildAt(0)).setChecked(true);
        pbLeft.setRotation(180);
        mTopRightMenu = new TopRightMenu(getActivity());

//添加菜单项
        final List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.transport, "3分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "5分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "15分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "30分钟"));
        mTopRightMenu
                .setWidth(320)      //默认宽度wrap_content
                .showIcon(false)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        rgNav.clearCheck();
                        mTvKchart.setText(menuItems.get(position).getText());
                        mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.main_text_color));
                        mTvKchart.setBackgroundResource(R.drawable.shape_trade_rb_bg_checked);
                        mViewpager.setCurrentItem(2, false);
                    }
                });
        tvCenter.setText(UserSharePrefernce.isFastTakeOrder(mContext) ? R.string.opened : R.string.closed);
        StringUtils.setOnlineTxTextStyle(tvLeft, "23.123", 1);
        StringUtils.setOnlineTxTextStyle(tvRight, "23.123", -1);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        v.findViewById(R.id.tv_order).setOnClickListener(this);
        v.findViewById(R.id.tv_center).setOnClickListener(this);
        v.findViewById(R.id.tv_close_order).setOnClickListener(this);
        v.findViewById(R.id.tv_deposit).setOnClickListener(this);
        v.findViewById(R.id.tv_kchart).setOnClickListener(this);
        return v;
    }


    private void findViews(View v) {
        headerView = (HeaderView) v.findViewById(R.id.header_view);
        rgNav = (NestRadioGroup) v.findViewById(R.id.rg_nav);
        pbLeft = (ProgressBar) v.findViewById(R.id.pb_left);
        pbRight = (ProgressBar) v.findViewById(R.id.pb_right);
        tvLeft = (TextView) v.findViewById(R.id.tv_left);
        tvRight = (TextView) v.findViewById(R.id.tv_right);
        tvCenter = (TextView) v.findViewById(R.id.tv_center);
        tvBottom = (TextView) v.findViewById(R.id.tv_bottom);
        mViewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);
        mTvKchart = (TextView) v.findViewById(R.id.tv_kchart);
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
                WebActivity.startActivity(mContext, "http://www.jd.com");
//                DepositActivity.startActivity(mContext);
                break;
            case R.id.tv_kchart:
                mTopRightMenu.showAsDropDown(mTvKchart);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
