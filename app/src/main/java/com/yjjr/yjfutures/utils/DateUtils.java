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
}
