package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.CloseOrder;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/6/23.
 */

public class SettlementListFragment extends ListFragment<CloseOrder> {

    private boolean mIsDemo;

    public static SettlementListFragment newInstance(boolean isDemo) {
        SettlementListFragment fragment = new SettlementListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, isDemo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    public BaseQuickAdapter<CloseOrder, BaseViewHolder> getAdapter() {
        SettlementListAdapter adapter = new SettlementListAdapter(null);
        adapter.setEnableLoadMore(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<CloseOrder> data = mAdapter.getData();
                OrderDetailActivity.startActivity(mContext, data.get(position));
            }
        });
        return adapter;
    }

    @Override
    protected void loadData() {
        /*long dataTime = new DateTime().minusYears(1).getMillis();
        HttpManager.getHttpService(mIsDemo).getCloseOrder(BaseApplication.getInstance().getTradeToken(mIsDemo), DateUtils.formatData(dataTime), DateUtils.formatData(System.currentTimeMillis()))
                .compose(RxUtils.<List<CloseOrder>>applySchedulers())
                .compose(this.<List<CloseOrder>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<CloseOrder>>() {
                    @Override
                    public void accept(@NonNull List<CloseOrder> closeOrders) throws Exception {
                        mAdapter.setNewData(closeOrders);
                        loadDataFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mLoadView.loadFail();
                    }
                });*/
        HttpManager.getBizService(mIsDemo).getCloseOrder(mPage, 10)
                .compose(RxUtils.<BizResponse<PageResponse<CloseOrder>>>applyBizSchedulers())
                .compose(this.<BizResponse<PageResponse<CloseOrder>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<PageResponse<CloseOrder>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<PageResponse<CloseOrder>> listBizResponse) throws Exception {
                        loadDataFinish();
                        PageResponse<CloseOrder> result = listBizResponse.getResult();
                        mAdapter.addData(result.getList());
                        if (mAdapter.getData().size() >= result.getTotal()) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        loadFailed();
                    }
                });
    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
