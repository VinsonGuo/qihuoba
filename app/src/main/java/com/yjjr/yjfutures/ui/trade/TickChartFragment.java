package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.widget.chart.TickChart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class TickChartFragment extends BaseFragment {


    private TickChart mChart;
    private String mSymbol;

    public TickChartFragment() {
        // Required empty public constructor
    }

    public static TickChartFragment newInstance(String symbol) {
        TickChartFragment fragment = new TickChartFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CONTENT_PARAMETER, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChart = new TickChart(mContext);
        return mChart;
    }

    @Override
    protected void initData() {
        super.initData();
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        if (quote != null) {
            mChart.addEntry((float) quote.getLastPrice());
            mChart.addEntry((float) quote.getLastPrice());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        if (quote != null) {
            mChart.addEntry((float) quote.getLastPrice());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
