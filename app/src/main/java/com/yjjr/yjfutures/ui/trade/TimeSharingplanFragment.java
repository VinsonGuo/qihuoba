package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.chart.TimeSharingplanChart;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 分时图Fragment
 */
public class TimeSharingplanFragment extends BaseFragment {


    private TimeSharingplanChart mChart;
    private String mSymbol;

    public TimeSharingplanFragment() {
        // Required empty public constructor
    }

    public static TimeSharingplanFragment newInstance(String symbol) {
        TimeSharingplanFragment fragment = new TimeSharingplanFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTENT_PARAMETER, symbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChart = new TimeSharingplanChart(mContext);
        return mChart;
    }

    @Override
    protected void initData() {
        super.initData();
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        DateTime dateTime = new DateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
        HttpManager.getHttpService().getFsData(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()))
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> list) throws Exception {
                        mChart.setStartTime(new DateTime(list.get(0).getsDate()));
                        mChart.addEntries(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }
}
