package com.yjjr.yjfutures.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/11.
 */

public class PollService extends Service {
    public static final String ACTION = "com.yjjr.yjfutures.PollingService";
    private Timer mTimer;
    private Disposable mSubscribe;

    public static void startService(Context context) {
        Intent intent = new Intent(context, PollService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, PollService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTimer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestData();
            }
        }, 5000);
        return START_STICKY;
    }

    private void requestData() {
        mSubscribe = HttpManager.getHttpService().getQuoteList(StaticStore.sSymbols, StaticStore.sExchange)
                .map(new Function<List<Quote>, List<Quote>>() {
                    @Override
                    public List<Quote> apply(@NonNull List<Quote> quotes) throws Exception {
                        for (Quote quote : quotes) {
                            StaticStore.sQuoteMap.put(quote.getSymbol(), quote);
                        }
                        return quotes;
                    }
                })
                .compose(RxUtils.<List<Quote>>applySchedulers())
                .subscribe(new Consumer<List<Quote>>() {
                    @Override
                    public void accept(@NonNull List<Quote> quotes) throws Exception {
                        EventBus.getDefault().post(new RefreshEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }
}
