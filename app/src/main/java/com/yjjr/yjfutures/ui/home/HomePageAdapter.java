package com.yjjr.yjfutures.ui.home;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import java.util.List;

/**
 * Created by dell on 2017/6/20.
 */

public class HomePageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public HomePageAdapter(@Nullable List<String> data) {
        super(R.layout.item_home_page, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
    }
}
