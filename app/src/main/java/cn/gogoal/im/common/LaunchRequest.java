package cn.gogoal.im.common;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
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

        if (!StringUtils.isActuallyEmpty(UserUtils.getToken())) {
            getMyStockData(UserUtils.getToken());
        }
    }

    private static void getMyStockData(String token) {

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "200");
        params.put("token", token);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                new MyStockAsyn().execute(responseInfo);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    private static class MyStockAsyn extends AsyncTask<String, Void, ArrayList<MyStockData>> {

        @Override
        protected ArrayList<MyStockData> doInBackground(String... params) {
            final ArrayList<MyStockData> result = new ArrayList<>();

            String json = params[0];//第一波json
            if (JsonUtils.getIntValue(params[0], "code") == 0) {

                //第一波解析结果，不含标签数据
                final ArrayList<MyStockData> myStockDatas =
                        JsonUtils.parseJsonObject(json, MyStockBean.class).getData();

                //获取结果所有股票的code集
                String stockCodes = getStockCodes(myStockDatas);
                HashMap<String, String> map = new HashMap<>();
                map.put("codes", stockCodes);



                return result;

            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MyStockData> datas) {
        }
    }

    class TagAysn extends AsyncTask<ArrayList<MyStockData>, Void, ArrayList<MyStockData>> {

        @Override
        protected ArrayList<MyStockData> doInBackground(ArrayList<MyStockData>... params) {
            ArrayList<MyStockData> noTagDatas = params[0];
            ArrayList<MyStockData> withTagDatas = new ArrayList<>();

            String stockCodes = getStockCodes(noTagDatas);
            HashMap<String,String> map=new HashMap<>();
            map.put("codes",stockCodes);
            new GGOKHTTP(map, GGOKHTTP.GET_STOCK_TAG, new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    if (JsonUtils.getIntValue(responseInfo, "code") == 0) {
//                        for (MyStockData data : myStockDatas) {
//                            StockTag tag = new StockTag();
//                            JSONObject objectTag = JSONObject.parseObject(responseInfo).getJSONObject("data");
//                            if (data.getStock_code() != null && objectTag.getString(data.getStock_code()) != null) {
//                                tag.setType(
//                                        StringUtils.parseStringDouble(
//                                                objectTag.getString(data.getStock_code())).intValue());
//                            } else {
//                                tag.setType(-2);
//                            }
//                            data.setTag(tag);
//                            if (!result.contains(data)) {
//                                result.add(data);
//                            }
//                        }
//                        KLog.e("TAG", result.size());
                    }
                }

                @Override
                public void onFailure(String msg) {
                    KLog.e(msg);
                }
            }).startGet();
            return withTagDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<MyStockData> datas) {
            super.onPostExecute(datas);
        }
    }

    public static String getStockCodes(@NonNull List<MyStockData> array) {
        List<String> codeArrs = new ArrayList<>();

        if (array.isEmpty()) {
            return null;
        }
        for (MyStockData data : array) {
            if (!TextUtils.isEmpty(data.getStock_code())) {
                codeArrs.add(data.getStock_code());
            }
        }
        return ArrayUtils.mosaicListElement(codeArrs);
    }

}
