package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.widget.LoadingView;

/**
 * Created by dell on 2017/6/22.
 */

public abstract class ListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    protected int mPage = 1;
    protected RecyclerView mRvList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected LoadingView mLoadView;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = (SwipeRefreshLayout) v;
        mRvList = (RecyclerView) v.findViewById(R.id.rv_list);
        RecyclerView.ItemAnimator animator = mRvList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        setManager();
        mLoadView = (LoadingView) v.findViewById(R.id.load_view);
        mLoadView.setOnReloadListener(new LoadingView.OnReloadListener() {
            @Override
            public void onReload() {
                loadData();
            }
        });
        mAdapter = getAdapter();
        if (mAdapter.isLoadMoreEnable()) {
            mAdapter.setOnLoadMoreListener(this, mRvList);
            mRvList.setAdapter(mAdapter);
        } else {
            mAdapter.bindToRecyclerView(mRvList);
        }
        View noDataView = inflater.inflate(R.layout.view_list_empty, mRvList, false);
        TextView tvNoData = (TextView) noDataView.findViewById(R.id.tv_no_data);
        tvNoData.setText(getNoDataText());
        mAdapter.setEmptyView(noDataView);
        mRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    protected String getNoDataText() {
        return "没有数据";
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    protected abstract void loadData();

    protected void loadDataFinish() {
        mAdapter.loadMoreComplete();
        if (mPage == 1) {
            mAdapter.getData().clear();
        }
        mLoadView.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

    protected void loadFailed() {
        mLoadView.setVisibility(View.VISIBLE);
        mLoadView.loadFail();
        mRefreshLayout.setRefreshing(false);
        if (mAdapter.isLoadMoreEnable()) {
            if (mPage == 1) {
                mLoadView.loadFail();
            } else {
                mAdapter.loadMoreFail();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void setManager() {
        mRvList.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    public abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }
}
