package com.yjjr.yjfutures.ui.market;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.GetSymbolsRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 行情页面
 */
public class MarketPriceFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    private MarketPriceAdapter mAdapter;

    public MarketPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market_price, container, false);
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MarketPriceAdapter(null);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TradeActivity.startActivity(mContext, mAdapter.getData().get(position).getSymbol());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
