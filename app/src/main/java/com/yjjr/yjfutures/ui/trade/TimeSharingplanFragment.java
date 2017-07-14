package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 分时图Fragment
 */
public class TimeSharingplanFragment extends BaseFragment {


    private TimeSharingplanChart mChart;
    private String mSymbol;
    private List<HisData> mDatas = new ArrayList<>(100);

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
        final Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        final DateTime dateTime = new DateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
        HttpManager.getHttpService().getFsData(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()))
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> list) throws Exception {
                        mDatas.clear();
                        mDatas.addAll(list);
                        mChart.setStartTime(new DateTime(list.get(0).getsDate()));
                        mChart.addEntries(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mChart.setNoDataText(getString(R.string.data_load_fail));
                    }
                });
        // 一分钟更新一下数据
        Observable.interval(1, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        DateTime dateTime = new DateTime();
                        return dateTime.getSecondOfMinute() == 1;
                    }
                })
                .flatMap(new Function<Long, ObservableSource<List<HisData>>>() {
                    @Override
                    public ObservableSource<List<HisData>> apply(@NonNull Long aLong) throws Exception {
                        HisData hisData = mDatas.get(mDatas.size() - 1);
                        return HttpManager.getHttpService().getFsData(quote.getSymbol(), quote.getExchange(), hisData.getsDate());
                    }
                })
                .filter(new Predicate<List<HisData>>() {
                    @Override
                    public boolean test(@NonNull List<HisData> hisDatas) throws Exception {
                        return hisDatas != null && hisDatas.size() > 0;
                    }
                })
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> hisDatas) throws Exception {
                        HisData data = hisDatas.get(hisDatas.size() - 1);
                        mDatas.add(data);
                        mChart.addEntry((float) data.getClose());
                    }
                }, RxUtils.commonErrorConsumer());
    }
}
