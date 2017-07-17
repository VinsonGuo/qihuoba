package com.yjjr.yjfutures.utils.http;

/**
 * HTTP配置
 * Created by guoziwei on 2017/7/5.
 */

public interface HttpConfig {
    String BASE_URL = "http://139.224.8.133:9001/QuoteService/QuoteService/";
    String NAME_SPACE = "http://tempuri.org/";
    String SOAP_ACTION = "http://tempuri.org/IQuoteService/";

    /**
     * 手机号码的正则
     */
    String REG_PHONE = "^0?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";
}
