package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.HistoryDataRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class FullScreenLineChartFragment extends BaseFullScreenChartFragment {

    private String mSymbol;
    private boolean mIsDemo;
    private Gson mGson = new Gson();


    public FullScreenLineChartFragment() {
        // Required empty public constructor
    }

    public static FullScreenLineChartFragment newInstance(String symbol, boolean isDemo) {
        FullScreenLineChartFragment fragment = new FullScreenLineChartFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CONTENT_PARAMETER, symbol);
        args.putBoolean(Constants.CONTENT_PARAMETER_2, isDemo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER_2, false);
        }
    }


    @Override
    protected void initData() {
        xAxisVolume.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (mData != null && value < mData.size()) {
                    DateTime dateTime = new DateTime(mData.get((int) value).getsDate());
                    return DateUtils.formatTime(dateTime.getMillis());
                }
                return "";
            }
        });
        final Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        if (quote == null) return;
        DateTime dateTime;
        if (quote.isRest()) { //未开盘，数据加载前一天的
            dateTime = DateUtils.nowDateTime();
            if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 7) { //星期一、星期天前一天还是没数据，要加载星期五的
                dateTime = dateTime.minusDays(1).withDayOfWeek(5).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            } else {
                dateTime = dateTime.minusDays(1).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            }
        } else {
            dateTime = DateUtils.nowDateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
        }

        if (SocketUtils.getSocket() == null) {
            mChartPrice.setNoDataText(getString(R.string.data_load_fail));
            mChartVolume.setNoDataText(getString(R.string.data_load_fail));
            return;
        }
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()), "min")));
        SocketUtils.getSocket().once(SocketUtils.HIS_DATA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("history data -> " + args[0].toString());
                mData.clear();
                final List<HisData> list = StringUtils.parseHisData(args[0].toString(), null);
                mData.addAll(list);

                mChartPrice.post(new Runnable() {
                    @Override
                    public void run() {
                        if (list.isEmpty()) {
                           mChartPrice.setNoDataText(getString(R.string.data_load_fail));
                            mChartVolume.setNoDataText(getString(R.string.data_load_fail));
                            return;
                        }

                        setLimitLine(quote);
                        initChartPriceData(mChartPrice);
                        initChartVolumeData(mChartVolume);
                    }
                });
            }
        });
    }


}
