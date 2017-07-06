package com.yjjr.yjfutures.store;

import com.yjjr.yjfutures.model.Quote;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dell on 2017/7/6.
 */

public class StaticStore {
    public static Map<String, Quote> sQuoteMap = new LinkedHashMap<>();
}
