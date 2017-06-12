package cn.gogoal.im.activity;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.ui.view.BarView;

/**
 * Created by huangxx on 2017/6/9.
 */

public class ChatDemoActivity extends BaseActivity {

    @BindView(R.id.barView)
    BarView barView;

    @Override
    public int bindLayout() {
        return R.layout.activity_chat_demo;
    }

    @Override
    public void doBusiness(Context mContext) {
        List<Float> values = new ArrayList<>();
        values.add(20.0f);
        values.add(-40.0f);
        values.add(60.0f);
        values.add(20.0f);
        values.add(-80.0f);
        List<String> dates = new ArrayList<>();
        dates.add("17Q1");
        dates.add("16Q4");
        dates.add("16Q3");
        dates.add("16Q2");
        dates.add("16Q1");
        Map<String, Object> map = new HashMap<>();
        map.put("dates", dates);
        map.put("values", values);
        barView.setChartData(map);
    }
}
