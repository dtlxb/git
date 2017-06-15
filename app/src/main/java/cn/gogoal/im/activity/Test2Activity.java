package cn.gogoal.im.activity;

import android.content.Context;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.fragment.stock.MarketFragment2;


public class Test2Activity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_test2;
    }

    @Override
    public void doBusiness(final Context mContext) {

        MarketFragment2 fragment = new MarketFragment2();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_my_container, fragment).commit();

    }
}
