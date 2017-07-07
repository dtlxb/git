package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.CompanyTagActivity;
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.activity.stock.EditMyStockActivity;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.bean.stock.StockTag;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.MyStockSortInteface;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.main.MainStockFragment;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SortView;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/4/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :自选股
 */
public class MyStockFragment extends BaseFragment implements MyStockSortInteface {

    //排序头，为了对齐，使用item的引用布局
    @BindView(R.id.item_mystock)
    ViewGroup itemMystock;

    //按价格排序按钮
    @BindView(R.id.tv_mystock_price)
    SortView tvMystockPrice;

    //按价格排序按钮
    @BindView(R.id.tv_mystock_rag)
    SortView tvMystockRag;

    //按涨跌幅排序按钮
    @BindView(R.id.tv_mystock_rate)
    SortView tvMystockRate;

    @BindView(R.id.rv_mystock)
    RecyclerView rvMyStock;

    @BindView(R.id.swipe_refresh_layout)
    RefreshLayout refreshLayout;

    //自选股集合
    private ArrayList<MyStockData> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my_stock;
    }

    @Override
    public void doBusiness(Context mContext) {

        initSortTitle();

        iniMyStockList();

        refreshMyStock(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新自选股，大盘缩略
                refreshMyStock(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.refreshComplete();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return ((LinearLayoutManager) rvMyStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0 ;
                return !rvMyStock.canScrollVertically(-1);
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshMyStock(AppConst.REFRESH_TYPE_RESUME);
        } catch (Exception e) {
            KLog.e(e.getMessage());
        }

    }

    public void refreshMyStock(int refreshType) {
        AppManager.getInstance().sendMessage("market_start_animation_refresh");
        getMyStockData(refreshType);
    }

    @OnClick({R.id.btn_news, R.id.btn_gonggao, R.id.btn_yanbao})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btn_news:
                intentNews(0);
                break;
            case R.id.btn_gonggao:
                intentNews(1);
                break;
            case R.id.btn_yanbao:
                intentNews(2);
                break;
        }
    }

    private void intentNews(int index) {
        Intent intent = new Intent(getActivity(), MyStockNewsActivity.class);
        intent.putExtra("showTabIndex", index);
        startActivity(intent);
    }

    private void iniMyStockList() {
        myStockAdapter = new MyStockAdapter(myStockDatas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMyStock.setLayoutManager(layoutManager);
        rvMyStock.addItemDecoration(new NormalItemDecoration(getContext()));

        rvMyStock.setItemAnimator(new DefaultItemAnimator());
        rvMyStock.setAdapter(myStockAdapter);
    }

    /**
     * 初始化排序头
     */
    private void initSortTitle() {
        tvMystockPrice.setDefaultText("最新价");
        tvMystockPrice.setViewStateNormal();
        tvMystockPrice.seOntSortListener(this);

        tvMystockRate.setDefaultText("涨跌幅");
        tvMystockRate.setViewStateNormal();
        tvMystockRate.seOntSortListener(this);

        tvMystockRag.setDefaultText("公司荣誉");
        tvMystockRag.setViewStateNormal();
        tvMystockRag.seOntSortListener(this);

    }

    private void getMyStockData(final int loadType) {

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "500");
        params.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {

                    initSortTitle();

                    noData(false);

                    myStockDatas.clear();

                    editEnable(true);

                    final ArrayList<MyStockData> parseData = JSONObject.parseObject(responseInfo, MyStockBean.class).getData();

                    String stockCodes =
                            StockUtils.getStockCodes(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("codes", stockCodes);

                    new GGOKHTTP(map, GGOKHTTP.GET_STOCK_TAG, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            if (JsonUtils.getIntValue(responseInfo, "code") == 0) {
                                for (MyStockData data : parseData) {
                                    StockTag tag = new StockTag();
                                    JSONObject objectTag = JSONObject.parseObject(responseInfo).getJSONObject("data");
                                    if (data.getStock_code() != null && objectTag.getString(data.getStock_code()) != null) {
                                        tag.setType(
                                                StringUtils.parseStringDouble(
                                                        objectTag.getString(data.getStock_code())).intValue());
                                    } else {
                                        tag.setType(-2);
                                    }
                                    data.setTag(tag);
                                    if (!myStockDatas.contains(data)) {
                                        myStockDatas.add(data);
                                    }
                                }
                            }
                            myStockAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String msg) {
                            KLog.e(msg);
                        }
                    }).startGet();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                } else if (code == 1001) {
                    myStockDatas.clear();
                    StockUtils.clearLocalMyStock();
                    myStockAdapter.notifyDataSetChanged();
                    noData(true);
                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                    editEnable(false);
                }

                AppManager.getInstance().sendMessage("market_stop_animation_refresh");
            }

            @Override
            public void onFailure(String msg) {
                AppManager.getInstance().sendMessage("market_stop_animation_refresh");
                UIHelper.toastError(getActivity(), msg);
                editEnable(false);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    /**
     * 没有正确加载自选股列表的时候(没网络，请求出错，或者没有自选股)时，不允许编辑
     */
    void editEnable(boolean enable) {
        try {
            final ImageView tvEditMyStock = ((MainStockFragment) getParentFragment()).getTvMystockEdit();
            tvEditMyStock.setEnabled(enable);
            tvEditMyStock.setClickable(enable);
            tvEditMyStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditMyStockActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("my_stock_edit_list", myStockDatas);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    void noData(boolean noData) {
        editEnable(!noData);
        try {
            itemMystock.setVisibility(noData ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            refreshLayout.setEnabled(!noData);
        } catch (Exception e) {
            e.getMessage();
        }

        if (noData && rvMyStock != null) {
            myStockAdapter.setEmptyView(R.layout.layout_no_mystock, new LinearLayout(getContext()));

            myStockAdapter.getEmptyView().findViewById(R.id.flag_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), StockSearchActivity.class));
                }
            });
        }
    }

    @Override
    public void doSort(final View view, final int sortType) {
        Collections.sort(myStockDatas, new Comparator<MyStockData>() {
            @Override
            public int compare(MyStockData o1, MyStockData o2) {
                if (view.getId() == R.id.tv_mystock_price) {
                    tvMystockRate.setViewStateNormal();
                    tvMystockRag.setViewStateNormal();
                    if (sortType == -1) {
                        return Double.compare(o2.getPrice(), o1.getPrice());
                    } else if (sortType == 1) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    } else {
                        try {
                            return Long.compare(CalendarUtils.parseString2Long(o2.getInsertdate()), CalendarUtils.parseString2Long(o1.getInsertdate()));
                        } catch (Exception e) {
                            getMyStockData(AppConst.REFRESH_TYPE_FIRST);
                            return 0;
                        }
                    }
                } else if (view.getId() == R.id.tv_mystock_rate) {
                    tvMystockPrice.setViewStateNormal();
                    tvMystockRag.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.parseStringDouble(o2.getChange_rate()).compareTo(StringUtils.parseStringDouble(o1.getChange_rate()));
                    } else if (sortType == 1) {
                        return StringUtils.parseStringDouble(o1.getChange_rate()).compareTo(StringUtils.parseStringDouble(o2.getChange_rate()));
                    } else {
                        try {
                            return Long.compare(CalendarUtils.parseString2Long(o2.getInsertdate()), CalendarUtils.parseString2Long(o1.getInsertdate()));
                        } catch (Exception e) {
                            getMyStockData(AppConst.REFRESH_TYPE_FIRST);
                            return 0;
                        }
                    }
                }else if (view.getId() == R.id.tv_mystock_rag) {
                    tvMystockPrice.setViewStateNormal();
                    tvMystockRate.setViewStateNormal();
                    if (sortType == -1) {
                        return compareLong(o2.getTag().getType(),o1.getTag().getType());
                    } else if (sortType == 1) {
                        return compareLong(o1.getTag().getType(),o2.getTag().getType());
                    } else {
                        try {
                            return Long.compare(CalendarUtils.parseString2Long(o2.getInsertdate()),
                                    CalendarUtils.parseString2Long(o1.getInsertdate()));
                        } catch (Exception e) {
                            getMyStockData(AppConst.REFRESH_TYPE_FIRST);
                            return 0;
                        }
                    }
                }
                return 0;
            }
        });
        myStockAdapter.notifyDataSetChanged();
    }

    public static int compareLong(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * 自选股适配器
     */
    private class MyStockAdapter extends CommonAdapter<MyStockData, BaseViewHolder> {

        private MyStockAdapter(List<MyStockData> datas) {
            super(R.layout.item_stock_rank_list, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockData data, final int position) {
            holder.setText(R.id.tv_mystock_stockname, data.getStock_name());
            holder.setText(R.id.tv_mystock_stockcode, data.getStock_code());

            TextView rateView = holder.getView(R.id.tv_mystock_rate);
            rateView.setClickable(false);

            TextView priceView = holder.getView(R.id.tv_mystock_price);

            priceView.setClickable(false);

            priceView.setText(StringUtils.saveSignificand(data.getPrice(), 2));

            //标签
            holder.setVisible(R.id.layout_stock_tag, true);
            if (data.getTag() == null) {
                holder.setText(R.id.tv_mystock_tag, "未鉴定");
            } else {
                setStockTag((TextView) holder.getView(R.id.tv_mystock_tag), data.getTag().getType());
//                holder.setText(R.id.tv_mystock_tag, data.getTag().getName());
            }

            //自作主张
            TextView typeView = holder.getView(R.id.tv_stock_type);

            if (data.getSymbol_type() == 1 || data.getSymbol_type() == 2) {//是否是股票、指数

                if (data.getSource().equalsIgnoreCase("hk")) {//是不是港股
                    typeView.setVisibility(View.VISIBLE);
                    typeView.setText("港股");
                    rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                    rateView.setBackgroundResource(R.drawable.shape_my_stock_price_gray);
                } else {
                    typeView.setText("指数");
                    typeView.setVisibility(data.getSymbol_type() == 2 ? View.VISIBLE : View.GONE);
                    rateView.setBackgroundResource(StockUtils.getStockRateBackgroundRes(data.getChange_rate()));

                    if (data.getSymbol_type() == 1) {
                        if (data.getStock_type() == 1) {//是否停牌退市啥的
                            rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                        } else {
                            rateView.setText(StockUtils.getStockStatus(data.getStock_type()));
                        }
                    } else {
                        rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                    }

                    priceView.setTextColor(ContextCompat.getColor(getActivity(),
                            StockUtils.getStockRateColor(data.getChange_rate())));
                    rateView.setTextColor(Color.WHITE);
                }
            } else {
                rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                typeView.setVisibility(View.VISIBLE);
                typeView.setText(StockUtils.getSympolType(data.getSymbol_type()));
                rateView.setBackgroundResource(R.drawable.shape_my_stock_price_gray);
            }

            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getSymbol_type() == 1) {//股票类型
                        if (!data.getSource().equalsIgnoreCase("hk")) {
                            NormalIntentUtils.go2StockDetail(v.getContext(),
                                    data.getStock_code(), data.getStock_name());
                        } else {
                            WaitDialog waitDialog = WaitDialog.getInstance("暂无 港股 详情页面",
                                    R.mipmap.login_error, false);
                            waitDialog.show(getActivity().getSupportFragmentManager());
                            waitDialog.dismiss(false);
                        }
                    } else if (data.getSymbol_type() == 2) {//指数类型
                        Intent intent = new Intent(getContext(), StockDetailMarketIndexActivity.class);
                        intent.putExtra("stockName", data.getStock_name());
//                        intent.putExtra("stockSource", data.getSource());
                        intent.putExtra("stockCode", data.getSource() + data.getStock_code());
                        startActivity(intent);

                    } else {//基金、债券、其他类型
                        WaitDialog waitDialog = WaitDialog.getInstance("暂无 " +
                                        StockUtils.getSympolType(data.getSymbol_type()) + " 详情页面",
                                R.mipmap.login_error, false);
                        waitDialog.show(getActivity().getSupportFragmentManager());
                        waitDialog.dismiss(false);
                    }
                }
            });

            holder.getView(R.id.layout_stock_tag).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CompanyTagActivity.class);
                    intent.putExtra("stock_code", data.getStock_code());
                    intent.putExtra("stock_name", data.getStock_name());
                    startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(v.getContext()).setMessage(
                            "删除自选股 " + data.getStock_name() + "(" + data.getStock_code() + ")?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StockUtils.deleteMyStock(getContext(),
                                            data.getSource() + data.getStock_code(),//使用股票代码+股票source
                                            new Impl<String>() {
                                                @Override
                                                public void response(int code, String resData) {
                                                    if (code == Impl.RESPON_DATA_SUCCESS) {
                                                        MyStockAdapter.this.removeItem(data);
                                                        MyStockAdapter.this.notifyItemRangeChanged(
                                                                position, getData().size() - position - 1
                                                        );
                                                    }
                                                }
                                            });
                                }
                            }).show();
                    return true;
                }
            });
        }

        private void setStockTag(TextView view, int type) {
            switch (type) {
                case -2:
                    view.setText("未鉴定");
                    view.setTextColor(getResColor(R.color.textColor_999999));
                    view.setBackgroundResource(R.drawable.shape_stock_tag_no_appraisal);
                    break;
                case -1:
                    view.setText("平凡");
                    view.setTextColor(getResColor(R.color.textColor_999999));
                    view.setBackgroundResource(R.drawable.shape_stock_tag_no_appraisal);
                    break;
                case 0:
                    view.setText("好公司");//历史好公司
                    view.setTextColor(getResColor(R.color.textColor_999999));
                    view.setBackgroundResource(R.drawable.shape_stock_tag_no_appraisal);
                    break;
                case 1:
                    view.setText("好公司");
                    view.setTextColor(getResColor(R.color.colorPrimary));
                    view.setBackgroundResource(R.drawable.shape_stock_tag_good_company);
                    break;
                case 2:
                    view.setText("希望之星");
                    view.setTextColor(0xFFFFA200);
                    view.setBackgroundResource(R.drawable.shape_stock_tag_good_in_future);
                    break;
            }
        }
    }
}
