package com.yjjr.yjfutures.ui.found;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Notice;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
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
                        loadDataFinish();
                        PageResponse<Notice> result = response.getResult();
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
    public BaseQuickAdapter<Notice, BaseViewHolder> getAdapter() {
        NoticeAdapter adapter = new NoticeAdapter(null);
        adapter.setEnableLoadMore(true);
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Notice notice = mAdapter.getData().get(position);
                notice.setIsread(1);
                mAdapter.notifyItemChanged(position);
                WebActivity.startActivity(mContext, HttpConfig.URL_NOTICE + notice.getId() + "/" + UserSharePrefernce.getAccount(mContext));
            }
        });
        return adapter;
    }


    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
