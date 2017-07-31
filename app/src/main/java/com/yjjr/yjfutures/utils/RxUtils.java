package com.yjjr.yjfutures.utils;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.event.UpdateUserInfoEvent;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29.
 */

public class RxUtils {

    /**
     * io线程执行，主线程观察
     * .compose(RxUtils.<T>applySchedulers())
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 增加了统一的判断逻辑
     */
    public static <T extends BizResponse> ObservableTransformer<T, T> applyBizSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable
                        .map(new Function<T, T>() {
                            @Override
                            public T apply(@NonNull final T t) throws Exception {
                                if (t.getRcode() == 99) {
                                    EventBus.getDefault().post(new UpdateUserInfoEvent());
                                }
                                if (t.getRcode() != 0) {
                                    throw new RuntimeException(t.getRmsg());
                                }
                                return t;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public static Consumer<? super Throwable> commonErrorConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LogUtils.e(throwable);
            }
        };
    }

    /**
     * 平仓请求
     */
    public static Observable<CommonResponse> createCloseObservable(Holding holding) {
        return HttpManager.getHttpService().sendOrder(BaseApplication.getInstance().getTradeToken(), holding.getSymbol(), StringUtils.getOppositeBuySell(holding.getBuySell()), 0, Math.abs(holding.getQty()), "市价")
                .map(new Function<CommonResponse, CommonResponse>() {
                    @Override
                    public CommonResponse apply(@NonNull CommonResponse commonResponse) throws Exception {
                        if (commonResponse.getReturnCode() < 0) {
                            throw new RuntimeException(commonResponse.getMessage());
                        }
                        return commonResponse;
                    }
                });
    }

    /**
     * 统一的发送验证码接口
     */
    public static void handleSendSms(final BaseActivity mContext, final View btn, final CountDownTimer timer, String phoneNumber) {
        btn.setEnabled(false);
        HttpManager.getBizService().sendSms(phoneNumber)
                .compose(RxUtils.applyBizSchedulers())
                .compose(mContext.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse>() {
                    @Override
                    public void accept(@NonNull BizResponse bizResponse) throws Exception {
                        timer.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        ToastUtils.show(mContext, throwable.getMessage());
                        btn.setEnabled(true);
                    }
                });
    }
}
