package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.SendOrderResponse;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/6/23.
 */

public class PositionListFragment extends ListFragment<Holding> {

    private TextView mTvProfit;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.closeing));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public BaseQuickAdapter<Holding, BaseViewHolder> getAdapter() {
        final PositionListAdapter adapter = new PositionListAdapter(null);
        final View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_position_list, mRvList, false);
        mTvProfit = (TextView) headerView.findViewById(R.id.tv_profit);
        adapter.setHeaderView(headerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_close_order) {
                    Holding holding = (Holding) adapter.getData().get(position);
                    closeOrder(holding);
                }
            }
        });
        headerView.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Holding> data = adapter.getData();
                closeAllOrder(data);
            }
        });
        return adapter;
    }

    private void closeAllOrder(List<Holding> data) {
        mProgressDialog.show();
        List<Observable<SendOrderResponse>> observables = new ArrayList<>();
        for (Holding holding : data) {
            observables.add(RxUtils.createCloseObservable(holding));
        }
        Observable.zip(observables, new Function<Object[], String>() {

            @Override
            public String apply(@NonNull Object[] objects) throws Exception {
                String msg = null;
                for (Object object : objects) {
                    SendOrderResponse sendOrderResponse = (SendOrderResponse) object;
                    msg = sendOrderResponse.getMessage();
                }
                return msg;
            }
        })
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtils.<String>applySchedulers())
                .compose(this.<String>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        ToastUtils.show(mContext, s);
                        mProgressDialog.dismiss();
                        EventBus.getDefault().post(new SendOrderEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
    }



    private void closeOrder(Holding holding) {
        mProgressDialog.show();
        RxUtils.createCloseObservable(holding)
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtils.<SendOrderResponse>applySchedulers())
                .compose(this.<SendOrderResponse>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<SendOrderResponse>() {
                    @Override
                    public void accept(@NonNull SendOrderResponse sendOrderResponse) throws Exception {
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, sendOrderResponse.getMessage());
                        EventBus.getDefault().post(new SendOrderEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
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
                            if (holding.getQty() != 0) {
                                list.add(holding);
                            }
                        }
                        mAdapter.setNewData(list);
                        mTvProfit.setText(DoubleUtil.format2Decimal(profit));
                        mTvProfit.setTextColor(ContextCompat.getColor(mContext, profit > 0 ? R.color.main_color_red : R.color.main_color_green));
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
