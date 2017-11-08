package com.yjjr.yjfutures.ui.publish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.ListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dell on 2017/11/6.
 */

public class MarketFragment extends ListFragment<Quote> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void loadData() {
        loadDataFinish();
        mAdapter.replaceData(StaticStore.getQuoteValues(false));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public BaseQuickAdapter<Quote, BaseViewHolder> getAdapter() {
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        MarketAdapter adapter = new MarketAdapter(null);
        adapter.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.header_market_list, mRvList, false));
        return adapter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (isResumed()) {
            Quote quote = StaticStore.getQuote(event.getSymbol(), false);
            int position = mAdapter.getData().indexOf(quote);
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
