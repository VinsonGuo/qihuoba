package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.text.TextUtils;

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
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.chart.ChartInfoViewHandler;
import com.yjjr.yjfutures.widget.chart.InfoViewListener;
import com.yjjr.yjfutures.widget.chart.KLineXValueFormatter;
import com.yjjr.yjfutures.widget.chart.YValueFormatter;

import org.joda.time.DateTime;

import java.util.List;

import io.socket.emitter.Emitter;

public class FullScreenKLineChartFragment extends BaseFullScreenChartFragment {

    private String mSymbol;
    private boolean mIsDemo;
    private Gson mGson = new Gson();
    private String mType;


    public FullScreenKLineChartFragment() {
        // Required empty public constructor
    }

    public static FullScreenKLineChartFragment newInstance(String symbol, boolean isDemo, String type) {
        FullScreenKLineChartFragment fragment = new FullScreenKLineChartFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CONTENT_PARAMETER, symbol);
        args.putBoolean(Constants.CONTENT_PARAMETER_2, isDemo);
        args.putString(Constants.CONTENT_PARAMETER_3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER_2, false);
            mType = getArguments().getString(Constants.CONTENT_PARAMETER_3);
        }
    }


    @Override
    protected void initData() {

        xAxisVolume.setValueFormatter(new KLineXValueFormatter(mType, mData));
        final Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        if (quote == null) return;

        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, quote, mData, mKInfo, mChartVolume));
        mChartVolume.setOnChartValueSelectedListener(new InfoViewListener(mContext, quote, mData, mLineInfo, mChartPrice));
        mChartPrice.setOnTouchListener(new ChartInfoViewHandler(mChartPrice));
        axisLeftPrice.setValueFormatter(new YValueFormatter(quote.getTick()));
//        mMvx.setType(type);
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
            // 如果现在的时间在六点之前，减少一天
            if (DateUtils.nowDateTime().isBefore(dateTime)) {
                dateTime.minusDays(1);
            }
        }
        if (HttpConfig.DAY.equals(mType)) {
            dateTime = dateTime.minusYears(1);
        } else if (HttpConfig.MIN15.equals(mType) || HttpConfig.MIN5.equals(mType)) {
//            dateTime = dateTime.minusWeeks(1);
            dateTime = dateTime.minusDays(5);
        } else if (HttpConfig.HOUR.equals(mType)) {
            dateTime = dateTime.minusMonths(1);
        }

        if (SocketUtils.getSocket() == null) {
            mChartPrice.setNoDataText(getString(R.string.data_load_fail));
            mChartVolume.setNoDataText(getString(R.string.data_load_fail));
            return;
        }
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()), mType)));
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

                        boolean isMin = TextUtils.equals(mType, HttpConfig.MIN);
                        if (isMin) {
                            setLimitLine(quote);
                        }
                        initChartKData(mChartPrice, isMin);
                        initChartVolumeData(mChartVolume);
                    }
                });
            }
        });
    }


}
