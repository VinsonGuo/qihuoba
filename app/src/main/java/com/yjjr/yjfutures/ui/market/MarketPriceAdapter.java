package com.yjjr.yjfutures.ui.market;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/7/3.
 */

public class MarketPriceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MarketPriceAdapter(@Nullable List<String> data) {
        super(R.layout.item_market_price, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
