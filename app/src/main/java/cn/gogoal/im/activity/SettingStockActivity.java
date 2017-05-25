package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/**
 * author wangjd on 2017/5/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :行情股票 设置
 */
public class SettingStockActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_setting;
    }
    @Override
    public void doBusiness(Context mContext) {
    }

    @OnClick({R.id.tv_set_stock_refresh_time, R.id.tv_set_stock_k_line,
            R.id.tv_set_stock_up_and_down_color})

    public void click(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_set_stock_refresh_time:
//                行情刷新频率
                intent = new Intent(view.getContext(), SetStockRefreshActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_set_stock_k_line:
                //K线设置
                intent = new Intent(view.getContext(), KlineSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_set_stock_up_and_down_color:
                //涨跌幅显示
                intent = new Intent(view.getContext(), RedGreenSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

}
