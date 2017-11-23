package com.yjjr.yjfutures.ui.home;

import android.graphics.Color;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/6/20.
 */

public class HomePageAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {

    private Map<String, String> mDescMap = new HashMap<>(10);
    private int[] colors = {
            Color.parseColor("#ff5656"),
            Color.parseColor("#925593"),
            Color.parseColor("#bd6959"),
            Color.parseColor("#bda5ce"),
            Color.parseColor("#8097c1"),
            Color.parseColor("#c7c577"),
            Color.parseColor("#12ba9a")};

    public HomePageAdapter(@Nullable List<Quote> data) {
        super(R.layout.item_home_page, data);
        /*
         * 产品：
         美原油CL
         美黄金GC
         美白银SI
         天然气NG
         迷你标普ES
         迷你纳指NQ
         德指DAX
         恒指HSI
         A50
         */
        mDescMap.put("CL", "1500元炒正宗美原油");
        mDescMap.put("GC", "国际黄金，全球热门");
        mDescMap.put("SI", "投资者青睐的避嫌品种");
        mDescMap.put("NG", "国际大宗，连续交易");
        mDescMap.put("ES", "交易量大，流动性好");
        mDescMap.put("NQ", "最强科技指数，投机性高");
        mDescMap.put("DAX", "振幅大，收益高");
        mDescMap.put("HSI", "香港恒生指数 每点50港币");
        mDescMap.put("CN", "A股市场风险对冲利器");
    }

    @Override
    protected void convert(BaseViewHolder helper, Quote item) {
        try {
            String title = StringUtils.getRuleName(item);
            helper.setText(R.id.tv_title, item.getSymbolname())
                    .setText(R.id.tv_desc, title);
            TextView tvPrice = helper.getView(R.id.tv_price);
            TextView tvChangeRate = helper.getView(R.id.tv_change_rate);
            double changeRate = item.getChangeRate();
            String changeText = DoubleUtil.format2Decimal(changeRate) + "%";
            tvPrice.setText(StringUtils.getStringByTick(item.getLastPrice(), item.getTick()));
            tvChangeRate.setText(changeText);
            double change = item.getChange();
            int color = ContextCompat.getColor(mContext, change >= 0 ? R.color.main_color_red : R.color.main_color_green);
            tvPrice.setTextColor(color);
            tvChangeRate.setTextColor(color);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}
