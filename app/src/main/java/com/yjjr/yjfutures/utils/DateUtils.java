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
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

    /**
     * 通过年和月来获取到此月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static ArrayList<String> getDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        int date = 1;
        calendar.set(year, month, date);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        final int finalMaxDay = maxDay;
        return new ArrayList<String>() {
            {
                for (int i = 1; i < finalMaxDay + 1; i++) {
                    add("" + i);
                }
            }
        };
    }

    /**
     * 获取从本年到beforeYear年之前的年数
     *
     * @param beforeYear 几年前
     * @return
     */
    public static ArrayList<String> getYears(int beforeYear) {
        int currentYear = getCurrentYear();
        int startYear = currentYear - beforeYear;
        ArrayList<String> listYear = new ArrayList<>();
        for (int i = currentYear; i >= startYear; i--) {
            listYear.add(String.valueOf(i));
        }
        return listYear;
    }

    /**
     * 获取本年
     *
     * @return
     */
    private static int getCurrentYear() {
        Calendar a = Calendar.getInstance();
        int year = a.get(Calendar.YEAR);
        return year;
    }


    /**
     * 月中的第几天
     *
     * @return
     */
    public static int getDayOfMoth() {
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        return date;
    }


    /**
     * @param year  传入年份
     * @param month 传入月份减一
     * @return
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);//Java月份才0开始算
        int dateOfMonth =
                cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    /**
     * 获取几天前的日期
     *
     * @param before
     * @return
     */
    public static String getDateBefore(int before,String format) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) - before);
        DateFormat dateFormat = new SimpleDateFormat(format);
        String result = dateFormat.format(now.getTime());
        return result;
    }



    public static int calculateDatePoor(String createTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = dateFormat.parse(createTime);
            long time = System.currentTimeMillis() - date.getTime();
            long dayNumber = time / 1000 / 60 / 60 / 24;
            dayNumber = (30 - dayNumber) < 0 ? 0 : (30 - dayNumber) ;
            return (int) dayNumber;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String calculateDateString(Context context, String createTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = dateFormat.parse(createTime);
            long time = System.currentTimeMillis() - date.getTime();
            long second = time / 1000 / 60 ; //min
            if(second < 10) return context.getString(R.string.monment_ago);
            else if(second < 60) return second + context.getString(R.string.min_before);
            else {
                try {
                    DateFormat dateFormatV2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    DateFormat dateFormat2 = new SimpleDateFormat("MM/dd HH:mm");
                    Date dateV2 = dateFormatV2.parse(createTime);
                    String formatDate = dateFormat2.format(dateV2);
                    return formatDate;
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                    DateFormat dateFormat2 = new SimpleDateFormat("MM/dd HH:mm");
                    String formatDate = dateFormat2.format(new Date());
                    return formatDate;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
