package com.yjjr.yjfutures.ui.market;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;

/**
 * 行情页面
 */
public class MarketPriceFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    public MarketPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market_price, container, false);
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        MarketPriceAdapter adapter = new MarketPriceAdapter(null);
        rvList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        for (int i = 0; i < 10; i++) {
            adapter.addData("test" + i);
        }
        return v;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TradeActivity.startActivity(mContext);
    }
}
