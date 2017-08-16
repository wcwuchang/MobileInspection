package com.tianjin.MobileInspection.until;

import android.annotation.SuppressLint;

import com.socks.library.KLog;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    // 获取当前日期
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    //时间转换成年月日形式
    public static String getYMD(String str){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=sdf.parse(str);
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy年MM月dd日");
            return sdf1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] getYMDArray(String datetime, String splite) {
        int[] date = {0, 0, 0, 0, 0};
        if (datetime != null && datetime.length() > 0) {
            String[] dates = datetime.split(splite);
            int position = 0;
            for (String temp : dates) {
                date[position] = Integer.valueOf(temp);
                position++;
            }
        }
        return date;
    }

    /**
     * 将当前时间戳转化为标准时间函数
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String time1) {

        int timestamp = Integer.parseInt(time1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            String str = sdf.format(new Timestamp(intToLong(timestamp)));
            time = str.substring(11, 16);
            String month = str.substring(5, 7);
            String day = str.substring(8, 10);
            time = getDate(month, day) + time;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    public static String getTime(int timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            String str = sdf.format(new Timestamp(intToLong(timestamp)));
            time = str.substring(11, 16);

            String month = str.substring(5, 7);
            String day = str.substring(8, 10);
            time = getDate(month, day) + time;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    public static String getHMS(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = null;
        try {
            return sdf.format(new Date(timestamp));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 将当前时间戳转化为标准时间函数
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getHMS(String time) {

        long timestamp = Long.parseLong(time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            String str = sdf.format(new Timestamp(timestamp));
            return str;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    // java Timestamp构造函数需传入Long型
    public static long intToLong(int i) {
        long result = (long) i;
        result *= 1000;
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate(String month, String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
        java.util.Date d = new java.util.Date();
        String str = sdf.format(d);
        String nowmonth = str.substring(5, 7);
        String nowday = str.substring(8, 10);
        String result = null;

        int temp = Integer.parseInt(nowday) - Integer.parseInt(day);
        switch (temp) {
            case 0:
                result = "今天";
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "前天";
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.parseInt(month) + "月");
                sb.append(Integer.parseInt(day) + "日");
                result = sb.toString();
                break;
        }
        return result;
    }

    /* 将字符串转为时间戳 */
    public static String getTimeToStamp(String time) {

        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date1=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date = new Date();
        try {
            date1=sdf1.parse(time);
            KLog.d(sdf.format(date1));
            date = sdf.parse(sdf.format(date1));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String tmptime = String.valueOf(date.getTime()).substring(0, 10);
        return tmptime;
    }

    public static String getBaiduCurrentTime(){
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date1= null;
        try {
            date1 = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tmptime = String.valueOf(date1.getTime()).substring(0, 10);
        return tmptime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYMD(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timestamp));
    }

    public static String getDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(new Date(timestamp * 1000));
    }

    public static String getTimestamp() {
        long time = System.currentTimeMillis() / 1000;
        return String.valueOf(time);
    }

    //获取yyyy-MM-dd HH:mm:SS格式
    public static String getyyyyMMddHHmmSS(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date= new Date(time);
        return sdf.format(date);
    }

    //获取yyyy-MM-dd格式
    public static String getyyyyMMdd(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date= new Date(time);
        return sdf.format(date);
    }

    public static String getNYR(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date=new Date(time);
        return sdf.format(date);
    }

    /**
     * long 值转换成时分秒
     * @return
     */
    public static String longToStringTime(long time){
        StringBuffer sb=new StringBuffer();
        long h=time/3600;
        long m=(time-h*3600)/60;
        long s=time-m*60-h*3600;
        if(h>0){
            sb.append(h).append("小时");
        }
        if(m>0){
            sb.append(m).append("分钟");
        }
        if(s>0){
            sb.append(s).append("秒");
        }
        return sb.toString();
    }

}