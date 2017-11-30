package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.HistoricalTicks;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2017/11/3.
 */

public class MarketHisAdapter extends BaseQuickAdapter<HistoricalTicks, BaseViewHolder> {

    private double tick;

    private SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    public MarketHisAdapter(@Nullable List<HistoricalTicks> data, double tick) {
        super(R.layout.item_market_his, data);
        this.tick = tick;
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoricalTicks item) {
        try {
            String s = mContext.getString(R.string.number_holder);
            if (item == null) {
                helper.setText(R.id.tv_time, s)
                        .setText(R.id.tv_price, s)
                        .setText(R.id.tv_vol, s);
            } else {
                helper.setText(R.id.tv_time, mFormat.format(item.getTime()))
                        .setText(R.id.tv_price, StringUtils.getStringByTick(item.getPrice(), tick))
                        .setText(R.id.tv_vol, item.getSize() + "");
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}
