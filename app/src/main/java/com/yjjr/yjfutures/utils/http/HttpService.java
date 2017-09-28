package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.model.AccountInfo;
import com.yjjr.yjfutures.model.CloseOrder;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.Exchange;
import com.yjjr.yjfutures.model.FilledOrder;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.HistoryDataRequest;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.IpResponse;
import com.yjjr.yjfutures.model.OpenOrder;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.model.Trade;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.utils.ActivityTools;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 1、登录
 * http://139.224.8.133:9100/api/login.ashx
 * 传参：uname,upass
 * <p>
 * 2、获取单个合约报价
 * http://139.224.8.133:9100/api/get_quote.ashx
 * 传参：symbol,exchange
 * <p>
 * 3、一次获取多个合约报价
 * http://139.224.8.133:9100/api/get_quote_list.ashx
 * 传参：symbol="CLQ7,ESU7,HSIN17"
 * <p>
 * exchange="NYMEX,GLOBEX,HKFE"
 * <p>
 * 4、获取成交明细
 * http://139.224.8.133:9100/api/get_trades.ashx
 * 传参：symbol,exchange,id,number
 * <p>
 * 5、获取分时图数据
 * http://139.224.8.133:9100/api/get_fs_data.ashx
 * 传参：symbol,exchange,starttime
 * <p>
 * 6、获取历史数据
 * http://139.224.8.133:9100/api/get_h_data.ashx
 * 传参：symbol,exchange,starttime,datatype
 * <p>
 * 7、下单
 * http://139.224.8.133:9100/api/send_order.ashx
 * 传参：account,symbol,buysell,price,qty,ordertype
 * <p>
 * 8、获取未成交订单
 * http://139.224.8.133:9100/api/get_open_order.ashx
 * 传参：account
 * <p>
 * 9、获取未成交合约
 * http://139.224.8.133:9100/api/get_unfill_order.ashx
 * 传参：account
 * <p>
 * 10、获取已成交订单
 * http://139.224.8.133:9100/api/get_filled_order.ashx
 * 传参：account
 * <p>
 * 11、获取新的已成交订单
 * http://139.224.8.133:9100/api/get_filled_order_ex.ashx
 * 传参：account,id
 * <p>
 * 12、获取持仓
 * http://139.224.8.133:9100/api/get_holding.ashx
 * 传参：account
 * <p>
 * 13、撤单
 * http://139.224.8.133:9100/api/cancel_order.ashx
 * 传参：account,ordered
 * <p>
 * 14、获取能交易的所有合约列表
 * http://139.224.8.133:9100/api/get_symbols.ashx
 * 传参：account
 * <p>
 * 15、密码重置
 * http://139.224.8.133:9100/api/reset_password.ashx
 * 传参：account,password
 * <p>
 * 16、修改密码
 * http://139.224.8.133:9100/api/change_password.ashx
 * 传参：account,oldpassword,newpassword
 * <p>
 * 17、获取平仓单（结算单用）
 * http://139.224.8.133:9100/api/get_closed_order.ashx
 * 传参：account,startdate,enddate
 * <p>
 * 18、获得资金状况
 * http://139.224.8.133:9100/api/get_account_info.ashx
 * 传参：account
 * <p>
 * 19、获取用户汇率列表
 * http://139.224.8.133:9100/api/get_user_exchange_rate_list.ashx
 * 传参：rootaccount,account
 */

public interface HttpService {

    /**
     * 1、登录
     * http://139.224.8.133:9100/api/login.ashx
     * 传参：uname,upass
     */
    @POST("/api/login2.ashx?uname=")
    @FormUrlEncoded
    Observable<UserLoginResponse> userLogin(@Field("phone") String phone, @Field("upass") String password, @Field("ip") String ip);

    /**
     * 2、获取单个合约报价
     * http://139.224.8.133:9100/api/get_quote.ashx
     * 传参：symbol,exchange
     */
    @GET("/api/get_quote.ashx")
    Observable<Quote> getQuote(@Query("symbol") String symbol, @Query("exchange") String exchange);

    /**
     * 3、一次获取多个合约报价
     * http://139.224.8.133:9100/api/get_quote_list.ashx
     */
    @GET("/api/get_quote_list.ashx")
    Observable<List<Quote>> getQuoteList(@Query("symbol") String symbol, @Query("exchange") String exchange);

    /**
     * 4、获取成交明细
     * http://139.224.8.133:9100/api/get_trades.ashx
     * 传参：symbol,exchange,id,number
     */
    @GET("/api/get_trades.ashx")
    Observable<List<Trade>> getTrades(@Query("symbol") String symbol, @Query("exchange") String exchange, @Query("id") String id, @Query("number") int number);

