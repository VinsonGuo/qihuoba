package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/11/3.
 */

public class MarketHisAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MarketHisAdapter(@Nullable List<String> data) {
        super(R.layout.item_market_his, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper
                .setText(R.id.tv_time, "00:00")
                .setText(R.id.tv_price, "13.22")
                .setText(R.id.tv_vol, (int) (Math.random() * 100) + "");
    }
}
