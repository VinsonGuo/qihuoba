package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.widget.PriceRealTimeChart;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends BaseFragment {


    public LineChartFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final PriceRealTimeChart chart = new PriceRealTimeChart(mContext);
        chart.addEntry(10, 10.2f);
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        float ask = (float) Math.random() + 10;
                        chart.addEntry(ask, (float) (ask + 0.2));
                    }
                });
        return chart;
    }

}
