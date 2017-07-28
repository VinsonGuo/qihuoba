package com.yjjr.yjfutures.ui.mine;


import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.model.biz.AssetRecord;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.CashRecord;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawDetailFragment extends ListFragment<CashRecord> {


    @Override
    protected void loadData() {
        HttpManager.getBizService().getCashRecord(mPage, 10)
                .compose(RxUtils.<BizResponse<PageResponse<CashRecord>>>applyBizSchedulers())
                .compose(this.<BizResponse<PageResponse<CashRecord>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<PageResponse<CashRecord>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<PageResponse<CashRecord>> response) throws Exception {
                        PageResponse<CashRecord> result = response.getResult();
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
    public BaseQuickAdapter<CashRecord, BaseViewHolder> getAdapter() {
        WithdrawDetailAdapter adapter = new WithdrawDetailAdapter(null);
        adapter.setEnableLoadMore(true);
        return adapter;
    }
}
