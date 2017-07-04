package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.widget.chart.TickChart;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class TickChartFragment extends BaseFragment {


    private TickChart mChart;

    public TickChartFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChart = new TickChart(mContext);
        return mChart;
    }

    @Override
    protected void initData() {
        super.initData();
        mChart.addEntry(10);
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        float ask = (float) Math.random() + 10;
                        mChart.addEntry(ask);
                    }
                });
    }
}
