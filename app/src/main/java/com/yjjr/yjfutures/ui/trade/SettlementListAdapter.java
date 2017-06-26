package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.SpannableUtil;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class SettlementListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SettlementListAdapter(@Nullable List<String> data) {
        super(R.layout.item_settlement_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tvPrice = helper.getView(R.id.tv_price);
        if (helper.getLayoutPosition() % 2 == 0) {
            tvPrice.setText(getPriceColor(2345, 123));
        } else {
            tvPrice.setText(getPriceColor(-2345, -123));
        }
    }

    private CharSequence getPriceColor(double y, double d) {
        String symbol = y >= 0 ? "+" : "-";
        int color = y >= 0 ? R.color.main_color : R.color.main_color_red;
        return TextUtils.concat(
                SpannableUtil.getStringByColor(mContext, symbol, color),
                SpannableUtil.getOnlinePriceString(mContext, String.valueOf(y), y),
                mContext.getString(R.string.yuan),
                SpannableUtil.getStringByColor(mContext, "\n($" + d + ")", color));
    }
}
