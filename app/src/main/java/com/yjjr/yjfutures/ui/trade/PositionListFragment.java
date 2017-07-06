package com.yjjr.yjfutures.ui.trade;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/6/23.
 */

public class PositionListFragment extends ListFragment<String> {
    @Override
    public BaseQuickAdapter<String, BaseViewHolder> getAdapter() {
        PositionListAdapter adapter = new PositionListAdapter(null);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("test" + i);
        }
        adapter.addData(data);

        return adapter;
    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
