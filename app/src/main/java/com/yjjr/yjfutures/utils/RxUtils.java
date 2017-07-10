package com.yjjr.yjfutures.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29.
 */

public class RxUtils {

    private static Gson sGson = new Gson();

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

    public static <T> Observable<T> createSoapObservable(final String methodName, final Object model, final Class<T> responseClz) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                try {

                    String url = HttpConfig.BASE_URL;
                    // SOAP Action
                    final String soapAction = HttpConfig.SOAP_ACTION + methodName;

                    // 指定WebService的命名空间和调用的方法名
                    SoapObject rpc = model2SoapObject(methodName, model);
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

                    // 设置是否调用的是dotNet开发的WebService
                    envelope.dotNet = true;
                    // 等价于envelope.bodyOut = rpc;
                    envelope.setOutputSoapObject(rpc);

                    final HttpTransportSE transport = new HttpTransportSE(url);
                    // 调用WebService
                    LogUtils.d(rpc.toString());
                    transport.call(soapAction, envelope);
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    SoapObject s = (SoapObject) result.getProperty(0);
                    T t = soapObject2Model(s, responseClz);
                    e.onNext(t);
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }


    public static Observable<SoapObject> createSoapObservable2(final String methodName, final Object model) {
        return Observable.create(new ObservableOnSubscribe<SoapObject>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SoapObject> e) throws Exception {
                try {

                    String url = HttpConfig.BASE_URL;
                    // SOAP Action
                    final String soapAction = HttpConfig.SOAP_ACTION + methodName;

                    // 指定WebService的命名空间和调用的方法名
                    SoapObject rpc = model2SoapObject(methodName, model);
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

                    // 设置是否调用的是dotNet开发的WebService
                    envelope.dotNet = true;
                    // 等价于envelope.bodyOut = rpc;
                    envelope.setOutputSoapObject(rpc);

                    final HttpTransportSE transport = new HttpTransportSE(url);
                    // 调用WebService
                    LogUtils.d(rpc.toString());
                    transport.call(soapAction, envelope);
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    LogUtils.d(result.toString());
                    SoapObject s = (SoapObject) result.getProperty(0);
                    e.onNext(s);
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public static Observable<SoapObject> createSoapObservable3(final String methodName, final SoapObject rpc) {
        return Observable.create(new ObservableOnSubscribe<SoapObject>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SoapObject> e) throws Exception {
                try {
                    // 命名空间

                    String url = HttpConfig.BASE_URL;
                    // SOAP Action
                    final String soapAction = HttpConfig.SOAP_ACTION + methodName;

                    // 指定WebService的命名空间和调用的方法名
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

                    // 设置是否调用的是dotNet开发的WebService
                    envelope.dotNet = true;
                    // 等价于envelope.bodyOut = rpc;
                    envelope.setOutputSoapObject(rpc);

                    final HttpTransportSE transport = new HttpTransportSE(url);
                    // 调用WebService
                    LogUtils.d(rpc.toString());
                    transport.call(soapAction, envelope);
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    SoapObject s = (SoapObject) result.getProperty(0);
                    LogUtils.d(s.toString());
                    e.onNext(s);
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public static SoapObject model2SoapObject(String methodName, Object o) throws Exception {

        SoapObject rpc = new SoapObject(HttpConfig.NAME_SPACE, methodName);
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if ("serialVersionUID".equals(field.getName()) || field.getName().startsWith("$")) {
                continue;
            }
            rpc.addProperty(field.getName(), field.get(o));
        }
        return rpc;
    }

    public static <T> T soapObject2Model(SoapObject soap, Class<T> clazz) throws Exception {
        Map<Object, Object> map = new HashMap<>(10);
        PropertyInfo pi = new PropertyInfo();
        int count = soap.getPropertyCount();
        for (int i = 0; i < count; i++) {
            soap.getPropertyInfo(i, pi);
            map.put(pi.getName(), soap.getProperty(i).toString());
        }
        JsonElement jsonElement = sGson.toJsonTree(map);
        return sGson.fromJson(jsonElement, clazz);
    }

    public static <T> T soapObject2Model(SoapObject soap, Type type) throws Exception {
        Map<Object, Object> map = new HashMap<>(10);
        PropertyInfo pi = new PropertyInfo();
        int count = soap.getPropertyCount();
        for (int i = 0; i < count; i++) {
            soap.getPropertyInfo(i, pi);
            map.put(pi.getName(), soap.getProperty(i).toString());
        }
        JsonElement jsonElement = sGson.toJsonTree(map);
        return sGson.fromJson(jsonElement, type);
    }
}
