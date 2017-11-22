package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2017/6/20.
 */

public class PositionListAdapter extends BaseQuickAdapter<Holds, BaseViewHolder> {

    private boolean mIsDemo;

    public PositionListAdapter(@Nullable List<Holds> data, boolean isDemo) {
        super(R.layout.item_position_list, data);
        mIsDemo = isDemo;
    }

    @Override
    protected void convert(BaseViewHolder helper, Holds item) {
        try {
            Quote quote = StaticStore.getQuote(item.getSymbol(), mIsDemo);
            helper.setText(R.id.tv_symbol, quote == null ? item.getSymbol() : quote.getSymbolname())
                    .setText(R.id.tv_open_price, TextUtils.concat("开仓价\t", SpannableUtil.getStringByColor(mContext, StringUtils.getStringByTick(item.getRivalPrice(), quote.getTick()), R.color.main_text_color)))
                    .setText(R.id.tv_current_price, TextUtils.concat("当前价\t", SpannableUtil.getStringByColor(mContext, StringUtils.getStringByTick(item.getMarketPrice(), quote.getTick()), R.color.main_text_color)))
                    .setText(R.id.tv_stop_lose, TextUtils.concat("止损额\t", SpannableUtil.getStringByColor(mContext, DoubleUtil.formatDecimal(item.getLossPriceLine()), R.color.main_text_color)))
                    .setText(R.id.tv_stop_win, TextUtils.concat("止盈额\t", SpannableUtil.getStringByColor(mContext, DoubleUtil.formatDecimal(item.getProfitPriceLine()), R.color.main_text_color)))
                    .setText(R.id.tv_profit, StringUtils.getProfitText(item.getUnrealizedPL()))
                    .setText(R.id.tv_hand, (TextUtils.equals(item.getBuySell(), "买入") ? "看涨" : "看跌") + Math.abs(item.getQty()) + "手")
                    .setText(R.id.tv_ticket, "订单ID " + item.getOrderId())
                    .addOnClickListener(R.id.tv_close_order)
                    .addOnClickListener(R.id.tv_setting);
            TextView tvCloseOrder = helper.getView(R.id.tv_close_order);
            // 是否为平仓中
//            boolean isClosing = item.getStatue() == 2;
            boolean isClosing = item.getFilledQty() < item.getQty();
            tvCloseOrder.setEnabled(!isClosing);
            tvCloseOrder.setText(isClosing ? String.format(Locale.getDefault(), "成交 %d/%d", item.getFilledQty(), item.getQty()) : "X 平仓");
            tvCloseOrder.setTextColor(ContextCompat.getColor(mContext, isClosing ? R.color.color_666666 : R.color.third_text_color));
            TextView tvHand = helper.getView(R.id.tv_hand);
            if (TextUtils.equals(item.getBuySell(), "买入")) {
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
