package com.yjjr.yjfutures.ui.trade;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/6/27.
 */

public class HandicapListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    String[] names = {"今持仓", "昨持仓", "今结", "昨结", "总手", "金额", "涨停", "跌停"};

    public HandicapListAdapter(@Nullable List<String> data) {
        super(R.layout.item_handicap_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int index = helper.getLayoutPosition();
        helper.setText(R.id.tv_name, names[index]).setText(R.id.tv_value, item);
    }
}
