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
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.widget.LoadingView;

/**
 * Created by dell on 2017/6/22.
 */

public abstract class ListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    protected int mPage = 0;
    protected RecyclerView mRvList;
    protected SwipeRefreshLayout mRefreshLayout;
    private LoadingView mLoadView;
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = (SwipeRefreshLayout) v;
        mRvList = (RecyclerView) v.findViewById(R.id.rv_list);
        setManager();
        mLoadView = (LoadingView) v.findViewById(R.id.load_view);
        mAdapter = getAdapter();
        if (mAdapter.isLoadMoreEnable()) {
            mAdapter.setOnLoadMoreListener(this, mRvList);
        }
        mRvList.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mRvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadView.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    protected void setManager() {
        mRvList.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    public void onRefresh() {
        mPage = 0;
        initData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        mRvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    try {
                        mAdapter.addData((T) ("test" + i));
                    } catch (Exception e) {
                        LogUtils.e(e);
                    }
                }
                mAdapter.loadMoreComplete();
            }
        }, 2000);
    }
}
