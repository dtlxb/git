package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.f10.CompanySummary;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :股票公司资料（个股Tab）
 */
public class CompanyInfoFragment extends BaseFragment {

    private String stockCode;

    public static CompanyInfoFragment newInstance(String stockCode) {
        CompanyInfoFragment infoFragment = new CompanyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stockCode", stockCode);
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        String stockCode = getArguments().getString("stockCode");

        HashMap<String, String> map = new HashMap<>();
        map.put("stock_code",stockCode);
        new GGOKHTTP(map, GGOKHTTP.COMPANY_SUMMARY, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                CompanySummary companySummary =
                        JSONObject.parseObject(responseInfo, CompanySummary.class);

            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }
}
