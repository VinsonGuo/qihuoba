package com.yjjr.yjfutures.ui.market;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/3.
 */

public class MarketPriceAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {
    public MarketPriceAdapter(@Nullable List<Quote> data) {
        super(R.layout.item_market_price, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Quote item) {
        try {
            double changeRate = item.getChangeRate();
            helper.setText(R.id.tv_symbol_name, item.getSymbolname())
                    .setText(R.id.tv_symbol, item.getSymbol())
                    .setText(R.id.tv_price, item.getLastPrice() == 0 ? "-" : StringUtils.getStringByTick(item.getLastPrice(), item.getTick()))
                    .setText(R.id.tv_trade_amount, item.getVol() + "")
                    .setVisible(R.id.reddot, item.isHolding());
            TextView tvChange = helper.getView(R.id.tv_change);
            TextView tvPrice = helper.getView(R.id.tv_price);
            tvChange.setText(changeRate == 0 ? "-" : DoubleUtil.format2Decimal(changeRate) + "%");
            double change = item.getChange();
            tvChange.setTextColor(ContextCompat.getColor(mContext, change > 0 ? R.color.main_color_red : R.color.main_color_green));
            tvPrice.setTextColor(ContextCompat.getColor(mContext, change > 0 ? R.color.main_color_red : R.color.main_color_green));
            tvChange.setBackgroundResource(change > 0 ? R.drawable.shape_red_border_bg : R.drawable.shape_green_border_bg);
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }
}
