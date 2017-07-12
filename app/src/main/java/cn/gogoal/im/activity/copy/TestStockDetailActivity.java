package cn.gogoal.im.activity.copy;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.stock.StockMapsFragment;

/**
 * Created by huangxx on 2017/6/20.
 */

public class TestStockDetailActivity extends BaseActivity {

    private String stockName;
    private String stockCode;
    private StockMapsFragment stockMapsFragment;

    @BindView(R.id.fragment_container)
    FrameLayout frameLayout;

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_test;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockName = "平安银行";
        stockCode = "000001";
        initList(stockCode);
    }

    /**
     * 初始化数据
     *
     * @param stockCode
     */
    private void initList(final String stockCode) {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {

                StockDetail bean = JSONObject.parseObject(responseInfo, StockDetail.class);

                if (bean.getCode() == 0) {

                    TreatData info = bean.getData();

                    //stockMapsFragment = StockMapsFragment.newInstance(info.getStock_type(), stockName, stockCode);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.add(R.id.fragment_container, stockMapsFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getApplicationContext(), "请检查网络");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, httpInterface).startGet();
    }
}
