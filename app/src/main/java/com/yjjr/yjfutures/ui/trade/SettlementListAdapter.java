package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.CloseOrder;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.SpannableUtil;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class SettlementListAdapter extends BaseQuickAdapter<CloseOrder, BaseViewHolder> {

    private double mExchange;

    public SettlementListAdapter(@Nullable List<CloseOrder> data) {
        super(R.layout.item_settlement_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CloseOrder item) {
        String buySell = item.getOpenBuySell();
        helper.setText(R.id.tv_time, item.getCloseDate().replace('T', '\n'))
                .setText(R.id.tv_info, item.getSymbol() + "\t" + Math.abs(item.getQty()) + "手")
                .setText(R.id.tv_type, buySell);
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.setText(getPriceColor(item.getRealizedPL(), item.getRealizedPL() * mExchange));
        TextView tvDirection = helper.getView(R.id.tv_direction);
        if(TextUtils.equals(buySell, "买入")){
            tvDirection.setText("看涨");
            tvDirection.setBackgroundResource(R.drawable.shape_online_tx_red);
        }else {
            tvDirection.setText("看跌");
            tvDirection.setBackgroundResource(R.drawable.shape_online_tx_green);
        }
    }

    private CharSequence getPriceColor(double y, double d) {
        String symbol = y >= 0 ? "+" : "";
        int color = y > 0 ? R.color.main_color_red : R.color.main_color_green;
        return TextUtils.concat(
                SpannableUtil.getStringByColor(mContext, symbol, color),
                SpannableUtil.getOnlinePriceString(mContext, DoubleUtil.format2Decimal(y), y),
                SpannableUtil.getStringBySize(SpannableUtil.getStringByColor(mContext, "\n($" + d + ")", color), 0.8f));
    }

    public void setExchange(double exchange) {
        mExchange = exchange;
    }
}
