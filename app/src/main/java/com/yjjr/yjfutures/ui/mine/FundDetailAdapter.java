package com.yjjr.yjfutures.ui.mine;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.AssetRecord;
import com.yjjr.yjfutures.utils.DateUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */

public class FundDetailAdapter extends BaseQuickAdapter<AssetRecord, BaseViewHolder> {
    public FundDetailAdapter(@Nullable List<AssetRecord> data) {
        super(R.layout.item_fund_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetRecord item) {
        helper.setText(R.id.tv_info, item.getAssetName())
                .setText(R.id.tv_time, DateUtils.formatData(item.getTradeTime()).replace(' ', '\n'))
                .setText(R.id.tv_money, item.getMoney() + "")
                .setTextColor(R.id.tv_money, ContextCompat.getColor(mContext, item.getMoney() > 0 ? R.color.main_color_red : R.color.main_color_green));
    }
}
