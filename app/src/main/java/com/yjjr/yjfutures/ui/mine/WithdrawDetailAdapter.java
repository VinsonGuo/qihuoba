package com.yjjr.yjfutures.ui.mine;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.SpannableUtil;

import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */

public class WithdrawDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public WithdrawDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_withdraw_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        SpannableString money = SpannableUtil.getStringByColor(mContext, SpannableUtil.getStringBySize(DoubleUtil.format2Decimal(2000.000), 1.2f), R.color.main_text_color);
        helper.setText(R.id.tv_info, TextUtils.concat("提现金额\n", money));
    }
}
