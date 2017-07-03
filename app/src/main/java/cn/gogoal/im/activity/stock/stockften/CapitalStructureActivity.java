package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股本结构
 */
public class CapitalStructureActivity extends BaseActivity {

    @BindView(R.id.rv_equity)
    RecyclerView rvEquity;

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

        BaseActivity.initRecycleView(rvEquity, null);
        rvEquity.setHasFixedSize(true);
        rvEquity.setNestedScrollingEnabled(false);

        getRestrictSale();
    }

    /**
     * 限售解禁
     */
    private void getRestrictSale() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.RESTRICT_SALE, ggHttpInterface).startGet();
    }
}
