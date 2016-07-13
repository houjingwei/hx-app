package com.huixiangtv.liveshow.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    /**
     * 判断是否date0和date1表示的日期是否是同年同月同周
     *
     * @param date0 整数表示的日期,格式yyyymmdd
     * @param date1 整数表示的日期,格式yyyymmdd
     * @return true 表示是同年同月同周
     */
    public static boolean isSameWeek(int date0, int date1) {

        int y0 = date0 / 10000;
        int m0 = (date0 % 10000) / 100;

        int y1 = date1 / 10000;
        int m1 = (date1 % 10000) / 100;

        if (y0 != y1 || m0 != m1)
            return false;

        int d0 = date0 % 100;
        int d1 = date1 % 100;

        Calendar cal = Calendar.getInstance();
        cal.set(y0, m0 - 1, d0);
        int week_of_year0 = cal.get(Calendar.WEEK_OF_YEAR);
        cal.set(y1, m1 - 1, d1);
        int week_of_year1 = cal.get(Calendar.WEEK_OF_YEAR);

        return week_of_year0 == week_of_year1;
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 判断是否date0和date1表示的日期是否是同年同月
     *
     * @param date0 整数表示的日期,格式yyyymmdd
     * @param date1 整数表示的日期,格式yyyymmdd
     * @return true 表示是同年同月同周
     */
    public static boolean isSameMonth(int date0, int date1) {

        int y0 = date0 / 10000;
        int m0 = (date0 % 10000) / 100;

        int y1 = date1 / 10000;
        int m1 = (date1 % 10000) / 100;

        return !(y0 != y1 || m0 != m1);
    }

    /**
     * 获得Calendar类型的日期实例日期的整数表示
     *
     * @param cal Calendar类型的日期实例
     * @return 日期的整数表示, 格式yyyymmdd
     */
    public static int getDate(Calendar cal) {

        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1)
                * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前的日期的整数表示
     *
     * @return 日期的整数表示, 格式yyyymmdd
     */
    public static int getDate() {

        Calendar cal = Calendar.getInstance();
        return getDate(cal);
    }

    /**
     * 获得Calendar类型的日期实例时间的整数表示
     *
     * @param cal 日历对象
     * @return 当天经过的秒数
     */
    public static int getTime(Calendar cal) {

        return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE)
                * 60 + cal.get(Calendar.SECOND);
    }

    /**
     * 获得当前的时间的整数表示
     *
     * @return 当天经过的秒数
     */
    public static int getTime() {

        Calendar cal = Calendar.getInstance();
        return getTime(cal);
    }

    /**
     * 获得当前的日期的整数表示
     *
     * @param cal Calendar类型的日期实例
     * @return 日期的整数表示, 格式YYYYMMDDHHMMSS
     */
    public static long getDateTime(Calendar cal) {

        long ymd = cal.get(Calendar.YEAR) * 10000
                + (cal.get(Calendar.MONTH) + 1) * 100
                + cal.get(Calendar.DAY_OF_MONTH);
        long hms = cal.get(Calendar.HOUR_OF_DAY) * 10000
                + cal.get(Calendar.MINUTE) * 100 + cal.get(Calendar.SECOND);
        long ret = ymd * 1000000 + hms;
        return ret;
    }

    /**
     * 获得当前的日期的整数表示
     *
     * @return 日期的整数表示, 格式YYYYMMDDHHMMSS
     */
    public static long getDateTime() {

        Calendar cal = Calendar.getInstance();
        return getDateTime(cal);
    }

    /**
     * 获得当前日期的字符串
     *
     * @param cal Calendar类型的日期实例
     * @return 当天的日期字符串"yyyy-mm-dd "
     */
    public static String getDateStr(Calendar cal) {

        String a = "" + getDate(cal);

        StringBuffer sb = new StringBuffer(10);
        sb.append(a.substring(0, 4));
        sb.append("-");
        sb.append(a.substring(4, 6));
        sb.append("-");
        sb.append(a.substring(6, 8));
        return sb.toString();
    }

    /**
     * 获得当前日期的字符串
     *
     * @param time Calendar类型的日期实例
     * @return 当天的日期字符串"mm-dd"
     */
    @SuppressWarnings("deprecation")
    public static String getDateStr(String time) {

        StringBuffer sb = new StringBuffer(5);
        Date d = StrToDate(time);
        Date current = new Date();
        if (d.getDay() == current.getDay()) {
            try {
                sb.append(time.substring(11, 16));

            } catch (Exception e) {
                return time;
            }
        } else {
            try {
                sb.append(time.substring(5, 10));

            } catch (Exception e) {
                return time;
            }
        }

        return sb.toString();
    }

    /**
     * 获得当前日期的字符串
     *
     * @return 当天的日期字符串"yyyy-mm-dd"
     */
    public static String getDateStr() {

        Calendar cal = Calendar.getInstance();
        return getDateStr(cal);
    }

    /**
     * 根据经过的毫秒数获得时间字符串
     *
     * @param milliseconds 经过的毫秒数
     * @return 时间字符串"hh:mm:ss"
     */
    public static String getTimeStr(long milliseconds) {

        long seconds = milliseconds / 1000;
        long h = seconds / 3600;
        long m = seconds % 3600 / 60;
        long s = seconds % 60;

        StringBuffer sb = new StringBuffer(10);
        if (h < 10)
            sb.append(0);
        sb.append(h);
        sb.append(":");
        if (m < 10)
            sb.append(0);
        sb.append(m);
        sb.append(":");
        if (s < 10)
            sb.append(0);
        sb.append(s);

        return sb.toString();
    }

    /**
     * 获得Calendar类型的时间字符串
     *
     * @param cal Calendar类型的日期实例
     * @return 时间字符串"hh:mm:ss"
     */
    public static String getTimeStr(Calendar cal) {

        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);

        StringBuffer sb = new StringBuffer(10);
        if (h < 10)
            sb.append(0);
        sb.append(h);
        sb.append(":");
        if (m < 10)
            sb.append(0);
        sb.append(m);
        sb.append(":");
        if (s < 10)
            sb.append(0);
        sb.append(s);

        return sb.toString();
    }

    /**
     * 获得当前时间的字符串
     *
     * @return 时间字符串"hh:mm:ss"
     */
    public static String getTimeStr() {

        Calendar cal = Calendar.getInstance();
        return getTimeStr(cal);
    }

    /**
     * 获取当天的时期时间字符串
     *
     * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
     */
    public static String getDateTimeStr() {

        Calendar cal = Calendar.getInstance();
        return getDateTimeStr(cal);
    }

    /**
     * 获取cal指定的时期时间字符串
     *
     * @param cal Calendar类型的日期实例
     * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
     */
    public static String getDateTimeStr(Calendar cal) {

        StringBuffer sb = new StringBuffer(20);
        sb.append(getDateStr(cal));
        sb.append(" ");
        sb.append(getTimeStr(cal));
        return sb.toString();
    }

    /**
     * 获取当天的时期时间字符串
     *
     * @param milliseconds 以毫秒为单位的当前时间
     * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
     */
    public static String getDateTimeStr(long milliseconds) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(new Date(milliseconds));
    }

    public static String getDateTimeStr(long milliseconds, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date(milliseconds));
    }

    //liuzw. 增加时区设置。 GMT+0,+1...
    public static String getDateTimeStr(long mills, String format, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(new Date(mills));
    }

    public static String formatDate(Date date, String format) {

        if (date != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format);
            return sdf1.format(date);
        }
        return null;
    }

    /**
     * 字符串转化为日期
     */
    public static Date StrToDate(String str) {

        Date returnDate = null;
        if (str != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                returnDate = sdf.parse(str.trim());
            } catch (Exception e) {
                System.err
                        .println("AppTools [Date StrToDate(String str)] Exception");
                return returnDate;
            }
        }
        return returnDate;
    }

    /**
     * 字符串转化为日期
     */
    public static Date StrToDate(String str, String format) {

        Date returnDate = null;
        if (StringUtil.isNotEmpty(str)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                returnDate = sdf.parse(str.trim());
            } catch (Exception e) {
                System.err
                        .println("AppTools [Date StrToDate(String str)] Exception");
                return returnDate;
            }
        }
        return returnDate;
    }

    /**
     * 字符串转化为日期
     */
    public static Date StrToTime(String str, String format) {

        Date returnDate = null;
        if (str != null) {
            String timeStr = str;
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                returnDate = sdf.parse(timeStr.trim());
            } catch (Exception e) {
                System.err
                        .println("AppTools [Date StrToDate(String str)] Exception");
                return returnDate;
            }
        }
        return returnDate;
    }

    public static Date getDateAtt(Date sourceDate, int day) {
        return new Date(sourceDate.getTime() + day * 24 * 60 * 60 * 1000);
    }

    public static String getDateTimeStr(Date date, String fomat) {

        SimpleDateFormat sdf = new SimpleDateFormat(fomat);
        return sdf.format(date);
    }

    public static boolean isAm(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH");

        int hour = Integer.parseInt(sdf.format(date));
        return hour < 12;
    }

    public static String getDateTimeStr(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateStr(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getDateStr(Date date, String format) {

        if (date == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getTimeStr(Date date) {

        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static boolean isAmOfCurrent() {

        return isAm(new Date());
    }

    public static Date getBeforeDate(Date date, int days) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, -days);
        String begin = new java.sql.Date(calendar.getTime().getTime())
                .toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        try {
            beginDate = sdf.parse(begin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beginDate;

    }

    public static Date getAfterDate(Date date, int days) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.DATE, days);

        return calendar.getTime();
    }

    public static Date getAfterDay(int days) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, days);

        return calendar.getTime();
    }

    public static Date getLastMonthDay() {

        Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.DATE, -1); //得到前一天
        calendar.add(Calendar.MONTH, -1); // 得到前一个月
        // int year = calendar.get(Calendar.YEAR);
        // int month = calendar.get(Calendar.MONTH) + 1;
        return calendar.getTime();
    }

    public static Date getLastMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1); // 得到前一个月
        return calendar.getTime();
    }

    public static Date getNextMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1); // 得到下一个月
        return calendar.getTime();
    }

    /**
     * 取指定 日期上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1); // 得到前一个月
        // 最后一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);

        return calendar.getTime();
    }

    public static Date getThisMonthFirstDate() {
        // 本月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 取指定月的最后一天天数
     *
     * @param month
     * @return
     */
    public static int getMonthLastDay(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置指定月
        calendar.set(Calendar.MONTH, month - 1);

        // 取最后一天天数
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取指定年和月的最后一天天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置指定年
        calendar.set(Calendar.YEAR, year);
        // 设置指定月
        calendar.set(Calendar.MONTH, month - 1);

        // 取最后一天天数
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天是今年的第几天
     *
     * @return
     */
    public static int getDayOfYear4today() {
        Calendar calendar = Calendar.getInstance();

        // 今天是今年的第几天
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDayFormDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonthFormDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYearFormDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * 取指定月和日的日期
     *
     * @param month
     * @param day
     * @return
     */
    public static Date getDateByMonthAndDay(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        // 设置指定月
        calendar.set(Calendar.MONTH, month - 1);
        // 设置指定天
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 取指定年、月和日的日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        // 设置指年
        calendar.set(Calendar.YEAR, year);
        // 设置指定月
        calendar.set(Calendar.MONTH, month - 1);
        // 设置指定天
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * description: 取指定年、月和日最后时间的日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     * @author zengyitan
     */
    public static Date getDate4EndTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        // 设置指年
        calendar.set(Calendar.YEAR, year);
        // 设置指定月
        calendar.set(Calendar.MONTH, month - 1);
        // 设置指定天
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 将指定的日期(年月日)的时分秒部分设置成当前最后一个时刻
     *
     * @param date
     * @return
     */
    public static Date getDate4EndTime(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 将指定的日期(年月日)的时分秒部分设置成当前最初一个时刻
     *
     * @param date
     * @return
     */
    public static Date getDate4StartTime(Date date) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取指定日期的星期
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Calendar中1-星期天，2-星期一，3-星期二，4-星期三，5-星期四，6-星期五，7-星期六
        String[] wee = {"", "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        return wee[calendar.get(Calendar.DAY_OF_WEEK)];
    }

}
