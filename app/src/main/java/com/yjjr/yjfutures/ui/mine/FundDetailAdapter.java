package com.yjjr.yjfutures.ui.mine;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */

public class FundDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public FundDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_fund_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_info, item);
    }
}
