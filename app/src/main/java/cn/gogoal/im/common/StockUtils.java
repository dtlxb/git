package cn.gogoal.im.common;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gogoal.im.R;
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
        SPTools.saveSetData(UserUtils.getMyAccountId()+"_my_stock_set", set);
    }

    /**
     * 获取本地自选股集合
     */
    public static Set<String> getMyStockSet() {
        return SPTools.getSetData(UserUtils.getMyAccountId()+"_my_stock_set", new HashSet<String>());
    }

    public static String getStockCodes(@NonNull JSONArray array) {
        List<String> codeArrs = new ArrayList<>();

        if (array.isEmpty()) {
            return null;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            if (!StringUtils.isActuallyEmpty(object.getString("stock_code"))) {
                codeArrs.add(object.getString("stock_code"));
            }
        }
        return ArrayUtils.mosaicListElement(codeArrs);
    }

    public static <T> String getStockCodes(@NonNull List<T> array) {
        List<String> codeArrs = new ArrayList<>();

        if (array.isEmpty()) {
            return null;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(array.get(i)));
            if (!StringUtils.isActuallyEmpty(object.getString("stock_code"))) {
                codeArrs.add(object.getString("stock_code"));
            }
        }
        return ArrayUtils.mosaicListElement(codeArrs);
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
    public static void addStock2MyStock(String stockCode) {
        if (stockCode == null) {
            return;
        }
        Set<String> myStockSet = getMyStockSet();
        myStockSet.add(stockCode);
        SPTools.saveSetData(UserUtils.getMyAccountId()+"_my_stock_set", myStockSet);
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
            SPTools.saveSetData(UserUtils.getMyAccountId()+"_my_stock_set", myStockSet);
        } else {
            myStockSet.remove(stockCode.substring(2));
        }
    }

    /**
     * 清除自选股
     */
    public static void clearLocalMyStock() {
        SPTools.clearItem(UserUtils.getMyAccountId()+"_my_stock_set");
    }

    //数据处理，1保存两位，2.正数补+，
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

    public static String plusMinus(double rate, boolean percent) {

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
    public static
    @ColorRes
    int getStockRateColor(String rateOrPriceString) {
        if (TextUtils.isEmpty(rateOrPriceString) || rateOrPriceString.equals("null")) {
            return R.color.stock_gray;
        }
        double rateOrPrice = Double.parseDouble(rateOrPriceString);

        return rateOrPrice == Double.NaN ? R.color.stock_gray : (rateOrPrice > 0 ? R.color.stock_red : (rateOrPrice == 0 ? R.color.stock_gray :
                R.color.stock_green));
    }

    /**
     * 根据判断的依据字段，返回股票颜色
     */
    public static
    @ColorRes
    int getStockRateColor(double rateOrPriceString) {
        return rateOrPriceString == Double.NaN ?
                R.color.stock_gray :
                (rateOrPriceString > 0 ?
                        R.color.stock_red : (rateOrPriceString == 0 ? R.color.stock_gray :
                        R.color.stock_green));
    }

    /**
     * 根据判断的依据字段，返回股票颜色
     */
    public static int getStockRateBackgroundRes(String rateOrPriceString) {
        if (TextUtils.isEmpty(rateOrPriceString) || rateOrPriceString.equals("null")) {
            return R.drawable.shape_my_stock_price_gray;
        }
        double rateOrPrice = Double.parseDouble(rateOrPriceString);

        return rateOrPrice == Double.NaN ? R.drawable.shape_my_stock_price_gray :
                (rateOrPrice > 0 ? R.drawable.shape_my_stock_price_red :
                        (rateOrPrice == 0 ? R.drawable.shape_my_stock_price_gray :
                                R.drawable.shape_my_stock_price_green));
    }

    /**
     * 根据判断的依据字段，返回股票颜色
     */
    public static int getStockRateBackgroundRes(double rateOrPriceString) {
        return rateOrPriceString == Double.NaN ? R.drawable.shape_my_stock_price_gray :
                (rateOrPriceString > 0 ? R.drawable.shape_my_stock_price_red :
                        (rateOrPriceString == 0 ? R.drawable.shape_my_stock_price_gray :
                                R.drawable.shape_my_stock_price_green));
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
     * stock_charge_type
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
     * 回调股票状态
     */
    public static void getStockStatus(String stockCode, @NonNull final Impl<String> callback) {
        final Map<String, String> map = new HashMap<>();
        map.put("stock_code", stockCode);
        new GGOKHTTP(map, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    if (object.getJSONObject("data") == null) {
                        callback.response(Impl.RESPON_DATA_ERROR, "data字段缺失");
                    } else {
                        try {
                            callback.response(Impl.RESPON_DATA_SUCCESS,
                                    object.getJSONObject("data").getString("stock_type"));
                        } catch (Exception e) {
                            callback.response(Impl.RESPON_DATA_ERROR, "stock_type字段缺失");
                        }
                    }
                } else if (object.getIntValue("code") == 1001) {
                    callback.response(Impl.RESPON_DATA_EMPTY, "没有查询到这支股票");
                } else {
                    callback.response(Impl.RESPON_DATA_ERROR, object.getString("message"));
                }
            }

            @Override
            public void onFailure(String msg) {
                callback.response(Impl.RESPON_DATA_ERROR, msg);
            }
        }).startGet();
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
    public static String getColseprice() {
        return SPTools.getString("closePrice", "0");
    }

    public static void savaColseprice(String closePrice) {
        SPTools.saveString("closePrice", closePrice);
    }

    /**
     * 添加自选股
     */
    public static void addMyStock(final String stock_code, final Impl<Boolean> callback) {
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
                KLog.e(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if (result.getIntValue("code") == 0) {
                    StockUtils.addStock2MyStock(stock_code);
                    if (callback != null) {
                        callback.response(Impl.RESPON_DATA_SUCCESS, true);
                    }
                } else {
                    if (callback != null)
                        callback.response(Impl.RESPON_DATA_ERROR, false);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callback != null)
                    callback.response(Impl.RESPON_DATA_ERROR, false);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, ggHttpInterface).startGet();
    }


    private static RotateAnimation rotateAnimation;//旋转动画

    private static void stopRefreshAnimation(ImageView btnRefresh) {
        if (rotateAnimation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, btnRefresh, R.mipmap.choose_stock);
        } else {
            btnRefresh.clearAnimation();
        }
    }

    private static void startRefreshAnimation(ImageView btnRefresh) {
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(btnRefresh, R.mipmap.img_loading_refresh);
        rotateAnimation.startNow();
    }

    public static void toggleAddDelStock(@NonNull String stockCode,
                                         @NonNull final ImageView imageView) {
        startRefreshAnimation(imageView);
        //删除自选股
        if (isMyStock(stockCode)) {
            deleteMyStockOld(stockCode, new Impl<String>() {
                @Override
                public void response(int code, String data) {
                    UIHelper.toast(imageView.getContext(),
                            "删除自选" + (code == Impl.RESPON_DATA_SUCCESS ? "成功" : "失败"));
                    stopRefreshAnimation(imageView);
                    imageView.setImageResource(
                            code == Impl.RESPON_DATA_SUCCESS ? R.mipmap.choose_stock : R.mipmap.not_choose_stock);
                }
            });
        }
        //添加自选股
        else {
            addMyStock(stockCode, new Impl<Boolean>() {
                @Override
                public void response(int code, Boolean data) {
                    UIHelper.toast(imageView.getContext(), "添加自选" + (data ? "成功" : "失败"));
                    stopRefreshAnimation(imageView);
                    imageView.setImageResource(
                            code == Impl.RESPON_DATA_SUCCESS ? R.mipmap.not_choose_stock : R.mipmap.choose_stock);
                }
            });
        }
    }

    /**
     * 删除自选股 新接口
     */
    public static void deleteMyStock(final Context context, final String full_code, final Impl<String> callBack) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("group_id", "0");
        param.put("full_codes", full_code);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = result.getIntValue("code");
                if (code == 0 || code == 1001) {
                    UIHelper.toast(context, "删除自选成功");
                    if (callBack != null) {
                        callBack.response(Impl.RESPON_DATA_SUCCESS, result.getString("message"));
                    }
                    removeStock(full_code.substring(2));
                } else {
                    if (callBack != null) {
                        callBack.response(Impl.RESPON_DATA_ERROR,
                                result.getString("message"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callBack != null) {
                    callBack.response(Impl.RESPON_DATA_ERROR, msg);
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.DELETE_MY_STOCKS, httpInterface).startGet();
    }

    /**
     * 删除自选股
     */
    public static void deleteMyStockOld(final String stock_code, final Impl<String> callBack) {
        final Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stock_code);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = result.getIntValue("code");
                if (code == 0 || code == 1001) {
                    if (callBack != null) {
                        callBack.response(Impl.RESPON_DATA_SUCCESS, result.getString("message"));
                    }
                    removeStock(stock_code);
                } else {
                    if (callBack != null) {
                        callBack.response(Impl.RESPON_DATA_ERROR,
                                result.getString("message"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callBack != null)
                    callBack.response(Impl.RESPON_DATA_ERROR, msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
    }
}
