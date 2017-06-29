package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;


import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.fragment.companytags.HistoryFragment;
import cn.gogoal.im.fragment.companytags.IncidentFragment;

/**
 * Created by huangxx on 2017/6/28.
 */

public class CompanyTagActivity extends BaseActivity {

    @BindView(R.id.layout_honor)
    FrameLayout layoutHonor;
    @BindView(R.id.layout_history)
    FrameLayout layoutHistory;

    private String stockCode;

    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = "300021";
        //业绩鉴定-历史
        setFragment(AppConst.TYPE_FRAGMENT_HISTORY);
        //行业地位
        setFragment(AppConst.TYPE_FRAGMENT_INDUSTRY);
        //未来事件提醒
        setFragment(AppConst.TYPE_FRAGMENT_INCIDENT_FUTURE);
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
            default:
                break;
        }
        transaction.commit();
    }

}
