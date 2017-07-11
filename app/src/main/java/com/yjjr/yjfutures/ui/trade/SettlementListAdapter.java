package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.FilledOrder;
import com.yjjr.yjfutures.utils.SpannableUtil;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class SettlementListAdapter extends BaseQuickAdapter<FilledOrder, BaseViewHolder> {

    private double mExchange;

    public SettlementListAdapter(@Nullable List<FilledOrder> data) {
        super(R.layout.item_settlement_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilledOrder item) {
        helper.setText(R.id.tv_time, item.getFilledTime().replace(' ', '\n'))
                .setText(R.id.tv_info, item.getSymbol() + "\t" + item.getFilledQty() + "手")
                .setText(R.id.tv_direction, item.getBuySell())
                .setText(R.id.tv_type, item.getBuySell());
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.setText(getPriceColor(item.getPrice(), 123));
    }

    private CharSequence getPriceColor(double y, double d) {
        String symbol = y >= 0 ? "+" : "-";
        int color = y >= 0 ? R.color.main_color_red : R.color.main_color_green;
        return TextUtils.concat(
                SpannableUtil.getStringByColor(mContext, symbol, color),
                SpannableUtil.getOnlinePriceString(mContext, String.valueOf(y), y),
                SpannableUtil.getStringBySize(SpannableUtil.getStringByColor(mContext, "\n($" + d + ")", color), 0.8f));
    }

    public void setExchange(double exchange) {
        mExchange = exchange;
    }
}
