package com.yjjr.yjfutures.utils;

import com.instacart.library.truetime.TrueTime;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hou on 2015/9/17.
 */
public class DateUtils {

    public static String formatData(long time) {
        try {
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formatDate = dateFormat2.format(time);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }

    public static String formatDataOnly(long time) {
        try {
            DateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
            String formatDate = dateFormat2.format(time);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }


    public static String formatDateTime(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = dateFormat.format(date);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }

    public static String formatDateTime(long date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = dateFormat.format(date);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }


    public static String formatTime(long millis) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String formatDate = dateFormat.format(millis);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }

    public static long parseTime(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.parse(time).getTime();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return 0;
    }

    public static Date nowDate() {
        if (TrueTime.isInitialized()) {
            return TrueTime.now();
        }
        return new Date();
    }

    public static long nowTime() {
        return nowDate().getTime();
    }

    public static DateTime nowDateTime() {
        return new DateTime(nowTime());
    }
}
