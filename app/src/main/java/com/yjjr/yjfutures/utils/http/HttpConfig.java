package com.yjjr.yjfutures.utils.http;

/**
 * HTTP配置
 * Created by guoziwei on 2017/7/5.
 */

public class HttpConfig {

    public static final String DOMAIN = "http://www.qihuofa.com";

    /**
     * 手机号码的正则
     */
    public static final String REG_PHONE = "^0?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";
    /**
     * 发送验证码的重试时间
     */
    public static final long SMS_TIME = 60 * 1000;
    public static String ALIPAY_ACCOUNT_CODE = "FKX02544EKPFEEKPCQSYC8";

    /**
     * 客服电话
     */
    public static String SERVICE_PHONE = "400-005-800";
    public static String QQ = "888666888";
    /**
     * 投诉电话
     */
    public static String COMPLAINT_PHONE = "0755-68888628";
}
