package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/3/8 0008.
 * Staff_id 1375
 * phone 18930640263
 *
 * 行情
 */
public class MarketActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_market;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();
    }

    private void iniTitle() {
        XTitle title = setMyTitle(R.string.title_stock_market, true);
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        title.setTitleColor(Color.WHITE);
        title.setLeftTextColor(Color.WHITE);
    }
}
