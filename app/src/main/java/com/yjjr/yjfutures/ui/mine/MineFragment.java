package com.yjjr.yjfutures.ui.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * mine fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvYue;
    private TextView tvMargin;
    private TextView tvNet;
    private TextView mTvTitle;

    public MineFragment() {
        // Required empty public constructor
    }

    private void findViews(View v) {
        TextView tvOne = (TextView) v.findViewById(R.id.tv_one);
        TextView tvTwo = (TextView) v.findViewById(R.id.tv_two);
        TextView tvThree = (TextView) v.findViewById(R.id.tv_three);
        TextView tvFour = (TextView) v.findViewById(R.id.tv_four);
        tvYue = (TextView) v.findViewById(R.id.tv_yue);
        tvMargin = (TextView) v.findViewById(R.id.tv_margin);
        tvNet = (TextView) v.findViewById(R.id.tv_net);
        mTvTitle = (TextView) v.findViewById(R.id.tv_title);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                if(userInfo != null) {
                    mTvTitle.setText(userInfo.getName());
                }
                refresh.setRefreshing(false);
            }
        });

        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        EventBus.getDefault().register(this);
        findViews(v);
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);
        v.findViewById(R.id.tv_setting).setOnClickListener(this);
        v.findViewById(R.id.btn_deposit).setOnClickListener(this);
        v.findViewById(R.id.btn_withdraw).setOnClickListener(this);
        v.findViewById(R.id.tv_customer_service).setOnClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo != null) {
            mTvTitle.setText(userInfo.getName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        Funds result = StaticStore.getFunds(false);
        tvYue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getAvailableFunds()));
        tvMargin.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getFrozenMargin()));
        tvNet.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getNetAssets()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                LoginActivity.startActivity(mContext);
                break;
            case R.id.btn_register:
                RegisterActivity.startActivity(mContext);
                break;
            case R.id.tv_setting:
                SettingActivity.startActivity(mContext);
                break;
            case R.id.tv_one:
                FundDetailActivity.startActivity(mContext);
                break;
            case R.id.tv_two:
                UserInfoActivity.startActivity(mContext);
                break;
            case R.id.tv_three:
                AboutUsActivity.startActivity(mContext);
                break;
            case R.id.tv_four:
                WebActivity.startActivity(mContext, HttpConfig.URL_WARNING);
                break;
            case R.id.btn_deposit:
                ActivityTools.toDeposit(mContext);
                break;
            case R.id.btn_withdraw:
                ActivityTools.toWithdraw(mContext);
                break;
            case R.id.tv_customer_service:
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
