package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 经营分析
 */
public class BusinessAnalysisFragment extends BaseFragment {

    //产品分类
    @BindView(R.id.textProductClass)
    TextView textProductClass;
    //产品名称
    @BindView(R.id.textProductName)
    TextView textProductName;
    //经营范围
    @BindView(R.id.textScopeBusiness)
    TextView textScopeBusiness;
    //左边列表
    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;

    private String stockCode;
    private String stockName;

    private AnalysisLeftAdapter leftAdapter;

    public static BusinessAnalysisFragment getInstance(String stockCode, String stockName) {
        BusinessAnalysisFragment fragment = new BusinessAnalysisFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_business_analysis;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        getAnalysisData();
    }

    private void getAnalysisData() {

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //FileUtil.writeRequestResponse(responseInfo,"F10DATA.TXT");
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONArray mainInduc = data.getJSONObject("main").getJSONArray("mainInduc");
                    textProductClass.setText(mainInduc.get(0).toString());
                    textProductName.setText(mainInduc.get(1).toString());
                    textScopeBusiness.setText(mainInduc.get(2).toString());

                    data.remove("main");

                    //取key集合
                    List<String> keyMSList = new ArrayList<>();
                    for (String dataMSKey : data.keySet()) {
                        keyMSList.add(dataMSKey);
                    }
                    //将时间戳转毫秒值并排序
                    List<Long> keyLongList = new ArrayList<>();
                    for (int i = 0; i < keyMSList.size(); i++) {
                        keyLongList.add(CalendarUtils.parseString2Long(keyMSList.get(i) + " 00:00:00"));
                    }
                    Collections.sort(keyLongList, Collections.reverseOrder());
                    //取排序后的数据
                    List<String> keyList = new ArrayList<>();
                    for (int i = 0; i < keyLongList.size(); i++) {
                        //keyList.add(CalendarUtils.parseDateFormatAll(keyLongList.get(i), "yyyy-MM-dd"));
                        for (int j = 0; j < keyMSList.size(); j++) {
                            if (keyLongList.get(i) == CalendarUtils.parseString2Long(keyMSList.get(j) + " 00:00:00")) {
                                keyList.add(keyMSList.get(j));
                            }
                        }
                    }
                    //造自己需要的数据
                    ArrayList<String> titleList = new ArrayList<>();
                    for (int i = 0; i < keyList.size(); i++) {
                        titleList.add(keyList.get(i));
                        for (int j = 0; j < FtenUtils.analysisName.length; j++) {
                            titleList.add(FtenUtils.analysisName[j]);
                        }
                    }
                    setLeftListData(titleList);

                    List<JSONObject> timeList = new ArrayList<>();
                    for (int i = 0; i < keyList.size(); i++) {
                        timeList.add(data.getJSONObject(keyList.get(i)));
                    }
                    KLog.e(timeList);

                    List<JSONArray> content1 = new ArrayList<>();
                    for (int i = 0; i < timeList.size(); i++) {
                        JSONArray array1 = new JSONArray();
                        array1.add(timeList.get(i).getJSONArray("产品"));
                        array1.add(timeList.get(i).getJSONArray("地区"));
                        array1.add(timeList.get(i).getJSONArray("行业"));
                        content1.add(array1);
                    }

                    List<JSONArray> content2 = new ArrayList<>();
                    for (int i = 0; i < content1.size(); i++) {
                    }

                    //FileUtil.writeRequestResponse(String.valueOf(content2), "F10DATA.TXT");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_BUSINESS_ANALYSIS, ggHttpInterface).startGet();
    }

    /**
     * 设置左边数据
     */
    private void setLeftListData(ArrayList<String> titleList) {

        leftAdapter = new AnalysisLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }


}
