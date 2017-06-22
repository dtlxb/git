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
import cn.gogoal.im.bean.PieBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.BarView;
import cn.gogoal.im.ui.view.LineBarView;
import cn.gogoal.im.ui.view.MultiBarView;
import cn.gogoal.im.ui.view.PieView;

/**
 * Created by huangxx on 2017/6/9.
 */

public class ChartDemoActivity extends BaseActivity {

    @BindView(R.id.barView)
    BarView barView;
    @BindView(R.id.lineBarView)
    LineBarView lineBarView;
    @BindView(R.id.multiBarView)
    MultiBarView multiBarView;
    @BindView(R.id.pieView)
    PieView pieView;

    @Override
    public int bindLayout() {
        return R.layout.activity_chat_demo;
    }

    @Override
    public void doBusiness(Context mContext) {
        initBarChart();
        initLineBarChart();
        initMultiBarView();
        initPieView();
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
        List<ChartBean> chartBeanList = new ArrayList<>();
        chartBeanList.add(new ChartBean(20.0f, 4.0f, "15Q1"));
        chartBeanList.add(new ChartBean(30.0f, 1.0f, "15Q2"));
        chartBeanList.add(new ChartBean(-20.0f, -3.0f, "15Q3"));
        chartBeanList.add(new ChartBean(40.0f, 5.0f, "15Q4"));
        chartBeanList.add(new ChartBean(-60.0f, -2.0f, "16Q1"));
        chartBeanList.add(new ChartBean(-20.0f, -3.0f, "16Q2"));
        chartBeanList.add(new ChartBean(-40.0f, -6.0f, "16Q3"));
        chartBeanList.add(new ChartBean(-10.0f, -2.0f, "16Q4"));

        lineBarView.setMarginBottom(AppDevice.dp2px(getActivity(), 30));
        lineBarView.setMarginLeft(AppDevice.dp2px(getActivity(), 40));
        lineBarView.setMarginRight(AppDevice.dp2px(getActivity(), 40));
        lineBarView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        lineBarView.setChartData(chartBeanList);
    }

    private void initMultiBarView() {
        List<ChartBean> chartBeanList = new ArrayList<>();
        chartBeanList.add(new ChartBean(20.0f, 30.0f, 40.0f, "2016-05-01"));
        chartBeanList.add(new ChartBean(20.0f, 10.0f, 15.0f, "2016-08-01"));
        chartBeanList.add(new ChartBean(30.0f, 30.0f, 40.0f, "2016-10-01"));
        chartBeanList.add(new ChartBean(18.0f, 36.0f, 28.0f, "2016-12-01"));
        chartBeanList.add(new ChartBean(5.0f, 10.0f, 50.0f, "2016-02-01"));
        multiBarView.setMarginBottom(AppDevice.dp2px(getActivity(), 30));
        multiBarView.setMarginLeft(AppDevice.dp2px(getActivity(), 40));
        multiBarView.setMarginRight(AppDevice.dp2px(getActivity(), 10));
        multiBarView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        multiBarView.setChartData(chartBeanList);
    }

    private void initPieView() {
        List<PieBean> pieBeanList = new ArrayList<>();
        pieBeanList.add(new PieBean("交通", 20.0f, R.color.color_text_value_grey));
        pieBeanList.add(new PieBean("学习", 30.0f, R.color.color_black));
        pieBeanList.add(new PieBean("农业", 10.0f, R.color.color_blue));
        pieBeanList.add(new PieBean("金融", 40.0f, R.color.color_red));
        pieBeanList.add(new PieBean("政治", 80.0f, R.color.live_close_title_color));
        pieView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        pieView.setPieType(0);
        pieView.setPieData(pieBeanList);
    }
}
