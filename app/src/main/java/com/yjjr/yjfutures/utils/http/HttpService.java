package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.UserLoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/6/19.
 */

public interface HttpService {

    @POST("/api/login.ashx")
    @FormUrlEncoded
    Observable<UserLoginResponse> userLogin(@Field("uname") String account, @Field("upass") String password);

    @GET("/api/get_quote.ashx")
//    @FormUrlEncoded
    Observable<Quote> getQuote(@Query("symbol") String symbol, @Query("exchange") String exchange);
}
