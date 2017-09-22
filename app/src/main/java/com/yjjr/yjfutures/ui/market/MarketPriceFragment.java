package com.yjjr.yjfutures.ui.market;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 行情页面
 */
public class MarketPriceFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    private MarketPriceAdapter mAdapter;
    /**
     * true为真实账户
     */
    private boolean mShowTitle = true;

    public MarketPriceFragment() {
        // Required empty public constructor
    }

    public static MarketPriceFragment newInstance(boolean b) {
        MarketPriceFragment fragment = new MarketPriceFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mShowTitle = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market_price, container, false);
        v.findViewById(R.id.tv_title).setVisibility(mShowTitle ? View.VISIBLE : View.GONE);
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rv_list);
        RecyclerView.ItemAnimator animator = rvList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MarketPriceAdapter(null);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setOnItemClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        mAdapter.replaceData(StaticStore.getQuoteValues(!mShowTitle));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TradeActivity.startActivity(mContext, mAdapter.getData().get(position).getSymbol(), !mShowTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (isResumed()) {
            Quote quote = StaticStore.getQuote(event.getSymbol(), !mShowTitle);
            int position = mAdapter.getData().indexOf(quote);
            mAdapter.notifyItemChanged(position);
        }
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        if (isResumed()) {
            mAdapter.replaceData(StaticStore.getQuoteValues(!mShowTitle));
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
