package com.yjjr.yjfutures.store;

import com.yjjr.yjfutures.model.Quote;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dell on 2017/7/6.
 */

public class StaticStore {
    public static String sSymbols;
    public static String sExchange;
    public static Set<String> sHoldSet = new HashSet<>();
    public static String sDemoSymbols;
    public static String sDemoExchange;
    public static Set<String> sDemoHoldSet = new HashSet<>();
    // 实盘
    private static Map<String, Quote> sQuoteMap = new LinkedHashMap<>();
    // 模拟盘的数据
    private static Map<String, Quote> sDemoQuoteMap = new LinkedHashMap<>();

    public static void putQuote(Quote quote, boolean isDemo) {
        if (isDemo) {
            sDemoQuoteMap.put(quote.getSymbol(), quote);
        } else {
            sQuoteMap.put(quote.getSymbol(), quote);
        }
    }

    public static Quote getQuote(String symbol, boolean isDemo) {
        if (isDemo) {
            return sDemoQuoteMap.get(symbol);
        } else {
            return sQuoteMap.get(symbol);
        }
    }

    public static Collection<Quote> getQuoteValues(boolean isDemo) {
        if (isDemo) {
            return sDemoQuoteMap.values();
        } else {
            return sQuoteMap.values();
        }
    }
}
