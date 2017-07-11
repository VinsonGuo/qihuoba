package com.yjjr.yjfutures.ui.trade;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.FilledOrder;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/6/23.
 */

public class SettlementListFragment extends ListFragment<FilledOrder> {
    @Override
    public BaseQuickAdapter<FilledOrder, BaseViewHolder> getAdapter() {
        SettlementListAdapter adapter = new SettlementListAdapter(null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderDetailActivity.startActivity(mContext);
            }
        });
        return adapter;
    }

    @Override
    protected void loadData() {
        HttpManager.getHttpService().getFilledOrder(BaseApplication.getInstance().getTradeToken())
                .compose(RxUtils.<List<FilledOrder>>applySchedulers())
                .compose(this.<List<FilledOrder>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<FilledOrder>>() {
                    @Override
                    public void accept(@NonNull List<FilledOrder> filledOrders) throws Exception {
                        mAdapter.setNewData(filledOrders);
                        loadDataFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mLoadView.loadFail();
                    }
                });
    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
