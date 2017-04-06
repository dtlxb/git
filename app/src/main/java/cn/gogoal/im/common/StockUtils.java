package cn.gogoal.im.common;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;

/**
 * Created by huangxx on 2017/2/14.
 */

public class StockUtils {
    /**
     * 存储自选股
     *
     * @param jsonArray
     */
    public static void savaStocks(JSONArray jsonArray) {
        SPTools.saveJsonArray("ggMyStock", jsonArray);
    }

    /**
     * 获取自选股信息
     *
     * @return
     */
    public static JSONArray getStocks() {
        return SPTools.getJsonArray("ggMyStock", new JSONArray());
    }

    /**
     * 清除自选股
     */
    public static void clearLocalMyStock() {
        SPTools.clearItem("ggMyStock");
    }

    /**
     * 添加单个股票
     *
     * @return
     */
    public static void addSingleStock(JSONObject stock) {
        JSONArray stocks = getStocks();
        if (stocks == null) {
            stocks = new JSONArray();
        }
        boolean hascode = false;
        for (int i = 0; i < stocks.size(); i++) {
            if (stock.getString("stock_code").equals(((JSONObject) stocks.get(i)).getString("stock_code"))) {
                hascode = true;
                break;
            }
        }
        if (!hascode) {
            stocks.add(0, stock);
            savaStocks(stocks);
        }
    }

    /**
    * 移除单个股票
    * */
    public static void removeSingleStock(JSONObject stock, String stockCode) {
        JSONArray stocks = getStocks();
        if (stocks != null) {
            for (int i = 0; i < stocks.size(); i++) {
                JSONObject list = stocks.getJSONObject(i);
                String stockCode1 = (String) list.get("stock_code");
                if (stockCode.equals(stockCode1)) {
                    stocks.remove(i);
                }
            }
            savaStocks(stocks);
        }
    }

    //判断是否已添加自选股
    public static boolean isMyStocks(String stockCode) {
        com.alibaba.fastjson.JSONArray array = StockUtils.getStocks();
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject list = array.getJSONObject(i);
            String stockCode1 = (String) list.get("stock_code");
            if (stockCode.equals(stockCode1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String plusMinus(double rate) {
        if (rate==0){
            return "--";
        }
        return (rate>0?"+":"") +
                StringUtils.saveSignificand(rate, 2) + "%";
    }

    /**
     * 股票搜索历史
     *
     * @param jsonArray
     */
    public static void savaSearchedStocks(JSONArray jsonArray) {
        SPTools.saveJsonArray("ggSearchedStock", jsonArray);
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
     * 删除单个股票搜索历史
     *
     * @return
     */
    public static void removeSearchedStock(String stockCode) {
        JSONArray stocks = getSearchedStocks();
        if (stocks != null) {
            for (int i = 0; i < stocks.size(); i++) {
                JSONObject list = stocks.getJSONObject(i);
                String stockCode1 = (String) list.get("stock_code");
                if (stockCode.equals(stockCode1)) {
                    stocks.remove(i);
                }
            }
            savaSearchedStocks(stocks);
        }
    }

    public static void removeAllSearchedStocks() {
        SPTools.clearItem("ggSearchedStock");
    }

    /**
     * 获取股票代码参数列表(600000;600010)
     *
     * @return
     */
    public static String getStocksParams(JSONArray result) {
        String stocks = "";
        for (int i = 0; i < result.size(); i++) {
            JSONObject stock = (JSONObject) result.get(i);
            stocks += stock.get("stock_code");
            stocks += ";";
        }
        stocks = stocks.substring(0, stocks.length() - 1);
        return stocks;
    }

    /**
    * 保存收盘价
    * */
    public static void savaColseprice(float colseprice) {
        SPTools.saveFloat("closePrice", colseprice);
    }

    /**
     * 获取收盘价
     */
    public static float getColseprice() {
        return SPTools.getFloat("closePrice", 0);
    }

    /**
     * 是否是交易时间
     *
     * @return
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
     *
     * @param type
     * @return
     */
    public static String getStockStatus(int type) {
        if (type == 0) {
            return "停牌";
        } else if (type == -1) {
            return "退市";
        } else if (type == -2) {
            return "未上市";
        } else if (type == 2) {
            return "暂停上市";
        } else {
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

    //股票状态
    public static String getStockState() {
        String status = "";
        Calendar now_time = Calendar.getInstance();
        if (StringData(now_time).equals("天") || StringData(now_time).equals("六")) {
            status = "休市";
        } else {
            if (now_time.get(Calendar.HOUR_OF_DAY) >= 15) {
                status = "已收盘";
            } else if (now_time.get(Calendar.HOUR_OF_DAY) >= 11) {
                if (now_time.get(Calendar.HOUR_OF_DAY) == 11) {
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
                if (now_time.get(Calendar.HOUR_OF_DAY) == 9 && now_time.get(Calendar.MINUTE) < 30) {
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

    //判断当前时间是星期几
    private static String StringData(Calendar c) {
        /*final  = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码 */

        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }
}
