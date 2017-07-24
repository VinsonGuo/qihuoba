package com.yjjr.yjfutures.utils.http;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

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

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.DOMAIN + ":9100/WebService.asmx/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static Retrofit getBizInstance() {
        if (sBizRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sBizRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.DOMAIN + ":9300/service/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sBizRetrofit;
    }

    public static BizService getBizService() {
        if (sBizService == null) {
            sBizService = getBizInstance().create(BizService.class);
        }
        return sBizService;
    }

    @NonNull
    public static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(new StethoInterceptor()).cookieJar(new CookieJar() {
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

    public static HttpService getHttpService() {
        if (sHttpService == null) {
            sHttpService = getInstance().create(HttpService.class);
        }
        return sHttpService;
    }
}
