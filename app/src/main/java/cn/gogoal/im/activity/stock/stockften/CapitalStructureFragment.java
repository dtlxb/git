package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股本结构
 */
public class CapitalStructureFragment extends BaseFragment {

    private String stockCode;
    private String stockName;

    public static CapitalStructureFragment getInstance(String stockCode, String stockName) {
        CapitalStructureFragment fragment = new CapitalStructureFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_capital_structure;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");
    }
}
