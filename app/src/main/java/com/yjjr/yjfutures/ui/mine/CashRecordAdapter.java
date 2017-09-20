package com.yjjr.yjfutures.ui.mine;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.CashRecord;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.SpannableUtil;

import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */

public class CashRecordAdapter extends BaseQuickAdapter<CashRecord, BaseViewHolder> {
    private final int mType;

    public CashRecordAdapter(@Nullable List<CashRecord> data, int type) {
        super(R.layout.item_cash_record, data);
        mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, CashRecord item) {
        SpannableString money = SpannableUtil.getStringByColor(mContext, SpannableUtil.getStringBySize(DoubleUtil.format2Decimal(item.getMoney()), 1.2f), R.color.main_text_color);
        helper.setText(R.id.tv_info, TextUtils.concat(mType == CashRecordActivity.WITHDRAW ? "提现金额\n" : "充值金额\n", money))
                .setText(R.id.tv_time, DateUtils.formatData(item.getCashTime()).replace(' ', '\n'))
                .setText(R.id.tv_status, item.getStatusCn())
                .setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, item.getStatus() == 3 ? R.color.main_text_color : R.color.main_color_red));
    }
}
