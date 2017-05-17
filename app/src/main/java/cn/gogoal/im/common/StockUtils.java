package cn.gogoal.im.common;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

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
        return ArrayUtils.mosaicListElement(getMyStockSet());
    }

    /**
     * 添加单个股票到自选股
     */
    public static void addStock2MyStock(JSONObject object) {
        Set<String> myStockSet = getMyStockSet();
        myStockSet.add(object.getString("stock_code"));
        SPTools.saveSetData("my_stock_set", myStockSet);
//        KLog.e(myStockSet.size()+";==="+myStockSet.toString());
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

        KLog.e(myStockSet.size()+";==="+myStockSet.toString());

        if (myStockSet.remove(stockCode)) {
            SPTools.saveSetData("my_stock_set", myStockSet);
        }else {
            myStockSet.remove(stockCode.substring(2));
        }
        KLog.e(myStockSet.size()+";==="+myStockSet.toString());
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

    public static String plusMinus(String rateString, boolean percent) {
        if (StringUtils.isActuallyEmpty(rateString)) {
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
                StringUtils.saveSignificand(rate, 2) + (percent ? "%" : "");
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

    /**
     * 根据判断的依据字段，返回股票颜色
     */
    public static int getStockRateColor(String rateOrPriceString) {
        if (TextUtils.isEmpty(rateOrPriceString) || rateOrPriceString.equals("null")) {
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

    public static String getSympolType(int sympolType) {
        switch (sympolType) {
            case 1:
                return "股票";
            case 2:
                return "指数";
            case 3:
                return "基金";
            case 4:
                return "债券";
            default:
                return "其他类型";
        }
    }

//    public static void go2StockDetail(Context context, String stockCode, String stockName) {
//        Intent intent = new Intent(context, StockDetailActivity.class);
//        intent.putExtra("stock_code", stockCode);
//        intent.putExtra("stock_name", stockName);
//        context.startActivity(intent);
//    }

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
        SPTools.saveFloat("closePrice", closePrice);
    }

    /**
     * 添加自选股
     */
    public static void addMyStock(final Context context, final String stock_name, final String stock_code) {
        KLog.e("添加自选股");
        final Map<String, String> param = new HashMap<String, String>();
        param.put("token", UserUtils.getToken());
        param.put("group_id", "0");
        param.put("stock_code", stock_code);
        param.put("stock_class", "0");
        param.put("source", "9");
        param.put("group_class", "1");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {
                KLog.e("添加自选",responseInfo);

                JSONObject result = JSONObject.parseObject(responseInfo);
                if (result.getIntValue("code") == 0) {
                    JSONObject singlestock = new JSONObject();
                    singlestock.put("stock_name", stock_name);
                    singlestock.put("stock_code", stock_code);
                    singlestock.put("stock_type", 1);
                    singlestock.put("price", 0);
                    singlestock.put("change_rate", 0);
                    StockUtils.addStock2MyStock(singlestock);
                    AppManager.getInstance().sendMessage("updata_my_stock_data");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(context, msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, ggHttpInterface).startGet();
    }

    /**
     * 删除自选股 新接口
     */
    public static void deleteMyStock(final Context context, final String full_code, final ToggleMyStockCallBack callBack) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("group_id", "0");
        param.put("full_codes", full_code);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = (int) result.get("code");
                if (code == 0 || code == 1001) {
                    UIHelper.toast(context, "删除自选成功");
                    if (callBack!=null) {
                        callBack.success();
                    }
                    removeStock(full_code.substring(2));
                } else {
                    if (callBack!=null) {
                        callBack.failed(JSONObject.parseObject(responseInfo).getString("message"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                callBack.failed(msg);
                UIHelper.toastError(context, msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.DELETE_MY_STOCKS, httpInterface).startGet();
    }
    /**
     * 删除自选股
     */
    public static void deleteMyStockOld(final Context context, final String stock_code, final ToggleMyStockCallBack callBack) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stock_code);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = (int) result.get("code");
                if (code == 0 || code == 1001) {
                    UIHelper.toast(context, "删除自选成功");
                    if (callBack!=null) {
                        callBack.success();
                    }
                    removeStock(stock_code);
                } else {
                    if (callBack!=null) {
                        callBack.failed(JSONObject.parseObject(responseInfo).getString("message"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                callBack.failed(msg);
                UIHelper.toastError(context, msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
    }

    public interface ToggleMyStockCallBack {
        void success();

        void failed(String msg);
    }
}
