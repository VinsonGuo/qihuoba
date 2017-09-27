package com.yjjr.yjfutures.utils.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yjjr.yjfutures.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dell on 2017/6/19.
 */

public class HttpManager {

    private static Retrofit sRetrofit;
    private static HttpService sHttpService;
    private static Retrofit sBizRetrofit;
    private static BizService sBizService;
    private static Retrofit sDemoRetrofit;
    private static HttpService sDemoHttpService;
    private static Retrofit sDemoBizRetrofit;
    private static BizService sDemoBizService;

    private static volatile OkHttpClient sClient;

    public synchronized static Retrofit getInstance() {
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

    public synchronized static Retrofit getDemoInstance() {
        if (sDemoRetrofit == null) {
            OkHttpClient client = getOkHttpClient();
            sDemoRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.DEMO_HOST)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sDemoRetrofit;
    }

    public synchronized static Retrofit getBizInstance() {
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

    public synchronized static Retrofit getDemoBizInstance() {
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


    public synchronized static BizService getBizService() {
        if (sBizService == null) {
            sBizService = getBizInstance().create(BizService.class);
        }
        return sBizService;
    }

    @NonNull
    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            sClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                private String token;

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request;
                    if (!TextUtils.isEmpty(token)) {
                        request = chain.request().newBuilder()
                                .addHeader("token", token)
                                .build();
                    } else {
                        request = chain.request();
                    }
                    Response response = chain.proceed(request);
                    if (!TextUtils.isEmpty(response.header("token"))) {
                        token = response.header("token");
                    }
                    return response;
                }
            }).addNetworkInterceptor(new StethoInterceptor()).cookieJar(new CookieJar() {
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
            }).addInterceptor(interceptor).build();
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

    public synchronized static HttpService getHttpService() {
        if (sHttpService == null) {
            sHttpService = getInstance().create(HttpService.class);
        }
        return sHttpService;
    }


    public static OkHttpClient getUnsafeOkHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
