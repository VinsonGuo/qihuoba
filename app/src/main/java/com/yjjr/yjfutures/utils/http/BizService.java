package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.model.CloseOrder;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.biz.Active;
import com.yjjr.yjfutures.model.biz.Alipay;
import com.yjjr.yjfutures.model.biz.AssetRecord;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.CashRecord;
import com.yjjr.yjfutures.model.biz.ChargeResult;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.model.biz.Notice;
import com.yjjr.yjfutures.model.biz.NumberResult;
import com.yjjr.yjfutures.model.biz.PageResponse;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.model.publish.News;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @POST("user/login?smartisanOS=android")
    Observable<BizResponse<UserInfo>> login(@Field("account") String account, @Field("password") String password, @Field("versionType") String versionType);

    @FormUrlEncoded
    @POST("user/bindAlipay")
    Observable<BizResponse<Alipay>> bindAlipay(@Field("account") String account, @Field("alipay") String alipay);

    /**
     * @param type type=1 注册；type=2 修改手机号；type=3 找回密码
     */
    @GET("sms/send")
    Observable<BizResponse> sendSms(@Query("recNum") String recNum, @Query("type") int type);

    @FormUrlEncoded
    @POST("user/setPayPwd")
    Observable<BizResponse> setPayPwd(@Field("account") String account, @Field("payPwd") String payPwd, @Field("hisPayPwd") String hisPayPwd);

    @FormUrlEncoded
    @POST("identityAuth/auth")
    Observable<BizResponse> auth(@Field("userName") String userName, @Field("idCardNo") String idCardNo);

    @GET("user/queryFundsOverview")
    Observable<BizResponse<Funds>> getFunds();

    @GET("dictionary/SERVICE_INFO")
    Observable<BizResponse<NumberResult>> getSerivceInfo();

    @GET("dictionary/RECHARGE_ACCOUNT")
    Observable<BizResponse<ChargeResult>> getChargeInfo();

    @GET("dictionary/list/BANNER_IMG")
    Observable<BizResponse<List<Info>>> getBanner();

    @GET("dictionary/list/WELCOME_IMG")
    Observable<BizResponse<List<Info>>> getWelcomImg();

    @FormUrlEncoded
    @POST("user/validPayPwd")
    Observable<BizResponse> validPayPwd(@Field("account") String account, @Field("payPwd") String payPwd);

    @GET("version/checkUpdate/android?appCode=" + BuildConfig.APPLICATION_ID)
    Observable<BizResponse<Update>> checkUpdate(@Query("version") String version);

    @FormUrlEncoded
    @POST("trader/getContractInfo")
    Observable<BizResponse<ContractInfo>> getContractInfo(@Field("symbol") String symbol);

    @FormUrlEncoded
    @POST("user/rechargeApply")
    Observable<BizResponse> rechargeApply(@Field("money") String money, @Field("accountType") String accountType, @Field("cid") String account);

    @FormUrlEncoded
    @POST("user/extractApply")
    Observable<BizResponse> extractApply(@Field("money") String money, @Field("accountType") String accountType);

    /**
     * 修改手机号码接口
     */
    @FormUrlEncoded
    @POST("user/resetMobile")
    Observable<BizResponse> resetMobile(@Field("recNum") String recNum, @Field("code") String code);


    /**
     * 忘记密码重置密码
     */
    @FormUrlEncoded
    @POST("user/resetPwd")
    Observable<BizResponse> resetPwd(@Field("account") String account, @Field("password") String password, @Field("code") String code);


    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register?smartisanOS=android")
    Observable<BizResponse> register(@Field("account") String account, @Field("password") String password, @Field("code") String code, @Field("versionType") String versionType);

    @POST("user/queryAssetRecord/{start}/{count}")
    Observable<BizResponse<PageResponse<AssetRecord>>> getAssetRecord(@Path("start") int start, @Path("count") int count);

    /**
     * 提现记录
     */
    @POST("user/queryCashRecord/{start}/{count}")
    Observable<BizResponse<PageResponse<CashRecord>>> getCashRecord(@Path("start") int start, @Path("count") int count);


    /**
     * 充值记录
     */
    @POST("user/queryRechargeRecord/{start}/{count}")
    Observable<BizResponse<PageResponse<CashRecord>>> getRechargeRecord(@Path("start") int start, @Path("count") int count);

    @POST("notice/list/{start}/{count}")
    Observable<BizResponse<PageResponse<Notice>>> getNotice(@Path("start") int start, @Path("count") int count);

    @POST("trader/closedOrderList/{start}/{count}")
    Observable<BizResponse<PageResponse<CloseOrder>>> getCloseOrder(@Path("start") int start, @Path("count") int count);

    @POST("trader/holdingList")
    Observable<BizResponse<List<Holds>>> getHolding();

    @FormUrlEncoded
    @POST("trader/sendOrder")
    Observable<BizResponse<CommonResponse>> sendOrder(
            @Field("cid") String account,
            @Field("symbol") String symbol,
            @Field("buysell") String buysell,
            @Field("price") double price,
            @Field("qty") int qty,
            @Field("ordertype") String ordertype,
            @Field("lossPriceLine") double lossPriceLine,
            @Field("profitPriceLine") double profitPriceLine,
            @Field("fee") double fee,
            @Field("marginYJ") double marginYJ);

    @FormUrlEncoded
    @POST("trader/closeOrder")
    Observable<BizResponse<CommonResponse>> closeOrder(
            @Field("cid") String account,
            @Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("trader/setRiskControlTrigger")
    Observable<BizResponse<CommonResponse>> setRiskControlTrigger(
            @Field("orderId") String orderId,
            @Field("lossPriceLine") double lossPriceLine,
            @Field("profitPriceLine") double profitPriceLine);

    @FormUrlEncoded
    @POST("trader/onkeyCloseOrder")
    Observable<BizResponse> closeAllOrder(
            @Field("cid") String account,
            @Field("symbol") String symbol);

    @FormUrlEncoded
    @POST("user/resetCapitalAccount")
    Observable<BizResponse> resetCapitalAccount(@Field("cid") String account);


    @GET("activict/queryActivict")
    Observable<BizResponse<Active>> getActivity();

    @GET("http://www.junchengtz.com/news/newsList.htm?type=0&date=")
    Observable<List<News>> getNewsList();

}
