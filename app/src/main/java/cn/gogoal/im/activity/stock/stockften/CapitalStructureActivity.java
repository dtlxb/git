package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股本结构
 */
public class CapitalStructureActivity extends BaseActivity {

    private String stockCode;
    private String stockName;

    @Override
    public int bindLayout() {
        return R.layout.activity_capital_structure;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");

        setMyTitle(stockName + "-股本", true);
    }
}
