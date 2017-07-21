package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.Alipay;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Login;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 登录
 * http://localhost:8080/service/user/login?account=Lj&password=123456
 * <p>
 * 短信验证码
 * http://localhost:8080/service/sms/send?recNum=15002078823
 * <p>
 * <p>
 * 验证短信验证码
 * http://localhost:8080/service/sms/validate?code=1584
 * <p>
 * 绑定支付宝
 * http://localhost:8080/service/user/login?account=Lj&alipay=123456
 * Created by guoziwei on 2017/7/20.
 */

public interface BizService {
    @FormUrlEncoded
    @POST("user/login")
    Observable<BizResponse<Login>> login(@Field("account") String account, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/bindAlipay")
    Observable<BizResponse<Alipay>> bindAlipay(@Field("account") String account, @Field("alipay") String alipay);
}
