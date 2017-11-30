package com.yjjr.yjfutures.utils;

import com.instacart.library.truetime.TrueTime;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.http.HttpConfig;

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

    public static DateTime getChartStartTime(Quote quote, String type) {
        DateTime dateTime;
        if (quote.isRest()) { //未开盘，数据加载前一天的
            dateTime = DateUtils.nowDateTime();
            if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 7) { //星期一、星期天前一天还是没数据，要加载星期五的
                dateTime = dateTime.minusDays(1).withDayOfWeek(5).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            } else {
                dateTime = dateTime.minusDays(1).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            }
        } else {
            dateTime = DateUtils.nowDateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            // 如果现在的时间在六点之前，减少一天
            if (DateUtils.nowDateTime().isBefore(dateTime)) {
                dateTime.minusDays(1);
            }
        }
        if (HttpConfig.DAY.equals(type)) {
            dateTime = dateTime.minusYears(1);
        } else if (HttpConfig.WEEK.equals(type)) {
            dateTime = dateTime.minusYears(3);
        } else if (HttpConfig.MONTH.equals(type)) {
            dateTime = new DateTime(0);
        } else if (HttpConfig.MIN15.equals(type) || HttpConfig.MIN5.equals(type) || HttpConfig.DAY5.equals(type)) {
//            dateTime = dateTime.minusWeeks(1);
            dateTime = dateTime.minusDays(7);
        } else if (HttpConfig.HOUR.equals(type)) {
            dateTime = dateTime.minusMonths(1);
        }
        return dateTime;
    }
}
