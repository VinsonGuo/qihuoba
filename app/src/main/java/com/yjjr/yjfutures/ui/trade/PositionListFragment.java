package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/6/23.
 */

public class PositionListFragment extends ListFragment<Holding> {

    private TextView mTvProfit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public BaseQuickAdapter<Holding, BaseViewHolder> getAdapter() {
        PositionListAdapter adapter = new PositionListAdapter(null);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_position_list, mRvList, false);
        mTvProfit = (TextView) headerView.findViewById(R.id.tv_profit);
        adapter.setHeaderView(headerView);
        return adapter;
    }

    @Override
    protected void loadData() {
        HttpManager.getHttpService().getHolding(BaseApplication.getInstance().getTradeToken())
                .compose(RxUtils.<List<Holding>>applySchedulers())
                .compose(this.<List<Holding>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<Holding>>() {
                    @Override
                    public void accept(@NonNull List<Holding> holdings) throws Exception {
                        List<Holding> list = new ArrayList<>(10);
                        double profit = 0;
                        for (Holding holding : holdings) {
                            profit += holding.getUnrealizedPL();
                            if(holding.getQty() != 0) {
                                list.add(holding);
                            }
                        }
                        mAdapter.setNewData(list);
                        mTvProfit.setText(DoubleUtil.format2Decimal(profit));
                        loadDataFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendOrderEvent event) {
        loadData();
    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
