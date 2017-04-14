package cn.gogoal.im.common.copy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;

/**
 * 新闻阅读相关工具类
 * Created by lixinsheng on 15/11/23.
 */
public class NewsUtils {

    public static void saveBrowsedNews(String id, String type) {
        if (!isReadTodayNews()) {
            SPTools.saveJsonArray("HaveReadNews", new JSONArray());
            SPTools.saveJsonArray("NewsRelatedStocks", new JSONArray());
        }
        JSONObject one = new JSONObject();
        one.put("id", id);
        one.put("type", type);
        boolean is = false;
        JSONArray array = SPTools.getJsonArray("HaveReadNews", new JSONArray());
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            if (id.equals(jsonObject.getString("id")) && type.equals(jsonObject.getString("type"))) {
                is = true;
            }
        }
        if (!is) array.add(one);
        SPTools.saveJsonArray("HaveReadNews", array);
    }

    public static Integer getBrowsedNewsCount() {
        if (!isReadTodayNews()) return 0;
        return SPTools.getJsonArray("HaveReadNews", new JSONArray()).size();
    }

    public static void saveRelatedStocks(String stockName, String stockCode, Double price, Double rate) {
        JSONObject one = new JSONObject();
        one.put("stockCode", stockCode);
        one.put("stockName", stockName);
        one.put("price", price);
        one.put("rate", rate);
        boolean is = false;
        JSONArray array = SPTools.getJsonArray("NewsRelatedStocks", new JSONArray());
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            if (stockCode.equals(jsonObject.getString("stockCode"))) {
                is = true;
            }
        }
        if (!is) array.add(one);
        SPTools.saveJsonArray("NewsRelatedStocks", array);
    }

    public static void getNewsRelatedStockRateSum(final NewsRelatedStocks callbac) {
        if ("".equals(getNewsRelatedStocksParams()) || !isReadTodayNews()) {
            SPTools.saveJsonArray("HaveReadNews", new JSONArray());
            SPTools.saveJsonArray("NewsRelatedStocks", new JSONArray());
            callbac.isBrowseNews(false);
        } else {
            GGOKHTTP.GGHttpInterface stockinterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    JSONObject result = JSONObject.parseObject(responseInfo);
                    int code = result.getInteger("code");
                    if (code == 0) {
                        Double rate = 0d;
                        JSONArray jsonArray = result.getJSONArray("data");
                        if (jsonArray.size() != 0 && !jsonArray.isEmpty()) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject one = (JSONObject) jsonArray.get(i);
                                Double onerate = one.getDouble("change_rate");
                                if (onerate != null) rate += onerate;
                            }
                            rate = rate / jsonArray.size();
                        }
                        callbac.getSumRate(rate);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    callbac.isBrowseNews(false);
                }
            };
            Map<String, String> params = new HashMap<>();
            params.put("stock_codes", getNewsRelatedStocksParams());
            new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, stockinterface).startGet();
        }
    }

    public static String getNewsRelatedStocksParams() {
        JSONArray result = SPTools.getJsonArray("NewsRelatedStocks", new JSONArray());
        if (result.size() == 0 || result.isEmpty()) return "";
        String stocks = "";
        for (int i = 0; i < result.size(); i++) {
            JSONObject stock = (JSONObject) result.get(i);
            stocks += stock.get("stockCode");
            stocks += ";";
        }
        stocks = stocks.substring(0, stocks.length() - 1);
        return stocks;
    }

    public static Integer getRelatedStocksCount() {
        return SPTools.getJsonArray("NewsRelatedStocks", new JSONArray()).size();
    }

    public static Boolean isReadTodayNews() {
        String savaedtime = SPTools.getString("NewsRelatedSavedTime", null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(new Date());
        if (null == savaedtime) {
            SPTools.saveString("NewsRelatedSavedTime", time);
            return false;
        } else if (time.equals(savaedtime)) {
            return true;
        } else {
            SPTools.saveString("NewsRelatedSavedTime", time);
            SPTools.clearItem("NewsRelatedStocks");
            SPTools.clearItem("HaveReadNews");
            return false;
        }
    }

    public static void saveMainTabs(JSONArray jsonArray) {
        SPTools.saveJsonArray("main_tabs", jsonArray);
        JSONArray tabs = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject one = new JSONObject();
            one.put("name", jsonObject.getString("name"));
            one.put("urls", "v1/" + jsonObject.getString("api"));
            tabs.add(one);
        }
        SPTools.saveJsonArray("main_tabs", tabs);
    }

//    public static List<String> getTabsNewsUrl() {
//        List<String> Url = Arrays.asList(GGOKHTTP.HOT_HEADLINES, GGOKHTTP.REPORT_HEADLINES, GGOKHTTP.ANNOCEMENT_HEADLINES,
//                GGOKHTTP.NEWS_HEADLINES, GGOKHTTP.TREASURE_HEADLINES, GGOKHTTP.NEWS_ELITE_HEADLINES);
//        JSONArray result = PersistTool.getJsonArray("main_tabs", null);
//        if (null == result) {
//            return Url;
//        } else {
//            List<String> Url_one = new ArrayList<>();
//            for (int i = 1; i < result.size(); i++) {
//                JSONObject one = (JSONObject) result.get(i);
//                Url_one.add(one.getString("urls"));
//            }
//            return Url_one;
//        }
//    }

//    public static List<String> getTabsNewsTitles() {
//        List<String> titles = Arrays.asList("推荐", "主题", "研报", "公告", "新闻", "业绩", "观点");
//        JSONArray result = PersistTool.getJsonArray("main_tabs", null);
//        if (null == result) {
//            return titles;
//        } else {
//            List<String> titles_one = new ArrayList<String>();
//            for (int i = 0; i < result.size(); i++) {
//                JSONObject one = (JSONObject) result.get(i);
//                titles_one.add(one.getString("name"));
//            }
//            return titles_one;
//        }
//    }

    public interface NewsRelatedStocks {
        void getSumRate(Double rate);

        boolean isBrowseNews(Boolean stockparams);
    }

    /*
      * 保存读过新闻的id
      * */
    public static void savaIsReadNewsId(JSONArray jsonArray) {
        SPTools.saveJsonArray("ggIsReadNewsId", jsonArray);
    }

    /**
     * 获取读过新闻的id
     */
    public static JSONArray getIsReadNewsId() {
        return SPTools.getJsonArray("ggIsReadNewsId", new JSONArray());
    }


    /**
     * 添加单个新闻的id
     */
    public static void addReadNewsId(JSONObject stock) {
        JSONArray stocks = getIsReadNewsId();
        if (stocks == null) {
            stocks = new JSONArray();
        }
        for (int i = 0; i < stocks.size(); i++) {
            if (stock.getString("id").equals(((JSONObject) stocks.get(i)).getString("id"))) {
                stocks.remove(i);
            }
        }
        stocks.add(0, stock);

        if (stocks.size() > 50) {
            JSONArray mStocks = new JSONArray();
            for (int i = 0; i < 50; i++) {
                mStocks.add(stocks.get(i));
            }
            savaIsReadNewsId(mStocks);
        } else {
            savaIsReadNewsId(stocks);
        }
    }

    //判断新闻是否已读过
    public static boolean isAlreadyRead(String id) {
        com.alibaba.fastjson.JSONArray array = NewsUtils.getIsReadNewsId();
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject list = array.getJSONObject(i);
            String new_id = String.valueOf(list.get("id"));
            if (id.equals(new_id)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}

