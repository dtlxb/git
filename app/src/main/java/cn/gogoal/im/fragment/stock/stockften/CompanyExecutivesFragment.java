package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 公司高管
 */
public class CompanyExecutivesFragment extends BaseFragment {

    private String stockCode;
    private String stockName;

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
    }
}
