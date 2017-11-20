package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.StringUtils;

import java.util.List;

/**
 * Created by dell on 2017/11/3.
 */

public class MarketHisAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {

    public MarketHisAdapter(@Nullable List<Quote> data) {
        super(R.layout.item_market_his, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Quote item) {
        helper
                .setText(R.id.tv_time, TextUtils.isEmpty(item.getLastTime()) ? "----" : item.getLastTime().split(" ")[1])
                .setText(R.id.tv_price, StringUtils.getStringByTick(item.getLastPrice(), item.getTick()))
                .setText(R.id.tv_vol, item.getLastSize() + "");
    }
}
