package com.yjjr.yjfutures.ui.trade;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.CloseOrder;
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
    @Override
    public BaseQuickAdapter<CloseOrder, BaseViewHolder> getAdapter() {
        SettlementListAdapter adapter = new SettlementListAdapter(null);
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
        long dataTime = new DateTime().minusYears(1).getMillis();
        HttpManager.getHttpService().getCloseOrder(BaseApplication.getInstance().getTradeToken(), DateUtils.formatData(dataTime), DateUtils.formatData(System.currentTimeMillis()))
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
                });
    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
