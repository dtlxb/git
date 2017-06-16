package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.adapter.ExecutivesAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ExecutivesData;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.copy.InnerListView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 公司高管
 */
public class CompanyExecutivesFragment extends BaseFragment {

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;

    private AnalysisLeftAdapter leftAdapter;
    private ExecutivesAdapter rightAdapter;

    public static CompanyExecutivesFragment getInstance(String stockCode, String stockName) {
        CompanyExecutivesFragment fragment = new CompanyExecutivesFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_executives;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        getExecutivesData();
    }

    private void getExecutivesData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                FileUtil.writeRequestResponse(responseInfo, "F10DATA.TXT");
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    setLeftListData(data.getJSONArray("senior_info_list"));
                    setRightListData(data.getJSONArray("senior_info_list"));
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SENIOR, ggHttpInterface).startGet();
    }

    /**
     * 设置左边列表数据
     */
    private void setLeftListData(JSONArray senior_info_list) {
        ArrayList<String> titleList = new ArrayList<>();

        titleList.add("高管列表");
        for (int i = 0; i < senior_info_list.size(); i++) {
            titleList.add(senior_info_list.getJSONObject(i).getString("name"));
        }

        leftAdapter = new AnalysisLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }

    /**
     * 设置右边列表数据
     */
    private void setRightListData(JSONArray senior_info_list) {
        ArrayList<ExecutivesData> contList = new ArrayList<>();
        contList.add(new ExecutivesData("学历", "职务"));

        for (int i = 0; i < senior_info_list.size(); i++) {
            contList.add(new ExecutivesData(senior_info_list.getJSONObject(i).getString("degree"),
                    senior_info_list.getJSONObject(i).getString("duty")));
        }

        rightAdapter = new ExecutivesAdapter(getContext(), contList);
        lsvRight.setAdapter(rightAdapter);
    }
}
