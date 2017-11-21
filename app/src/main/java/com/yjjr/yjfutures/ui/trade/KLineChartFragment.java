package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.Gson;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.HistoryDataRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.chart.ChartScrollTouchListener;
import com.yjjr.yjfutures.widget.chart.InfoViewListener;
import com.yjjr.yjfutures.widget.chart.KLineXValueFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import io.socket.emitter.Emitter;

public class KLineChartFragment extends BaseFullScreenChartFragment {

    private String mSymbol;
    private boolean mIsDemo;
    private Gson mGson = new Gson();
    private String mType;
    private Quote mQuote;


    public KLineChartFragment() {
        // Required empty public constructor
    }

    public static KLineChartFragment newInstance(String symbol, boolean isDemo, String type) {
        KLineChartFragment fragment = new KLineChartFragment();
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
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER_2, false);
            mType = getArguments().getString(Constants.CONTENT_PARAMETER_3);
        }
    }


    @Override
    protected void initData() {
//        xAxisVolume.setDrawLabels(false);
        axisLeftPrice.setDrawLabels(false);
        axisLeftVolume.setDrawLabels(false);

        mChartPrice.setDrawBorders(false);
        mChartVolume.setDrawBorders(false);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mChartVolume.getLayoutParams();
        params.topMargin = DisplayUtils.dip2px(mContext, -30);
        params.height = params.height + DisplayUtils.dip2px(mContext, 30);
        mChartVolume.setLayoutParams(params);

        xAxisVolume.setValueFormatter(new KLineXValueFormatter(mType, mData));
        mChartPrice.setDragEnabled(false);
        mChartVolume.setDragEnabled(false);
        OnChartGestureListener l = new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                FullScreenChartActivity.startActivity(mContext, mSymbol, mIsDemo);
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        };
        mChartPrice.setOnChartGestureListener(l);
        mChartVolume.setOnChartGestureListener(l);
        mChartPrice.setOnTouchListener(new ChartScrollTouchListener());
        mChartVolume.setOnTouchListener(new ChartScrollTouchListener());
        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, mQuote, mData, mKInfo, mChartVolume));
        mChartVolume.setOnChartValueSelectedListener(new InfoViewListener(mContext, mQuote, mData, mKInfo, mChartPrice));

       mQuote = StaticStore.getQuote(mSymbol, mIsDemo);
       /*  if (mQuote == null) return;

        DateTime dateTime = DateUtils.getChartStartTime(mQuote, mType);

        if (SocketUtils.getSocket() == null) {
            mChartPrice.setNoDataText(getString(R.string.data_load_fail));
            mChartVolume.setNoDataText(getString(R.string.data_load_fail));
            return;
        }
        String s = mGson.toJson(new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), DateUtils.formatData(dateTime.getMillis()), mType));
        LogUtils.d("request data -> %s", s);
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, s);
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
                            setLimitLine(mQuote);
                        }
                        initChartKData(mChartPrice, isMin);
                        initChartVolumeData(mChartVolume);
                    }
                });
            }
        });*/
       loadDataByType(mType);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OneMinuteEvent event) {
        //一分钟更新一下数据
        final HisData hisData = mData.get(mData.size() - 1);
        if (mQuote == null || mQuote.isRest() || SocketUtils.getSocket() == null || hisData == null) {
            return;
        }
        if (!TextUtils.equals(mType, HttpConfig.MIN)) {
            return;
        }
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), hisData.getsDate(), HttpConfig.MIN)));
        SocketUtils.getSocket().once(SocketUtils.HIS_DATA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("history data -> " + args[0].toString());
                final List<HisData> hisDatas = StringUtils.parseHisData(args[0].toString(), hisData);
                mChartPrice.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hisDatas == null || hisDatas.isEmpty()) {
                            return;
                        }
                        addKData(hisDatas);
                    }
                });
            }
        });
    }

    public void loadDataByType(String type) {
        SocketUtils.getSocket().off(SocketUtils.HIS_DATA);
        mType = type;
        xAxisVolume.setValueFormatter(new KLineXValueFormatter(mType, mData));
        final Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        if(quote == null) {
            return;
        }

        DateTime dateTime = DateUtils.getChartStartTime(quote, mType);
        if (SocketUtils.getSocket() == null) {
            mChartPrice.setNoDataText(getString(R.string.data_load_fail));
            mChartVolume.setNoDataText(getString(R.string.data_load_fail));
            return;
        }

        String json = mGson.toJson(new HistoryDataRequest(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()), mType));
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, json);
        LogUtils.d("history request data -> %s", json);
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
//                        if (isMin) {
//                            setLimitLine(quote);
//                        }
                        initChartKData(mChartPrice, isMin);
                        initChartVolumeData(mChartVolume);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
