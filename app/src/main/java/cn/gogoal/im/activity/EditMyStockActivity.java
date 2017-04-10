package cn.gogoal.im.activity;

import android.content.Context;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class EditMyStockActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_edit_mystock;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("自选股",true).addAction(new XTitle.TextAction(getString(R.string.complete)) {
            @Override
            public void actionClick(View view) {

            }
        });
        ArrayList<MyStockData> myStockdata = (ArrayList<MyStockData>) getIntent().getSerializableExtra("my_stock_edit");
        KLog.e(myStockdata.size());
    }
}
