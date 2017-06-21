package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

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
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.StockDetailText2;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.stock.CompanyFinanceFragment;
import cn.gogoal.im.fragment.stock.CompanyInfoFragment;
import cn.gogoal.im.fragment.stock.StockNewsMinFragment;
import cn.gogoal.im.ui.view.XTitle;


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
    ViewPager chartLayout;

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

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private String stockCode;
    private String stockName;

    private List<StockDetailText2> listHead = new ArrayList<>();
    private HeadInfoAdapter headInfoAdapter;

    private List<StockDetailText2> listTreat = new ArrayList<>();
    private TreatInfoAdapter treatInfoAdapter;
    private XTitle xTitle;

    //====================================copy=========================

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");

        initTitle();//初始化标题

        initHead();//初始化头部控件

        getStockHeadInfo(AppConst.REFRESH_TYPE_FIRST);//获取交易数据，并填充头部

        setChart();//设置表格

        initNews();//个股新闻

        iniRefresh(swiperefreshlayout);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockHeadInfo(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                AppManager.getInstance().sendMessage("refresh_stock_news");//发个消息刷新新闻
            }
        });
    }

    private void refreshAll(int refreshType){
        getStockHeadInfo(refreshType);
    }

//    /**
//     * 中间Eps模块数据,图表ChatImage数据
//     */
//    private void initChartImage() {
//        ViewGroup.LayoutParams params = vpImageChart.getLayoutParams();
//        params.width = AppDevice.getWidth(getActivity());
//        params.height = 350 * AppDevice.getWidth(getActivity()) / 560;
//        vpImageChart.setLayoutParams(params);
//
//        vpImageChart.setOffscreenPageLimit(3);
//
//        Map<String, String> param = new HashMap<>();
//        param.put("stock_code", stockCode);
//        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
//
//                if (code == 0) {
//                    final ChartImageBean.ChartImage chartImage = JSONObject.parseObject(responseInfo, ChartImageBean.class).getData();
//
//                    vpImageChart.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//                        @Override
//                        public Fragment getItem(int position) {
//                            return ImageChartFragment.getInstance(position == 0 ? chartImage.getEps_img() : (
//                                    position == 1 ? chartImage.getProfit_img() : chartImage.getPe_img()
//                            ));
//                        }
//
//                        @Override
//                        public int getCount() {
//                            return 3;
//                        }
//
//                        @Override
//                        public CharSequence getPageTitle(int position) {
//                            return arrStockChartImage[position];
//                        }
//                    });
//                    tabStockDetailChartImg.setupWithViewPager(vpImageChart);
//
//                } else if (code == 1001) {
//
//                } else {
//
//                }
//
//            }
//
//            public void onFailure(String msg) {
//                UIHelper.toastError(getActivity(), msg);
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.DM_GET_IMG, ggHttpInterface).startGet();
//    }

    /**初始化头部控件*/
    private void initHead() {
        rvStockDetailHead.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        headInfoAdapter = new HeadInfoAdapter(listHead);
        rvStockDetailHead.setAdapter(headInfoAdapter);

        rvStockDetailTreat.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        treatInfoAdapter = new TreatInfoAdapter(listTreat);
        rvStockDetailTreat.setAdapter(treatInfoAdapter);
    }

    /**初始化标题*/
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

    /**获取交易数据，并填充头部*/
    private void getStockHeadInfo(final int refreshType) {
        Map<String, String> map = new HashMap<>();
        map.put("stock_code", stockCode);
        new GGOKHTTP(map, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    listHead.clear();
                    listTreat.clear();
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
                    treatValue.add(formatCapitalization(
                            StringUtils.pareseStringDouble(treatData.getTurnover()), 2));

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

                    if (refreshType== AppConst.REFRESH_TYPE_SWIPEREFRESH ||
                            refreshType==AppConst.REFRESH_TYPE_PARENT_BUTTON){
                        UIHelper.toast(getActivity(),"刷新成功");
                    }

                    scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                            KLog.e(scrollY);
//                            KLog.d(oldScrollY);
                            //TODO 往上滑动，滑出头部显示交易价格
                        }
                    });


                } else if (code == 1001) {
                    setStockHeadColor("-1");//设置颜色
                    //值数据从缓存中取
                } else {
                    setStockHeadColor("-1");//设置颜色
                }
                swiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                setStockHeadColor("-1");//设置颜色
                UIHelper.toastError(getActivity(), msg);
                swiperefreshlayout.setRefreshing(false);
            }
        }).startGet();

    }

    /**个股新闻*/
    private void initNews() {

        final List<Fragment> fragments=new ArrayList<>();
        fragments.add(StockNewsMinFragment.getInstance(stockCode,stockName,0));
        fragments.add(StockNewsMinFragment.getInstance(stockCode,stockName,1));
        fragments.add(StockNewsMinFragment.getInstance(stockCode,stockName,2));
        fragments.add(CompanyInfoFragment.newInstance(stockCode));
        fragments.add(new CompanyFinanceFragment());

        vpNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
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
            setStatusColor(getResColor(R.color.stock_gray));
            xTitle.setBackgroundColor(getResColor(R.color.stock_gray));
            layoutHead.setBackgroundColor(getResColor(R.color.stock_gray));
        } else {
            setStatusColorId(StockUtils.getStockRateColor(change_rate));
            xTitle.setBackgroundResource(StockUtils.getStockRateColor(change_rate));
            layoutHead.setBackgroundResource(StockUtils.getStockRateColor(change_rate));
        }
    }

    /**设置表格*/
    private void setChart() {
        for (String chartTitle : arrStockChart) {
            tabStockDetailChart.addTab(tabStockDetailChart.newTab().setText(chartTitle));
        }
    }

    //========================================数据处理区 START======================================
    private String formatVolume(String volume, boolean typeHead) {
        if (StringUtils.isActuallyEmpty(volume)) {
            return "--";
        }
        if (Integer.parseInt(volume) == 0 || Integer.parseInt(volume) == Double.NaN) {
            return "0手";
        }

        if (volume.length() > 6) {
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
