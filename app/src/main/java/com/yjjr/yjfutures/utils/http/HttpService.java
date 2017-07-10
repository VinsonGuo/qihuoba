package com.yjjr.yjfutures.utils.http;

import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.OpenOrder;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.SendOrderResponse;
import com.yjjr.yjfutures.model.UserLoginResponse;

import java.util.List;

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

    /**
     * 1、登录
     * http://139.224.8.133:9100/api/login.ashx
     * 传参：uname,upass
     */
    @POST("/api/login.ashx")
    @FormUrlEncoded
    Observable<UserLoginResponse> userLogin(@Field("uname") String account, @Field("upass") String password);

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
     http://139.224.8.133:9100/api/get_trades.ashx
     传参：symbol,exchange,id,number
     */

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
    Observable<SendOrderResponse> getHistoryData(
            @Query("account") String account,
            @Query("symbol") String symbol,
            @Query("buysell") String buysell,
            @Query("price") double price,
            @Query("qty") int qty,
            @Query("ordertype") String ordertype);

    /**
     * 8、获取未成交订单
     * http://139.224.8.133:9100/api/get_open_order.ashx
     * 传参：account
     */
    @GET("/api/get_open_order.ashx")
    Observable<List<OpenOrder>> getOpenOrder(@Query("account") String account);

    /**
     * 9、获取未成交合约
     http://139.224.8.133:9100/api/get_unfill_order.ashx
     传参：account

     */

    /**
     * 10、获取已成交订单
     * http://139.224.8.133:9100/api/get_filled_order.ashx
     * 传参：account
     */
    @GET("/api/get_filled_order.ashx")
    Observable<List<OpenOrder>> getFilledOrder(@Query("account") String account);

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
}
