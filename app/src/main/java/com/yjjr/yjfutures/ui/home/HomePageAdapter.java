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
        mDescMap.put("CL", "");
        mDescMap.put("GC", "");
        mDescMap.put("SI", "");
        mDescMap.put("NG", "");
        mDescMap.put("ES", "");
        mDescMap.put("NQ", "");
        mDescMap.put("DAX", "");
        mDescMap.put("HSI", "");
        mDescMap.put("A50", "");
    }

    @Override
    protected void convert(BaseViewHolder helper, Quote item) {
        String title;
        String symbol = item.getSymbol();
        switch (item.getExchange()) {
            case "DTB":
                title = symbol.substring(1, 4);
                break;
            case "HKFE":
                title = symbol.substring(0, 3);
                break;
            default:
                title = symbol.substring(0, 2);
                break;
        }
        helper.setText(R.id.tv_title, item.getSymbolname())
                .setText(R.id.tv_desc, symbol)
                .setText(R.id.tv_icon, title);
        int colorIndex = (helper.getLayoutPosition()-getHeaderLayoutCount()) % (colors.length - 1);
        helper.setTextColor(R.id.tv_icon, colors[colorIndex]);
        TextView tvInfo = helper.getView(R.id.tv_info);
        double change = item.getChangeRate();
        String changeText = change == 0 ? "-" : DoubleUtil.format2Decimal(change) + "%";
        tvInfo.setText(DoubleUtil.formatDecimal(item.getLastPrice()) + "\n" + changeText);
        tvInfo.setTextColor(ContextCompat.getColor(mContext, change > 0 ? R.color.main_color_red : R.color.main_color_green));
    }
}
