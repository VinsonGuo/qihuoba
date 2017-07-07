package com.yjjr.yjfutures.utils;

import android.content.Context;


import com.yjjr.yjfutures.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hou on 2015/9/17.
 */
public class DateUtils {


    /**
     * @param dateString yyyy-MM-dd'T'HH:mm:ss
     * @return
     */
    public static String formatData5(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            String formatDate = dateFormat2.format(date);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = dateFormat2.format(new Date());
            return formatDate;
        }
    }

    public static String formatData8(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            String formatDate = dateFormat2.format(date);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }

    /**
     * @param dateString yyyy-MM-dd'T'HH:mm:ss
     * @return
     */
    public static String formatData4(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd HH:mm");
            Date date = dateFormat.parse(dateString);
            String formatDate = dateFormat2.format(date);
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd HH:mm");
            String formatDate = dateFormat2.format(new Date());
            return formatDate;
        }
    }

    public static String getTimeNow(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String formatDate = dateFormat.format(new Date());
            return formatDate;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return "";
    }


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

    public static String formatDataNoT(long time) {
        return new DateTime(time, DateTimeZone.forID("GMT")).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static long parseData(String dataString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = dateFormat.parse(dataString);
            return date.getTime();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return 0;
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
}
