package com.yjjr.yjfutures.ui.trade;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.HistoryDataRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.widget.chart.TimeSharingplanChart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

/**
 * 分时图Fragment
 */
public class TimeSharingplanFragment extends BaseFragment {


    private TimeSharingplanChart mChart;
    private String mSymbol;
    private List<HisData> mDatas = new ArrayList<>(100);
    private Quote mQuote;
    private boolean mIsDemo;
    private Gson mGson = new Gson();

    public TimeSharingplanFragment() {
        // Required empty public constructor
    }

    public static TimeSharingplanFragment newInstance(String symbol, boolean isDemo) {
        TimeSharingplanFragment fragment = new TimeSharingplanFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTENT_PARAMETER, symbol);
        bundle.putBoolean(Constants.CONTENT_PARAMETER_2, isDemo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER_2);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mQuote = StaticStore.getQuote(mSymbol, mIsDemo);
        mChart = new TimeSharingplanChart(mContext);
        mChart.setQuote(mQuote);
        return mChart;
    }

    @Override
    protected void initData() {
        super.initData();
        DateTime dateTime;
        if (mQuote.isRest()) { //未开盘，数据加载前一天的
            dateTime = DateUtils.nowDateTime();
            if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 7) { //星期一、星期天前一天还是没数据，要加载星期五的
                dateTime = dateTime.minusDays(1).withDayOfWeek(5).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            } else {
                dateTime = dateTime.minusDays(1).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            }
        } else {
            dateTime = DateUtils.nowDateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
        }
//        HttpManager.getHttpService().getFsData(mQuote.getSymbol(), mQuote.getExchange(), DateUtils.formatData(dateTime.getMillis()))
       /* HttpManager.getHttpService().getHistoryData(HttpConfig.KLINE_URL, new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), DateUtils.formatData(dateTime.getMillis()), "min"))
                .map(new Function<List<HisData>, List<HisData>>() {
                    @Override
                    public List<HisData> apply(@NonNull List<HisData> hisDatas) throws Exception {
                        if (hisDatas == null || hisDatas.isEmpty()) {
                            throw new RuntimeException("数据为空");
                        }
                        return hisDatas;
                    }
                })
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> list) throws Exception {
                        mDatas.clear();
                        mDatas.addAll(list);
                        mChart.addEntries(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mChart.setNoDataText(getString(R.string.data_load_fail));
                    }
                });*/
        if (SocketUtils.getSocket() == null) {
            mChart.setNoDataText(getString(R.string.data_load_fail));
            return;
        }

        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), DateUtils.formatData(dateTime.getMillis()), "min")));
        SocketUtils.getSocket().once(SocketUtils.HIS_DATA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("history data -> " + args[0].toString());
                List<HisData> list = mGson.fromJson(args[0].toString(), new TypeToken<List<HisData>>() {
                }.getType());

                mDatas.clear();
                mDatas.addAll(list);
                mChart.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDatas == null || mDatas.isEmpty()) {
                            mChart.setNoDataText(getString(R.string.data_is_null));
                            return;
                        }
                        mChart.addEntries(mDatas);
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OneMinuteEvent event) {
        if (mQuote == null || mQuote.isRest() || SocketUtils.getSocket() == null) {
            return;
        }
        //一分钟更新一下数据
        final HisData hisData = mDatas.get(mDatas.size() - 1);
//        HttpManager.getHttpService().getFsData(mQuote.getSymbol(), mQuote.getExchange(), hisData.getsDate())
       /* HttpManager.getHttpService().getHistoryData(HttpConfig.KLINE_URL, new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), hisData.getsDate(), "min"))
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
                        for (HisData data : hisDatas) {
                            if (!mDatas.contains(data)) {
                                mDatas.add(data);
                                mChart.addEntry(data);
                            }
                        }
                    }
                }, RxUtils.commonErrorConsumer());*/
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), hisData.getsDate(), "min")));
        SocketUtils.getSocket().once(SocketUtils.HIS_DATA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("history data -> " + args[0].toString());
                final List<HisData> hisDatas = mGson.fromJson(args[0].toString(), new TypeToken<List<HisData>>() {
                }.getType());
                mChart.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hisDatas == null || hisDatas.isEmpty()) {
                            return;
                        }
                        for (HisData data : hisDatas) {
                            if (!mDatas.contains(data)) {
                                mDatas.add(data);
                                mChart.addEntry(data);
                            }
                        }
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (TextUtils.equals(event.getSymbol(), mSymbol)) {
            Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
            mChart.refreshEntry((float) quote.getLastPrice());
        }
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        mChart.refreshEntry((float) quote.getLastPrice());
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
