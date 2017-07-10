package com.yjjr.yjfutures.ui.trade;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.GetSymbolsRequest;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.OpenOrder;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/6/23.
 */

public class PositionListFragment extends ListFragment<Holding> {
    @Override
    public BaseQuickAdapter<Holding, BaseViewHolder> getAdapter() {
        PositionListAdapter adapter = new PositionListAdapter(null);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_position_list, mRvList, false);
        adapter.setHeaderView(headerView);
        return adapter;
    }

    @Override
    protected void loadData() {
        RxUtils.createSoapObservable2("GetHolding", new GetSymbolsRequest(BaseApplication.getInstance().getTradeToken()))
                .map(new Function<SoapObject, List<Holding>>() {
                    @Override
                    public List<Holding> apply(@NonNull SoapObject soapObject) throws Exception {
                        if (soapObject.getPropertyCount() == 0) {
                            throw new RuntimeException("加载失败");
                        }
                        List<Holding> orders = new ArrayList<>();
                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                            orders.add(RxUtils.soapObject2Model((SoapObject) soapObject.getProperty(i), Holding.class));
                        }
                        return orders;
                    }
                })
                .compose(RxUtils.<List<Holding>>applySchedulers())
                .compose(this.<List<Holding>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<Holding>>() {
                    @Override
                    public void accept(@NonNull List<Holding> holdings) throws Exception {
                        mAdapter.setNewData(holdings);
                        loadDataFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });

    }

    @Override
    protected void setManager() {
        super.setManager();
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
    }
}
