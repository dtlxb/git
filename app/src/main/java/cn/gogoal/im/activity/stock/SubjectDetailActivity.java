package cn.gogoal.im.activity.stock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.BigDataDetailAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.bigdata.BigDataDetailList;
import cn.gogoal.im.bean.stock.bigdata.BigDataDetailListBean;
import cn.gogoal.im.bean.stock.bigdata.ChartBean;
import cn.gogoal.im.bean.stock.bigdata.ChartData;
import cn.gogoal.im.bean.stock.bigdata.subject.SubjectDetailBean;
import cn.gogoal.im.bean.stock.bigdata.subject.SubjectDetailData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.HideChartGestureListener;
import cn.gogoal.im.common.HtmlTagHandler;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;

import static android.text.Html.fromHtml;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主题内容
 */
public class SubjectDetailActivity extends BaseActivity {

    @BindView(R.id.tv_opportunity)
    TextView tvOpportunity;

    @BindView(R.id.rv_subject_about_stocks)
    RecyclerView rvSubjectAboutStocks;

    @BindView(R.id.iv_describe)
    ImageView imgDescibe;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.line_chart_price)
    LineChart mChartPrice;

    @BindView(R.id.line_chart_attention)
    LineChart mChartAttention;

    @BindView(R.id.tv_point_info)
    TextView tvPointInfo;

    @BindView(R.id.rdg_change_chart)
    RadioGroup rdgChangeChart;

    private List<BigDataDetailList> subjectContentList;
    private BigDataDetailAdapter subjectContentAdapter;
    private String subjectId;

    @Override
    public int bindLayout() {
        return R.layout.activity_subject;
    }

    @Override
    public void doBusiness(Context mContext) {
        String subject_type = getIntent().getStringExtra("subject_type");
        setMyTitle(subject_type + "产业", true);

        subjectId = getIntent().getStringExtra("subject_id");

        BaseActivity.iniRefresh(refreshLayout);

        subjectContentList = new ArrayList<>();
        subjectContentAdapter = new BigDataDetailAdapter(subjectContentList);
        rvSubjectAboutStocks.setLayoutManager(new LinearLayoutManager(mContext));
        rvSubjectAboutStocks.setNestedScrollingEnabled(false);
        rvSubjectAboutStocks.setAdapter(subjectContentAdapter);

        //
        if (StringUtils.isActuallyEmpty(subjectId)) {
            tvOpportunity.setText("获取主题信息失败,请稍后重试");
        } else {
            getSubjectDetail(AppConst.REFRESH_TYPE_FIRST);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubjectDetail(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.setRefreshing(false);
            }
        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getSubjectDetail(AppConst.REFRESH_TYPE_RELOAD);
            }
        });

        rdgChangeChart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_checked_attention:
                        mChartPrice.setVisibility(View.GONE);
                        mChartAttention.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdb_checked_price:
                        mChartAttention.setVisibility(View.GONE);
                        mChartPrice.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubjectDetail(AppConst.REFRESH_TYPE_RESUME);
    }

    //请求基本详情数据
    private void getSubjectDetail(int refreshType) {
        if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
            xLayout.setStatus(XLayout.Loading);
        }

        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("id", subjectId);
        new GGOKHTTP(params, GGOKHTTP.GET_RECOMMEND_CONTENT, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    SubjectDetailData subjectDetailData =
                            JSONObject.parseObject(responseInfo, SubjectDetailBean.class).getData();

                    tvOpportunity.setText(
                            fromHtml(
                                    subjectDetailData.getTheme_summarize().getDescribe(), null, new HtmlTagHandler()));

                    RequestOptions options = new RequestOptions();
                    options.fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(getActivity())
                            .load(subjectDetailData.getTheme_summarize().getPhone_image_url())
                            .apply(options)
                            .into(imgDescibe);

                    //
                    getStockPoolDatas(StockUtils.getStockCodes(
                            subjectDetailData.getStocks()), subjectDetailData.getTheme_name());
                    //
                    getThemeWordsAttentionYear(StockUtils.getStockCodes(subjectDetailData.getStocks()),
                            subjectDetailData.getTheme_name());

                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null)
                    xLayout.setStatus(XLayout.Error);
            }
        }).startGet();
    }

    //通过详情数据的股票集参数二次请求股票池详细数据
    private void getStockPoolDatas(String stock_codes, String keyword) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("rows", "15");
        params.put("page", "1");
        params.put("get_hq", "1");
        params.put("stock_codes", stock_codes);
        params.put("keyword", keyword);

        params.put("order", "price_change_rate");
        params.put("order_type", "-1");

        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_POOL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    subjectContentList.clear();
                    List<BigDataDetailList> subjectContents =
                            JSONObject.parseObject(responseInfo, BigDataDetailListBean.class).getData();

                    subjectContentList.addAll(subjectContents);
                    subjectContentAdapter.notifyDataSetChanged();

                    xLayout.setStatus(XLayout.Success);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    //请求主题追踪,绘图
    public void getThemeWordsAttentionYear(String stockCodes, String themeName) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("stock_codes", stockCodes);
        params.put("theme_word", themeName);

        new GGOKHTTP(params, GGOKHTTP.GET_THEME_WORDS_ATTENTION_YEAR, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                ArrayList<ChartData> chartDatas =
                        JSONObject.parseObject(responseInfo, ChartBean.class).getData();

                setChart(mChartPrice, chartDatas, true);

                setChart(mChartAttention, chartDatas, false);
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    @SuppressLint("SetTextI18n")
    private void setPointValue(ChartData chartDatas) {
        String attention = StringUtils.saveSignificand(100 * chartDatas.getAttention(), 3) + "%";
        String price = StringUtils.save2Significand(chartDatas.getIndex_price());

        String hollText = "年份：" + CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
                chartDatas.getDate()) + "\u3000价格：" +
                price + "\u3000关注度：" + attention;

//        年(0)份(1)：(2)2(3)0(4)1(5)7(6)-(7)0(8)3(9)-(10)0(11)2(12)　(13)关(14)注(15)度(16)：(17)0(18).(19)1(20)0(21)%(22)　(23)价(24)格(25)：(26)1(27)0(28)5(29)8(30)
        SpannableString spannableString = new SpannableString(hollText);
        //日期
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.textColor_333333)),
                0, 3,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.colorPrimary)),
                3, 14,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //价格
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.textColor_333333)),
                14, 17,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.colorPrimary)),
                17, 17 + price.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //关注度
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.textColor_333333)),
                17 + price.length(), 22 + price.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.colorPrimary)),
                22 + price.length(), hollText.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tvPointInfo.setText(spannableString);


    }

    private void setChart(final LineChart mChart, final ArrayList<ChartData> chartDatas, boolean showPrice) {
        //基本配置部分
        mChart.setNoDataText("没有图表数据");
        mChart.setDrawGridBackground(false);//禁用背景
        mChart.setDoubleTapToZoomEnabled(false);//禁止双击放大
        mChart.getDescription().setEnabled(false);//机你用描述
        mChart.setTouchEnabled(true);//允许触摸跟手
        mChart.setKeepPositionOnRotation(false);//
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.animateX(800);

        mChart.setHighlightPerDragEnabled(true);
        mChart.setDrawBorders(false);
        mChart.setBorderWidth(1f);
        mChart.setBorderColor(Color.GRAY);

        //X轴
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? CalendarUtils.getStringDate("yyyy-MM-dd", chartDatas.get(0).getDate()) :
                        (value == 180?CalendarUtils.getStringDate("yyyy-MM-dd", chartDatas.get(chartDatas.size() - 1).getDate()) : "");
            }
        });

        //右侧Y轴
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.removeAllLimitLines();
        rightAxis.disableGridDashedLine();
        rightAxis.disableAxisLineDashedLine();
        rightAxis.setDrawTopYLabelEntry(false);
        rightAxis.setDrawLimitLinesBehindData(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "\u3000\u3000";
            }
        });

        //左边Y轴
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines


        ChartData minPriceData = Collections.min(chartDatas, new Comparator<ChartData>() {
            @Override
            public int compare(ChartData o1, ChartData o2) {
                return compareTo(o1.getIndex_price(), o2.getIndex_price());
            }
        });

        ChartData maxPriceData = Collections.max(chartDatas, new Comparator<ChartData>() {
            @Override
            public int compare(ChartData o1, ChartData o2) {
                return compareTo(o1.getIndex_price(), o2.getIndex_price());
            }
        });


        ChartData minAttentionData = Collections.min(chartDatas, new Comparator<ChartData>() {
            @Override
            public int compare(ChartData o1, ChartData o2) {
                return Double.compare(o1.getAttention(), o2.getAttention());
            }
        });

        ChartData maxAttentionData = Collections.max(chartDatas, new Comparator<ChartData>() {
            @Override
            public int compare(ChartData o1, ChartData o2) {
                return Double.compare(o1.getAttention(), o2.getAttention());
            }
        });

        leftAxis.setAxisMaximum(showPrice ? maxPriceData.getIndex_price() :
                Float.parseFloat(StringUtils.saveSignificand(100 * maxAttentionData.getAttention(), 3)));
        leftAxis.setAxisMinimum(showPrice ? minPriceData.getIndex_price() :
                Float.parseFloat(StringUtils.saveSignificand(100 * minAttentionData.getAttention(), 3)));

        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        setData(mChart, chartDatas, showPrice);
        mChart.getLineData().setDrawValues(false);

        Legend l = mChart.getLegend();
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.NONE);

        //===================监听=================
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setPointValue(chartDatas.get((int) e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart.setOnChartGestureListener(new HideChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                tvPointInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                mChart.highlightValue(null);
                tvPointInfo.setVisibility(View.INVISIBLE);
            }
        });

    }

    public static int compareTo(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    private void setData(LineChart mChart, List<ChartData> chartDatas, boolean showPrice) {//45, 100
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < chartDatas.size(); i++) {
            float val;
            if (showPrice) {
                val = chartDatas.get(i).getIndex_price();
            } else {
                val = Float.parseFloat(StringUtils.saveSignificand(100 * chartDatas.get(i).getAttention(), 3));
            }
            values.add(new Entry(i, val, ContextCompat.getDrawable(getActivity(), R.drawable.star)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setColor(Color.parseColor("#2091FF"));
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(0.5f);
            set1.setDrawHorizontalHighlightIndicator(true);
            set1.setHighLightColor(Color.BLACK);

            set1.setDrawCircles(false);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setHighlightEnabled(true);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_chart);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            LineData data = new LineData(dataSets);

            mChart.setData(data);
        }
    }
}
