package com.yjjr.yjfutures.ui.mine;


import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.model.biz.AssetRecord;
import com.yjjr.yjfutures.model.biz.BizResponse;
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
                .compose(RxUtils.<BizResponse<PageResponse<AssetRecord>>>applyBizSchedulers())
                .compose(this.<BizResponse<PageResponse<AssetRecord>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<PageResponse<AssetRecord>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<PageResponse<AssetRecord>> response) throws Exception {
                        PageResponse<AssetRecord> result = response.getResult();
                        mAdapter.addData(result.getList());
                        loadDataFinish();
                        if(mAdapter.getData().size() >= result.getTotal()) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        loadFailed();
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
