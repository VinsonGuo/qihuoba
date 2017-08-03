package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.StringUtils;

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
        try {
            Quote quote = StaticStore.sQuoteMap.get(item.getSymbol());
            helper.setText(R.id.tv_symbol, quote.getSymbolname())
                    .setText(R.id.tv_open_price, TextUtils.concat("开仓价\t", SpannableUtil.getStringByColor(mContext, StringUtils.getStringByTick(item.getAvgPrice(), quote.getTick()), R.color.main_text_color)))
                    .setText(R.id.tv_current_price, TextUtils.concat("当前价\t", SpannableUtil.getStringByColor(mContext, StringUtils.getStringByTick(item.getMarketPrice(), quote.getTick()), R.color.main_text_color)))
                    .setText(R.id.tv_stop_lose, "止损价\t")
                    .setText(R.id.tv_stop_win, "止盈价\t")
                    .setText(R.id.tv_profit, StringUtils.getProfitText(item.getUnrealizedPL()))
                    .setText(R.id.tv_hand, item.getBuySell() + Math.abs(item.getQty()) + "手")
                    .addOnClickListener(R.id.tv_close_order);
            TextView tvHand = helper.getView(R.id.tv_hand);
            if (item.getQty() > 0) {
                tvHand.setBackgroundResource(R.drawable.shape_online_tx_red);
            } else {
                tvHand.setBackgroundResource(R.drawable.shape_online_tx_green);
            }
            TextView tvProfit = helper.getView(R.id.tv_profit);
            tvProfit.setTextColor(StringUtils.getProfitColor(mContext, item.getUnrealizedPL()));
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

}
