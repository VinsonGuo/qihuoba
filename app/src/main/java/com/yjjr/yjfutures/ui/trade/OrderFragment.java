package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.TradeInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OrderFragment extends BaseFragment {

    private TradeInfoView mTradeInfoView;
    private boolean mIsDemo;

    public static OrderFragment newInstance(boolean isDemo) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, isDemo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        final HeaderView headerView = (HeaderView) v.findViewById(R.id.header_view);
        headerView.bindActivity(getActivity());
        mTradeInfoView = (TradeInfoView) v.findViewById(R.id.trade_info);
        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettlementActivity.startActivity(mContext, mIsDemo);
            }
        });
        headerView.setBackImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return v;
    }


    @Override
    protected void initData() {
        super.initData();
        PositionListFragment positionListFragment = PositionListFragment.newInstance(mIsDemo);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, positionListFragment)
                .commit();
        positionListFragment.setUserVisibleHint(true);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(SendOrderEvent event) {
//        getTradeInfo();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        if(isFragmentVisible()) {
            Funds result = StaticStore.getFunds(mIsDemo);
            mTradeInfoView.setValues(mIsDemo, result.getFrozenMargin(), result.getAvailableFunds(), result.getNetAssets());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
