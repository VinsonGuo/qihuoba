package com.yjjr.yjfutures.ui.found;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.CashRecord;
import com.yjjr.yjfutures.model.biz.Notice;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.ui.mine.WithdrawDetailAdapter;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/8/2.
 */

public class NoticeListFragment extends ListFragment<Notice> {
    @Override
    protected void loadData() {
        HttpManager.getBizService().getNotice(mPage, 10)
                .compose(RxUtils.<BizResponse<PageResponse<Notice>>>applyBizSchedulers())
                .compose(this.<BizResponse<PageResponse<Notice>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<PageResponse<Notice>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<PageResponse<Notice>> response) throws Exception {
                        PageResponse<Notice> result = response.getResult();
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
    public BaseQuickAdapter<Notice, BaseViewHolder> getAdapter() {
        NoticeAdapter adapter = new NoticeAdapter(null);
        adapter.setEnableLoadMore(true);
        return adapter;
    }
}
