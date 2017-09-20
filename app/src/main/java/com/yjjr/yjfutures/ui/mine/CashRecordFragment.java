package com.yjjr.yjfutures.ui.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.CashRecord;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 充值、提现的fragment
 */
public class CashRecordFragment extends ListFragment<CashRecord> {

    private int mType;

    public static CashRecordFragment newInstance(int type) {
        CashRecordFragment fragment = new CashRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CONTENT_PARAMETER, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(Constants.CONTENT_PARAMETER, CashRecordActivity.DEPOSIT);
        }
    }

    @Override
    protected void loadData() {
        Observable<BizResponse<PageResponse<CashRecord>>> observable;
        if (mType == CashRecordActivity.WITHDRAW) {
            observable = HttpManager.getBizService().getCashRecord(mPage, 10);
        } else {
            observable = HttpManager.getBizService().getRechargeRecord(mPage, 10);
        }
        observable
                .compose(RxUtils.<BizResponse<PageResponse<CashRecord>>>applyBizSchedulers())
                .compose(this.<BizResponse<PageResponse<CashRecord>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<PageResponse<CashRecord>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<PageResponse<CashRecord>> response) throws Exception {
                        loadDataFinish();
                        PageResponse<CashRecord> result = response.getResult();
                        mAdapter.addData(result.getList());
                        if (mAdapter.getData().size() >= result.getTotal()) {
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
        CashRecordAdapter adapter = new CashRecordAdapter(null,mType);
        adapter.setEnableLoadMore(true);
        return adapter;
    }
}
