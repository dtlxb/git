package hply.com.niugu.stock;

import java.util.Calendar;

/**
 * Created by huangxx on 2017/2/14.
 */

public class StockUtils {

    /**
     * 是否是交易时间
     */
    public static boolean isTradeTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int time = hour * 60 + c.get(Calendar.MINUTE);
        return (c.get(Calendar.DAY_OF_WEEK)) != Calendar.SUNDAY
                && (c.get(Calendar.DAY_OF_WEEK)) != Calendar.SATURDAY
                && ((time >= 570 && time <= 690) || (time >= 780 && time <= 900));
    }

    /**
     * 获取股票状态信息
     */
    public static String getStockStatus(int stockType) {
        switch (stockType) {
            case -2:
                return "未上市";
            case -1:
                return "退市";
            case 0:
                return "停牌";
            case 1:
                return "正常";
            case 2:
                return "暂停上市";
            default:
                return "0.00";
        }
    }

    /**
     * 基准年计算
     */
    public static int getbaseYear() {
        int year;
        int month;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        if (month >= 4) {
            year = year - 1;
        } else {
            year = year - 2;
        }
        return year;
    }

    //交易状态
    public static String getTreatState() {
        String status = "";
        Calendar now_time = Calendar.getInstance();
        if (now_time.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                now_time.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            status = "休市";
        } else {
            if (now_time.get(Calendar.HOUR_OF_DAY) >= 15) {
                status = "已收盘";
            } else if (now_time.get(Calendar.HOUR_OF_DAY) >= 11) {
                if (now_time.get(Calendar.HOUR_OF_DAY) - 11 == 0) {
                    if (now_time.get(Calendar.MINUTE) > 30) {
                        status = "午盘休市";
                    } else {
                        status = "交易中";
                    }
                } else if (now_time.get(Calendar.HOUR_OF_DAY) < 13) {
                    status = "午盘休市";
                } else {
                    status = "交易中";
                }
            } else if (now_time.get(Calendar.HOUR_OF_DAY) >= 9) {
                if (now_time.get(Calendar.HOUR_OF_DAY) - 9 == 0 && now_time.get(Calendar.MINUTE) < 30) {
                    status = "未开盘";
                } else {
                    status = "交易中";
                }
            } else {
                status = "未开盘";
            }
        }
        return status;
    }

    public String largeAmounts(double large) {
        return "";
    }

    /**
     * 保存收盘价
     */
    public static double getColseprice() {
        return 0;
    }
}
