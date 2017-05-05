package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.ChartImageBean;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.StockDetailText2;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.stock.ImageChartFragment;
import cn.gogoal.im.fragment.stock.StockDetailNewTab;
import cn.gogoal.im.ui.view.XTitle;
import hply.com.niugu.stock.TimesFivesBitmap;


/**
 * author wangjd on 2017/4/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :个股详情 ☆☆☆
 */
public class StockDetailActivity extends BaseActivity {

    @BindView(R.id.tv_stock_detail_price)
    TextView tvStockDetailPrice;

    @BindView(R.id.tv_stock_detail_change_value)
    TextView tvStockDetailChangeValue;

    @BindView(R.id.tv_stock_detail_change_rate)
    TextView tvStockDetailChangeRate;

    @BindView(R.id.rv_stock_detail_head)
    RecyclerView rvStockDetailHead;

    @BindView(R.id.rv_stock_detail_treat)
    RecyclerView rvStockDetailTreat;

    @BindArray(R.array.treat_info)
    String[] treatDescArray;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.tab_stock_detail_chart)
    TabLayout tabStockDetailChart;

    @BindView(R.id.chart_layout)
    FrameLayout chartLayout;

    @BindView(R.id.tab_stock_detail_news)
    TabLayout tabStockDetailNews;

    @BindView(R.id.vp_news)
    ViewPager vpNews;

    @BindView(R.id.layout_stock_detail_head)
    View layoutHead;

    String subTitleText = "%s\u3000%s";

    @BindArray(R.array.stock_chart_array)
    String[] arrStockChart;

    @BindArray(R.array.stock_news_array)
    String[] arrStockNews;

    @BindView(R.id.tab_stock_detail_chart_img)
    TabLayout tabStockDetailChartImg;

    @BindView(R.id.vp_chart)
    ViewPager vpImageChart;

    @BindArray(R.array.srock_chart_image)
    String[] arrStockChartImage;

    private int refreshType;

    private String stockCode;
    private String stockName;

    private List<StockDetailText2> listHead = new ArrayList<>();
    private HeadInfoAdapter headInfoAdapter;

    private List<StockDetailText2> listTreat = new ArrayList<>();
    private TreatInfoAdapter treatInfoAdapter;
    private XTitle xTitle;

    //====================================copy=========================
    private TimesFivesBitmap fiveDayBitmap;
    private TimesFivesBitmap timesBitmap;
    private HashMap<String, Bitmap> map = new HashMap<>();
    private boolean canRefreshLine=true;
    private int stock_charge_type = 1;


    @Override
    public int bindLayout() {
        return R.layout.activity_stock_detail;
    }

    @Override
    public void setStatusBar(boolean light) {
        StatusBarUtil.with(this).setColor(getResColor(R.color.stock_green));
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");

        initTitle();

        initHead();

        getStockHeadInfo();//

        setChart();

        initNews();

        initChartImage();

        iniRefresh(swiperefreshlayout);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockHeadInfo();

                AppManager.getInstance().sendMessage("refresh_stock_news");//发个消息刷新新闻


            }
        });
    }

    /**
     * 中间Eps模块数据,图表ChatImage数据
     */
    private void initChartImage() {
        ViewGroup.LayoutParams params = vpImageChart.getLayoutParams();
        params.width = AppDevice.getWidth(getActivity());
        params.height = 350 * AppDevice.getWidth(getActivity()) / 560;
        vpImageChart.setLayoutParams(params);

        vpImageChart.setOffscreenPageLimit(3);

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    final ChartImageBean.ChartImage chartImage = JSONObject.parseObject(responseInfo, ChartImageBean.class).getData();

                    vpImageChart.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return ImageChartFragment.getInstance(position == 0 ? chartImage.getEps_img() : (
                                    position == 1 ? chartImage.getProfit_img() : chartImage.getPe_img()
                            ));
                        }

                        @Override
                        public int getCount() {
                            return 3;
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return arrStockChartImage[position];
                        }
                    });
                    tabStockDetailChartImg.setupWithViewPager(vpImageChart);

                } else if (code == 1001) {

                } else {

                }

            }

            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.DM_GET_IMG, ggHttpInterface).startGet();
    }

    private void initHead() {
        rvStockDetailHead.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        headInfoAdapter = new HeadInfoAdapter(listHead);
        rvStockDetailHead.setAdapter(headInfoAdapter);

        rvStockDetailTreat.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        treatInfoAdapter = new TreatInfoAdapter(listTreat);
        rvStockDetailTreat.setAdapter(treatInfoAdapter);
    }

    private void initTitle() {
        xTitle = setMyTitle(stockName + "(" + stockCode + ")" + "\n" + String.format(subTitleText,
                StockUtils.getTreatState(), CalendarUtils.getCurrentTime("MM-dd HH:mm")), true)
                .setSubTitleSize(TypedValue.COMPLEX_UNIT_SP, 10)
                .setSubTitleColor(Color.argb(204, 255, 255, 255))
                .setLeftImageResource(R.mipmap.image_title_back_255)
                .setLeftText(getString(R.string.str_title_back))
                .setTitleColor(Color.WHITE)
                .setLeftTextColor(Color.WHITE);

        xTitle.addAction(new XTitle.ImageAction(getResDrawable(R.mipmap.refresh_white)) {
            @Override
            public void actionClick(View view) {
                //TODO 刷新
            }
        });

    }

    private void getStockHeadInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("stock_code", stockCode);
        new GGOKHTTP(map, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.file("TAG", getExternalFilesDir("json"),
                        "stockDetail_" + stockName + ".txt", responseInfo);
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    TreatData treatData = JSONObject.parseObject(responseInfo, StockDetail.class).getData();
                    setStockHeadColor(treatData.getChange_rate());//设置颜色
                    //设置标题、副标题
                    xTitle.setTitle(stockName + "(" + stockCode + ")\n" + (
                            treatData.getStock_type() == 1 ?
                                    String.format(subTitleText, StockUtils.getTreatState(), CalendarUtils.getCurrentTime("MM-dd HH:mm")) :
                                    String.format(subTitleText, StringUtils.saveSignificand(StringUtils.pareseStringDouble(treatData.getOpen_price()), 2),
                                            StockUtils.getStockStatus(treatData.getStock_type()))
                    ));

                    tvStockDetailPrice.setText(StringUtils.saveSignificand(treatData.getPrice(), 2));
                    tvStockDetailChangeValue.setText(
                            StockUtils.plusMinus(StringUtils.saveSignificand(
                                    StringUtils.pareseStringDouble(treatData.getChange_value()), 2), false));

                    tvStockDetailChangeRate.setText(
                            StockUtils.plusMinus(treatData.getChange_rate(), true));

                    /*=================数据重构======================================*/
                    //================ head ======================
                    listHead.add(new StockDetailText2("今开", StringUtils.saveSignificand(treatData.getOpen_price(), 2)));
                    listHead.add(new StockDetailText2("昨收", StringUtils.saveSignificand(treatData.getClose_price(), 2)));
                    listHead.add(new StockDetailText2("成交量",
                            formatVolume(treatData.getVolume(), true)));//TODO "volume": 14793700,14.79万手
                    listHead.add(new StockDetailText2("换手率",
                            StringUtils.saveSignificand(
                                    StringUtils.pareseStringDouble(treatData.getTurnover_rate()) * 100, 2) + "%"));
                    headInfoAdapter.notifyDataSetChanged();

                    //=============== treat info===================
                    List<String> treatValue = new ArrayList<>(treatDescArray.length);
                    treatValue.add(StringUtils.saveSignificand(
                            StringUtils.pareseStringDouble(treatData.getHigh_price()), 2));
                    treatValue.add(StringUtils.saveSignificand(
                            StringUtils.pareseStringDouble(treatData.getLow_price()), 2));
                    //TODO "turnover": 24892.5896,-> 2.49亿
                    treatValue.add(formatCapitalization(
                            StringUtils.pareseStringDouble(treatData.getTurnover()), 2));

                    //TODO "volume_inner": 7215783,,-> 7.22万
                    treatValue.add(formatVolume(treatData.getVolume_inner(), false));//内盘
                    treatValue.add(formatVolume(treatData.getVolume_outer(), false));//外盘
                    treatValue.add(formatCapitalization(
                            StringUtils.pareseStringDouble(treatData.getTcap()), 1));//总市值

                    treatValue.add(StringUtils.saveSignificand(treatData.getPb_y1(), 2));//市盈率
                    treatValue.add(StringUtils.saveSignificand(treatData.getAmplitude(), 2) + "%");//振幅
                    treatValue.add(formatCapitalization(
                            StringUtils.pareseStringDouble(treatData.getMcap()), 1));//流通市值

                    for (int i = 0; i < treatDescArray.length; i++) {
                        listTreat.add(new StockDetailText2(treatDescArray[i], treatValue.get(i)));
                    }
                    treatInfoAdapter.notifyDataSetChanged();

                } else if (code == 1001) {
                    setStockHeadColor("-1");//设置颜色
                    //值数据从缓存中取
                } else {
                    setStockHeadColor("-1");//设置颜色
                }
            }

            @Override
            public void onFailure(String msg) {
                setStockHeadColor("-1");//设置颜色
                UIHelper.toastError(getActivity(), msg);
            }
        }).startGet();
    }

    private void initNews() {
        vpNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return StockDetailNewTab.getInstance(position);
            }

            @Override
            public int getCount() {
                return arrStockNews.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return arrStockNews[position];
            }
        });
        tabStockDetailNews.setupWithViewPager(vpNews);
    }

    private void setStockHeadColor(String change_rate) {
        if (TextUtils.isEmpty(change_rate)) {
            StatusBarUtil.with(getActivity()).setColor(getResColor(R.color.stock_gray));
            xTitle.setBackgroundColor(getResColor(R.color.stock_gray));
            layoutHead.setBackgroundColor(getResColor(R.color.stock_gray));
        } else {
            double rate = Double.parseDouble(change_rate);
            if (rate > 0) {
                StatusBarUtil.with(getActivity()).setColor(
                        getResColor(R.color.stock_red));
                xTitle.setBackgroundColor(getResColor(R.color.stock_red));
                layoutHead.setBackgroundColor(getResColor(R.color.stock_red));
            } else if (rate < 0) {
                StatusBarUtil.with(getActivity()).setColor(
                        getResColor(R.color.stock_green));
                xTitle.setBackgroundColor(getResColor(R.color.stock_green));
                layoutHead.setBackgroundColor(getResColor(R.color.stock_green));
            } else {
                StatusBarUtil.with(getActivity()).setColor(
                        getResColor(R.color.stock_gray));
                xTitle.setBackgroundColor(getResColor(R.color.stock_gray));
                layoutHead.setBackgroundColor(getResColor(R.color.stock_gray));
            }
        }
    }

    private void setChart() {
        for (String chartTitle : arrStockChart) {
            tabStockDetailChart.addTab(tabStockDetailChart.newTab().setText(chartTitle));
        }
    }

    //========================================数据处理区 START======================================
    private String formatVolume(String volume, boolean typeHead) {
        if (TextUtils.isEmpty(volume)) {
            return "--";
        }
        if (Integer.parseInt(volume) == 0 || Integer.parseInt(volume) == Double.NaN) {
            return "0手";
        }

        if (volume.toString().length() > 6) {
            return StringUtils.saveSignificand(Double.parseDouble(String.valueOf(volume)) / 1000000d, 2)
                    + (typeHead ? "万手" : "万");
        } else {
            return StringUtils.saveSignificand(Double.parseDouble(String.valueOf(volume)) / 100d, 0) + "手";
        }
    }

    private String formatCapitalization(double volume, int unit) {
        if (volume == 0) {
            return "0";
        }
        if (Double.valueOf(volume).toString().length() < 7) {
            return StringUtils.saveSignificand(volume, 2) + "万";
        } else {
            return StringUtils.saveSignificand(volume / 10000d, unit) + "亿";
        }
    }
    //========================================数据处理区 END======================================

    /**
     * 头部适配器
     */
    private class HeadInfoAdapter extends CommonAdapter<StockDetailText2, BaseViewHolder> {

        public HeadInfoAdapter(List<StockDetailText2> data) {
            super(R.layout.item_rv_stock_detail_head, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, StockDetailText2 item, int position) {
            holder.setText(R.id.tv_stock_detail_head_desc, item.getTextDesc());
            holder.setText(R.id.tv_stock_detail_head_value, item.getTextValue());
        }
    }

    /**
     * 交易适配器
     */
    private class TreatInfoAdapter extends CommonAdapter<StockDetailText2, BaseViewHolder> {

        private int itemWidth;

        private TreatInfoAdapter(List<StockDetailText2> data) {
            super(R.layout.item_rv_stock_detail_treat, data);
            itemWidth = (AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 50)) / 3;
        }

        @Override
        protected void convert(BaseViewHolder holder, StockDetailText2 item, int position) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = itemWidth;
            holder.itemView.setLayoutParams(params);

            holder.setText(R.id.tv_stock_detail_head_desc, item.getTextDesc());
            holder.setText(R.id.tv_stock_detail_head_value, item.getTextValue());
        }
    }

    //异步画图

}
