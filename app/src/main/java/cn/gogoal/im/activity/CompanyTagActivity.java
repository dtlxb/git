package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;


import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.fragment.companytags.HistoryFragment;

/**
 * Created by huangxx on 2017/6/28.
 */

public class CompanyTagActivity extends BaseActivity {

    @BindView(R.id.layout_honor)
    FrameLayout layoutHonor;
    @BindView(R.id.layout_history)
    FrameLayout layoutHistory;

    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void doBusiness(Context mContext) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setHistoryFragment(transaction);
    }

    private void setHistoryFragment(FragmentTransaction transaction) {
        transaction.replace(R.id.layout_history, HistoryFragment.newInstance());
        transaction.commit();
    }

}
