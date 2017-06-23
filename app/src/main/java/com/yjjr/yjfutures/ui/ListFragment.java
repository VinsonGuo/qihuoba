package com.yjjr.yjfutures.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;

/**
 * Created by dell on 2017/6/22.
 */

public abstract class ListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int mPage = 0;
    private RecyclerView mRvList;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = (SwipeRefreshLayout) v;
        mRvList = (RecyclerView) v.findViewById(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(mContext));
        BaseQuickAdapter<T, BaseViewHolder> adapter = getAdapter();
        mRvList.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(this);
        onRefresh();
        return v;
    }

    public abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    public void onRefresh() {
        mPage = 0;
    }
}
