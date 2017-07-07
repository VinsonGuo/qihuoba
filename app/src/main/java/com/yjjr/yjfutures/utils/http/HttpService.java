package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.model.UserLoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/6/19.
 */

public interface HttpService {

    @POST("UserLogin")
    @FormUrlEncoded
    Observable<UserLoginResponse> userLogin(@Field("Account") String account, @Field("Password") String password);
}
