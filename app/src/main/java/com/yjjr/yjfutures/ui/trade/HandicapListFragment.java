package com.yjjr.yjfutures.ui.trade;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.ListFragment;

/**
 * Created by dell on 2017/6/27.
 */

public class HandicapListFragment extends ListFragment<String> {
    @Override
    public BaseQuickAdapter<String, BaseViewHolder> getAdapter() {
        HandicapListAdapter adapter = new HandicapListAdapter(null);
        for (int i = 0; i < 8; i++) {
            adapter.addData("123" + i);
        }
        adapter.setEnableLoadMore(false);
        return adapter;
    }

    @Override
    protected void setManager() {
        mRvList.setLayoutManager(new GridLayoutManager(mContext, 2));
    }
}
