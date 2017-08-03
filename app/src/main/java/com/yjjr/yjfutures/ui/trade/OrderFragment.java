package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.TradeInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
        getTradeInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendOrderEvent event) {
        if (isFragmentVisible) {
            getTradeInfo();
        }
    }

    private void getTradeInfo() {
        HttpManager.getBizService().getFunds()
                .retry()
                .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                .compose(this.<BizResponse<Funds>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Funds>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                        Funds result = fundsBizResponse.getResult();
                        mTradeInfoView.setValues(result.getFrozenMargin(), result.getNetAssets(), result.getAvailableFunds());
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
