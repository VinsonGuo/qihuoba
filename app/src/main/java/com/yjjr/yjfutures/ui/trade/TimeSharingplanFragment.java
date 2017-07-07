package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.GetFSDataRequest;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.chart.TimeSharingplanChart;

import org.joda.time.DateTime;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        GetFSDataRequest request = new GetFSDataRequest(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()));
        LogUtils.d(request.toString());
        mChart.setStartTime(dateTime);
        SoapObject soapObject = new SoapObject(HttpConfig.NAME_SPACE, "GetFSData");
        soapObject.addProperty("Symbol",quote.getSymbol());
        soapObject.addProperty("Exchange",quote.getExchange());
        soapObject.addProperty("StartTime",DateUtils.formatData(dateTime.getMillis()));
        RxUtils.createSoapObservable3("GetFSData",soapObject)
                .map(new Function<SoapObject, List<HisData>>() {
                    @Override
                    public List<HisData> apply(@NonNull SoapObject soapObject) throws Exception {
                        List<HisData> list = new ArrayList<>(300);
                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                            list.add(RxUtils.soapObject2Model((SoapObject) soapObject.getProperty(i),HisData.class));
                        }
                        return list;
                    }
                })
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> list) throws Exception {
                        LogUtils.d(list.toString());
                        mChart.addEntries(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
        /*Observable.interval(1, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        DateTime now = DateTime.now();
                        if (now.getSecondOfMinute() == 0) {
                            mChart.addLastEntry();
                        } else {
                            float ask = (float) Math.random() + 10;
                            mChart.refreshEntry(ask);
                        }
                    }
                });*/
    }
}
