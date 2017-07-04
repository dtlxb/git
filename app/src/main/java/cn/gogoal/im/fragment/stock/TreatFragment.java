package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.bean.PieBean;
import cn.gogoal.im.bean.stock.MoneyBean;
import cn.gogoal.im.bean.stock.MoneyTrade;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.ThreeText;
import cn.gogoal.im.bean.stock.TodayInfoBean;
import cn.gogoal.im.bean.stock.TradeBean;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.fragment.copy.TimesFragment;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.PieView;
import cn.gogoal.im.ui.view.ProgressBarView;
import cn.gogoal.im.ui.view.XLayout;
import hply.com.niugu.bean.TimeDetialBean;
import hply.com.niugu.bean.TimeDetialData;


/**
 * author wangjd on 2017/5/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class TreatFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.progress)
    ProgressBarView progressView;

    private PieView pieView;

    //===================五档====================
    private WudangAdapter wudangAdapter;
    private List<ThreeText> threeTexts;
    private double closePrice;
    private List<ChartBean> chartBeanList;

    //===================明细=====================
    private MingxiAdapter mingxiAdapter;
    private List<TimeDetialData> timeDetailDatas;
    private boolean fromStockDetail;
    private int type;

    //===================资金=====================
    private MoneyAdapter moneyAdapter;
    private List<MoneyTrade> moneyDatas;
    private List<PieBean> pieDatas;

    private float itemHeight;
    private String stockCode;
    private boolean needAnim;//需要动画

    @Override
    public int bindLayout() {
        return R.layout.fragment_treat;
    }

    /**
     * @param stockCode       股票代码
     * @param type            类型：五档还是明细
     * @param fromStockDetail 使用环境：个股详情页，还是横屏大图页使用
     */
    public static TreatFragment getInstance(String stockCode, int type, boolean fromStockDetail) {
        TreatFragment fragment = new TreatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", stockCode);
        bundle.putInt("type", type);
        bundle.putBoolean("from_stock_detail", fromStockDetail);
        if (type == AppConst.TREAT_TYPE_WU_DANG) {
            fragment.itemHeight = fromStockDetail ? 15.2f : 22.5f;
        } else if (type == AppConst.TREAT_TYPE_MING_XI) {
            fragment.itemHeight = 19;
        } else if (type == AppConst.TREAT_TYPE_MONEY) {
            fragment.itemHeight = fromStockDetail ? 11.2f : 21f;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        stockCode = getArguments().getString("stock_code");
        type = getArguments().getInt("type");
        fromStockDetail = getArguments().getBoolean("from_stock_detail");
        chartBeanList = new ArrayList<>();

        if (stockCode != null) {
            if (type == AppConst.TREAT_TYPE_WU_DANG) {
                threeTexts = new ArrayList<>();
                wudangAdapter = new WudangAdapter(threeTexts);
                recyclerView.setAdapter(wudangAdapter);
                recyclerView.addItemDecoration(new WudangDivider(getResColor(R.color.chart_text_color)));
                needAnim = false;
                getTreatWudang();
            } else if (type == AppConst.TREAT_TYPE_MING_XI) {
                timeDetailDatas = new ArrayList<>();
                mingxiAdapter = new MingxiAdapter(timeDetailDatas);
                recyclerView.setAdapter(mingxiAdapter);
                getTreatChart();
                getStockTimeDetail();
            } else if (type == AppConst.TREAT_TYPE_MONEY) {
                moneyDatas = new ArrayList<>();
                pieDatas = new ArrayList<>();
                moneyAdapter = new MoneyAdapter(moneyDatas);
                recyclerView.setAdapter(moneyAdapter);
                View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_map_header, new LinearLayout(getActivity()), false);
                pieView = (PieView) headerView.findViewById(R.id.money_pie);
                moneyAdapter.addHeaderView(headerView);
                getTreatChart();
                getMoneyDetail();

                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleParentTab();
                    }
                });
            }

        }

        xLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleParentTab();
            }
        });
    }

    @Subscriber(tag = "refresh_stock_news")
    void updataTreatData(String msg) {
        if (type == AppConst.TREAT_TYPE_WU_DANG) {
            needAnim = false;
            getTreatWudang();
        } else if (type == AppConst.TREAT_TYPE_MING_XI) {
            getStockTimeDetail();
        } else if (type == AppConst.TREAT_TYPE_MONEY) {
            //getMoneyDetail();资金不需实时刷新
        }
    }

    private void toggleParentTab() {
        if (fromStockDetail) {
            ((StockMapsFragment) getParentFragment()).toggleTreatMode();
        } else {
            ((TimesFragment) getParentFragment()).toggleTreatMode();
        }
    }

    private void getTreatWudang() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    threeTexts.clear();
                    TreatData treatData = JSONObject.parseObject(responseInfo, StockDetail.class).getData();

                    float sell = parseFloat(treatData.getSell5_volume()) + parseFloat(treatData.getSell4_volume()) +
                            parseFloat(treatData.getSell3_volume()) + parseFloat(treatData.getSell2_volume()) +
                            parseFloat(treatData.getSell1_volume());

                    float buy = parseFloat(treatData.getBuy5_volume()) + parseFloat(treatData.getBuy4_volume()) +
                            parseFloat(treatData.getBuy3_volume()) + parseFloat(treatData.getBuy2_volume()) +
                            parseFloat(treatData.getBuy1_volume());

                    threeTexts.add(new ThreeText("卖5", treatData.getSell5_price(), treatData.getSell5_volume()));
                    threeTexts.add(new ThreeText("卖4", treatData.getSell4_price(), treatData.getSell4_volume()));
                    threeTexts.add(new ThreeText("卖3", treatData.getSell3_price(), treatData.getSell3_volume()));
                    threeTexts.add(new ThreeText("卖2", treatData.getSell2_price(), treatData.getSell2_volume()));
                    threeTexts.add(new ThreeText("卖1", treatData.getSell1_price(), treatData.getSell1_volume()));

                    threeTexts.add(new ThreeText("买1", treatData.getBuy1_price(), treatData.getBuy1_volume()));
                    threeTexts.add(new ThreeText("买2", treatData.getBuy2_price(), treatData.getBuy2_volume()));
                    threeTexts.add(new ThreeText("买3", treatData.getBuy3_price(), treatData.getBuy3_volume()));
                    threeTexts.add(new ThreeText("买4", treatData.getBuy4_price(), treatData.getBuy4_volume()));
                    threeTexts.add(new ThreeText("买5", treatData.getBuy5_price(), treatData.getBuy5_volume()));

                    chartBeanList.clear();
                    chartBeanList.add(new ChartBean(buy, "#ed1b1b"));
                    chartBeanList.add(new ChartBean(sell, "#26b844"));
                    progressView.setTextSize(AppDevice.dp2px(getActivity(), 9));
                    progressView.setChartData(chartBeanList, needAnim);
                    wudangAdapter.notifyDataSetChanged();

                    closePrice = StringUtils.parseStringDouble(treatData.getClose_price());
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
    }

    private void getTreatChart() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    TreatData treatData = JSONObject.parseObject(responseInfo, StockDetail.class).getData();

                    float sell = parseFloat(treatData.getSell5_volume()) + parseFloat(treatData.getSell4_volume()) +
                            parseFloat(treatData.getSell3_volume()) + parseFloat(treatData.getSell2_volume()) +
                            parseFloat(treatData.getSell1_volume());
                    float buy = parseFloat(treatData.getBuy5_volume()) + parseFloat(treatData.getBuy4_volume()) +
                            parseFloat(treatData.getBuy3_volume()) + parseFloat(treatData.getBuy2_volume()) +
                            parseFloat(treatData.getBuy1_volume());

                    chartBeanList.clear();
                    chartBeanList.add(new ChartBean(buy, "#ed1b1b"));
                    chartBeanList.add(new ChartBean(sell, "#26b844"));
                    progressView.setChartData(chartBeanList, needAnim);
                    progressView.setTextSize(AppDevice.dp2px(getActivity(), 9));
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
    }

    private float parseFloat(String floatString){
        if (StringUtils.isActuallyEmpty(floatString)){
            return 0.0f;
        }else {
            try {
                return Float.parseFloat(floatString);
            }catch (Exception e){
                return 0.0f;
            }
        }
    }

    //分时数据交易明细
    private void getStockTimeDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("limit", "10");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    timeDetailDatas.clear();
                    List<TimeDetialData> cacheData =
                            JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData();
                    if (fromStockDetail && cacheData.size() >= 8) {
                        timeDetailDatas.addAll(cacheData.subList(0, 8));
                    } else {
                        timeDetailDatas.addAll(cacheData);
                    }
                    mingxiAdapter.notifyDataSetChanged();
                }
            }

            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_TIME_DETIAL, ggHttpInterface).startGet();
    }

    //资金数据
    private void getMoneyDetail() {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if (result.getIntValue("code") == 0) {
                    MoneyBean moneyBean = JSONObject.parseObject(responseInfo, TradeBean.class).getData();
                    TodayInfoBean todayInfoBean = moneyBean.getTodayInfo();
                    initMoneyAdapter(todayInfoBean);
                } else {
                    drawPieMap();
                }
            }

            public void onFailure(String msg) {
                drawPieMap();
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FUAN_INFO, ggHttpInterface).startGet();
    }

    private void initMoneyAdapter(TodayInfoBean todayInfoBean) {

        double total = todayInfoBean.getFlow_into_large_fund() + todayInfoBean.getFlow_into_middle_fund()
                + todayInfoBean.getFlow_into_small_fund() + todayInfoBean.getFlow_out_large_fund() +
                todayInfoBean.getFlow_out_middle_fund() + todayInfoBean.getFlow_out_small_fund();

        moneyDatas.clear();
        moneyDatas.add(new MoneyTrade("大单", namParse(todayInfoBean.getFlow_into_large_fund()),
                namParse(todayInfoBean.getFlow_into_large_fund(), total), "#ca2c36"));
        moneyDatas.add(new MoneyTrade("中单", namParse(todayInfoBean.getFlow_into_middle_fund()),
                namParse(todayInfoBean.getFlow_into_middle_fund(), total), "#e1515b"));
        moneyDatas.add(new MoneyTrade("小单", namParse(todayInfoBean.getFlow_into_small_fund()),
                namParse(todayInfoBean.getFlow_into_small_fund(), total), "#ff9a99"));
        moneyDatas.add(new MoneyTrade("大单", namParse(todayInfoBean.getFlow_out_large_fund()),
                namParse(todayInfoBean.getFlow_out_large_fund(), total), "#247c54"));
        moneyDatas.add(new MoneyTrade("中单", namParse(todayInfoBean.getFlow_out_middle_fund()),
                namParse(todayInfoBean.getFlow_out_middle_fund(), total), "#34b578"));
        moneyDatas.add(new MoneyTrade("小单", namParse(todayInfoBean.getFlow_out_small_fund()),
                namParse(todayInfoBean.getFlow_out_small_fund(), total), "#63dca4"));
        drawPieMap();
        moneyAdapter.notifyDataSetChanged();
    }

    private void drawPieMap() {
        pieView.setPieType(2);
        pieView.setNeedInnerCircle(true);

        float percent;
        if (fromStockDetail) {
            percent = 0.3f;
        } else {
            percent = 0.2f;
        }

        int width = (int) (percent * AppDevice.getWidth(getActivity()));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width - AppDevice.dp2px(getActivity(), 22));
        pieView.setLayoutParams(params);
        pieView.setMarginLeft(AppDevice.dp2px(getActivity(), 16));
        pieView.setMarginRight(AppDevice.dp2px(getActivity(), 18));

        pieDatas.clear();
        for (int i = moneyDatas.size() - 1; i > -1; i--) {
            pieDatas.add(new PieBean(moneyDatas.get(i).getTradeType(), parseFloat(moneyDatas.get(i).getTradePer().substring(0, moneyDatas.get(i).getTradePer().length() - 1)),
                    moneyDatas.get(i).getColor()));
        }

        pieView.setPieData(pieDatas);
    }

    private String namParse(double number, double total) {
        return (int) Math.rint(number * 100 / total) + "%";
    }

    private String namParse(double number) {
        String num;
        int intNum = (int) Math.rint(number / 10000);
        if (intNum == 0) {
            num = "0";
        } else {
            num = String.valueOf(intNum) + "万";
        }
        return num;
    }


    private class WudangAdapter extends CommonAdapter<ThreeText, BaseViewHolder> {

        private WudangAdapter(List<ThreeText> data) {
            super(R.layout.item_treat_3_text, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ThreeText data, int position) {

            View view = holder.getView(R.id.item_view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    AppDevice.dp2px(getContext(), itemHeight));
            view.setLayoutParams(params);

            TextView tvTreatPrice = holder.getView(R.id.tv_treat_price);

            if (type == AppConst.TREAT_TYPE_MING_XI) {
                tvTreatPrice.setGravity(Gravity.RIGHT);
            } else {
                tvTreatPrice.setGravity(Gravity.CENTER);
            }

            holder.setText(R.id.tv_treat_name, data.getName());

            String price = StringUtils.parseStringDouble(data.getPrice(), 2);

            tvTreatPrice.setText(price);

            holder.setText(R.id.tv_treat_value, formatValue(data.getValue()));

            if (Double.parseDouble(price) > closePrice) {
                holder.setTextColor(R.id.tv_treat_price, getResColor(R.color.stock_red));
            } else if (Double.parseDouble(price) == closePrice || Double.parseDouble(price) == 0) {
                holder.setTextColor(R.id.tv_treat_price, getResColor(R.color.gray_light));
            } else {
                holder.setTextColor(R.id.tv_treat_price, getResColor(R.color.stock_green));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleParentTab();
                }
            });
        }

        private String formatValue(String value) {
            if (TextUtils.isEmpty(value)) {
                return "--";
            } else {
                try {
                    int parseInt = Integer.parseInt(value);
                    return String.valueOf(parseInt / 100);
                } catch (Exception e) {
                    return "--";
                }

            }
        }
    }

    private class WudangDivider extends XDividerItemDecoration {


        private WudangDivider(@ColorInt int color) {
            super(getActivity(), 1, color);
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] show = new boolean[4];
            if (itemPosition == 4) {
                show[3] = true;
            }
            return show;
        }
    }

    private class MingxiAdapter extends CommonAdapter<TimeDetialData, BaseViewHolder> {

        private MingxiAdapter(List<TimeDetialData> data) {
            super(R.layout.item_treat_3_text, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, TimeDetialData data, int position) {
            View view = holder.getView(R.id.item_view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    AppDevice.dp2px(getContext(), itemHeight));
            view.setLayoutParams(params);

            holder.setText(R.id.tv_treat_name, CalendarUtils.getHour$Min(data.getUpdate_time()));
            TextView tvDealPrice = holder.getView(R.id.tv_treat_price);
            tvDealPrice.setText(StringUtils.parseStringDouble(data.getPrice(), 2));
            if (data.getLast_price_change() > 0) {
                tvDealPrice.setTextColor(Color.parseColor("#F34957"));
            } else {
                tvDealPrice.setTextColor(Color.parseColor("#1ebf61"));
            }

            TextView tvDealVolume = holder.getView(R.id.tv_treat_value);
            tvDealVolume.setText(data.getVolume());
            switch (data.getTransaction_type()) {
                case 1:
                    tvDealVolume.setTextColor(getResColor(R.color.stock_green));
                    break;
                case 2:
                    tvDealVolume.setTextColor(getResColor(R.color.stock_red));
                    break;
                case 3:
                    tvDealVolume.setTextColor(Color.parseColor("#858585"));
                    break;
                default:
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleParentTab();
                }
            });
        }
    }


    private class MoneyAdapter extends CommonAdapter<MoneyTrade, BaseViewHolder> {
        public MoneyAdapter(List<MoneyTrade> data) {
            super(R.layout.item_treat_3_text, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MoneyTrade data, int position) {
            View view = holder.getView(R.id.item_view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    AppDevice.dp2px(getContext(), itemHeight));
            view.setLayoutParams(params);

            TextView percentTv = holder.getView(R.id.tv_treat_value);
            percentTv.setTextColor(Color.parseColor(data.getColor()));
            holder.setText(R.id.tv_treat_name, data.getTradeType());
            holder.setText(R.id.tv_treat_price, data.getTradeNum());
            percentTv.setText(data.getTradePer());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleParentTab();
                }
            });
        }
    }
}
