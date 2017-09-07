package com.yjjr.yjfutures.ui.market;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.widget.guideview.Guide;
import com.yjjr.yjfutures.widget.guideview.GuideBuilder;
import com.yjjr.yjfutures.widget.guideview.SimpleComponent;

import java.util.List;

/**
 * Created by dell on 2017/7/3.
 */

public class MarketPriceAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {

    private boolean isShow = false;
    private boolean isDemo;

    public MarketPriceAdapter(@Nullable List<Quote> data, boolean isDemo) {
        super(R.layout.item_market_price, data);
        this.isDemo = isDemo;
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

            int position = helper.getLayoutPosition();
            if (position == 2 && isDemo && !isShow) {
                showGuideView(helper.getConvertView(), position);
                isShow = true;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

    private void showGuideView(final View v, final int position) {
        //test
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(v)
//                .setFullingViewId(R.id.tv_title1)
                .setAlpha(150)
                .setHighTargetCorner(20)
//                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                getOnItemClickListener().onItemClick(MarketPriceAdapter.this, v, position);
            }
        });

        builder1.addComponent(new SimpleComponent());
        final Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        v.post(new Runnable() {
            @Override
            public void run() {
                guide.show((Activity) mContext);
            }
        });

    }
}
