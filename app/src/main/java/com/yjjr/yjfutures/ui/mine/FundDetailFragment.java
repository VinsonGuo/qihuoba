package com.yjjr.yjfutures.ui.mine;


import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.ui.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundDetailFragment extends ListFragment<String> {


    @Override
    protected void loadData() {
        for (int i = 0; i < 100; i++) {
            mAdapter.addData("test" + i);
        }
        loadDataFinish();
    }

    @Override
    public BaseQuickAdapter<String, BaseViewHolder> getAdapter() {
        FundDetailAdapter adapter = new FundDetailAdapter(null);
        return adapter;
    }
}
