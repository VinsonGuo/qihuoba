package com.yjjr.yjfutures.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by hou on 2015/8/14.
 */
public class DoubleUtil {

    public static double parseDouble(String parserDouble) {
        try {
            return Double.parseDouble(parserDouble);
        } catch (Exception e) {
            return 0;
        }
    }


    public static String format2Decimal(Double d) {
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMinimumFractionDigits(2);
        instance.setMaximumFractionDigits(2);
        return instance.format(d);
    }

    public static String formatDecimal(Double d) {
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMinimumFractionDigits(0);
        instance.setMaximumFractionDigits(8);
        return instance.format(d).replace(",", "");
    }


    public static String format6Decimal(Double d) {
        return new DecimalFormat("######0.000000").format(d);
    }

    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    /**
     * 保留两位四舍五入
     *
     * @param d
     * @return
     */
    public static double round2Decimal(double d) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


//    public static String round(double d){
//        if(d > 1000){
//            double rnum = Math.rint(d);
//            String result = format2Decimal(rnum / 1000) + "K";
//            return result;
//        }

//        else
//        return format2Decimal(d);
//    }

    /**
     * 小数点后最后一位加一
     */
    public static String addLast(String d) {
        if (!NumberUtils.isNumber(d)) {
            return d;
        }
        if (d.indexOf(".") > 0) {
            String[] split = d.split("\\.");
            int len = split[1].length();
            String s = "0.";
            for (int i = 1; i < len; i++) {
                s += "0";
            }
            s += "1";
            double resultDouble = ArithUtils.add(Double.valueOf(d), Double.valueOf(s));
            return String.valueOf(resultDouble);
        } else {
            return String.valueOf(Integer.valueOf(d) + 1);
        }
    }

    /**
     * 小数点后最后一位减一
     */
    public static String subLast(String d) {
        if (!NumberUtils.isNumber(d)) {
            return d;
        }
        if (d.indexOf(".") > 0) {
            String[] split = d.split("\\.");
            int len = split[1].length();
            String s = "0.";
            for (int i = 1; i < len; i++) {
                s += "0";
            }
            s += "1";
            double resultDouble = ArithUtils.sub(Double.valueOf(d), Double.valueOf(s));
            return String.valueOf(resultDouble);
        } else {
            return Integer.valueOf(d) - 1 + "";
        }
    }


    public static double getPointsizeByDigits(int digits) {
        if (digits < 1) return 1;
        return Math.pow(10, -digits);
    }


}
