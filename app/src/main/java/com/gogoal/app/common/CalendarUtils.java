package com.gogoal.app.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.gogoal.app.common.StringUtils.formatInteger;


public class CalendarUtils {

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天

    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    private int daysOfMonth = 0;      //某月的天数
    private int dayOfWeek = 0;        //具体某一天是星期几
    private int eachDayOfWeek = 0;

    // 判断是否为闰年
    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 指定格式返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间(格式以HH:mm形式)
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String formatFriendly(String dateString) {
        if (dateString == null) {
            return "";
        }
        Date date = parseString2Date(dateString);

        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 延后几秒
     */
    private Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 延后几毫秒
     */
    private Date addMillisecond(Date date, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    //得到某月有多少天数
    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    //指定某年中的某月的第一天是星期几
    public int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    //指定某年中的某月的某一天是星期几
    public int getWeekDayOfLastMonth(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        eachDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return eachDayOfWeek;
    }

    /**
     * 将标准表时间搓转成date
     */
    public static Date parseString2Date(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 将时间戳转换为时间
     */
    public static String parseStampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    //时间毫秒值转成yyyy-MM-dd HH:mm:ss格式
    public static String parseDateFormatAll(long dateMill) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(dateMill);
    }

    //date时间转成yyyy-MM-dd HH:mm:ss格式
    public static String parseDateFormatAll(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    //date时间转成yyyy-MM-dd HH:mm:ss格式
    public static String parseDateFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        return format.format(date);
    }

    //时间毫秒值转成yyyy-MM-dd格式
    public static String parseDateFormat(long dateMill) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(dateMill).substring(0, 10);
    }

    //传入一个标准的完整时间戳，返回时、分
    public static String getHour$Min(String yMDHms) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(simpleDateFormat.parse(yMDHms));
            return formatInteger(c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    formatInteger(c.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yMDHms.substring(11, 16);
    }

    public static String getHourMin(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timeMillis);
            return formatInteger(c.get(Calendar.HOUR_OF_DAY)) + ":" + formatInteger(c.get(Calendar.MINUTE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "日期格式异常";
    }

    //传入一个标准的完整时间戳，返回月、日
    public static String getMonth$Day(String yMDHms) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(simpleDateFormat.parse(yMDHms));
            if (c.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
                return c.get(Calendar.YEAR) + "-" + formatInteger(c.get(Calendar.MONTH) + 1) + "-" +
                        formatInteger(c.get(Calendar.DAY_OF_MONTH));
            } else {
                return formatInteger(c.get(Calendar.MONTH) + 1) + "-" + formatInteger(c.get(Calendar.DAY_OF_MONTH));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return yMDHms.substring(5, 10);
        }
    }

    public static String startTime(String yMDHms) {
        if (yMDHms == null) {
            return "";
        }

        Date date = parseString2Date(yMDHms);

        long diff = date.getTime() - new Date().getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年后";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "月后";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天后";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时后";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟后";
        }
        if (diff > 0 && diff < 60) {
            return "即将开始";
        }
        return "进行中";
    }

    public static String startDate(String dateString) {
        if (dateString == null) {
            return "";
        }
        Date date = parseString2Date(dateString);

        long diff = date.getTime() - new Date().getTime();
        if (diff > year) {
            return dateString.substring(0, 10);
        }
        if (diff > month) {
            return getMonth$Day(dateString);
        }
        if (diff > day) {
            if ((diff / day) == 1) {
                return "明天\u3000" + getHour$Min(dateString);
            } else if ((diff / day) == 2) {
                return "后天\u3000" + getHour$Min(dateString);
            } else {
                return getMonth$Day(dateString) + "\u3000" + getHour$Min(dateString);
            }
        }
        if (diff > hour) {
            return (diff / hour) + "小时后";
        }
        if (diff > minute) {
            return (diff / hour) + "分钟后";
        }
        if (diff > 0 && diff < 60) {
            return "即将开始";
        }
        return "进行中";
    }

    /**
     * 消息时间处理
     */
    public static String formatMyMessage(String dateString) {

        Date date = parseString2Date(dateString);

        if (date == null) {
            return "--";
        }
        long diff = new Date().getTime() - date.getTime();
        String str;

        if (diff > day) {
            str = formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", dateString);
            return str;
        }

        str = formatDate("yyyy-MM-dd HH:mm:ss", "HH:mm", dateString);

        return str;
    }

    /**
     * 格式化时间字符串
     *
     * @param tofomat;
     * @param date;
     * @return data;
     */
    public static String formatDate(String originFormat, String tofomat, String date) {
        SimpleDateFormat df = new SimpleDateFormat(tofomat, Locale.CHINA);
        if (isEmpty(date)) {
            return df.format(new Date());
        } else {
            SimpleDateFormat df1 = new SimpleDateFormat(originFormat, Locale.CHINA);
            try {
                Date mdata = df1.parse(date);
                return df.format(mdata);
            } catch (ParseException e) {
                return date;
            }
        }
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input;
     * @return boolean;
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    /**
    * 获取当前时间的时间戳
    * */
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }
}
