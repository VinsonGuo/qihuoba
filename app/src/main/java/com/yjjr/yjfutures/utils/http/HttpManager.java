package com.yjjr.yjfutures.utils.http;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yjjr.yjfutures.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dell on 2017/6/19.
 */

public class HttpManager {

    private static volatile Retrofit sRetrofit;
    private static volatile HttpService sHttpService;
    private static volatile Retrofit sBizRetrofit;
    private static volatile BizService sBizService;
    private static volatile Retrofit sDemoRetrofit;
    private static volatile HttpService sDemoHttpService;
    private static volatile Retrofit sDemoBizRetrofit;
    private static volatile BizService sDemoBizService;

    private static volatile OkHttpClient sClient;

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.DOMAIN + ":9100/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static Retrofit getDemoInstance() {
        if (sDemoRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sDemoRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.DEMO_HOST + ":9100/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sDemoRetrofit;
    }

    public static Retrofit getBizInstance() {
        if (sBizRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sBizRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.BIZ_HOST + "/service/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sBizRetrofit;
    }

    public static Retrofit getDemoBizInstance() {
        if (sDemoBizRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sDemoBizRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.BIZ_HOST + "/simulation/service/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sDemoBizRetrofit;
    }

    public static BizService getBizService(boolean isDemo) {
        if (isDemo) {
            if (sDemoBizService == null) {
                sDemoBizService = getDemoBizInstance().create(BizService.class);
            }
            return sDemoBizService;
        } else {
            if (sBizService == null) {
                sBizService = getBizInstance().create(BizService.class);
            }
            return sBizService;
        }
    }


    public static BizService getBizService() {
        if (sBizService == null) {
            sBizService = getBizInstance().create(BizService.class);
        }
        return sBizService;
    }

    @NonNull
    public synchronized static OkHttpClient getOkHttpClient() {
        if(sClient == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            sClient = new OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(new StethoInterceptor()).cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            }).build();
        }
        return sClient;
    }

    /**
     * 获取交易服务器接口
     *
     * @param isDemo 是否是模拟账户
     */
    public static HttpService getHttpService(boolean isDemo) {
        if (isDemo) {
            if (sDemoHttpService == null) {
                sDemoHttpService = getDemoInstance().create(HttpService.class);
            }
            return sDemoHttpService;
        } else {
            if (sHttpService == null) {
                sHttpService = getInstance().create(HttpService.class);
            }
            return sHttpService;
        }
    }

    public static HttpService getHttpService() {
        if (sHttpService == null) {
            sHttpService = getInstance().create(HttpService.class);
        }
        return sHttpService;
    }
}
