package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.utils.DoubleUtil;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class PositionListAdapter extends BaseQuickAdapter<Holding, BaseViewHolder> {
    public PositionListAdapter(@Nullable List<Holding> data) {
        super(R.layout.item_position_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Holding item) {
        helper.setText(R.id.tv_symbol, item.getSymbol())
                .setText(R.id.tv_open_price, "开仓价\t" + DoubleUtil.formatDecimal(item.getAvgPrice()))
                .setText(R.id.tv_current_price, "当前价\t" + DoubleUtil.formatDecimal(item.getMarketPrice()))
                .setText(R.id.tv_stop_lose, "止损价\t")
                .setText(R.id.tv_stop_win, "止盈价\t")
                .setText(R.id.tv_profit, DoubleUtil.format2Decimal(item.getUnrealizedPL()))
                .setText(R.id.tv_hand, item.getBuySell() + Math.abs(item.getQty()) + "手")
                .addOnClickListener(R.id.tv_close_order);
        TextView tvHand = helper.getView(R.id.tv_hand);
        if (item.getQty() > 0) {
            tvHand.setBackgroundResource(R.drawable.shape_online_tx_red);
        } else {
            tvHand.setBackgroundResource(R.drawable.shape_online_tx_green);
        }
        TextView tvProfit = helper.getView(R.id.tv_profit);
        tvProfit.setTextColor(ContextCompat.getColor(mContext, item.getUnrealizedPL() > 0 ? R.color.main_color_red : R.color.main_color_green));
    }
}
