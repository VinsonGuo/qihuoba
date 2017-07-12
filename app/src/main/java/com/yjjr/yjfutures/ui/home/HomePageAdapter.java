package com.yjjr.yjfutures.ui.home;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.DoubleUtil;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class HomePageAdapter extends BaseQuickAdapter<Quote, BaseViewHolder> {
    public HomePageAdapter(@Nullable List<Quote> data) {
        super(R.layout.item_home_page, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Quote item) {
        helper.setText(R.id.tv_title, item.getSymbolname())
                .setText(R.id.tv_desc, item.getSymbol())
                .setText(R.id.tv_icon, item.getSymbol());
        TextView tvInfo = helper.getView(R.id.tv_info);
        double change = item.getChangeRate();
        String changeText = change == 0 ? "-" : DoubleUtil.format2Decimal(change) + "%";
        tvInfo.setText(DoubleUtil.format2Decimal(item.getLastPrice()) + "\n" + changeText);
        tvInfo.setTextColor(ContextCompat.getColor(mContext, change > 0 ? R.color.main_color_red : R.color.main_color_green));
    }
}
