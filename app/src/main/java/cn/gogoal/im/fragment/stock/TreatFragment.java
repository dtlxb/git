package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.ThreeText;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.fragment.copy.TimesFragment;
import cn.gogoal.im.ui.XDividerItemDecoration;
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

    //===================五档====================
    private WudangAdapter wudangAdapter;
    private List<ThreeText> threeTexts;
    private double closePrice;

    //===================明细=====================
    private MingxiAdapter mingxiAdapter;
    private List<TimeDetialData> timeDetialDatas;
    private boolean fromStockDetail;
    private int type;

    //===================资金=====================
    private MoneyAdapter moneyAdapter;
    /*private List<TimeDetialData> timeDetialDatas;
    private boolean fromStockDetail;
    private int type;*/

    private float itemHeight;
    private String stockCode;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
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
            fragment.itemHeight = fromStockDetail ? 17f : 24.5f;
        } else {
            fragment.itemHeight = 22;
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

        if (stockCode != null) {
            if (type == AppConst.TREAT_TYPE_WU_DANG) {
                threeTexts = new ArrayList<>();
                wudangAdapter = new WudangAdapter(threeTexts);
                recyclerView.setAdapter(wudangAdapter);
                recyclerView.addItemDecoration(new WudangDivider(getResColor(R.color.chart_text_color)));
                getTreatWudang();
            } else if (type == AppConst.TREAT_TYPE_MING_XI) {
                timeDetialDatas = new ArrayList<>();
                mingxiAdapter = new MingxiAdapter(timeDetialDatas);
                recyclerView.setAdapter(mingxiAdapter);
                getStockTimeDetail();
            }
        }

        xLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleParentTab();
            }
        });
    }

    @Subscriber(tag = "updata_treat_data")
    void updataTreatData(String msg) {
        if (type == AppConst.TREAT_TYPE_WU_DANG) {
            getTreatWudang();
        } else if (type == AppConst.TREAT_TYPE_MING_XI) {
            getStockTimeDetail();
        }
    }

    private void toggleParentTab() {
        if (fromStockDetail) {
            ((CopyStockDetailActivity) getActivity()).toggleTreatMode();
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


                    wudangAdapter.notifyDataSetChanged();

                    closePrice = StringUtils.parseStringDouble(treatData.getClose_price());
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();
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
                    timeDetialDatas.clear();
                    List<TimeDetialData> cacheData =
                            JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData();
                    if (fromStockDetail && cacheData.size() >= 8) {
                        timeDetialDatas.addAll(cacheData.subList(0, 8));
                    } else {
                        timeDetialDatas.addAll(cacheData);
                    }
                    mingxiAdapter.notifyDataSetChanged();
                }
            }

            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_TIME_DETIAL, ggHttpInterface).startGet();
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


    private class MoneyAdapter {
    }
}
