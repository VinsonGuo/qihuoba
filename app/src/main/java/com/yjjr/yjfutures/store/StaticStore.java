package com.yjjr.yjfutures.store;

import com.yjjr.yjfutures.model.Quote;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dell on 2017/7/6.
 */

public class StaticStore {
    // 实盘
    public static Map<String, Quote> sQuoteMap = new LinkedHashMap<>();
    public static String sSymbols;
    public static String sExchange;

    // 模拟盘的数据
    public static Map<String, Quote> sDemoQuoteMap = new LinkedHashMap<>();
    public static String sDemoSymbols;
    public static String sDemoExchange;

}
