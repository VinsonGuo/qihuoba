package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class PositionListAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public PositionListAdapter(@Nullable List<String> data) {
        super(R.layout.item_position_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_symbol, item);
    }
}
