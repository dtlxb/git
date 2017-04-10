package cn.gogoal.im.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import cn.gogoal.im.R;

/**
 * Created by huangxx on 2017/2/14.
 */

public class StockUtils {

    /**传入任意类型的array,只要每个object里含有字段stock_code*/
    public static void saveMyStock(JSONArray array){
        Set<String> set=new HashSet<>();
        for (int i=0;i<array.size();i++){
            JSONObject object= (JSONObject) array.get(i);
            set.add(object.getString("stock_code"));
        }
        SPTools.saveSetData("my_stock_set",set);
    }

    /**获取本地自选股集合*/
    public static Set<String> getMyStockSet(){
        return SPTools.getSetData("my_stock_set", null);
    }

    /**获取本地自选股缓存*/
    public static String getMyStockString(){
        return cacheMyStock(getMyStockSet());
    }

    /**集合拼Srtring*/
    private static String cacheMyStock(Set<String> myStockArr) {
        String allMyStock="";
        for (String stockCode:myStockArr){
            allMyStock+=stockCode;
            allMyStock+=";";
        }
        return allMyStock.substring(0,allMyStock.length()-1);
    }

    /**添加单个股票到自选股*/
    public static void addStock2MyStock(JSONObject object){
        Set<String> myStockSet = getMyStockSet();
        myStockSet.add(object.getString("stock_code"));
        SPTools.saveSetData("my_stock_set",myStockSet);
    }

    /**判断股票是否在自选股中*/
    public static boolean isMyStock(JSONObject object){
        return getMyStockSet().contains(object.getString("stock_code"));
    }

    /**判断股票是否在自选股中*/
    public static boolean isMyStock(String stockCode){
        return getMyStockSet().contains(stockCode);
    }

    /**我的自选股移除*/
    public static void removeStock(String stockCode){
        Set<String> myStockSet = getMyStockSet();
        if (myStockSet.remove(stockCode)){
            SPTools.saveSetData("my_stock_set",myStockSet);
        }
    }

    /**我的自选股移除*/
    public static void removeStock(JSONObject jsonObject){
        Set<String> myStockSet = getMyStockSet();
        if (myStockSet.remove(jsonObject.getString("stock_code"))){
            SPTools.saveSetData("my_stock_set",myStockSet);
        }
    }

    /**
     * 清除自选股
     */
    public static void clearLocalMyStock() {
        SPTools.clearItem("my_stock_set");
    }

    public static String plusMinus(double rate) {
        if (rate==0){
            return "0.00%";
        }
        return (rate>0?"+":"") +
                StringUtils.saveSignificand(rate, 2) + "%";
    }

    public static int getStockRateColor(double rateOrPrice){
        return rateOrPrice>0? R.color.stock_red:(rateOrPrice==0?R.color.stock_gray:
        R.color.stock_green);
    }

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
        switch (stockType){
            case -2:return "未上市";
            case -1:return "退市";
            case 0:return "停牌";
            case 1:return "正常";
            case 2:return "暂停上市";
            default:return "0.00";
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
    public static String getStockState() {
        String status = "";
        Calendar now_time = Calendar.getInstance();
        if (now_time.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY ||
                now_time.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
            status = "休市";
        } else {
            if (now_time.get(Calendar.HOUR_OF_DAY) >= 15) {
                status = "已收盘";
            } else if (now_time.get(Calendar.HOUR_OF_DAY) >= 11) {
                if (now_time.get(Calendar.HOUR_OF_DAY) - 11==0) {
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
                if (now_time.get(Calendar.HOUR_OF_DAY)-9==0 && now_time.get(Calendar.MINUTE) < 30) {
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

    /**保存收盘价*/
    public static double getColseprice() {
        return 0;
    }
}
