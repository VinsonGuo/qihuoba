package com.yjjr.yjfutures.ui.mine;


import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.model.biz.AssetRecord;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundDetailFragment extends ListFragment<AssetRecord> {


    @Override
    protected void loadData() {
        HttpManager.getBizService().getAssetRecord(mPage, 10)
                .compose(RxUtils.<PageResponse<AssetRecord>>applySchedulers())
                .compose(this.<PageResponse<AssetRecord>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<PageResponse<AssetRecord>>() {
                    @Override
                    public void accept(@NonNull PageResponse<AssetRecord> response) throws Exception {
                        mAdapter.addData(response.getList());
                        loadDataFinish();
                        mAdapter.loadMoreComplete();
                        if(mAdapter.getData().size() >= response.getTotal()) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mAdapter.loadMoreFail();
                    }
                });
    }

    @Override
    public BaseQuickAdapter<AssetRecord, BaseViewHolder> getAdapter() {
        FundDetailAdapter adapter = new FundDetailAdapter(null);
        adapter.setEnableLoadMore(true);
        return adapter;
    }
}
