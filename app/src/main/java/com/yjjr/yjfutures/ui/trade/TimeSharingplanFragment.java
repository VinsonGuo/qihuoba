package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.widget.chart.TimeSharingplanChart;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 分时图Fragment
 */
public class TimeSharingplanFragment extends BaseFragment {


    private TimeSharingplanChart mChart;

    public TimeSharingplanFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChart = new TimeSharingplanChart(mContext);
        return mChart;
    }

    @Override
    protected void initData() {
        super.initData();
        mChart.addEntry(9, (float) (9 + 0.2));
        for (int i = 0; i < 20; i++) {
            float ask = (float) Math.random() + 10;
            mChart.addEntry(ask, (float) (ask + 0.2));
        }
        Observable.interval(1, TimeUnit.SECONDS)
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
                            mChart.refreshEntry(ask, (float) (ask + 0.2));
                        }
                    }
                });
    }
}