    /**
     * 5、获取分时图数据
     * http://139.224.8.133:9100/api/get_fs_data.ashx
     * 传参：symbol,exchange,starttime
     */
    @GET("/api/get_fs_data.ashx")
    Observable<List<HisData>> getFsData(@Query("symbol") String symbol, @Query("exchange") String exchange, @Query("starttime") String starttime);

    /**
     * 6、获取历史数据
     * http://139.224.8.133:9100/api/get_h_data.ashx
     * 传参：symbol,exchange,starttime,datatype
     */
    @GET("/api/get_h_data.ashx")
    Observable<List<HisData>> getHistoryData(@Query("symbol") String symbol, @Query("exchange") String exchange, @Query("starttime") String starttime, @Query("datatype") String datatype);

    /**
     * 7、下单
     * http://139.224.8.133:9100/api/send_order.ashx
     * 传参：account,symbol,buysell,price,qty,ordertype
     */
    @GET("/api/send_order.ashx")
    Observable<CommonResponse> sendOrder(
            @Query("account") String account,
            @Query("symbol") String symbol,
            @Query("buysell") String buysell,
            @Query("price") double price,
            @Query("qty") int qty,
            @Query("ordertype") String ordertype);

    /**
     * 9、获取未成交合约
     http://139.224.8.133:9100/api/get_unfill_order.ashx
     传参：account

     */

    /**
     * 8、获取未成交订单
     * http://139.224.8.133:9100/api/get_open_order.ashx
     * 传参：account
     */
    @GET("/api/get_open_order.ashx")
    Observable<List<OpenOrder>> getOpenOrder(@Query("account") String account);

    /**
     * 10、获取已成交订单
     * http://139.224.8.133:9100/api/get_filled_order.ashx
     * 传参：account
     */
    @GET("/api/get_filled_order.ashx")
    Observable<List<FilledOrder>> getFilledOrder(@Query("account") String account);

    /**
     * 11、获取新的已成交订单
     * http://139.224.8.133:9100/api/get_filled_order_ex.ashx
     * 传参：account,id
     */
    @GET("/api/get_filled_order_ex.ashx")
    Observable<List<OpenOrder>> getNewFilledOrder(@Query("account") String account);

    /**
     * 12、获取持仓
     * http://139.224.8.133:9100/api/get_holding.ashx
     * 传参：account
     */
    @GET("/api/get_holding.ashx")
    Observable<List<Holding>> getHolding(@Query("account") String account);

    /**
     * 14、获取能交易的所有合约列表
     * http://139.224.8.133:9100/api/get_symbols.ashx
     * 传参：account
     */
    @GET("/api/get_symbols.ashx")
    Observable<List<Symbol>> getSymbols(@Query("account") String account);

    /**
     * 16、修改密码
     * http://139.224.8.133:9100/api/change_password.ashx
     * 传参：account,oldpassword,newpassword
     */
    @GET("/api/change_password.ashx")
    Observable<CommonResponse> changePassword(@Query("account") String account, @Query("oldpassword") String oldpassword, @Query("newpassword") String newpassword);

    /**
     * 17、获取平仓单（结算单用）
     * http://139.224.8.133:9100/api/get_closed_order.ashx
     * 传参：account,startdate,enddate
     */
    @GET("/api/get_closed_order.ashx")
    Observable<List<CloseOrder>> getCloseOrder(@Query("account") String account, @Query("startdate") String startdate, @Query("enddate") String enddate);

    /**
     * 18、获得资金状况
     * http://139.224.8.133:9100/api/get_account_info.ashx
     * 传参：account
     */
    @GET("/api/get_account_info.ashx")
    Observable<AccountInfo> getAccountInfo(@Query("account") String account);


    /**
     * 19、获取用户汇率列表
     * http://139.224.8.133:9100/api/get_user_exchange_rate_list.ashx
     * 传参：rootaccount,account
     */
    @GET("/api/get_user_exchange_rate_list.ashx")
    Observable<List<Exchange>> getUserExchange(@Query("rootaccount") String rootaccount, @Query("account") String account);

    /**
     * 20、用户注册
     * http://139.224.8.133:9100/api/register_online_2.ashx
     * 传参：RootAccount, RegisterAccount, Name, MobileNo, AccountType, MainAccount, LiveAccount
     */
    @GET("/api/register_online_2.ashx")
    Observable<List<Exchange>> register(@Query("rootaccount") String rootaccount, @Query("account") String account);

    @POST
    Observable<List<HisData>> getHistoryData(@Url String url, @Body HistoryDataRequest request);


    @Headers("User-Agent: ")
    @GET(HttpConfig.IP_URL)
    Call<IpResponse> getIp();
}
