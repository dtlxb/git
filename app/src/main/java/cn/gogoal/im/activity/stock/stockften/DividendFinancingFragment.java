package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 分红融资
 */
public class DividendFinancingFragment extends BaseFragment {

    private String stockCode;
    private String stockName;

    public static DividendFinancingFragment getInstance(String stockCode, String stockName) {
        DividendFinancingFragment fragment = new DividendFinancingFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_dividend_financing;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");
    }
}
