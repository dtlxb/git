package cn.gogoal.im.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;

/**
 * Created by huangxx on 2017/2/14.
 */

public class StockUtils {

    /**
     * 传入任意类型的array,只要每个object里含有字段stock_code
     */
    public static void saveMyStock(JSONArray array) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            set.add(object.getString("stock_code"));
        }
        SPTools.saveSetData("my_stock_set", set);
    }

    /**
     * 获取本地自选股集合
     */
    public static Set<String> getMyStockSet() {
        return SPTools.getSetData("my_stock_set", null);
    }

    /**
     * 获取本地自选股缓存
     */
    public static String getMyStockString() {
        return cacheMyStock(getMyStockSet());
    }

    /**
     * 集合拼Srtring
     */
    private static String cacheMyStock(Set<String> myStockArr) {
        String allMyStock = "";
        for (String stockCode : myStockArr) {
            allMyStock += stockCode;
            allMyStock += ";";
        }
        return allMyStock.substring(0, allMyStock.length() - 1);
    }

    /**
     * 添加单个股票到自选股
     */
    public static void addStock2MyStock(JSONObject object) {
        Set<String> myStockSet = getMyStockSet();
        myStockSet.add(object.getString("stock_code"));
        SPTools.saveSetData("my_stock_set", myStockSet);
    }

    /**
     * 判断股票是否在自选股中
     */
    public static boolean isMyStock(JSONObject object) {
        return getMyStockSet().contains(object.getString("stock_code"));
    }

    /**
     * 判断股票是否在自选股中
     */
    public static boolean isMyStock(String stockCode) {
        return getMyStockSet().contains(stockCode);
    }

    /**
     * 我的自选股移除
     */
    public static void removeStock(String stockCode) {
        Set<String> myStockSet = getMyStockSet();
        if (myStockSet.remove(stockCode)) {
            SPTools.saveSetData("my_stock_set", myStockSet);
        }
    }

    /**
     * 我的自选股移除
     */
    public static void removeStock(JSONObject jsonObject) {
        Set<String> myStockSet = getMyStockSet();
        if (myStockSet.remove(jsonObject.getString("stock_code"))) {
            SPTools.saveSetData("my_stock_set", myStockSet);
        }
    }

    /**
     * 清除自选股
     */
    public static void clearLocalMyStock() {
        SPTools.clearItem("my_stock_set");
    }

    public static String plusMinus(String rateString,boolean percent) {
        if (null == rateString) {
            return "--";
        }

        double rate = Double.parseDouble(rateString);

        if (rate == Double.NaN) {
            return "--";
        }

        if (rate == 0) {
            return "0.00%";
        }
        return (rate > 0 ? "+" : "") +
                StringUtils.saveSignificand(rate, 2) +(percent?"%":"");
    }

    /**
     * 获取搜索历史
     *
     * @return
     */
    public static JSONArray getSearchedStocks() {
        return SPTools.getJsonArray("ggSearchedStock", new JSONArray());
    }

    /**
     * 添加单个股票搜索历史
     *
     * @return
     */
    public static void addSearchedStock(JSONObject stock) {
        JSONArray stocks = getSearchedStocks();
        if (stocks == null) {
            stocks = new JSONArray();
        }
        for (int i = 0; i < stocks.size(); i++) {
            if (stock.getString("stock_code").equals(((JSONObject) stocks.get(i)).getString("stock_code")) || i > 5) {
                stocks.remove(i);
            }
        }
        stocks.add(0, stock);
        savaSearchedStocks(stocks);
    }

    /**
     * 股票搜索历史
     *
     * @param jsonArray
     */
    public static void savaSearchedStocks(JSONArray jsonArray) {
        SPTools.saveJsonArray("ggSearchedStock", jsonArray);
    }

    public static int getStockRateColor(String rateOrPriceString) {
        if (TextUtils.isEmpty(rateOrPriceString)) {
            return R.color.stock_gray;
        }
        double rateOrPrice = Double.parseDouble(rateOrPriceString);

        return rateOrPrice == Double.NaN ? R.color.stock_gray : (rateOrPrice > 0 ? R.color.stock_red : (rateOrPrice == 0 ? R.color.stock_gray :
                R.color.stock_green));
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
     * @param diaplayView 设置颜色的视图控件
     * @param rate        依据字段
     */
    private void setStockUpOrDownColor(View diaplayView, double rate) {
        if (rate > 0) {
            StatusBarUtil.with((Activity) diaplayView.getContext()).setColor(
                    ContextCompat.getColor(diaplayView.getContext(), R.color.stock_red)
            );
            diaplayView.setBackgroundColor(
                    ContextCompat.getColor(diaplayView.getContext(),R.color.stock_red));
        } else if (rate < 0) {
            diaplayView.setBackgroundColor(
                    ContextCompat.getColor(diaplayView.getContext(),R.color.stock_green));
        } else {
            diaplayView.setBackgroundColor(
                    ContextCompat.getColor(diaplayView.getContext(),R.color.stock_gray));
        }
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

//    public static void go2StockDetail(Context context, String stockCode, String stockName) {
//        Intent intent = new Intent(context, StockDetailActivity.class);
//        intent.putExtra("stock_code", stockCode);
//        intent.putExtra("stock_name", stockName);
//        context.startActivity(intent);
//    }

    public static void go2StockDetail(Context context, String stockCode, String stockName) {
        Intent intent = new Intent(context, CopyStockDetailActivity.class);
        intent.putExtra("stock_code", stockCode);
        intent.putExtra("stock_name", stockName);
        context.startActivity(intent);
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

    public static void savaColseprice(float closePrice) {
        SPTools.saveFloat("closePrice",closePrice);
    }
}
