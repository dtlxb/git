package cn.gogoal.im.activity;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.BarView;
import cn.gogoal.im.ui.view.LineBarView;

/**
 * Created by huangxx on 2017/6/9.
 */

public class ChatDemoActivity extends BaseActivity {

    @BindView(R.id.barView)
    BarView barView;
    @BindView(R.id.lineBarView)
    LineBarView lineBarView;

    @Override
    public int bindLayout() {
        return R.layout.activity_chat_demo;
    }

    @Override
    public void doBusiness(Context mContext) {
        initBarChart();
        initLineBarChart();
    }

    private void initBarChart() {
        List<ChartBean> chartBeanList = new ArrayList<>();
        chartBeanList.add(new ChartBean(20.0f, "17Q1"));
        chartBeanList.add(new ChartBean(-20.0f, "16Q4"));
        chartBeanList.add(new ChartBean(40.0f, "16Q3"));
        chartBeanList.add(new ChartBean(60.0f, "16Q2"));
        chartBeanList.add(new ChartBean(-50.0f, "16Q1"));
        barView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        barView.setMarginBottom(AppDevice.dp2px(getActivity(), 20));
        barView.setMarginTop(AppDevice.dp2px(getActivity(), 20));
        barView.setChartData(chartBeanList);
    }

    private void initLineBarChart() {
        ChartBean chartBean1 = new ChartBean(20.0f, 4.0f, "15Q1");
        ChartBean chartBean2 = new ChartBean(30.0f, 1.0f, "15Q2");
        ChartBean chartBean3 = new ChartBean(-20.0f, -3.0f, "15Q3");
        ChartBean chartBean4 = new ChartBean(40.0f, 5.0f, "15Q4");
        ChartBean chartBean5 = new ChartBean(-60.0f, -2.0f, "16Q1");
        ChartBean chartBean6 = new ChartBean(-20.0f, -3.0f, "16Q2");
        ChartBean chartBean7 = new ChartBean(-40.0f, -6.0f, "16Q3");
        ChartBean chartBean8 = new ChartBean(-10.0f, -2.0f, "16Q4");

        List<ChartBean> chartBeanList = new ArrayList<>();
        chartBeanList.add(chartBean1);
        chartBeanList.add(chartBean2);
        chartBeanList.add(chartBean3);
        chartBeanList.add(chartBean4);
        chartBeanList.add(chartBean5);
        chartBeanList.add(chartBean6);
        chartBeanList.add(chartBean7);
        chartBeanList.add(chartBean8);

        lineBarView.setChartData(chartBeanList);
        lineBarView.setMarginBottom(AppDevice.dp2px(getActivity(), 30));
        lineBarView.setMarginLeft(AppDevice.dp2px(getActivity(), 40));
        lineBarView.setMarginRight(AppDevice.dp2px(getActivity(), 40));
        lineBarView.setTextSize(AppDevice.dp2px(getActivity(), 10));
    }
}
