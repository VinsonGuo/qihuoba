package com.yjjr.yjfutures.ui.publish;

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

public class MarketAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {


    public MarketAdapter(@Nullable List<Quote> data) {
        super(R.layout.item_market, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Quote item) {
        try {
            double changeRate = item.getChangeRate();
            helper.setText(R.id.tv_symbol_name, item.getSymbolname())
                    .setText(R.id.tv_symbol, item.getSymbol())
                    .setText(R.id.tv_price, item.getLastPrice() == 0 ? "-" : StringUtils.getStringByTick(item.getLastPrice(), item.getTick()))
                    .setText(R.id.tv_trade_amount, item.getVol() + "")
                    .setVisible(R.id.reddot, item.isHolding());
            TextView tvChange = helper.getView(R.id.tv_change);
            final TextView tvPrice = helper.getView(R.id.tv_price);
            tvChange.setText(DoubleUtil.format2Decimal(changeRate) + "%");
            double change = item.getChange();
            tvChange.setTextColor(ContextCompat.getColor(mContext, change >= 0 ? R.color.main_color_red : R.color.main_color_green));
            tvPrice.setTextColor(ContextCompat.getColor(mContext, change >= 0 ? R.color.main_color_red : R.color.main_color_green));
            tvChange.setBackgroundResource(change >= 0 ? R.drawable.shape_red_border_bg : R.drawable.shape_green_border_bg);

           /* int colorFrom = mContext.getResources().getColor(R.color.color_5_000000);
            int colorTo = mContext.getResources().getColor(R.color.transparent);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(250); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                   helper.getConvertView().setBackgroundColor((int) animator.getAnimatedValue());
                }

            });
            colorAnimation.start();*/

        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

}
