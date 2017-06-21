package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.text.TextUtils;


import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29.
 */

public class RxUtils {

    /**
     * io线程执行，主线程观察
     * .compose(RxUtils.<T>applySchedulers())
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

  /*  public static <T extends CommonResponse> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .map(RxUtils.<T>isSuccessFunc1())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }*/

    /*public static <T extends CommonResponse> Func1<T, T> isSuccessFunc1() {
        return new Func1<T, T>() {
            @Override
            public T call(T commonResponse) {
                // 检测token过期
                if (BaseApplication.getInstance().isRealAccount()
                        && TextUtils.equals(commonResponse.getReturnCode(), "-4002")) {
                    EventBus.getDefault().post(new UnauthorizeEvent());
                }
                if (!commonResponse.isSuccess()) {
                    throw new BusinessException(commonResponse.getMessage());
                }
                return commonResponse;
            }
        };
    }*/

    public static Action1<Throwable> commonErrorAction(final Context context) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtils.e(throwable);
                if (throwable instanceof BusinessException) {
                    ToastUtils.show(context, throwable.getMessage());
                } else if (throwable instanceof IllegalStateException) {
//                    ToastUtils.show(context, R.string.send_request_fail);
                }
            }
        };
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String detailMessage) {
            super(detailMessage);
        }

        public BusinessException() {
        }
    }
}
