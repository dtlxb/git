package cn.gogoal.im.common;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.bean.stock.StockTag;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

/**
 * author wangjd on 2017/6/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :APP启动需要获取保存的请求
 */
public class LaunchRequest {

    //只需调用init方法即可;
    public static void init() {

        //1.请求专属顾问
        UserUtils.getAdvisers(null);

        //2.拉取我的群组
        UserUtils.getMyGroupList(null);

        //3.获取我的诊断工具
        UserUtils.getAllMyTools(null);

        if (!StringUtils.isActuallyEmpty(UserUtils.getToken())){

        }
    }

    private void getMyStockData(String token) {

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "500");
        params.put("token", token);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {

                    final ArrayList<MyStockData> parseData = JSONObject.parseObject(responseInfo, MyStockBean.class).getData();

                    String stockCodes =
                            StockUtils.getStockCodes(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("codes", stockCodes);

                    new GGOKHTTP(map, GGOKHTTP.GET_STOCK_TAG, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            if (JsonUtils.getIntValue(responseInfo, "code") == 0) {
                                for (MyStockData data : parseData) {
                                    StockTag tag = new StockTag();
                                    JSONObject objectTag = JSONObject.parseObject(responseInfo).getJSONObject("data");
                                    if (data.getStock_code() != null && objectTag.getString(data.getStock_code()) != null) {
                                        tag.setType(
                                                StringUtils.parseStringDouble(
                                                        objectTag.getString(data.getStock_code())).intValue());
                                    } else {
                                        tag.setType(-2);
                                    }
                                    data.setTag(tag);
//                                    if (!myStockDatas.contains(data)) {
//                                        myStockDatas.add(data);
//                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(String msg) {
                            KLog.e(msg);
                        }
                    }).startGet();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                } else if (code == 1001) {
                } else {
                }

                AppManager.getInstance().sendMessage("market_stop_animation_refresh");
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }
}
