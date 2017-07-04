package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;


import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.fragment.companytags.GoodCompanyFragment;
import cn.gogoal.im.fragment.companytags.HistoryFragment;
import cn.gogoal.im.fragment.companytags.IncidentFragment;
import cn.gogoal.im.fragment.companytags.RevenueRateFragment;
import cn.gogoal.im.fragment.companytags.StockHoldersFragment;
import cn.gogoal.im.fragment.companytags.TurnoverRateFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/6/28.
 */

public class CompanyTagActivity extends BaseActivity {

    @BindView(R.id.layout_honor)
    FrameLayout layoutHonor;
    @BindView(R.id.layout_history)
    FrameLayout layoutHistory;
    private XTitle xTitle;

    private String stockCode;
    private String stockName;

    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");
        //业绩鉴定-历史
        setFragment(AppConst.TYPE_FRAGMENT_HISTORY);
        //行业地位
        setFragment(AppConst.TYPE_FRAGMENT_INDUSTRY);
        //未来事件提醒
        setFragment(AppConst.TYPE_FRAGMENT_INCIDENT_FUTURE);
        //事件足迹
        setFragment(AppConst.TYPE_FRAGMENT_INCIDENT_FEET);
        //分歧度
        setFragment(AppConst.TYPE_FRAGMENT_TURNOVER_RATE);
        //业绩季节性
        setFragment(AppConst.TYPE_FRAGMENT_REVENUE_RATE);
        //主流机构持仓
        setFragment(AppConst.TYPE_FRAGMENT_STOCK_HOLDERS);
        //公司荣誉
        setFragment(AppConst.TYPE_FRAGMENT_GOOD_COMPANY);

        setMyTitle(stockName + "(" + stockCode + ")", true);

    }


    private void setFragment(int fragmentType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentType) {
            case AppConst.TYPE_FRAGMENT_HISTORY:
                transaction.replace(R.id.layout_history, HistoryFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_INDUSTRY:
                transaction.replace(R.id.layout_industry, HistoryFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_INCIDENT_FUTURE:
                transaction.replace(R.id.layout_warning, IncidentFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_INCIDENT_FEET:
                transaction.replace(R.id.layout_accident_feet, IncidentFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_TURNOVER_RATE:
                transaction.replace(R.id.layout_divide, TurnoverRateFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_REVENUE_RATE:
                transaction.replace(R.id.layout_season, RevenueRateFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_STOCK_HOLDERS:
                transaction.replace(R.id.layout_take_position, StockHoldersFragment.newInstance(stockCode, fragmentType));
                break;
            case AppConst.TYPE_FRAGMENT_GOOD_COMPANY:
                transaction.replace(R.id.layout_honor, GoodCompanyFragment.newInstance(stockCode, fragmentType));
                break;
            default:
                break;
        }
        transaction.commit();
    }

}
