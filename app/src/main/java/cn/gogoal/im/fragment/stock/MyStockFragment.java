package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.activity.stock.EditMyStockActivity;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.bean.stock.MyStockMarketBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
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

/**
 * author wangjd on 2017/4/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockFragment extends BaseFragment implements MyStockSortInteface {

    //缩略行情——指数名
    @BindView(R.id.tv_tiny_mystock_market_name)
    TextView tvTinyMarketName;

    //缩略行情——指数价格
    @BindView(R.id.tv_tiny_mystock_market_price)
    TextView tvTinyMarketPrice;

    //缩略行情——指数价格涨幅和变换率
    @BindView(R.id.tv_tiny_mystock_market_price_change$rate)
    TextView tvTinyMarketpriceChange$Rate;

    //排序头，为了对齐，使用item的引用布局
    @BindView(R.id.item_mystock)
    PercentRelativeLayout itemMystock;

    //按价格排序按钮
    @BindView(R.id.tv_mystock_price)
    SortView tvMystockPrice;

    //按涨跌幅排序按钮
    @BindView(R.id.tv_mystock_rate)
    SortView tvMystockRate;

    @BindView(R.id.tv_mystock_stockcode)
    View flagLayout;

    @BindView(R.id.tv_mystock_stockname)
    TextView sortTitleName;

    @BindView(R.id.rv_mystock)
    RecyclerView rvMyStock;

    @BindView(R.id.layout_tiny_market)
    LinearLayout layoutTinyMarket;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    //自选股集合
    private ArrayList<MyStockData> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my_stock;
    }

    @Override
    public void doBusiness(Context mContext) {

        BaseActivity.iniRefresh(refreshLayout);

        initSortTitle(mContext);

        iniMyStockList();

        refreshMyStock(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新自选股，大盘缩略
                refreshMyStock(AppConst.REFRESH_TYPE_SWIPEREFRESH);

                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshMyStock(AppConst.REFRESH_TYPE_FIRST);
        } catch (Exception e) {
            KLog.e(e.getMessage());
        }
    }

    public void refreshMyStock(int refreshType) {
        AppManager.getInstance().sendMessage("market_start_animation_refresh");
        getMyStockData(refreshType);
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
     * 切换指数缩略
     */
    public void changeIitem(final MyStockMarketBean.MyStockMarketData data) {
        if (data == null) {
            return;
        }

        tvTinyMarketName.setText(data.getName());
        tvTinyMarketPrice.setText(StringUtils.saveSignificand(data.getPrice(), 2));
        tvTinyMarketpriceChange$Rate.setText(StockUtils.plusMinus(data.getPrice_change() + "", false) + "  " +
                StockUtils.plusMinus(String.valueOf(data.getPrice_change_rate()), true));

        tvTinyMarketPrice.setTextColor(ContextCompat.getColor(getContext(),
                StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate()))));
        tvTinyMarketpriceChange$Rate.setTextColor(ContextCompat.getColor(getContext(),
                StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate()))));

        layoutTinyMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StockDetailMarketIndexActivity.class);
                intent.putExtra("stockName", data.getName());
                intent.putExtra("stockCode", data.getFullcode());
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化排序头
     */
    private void initSortTitle(Context ctx) {
        itemMystock.setPadding(0, AppDevice.dp2px(ctx, 5), 0, AppDevice.dp2px(ctx, 5));
        flagLayout.setVisibility(View.GONE);
        sortTitleName.setText("全部");
        AppDevice.setViewWidth$Height(itemMystock, -1, AppDevice.dp2px(ctx, 40));

        tvMystockPrice.setDefaultText("最新价");
        tvMystockRate.setDefaultText("涨跌幅");
        tvMystockPrice.setViewStateNormal();
        tvMystockRate.setViewStateNormal();

        tvMystockPrice.seOntSortListener(this);
        tvMystockRate.seOntSortListener(this);

    }

    private void getMyStockData(final int loadType) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "200");
        params.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    noData(false);
                    myStockDatas.clear();

                    editEnable(true);

                    List<MyStockData> parseData = JSONObject.parseObject(responseInfo, MyStockBean.class).getData();

                    myStockDatas.addAll(parseData);

                    myStockAdapter.notifyDataSetChanged();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    if (loadType == AppConst.REFRESH_TYPE_SWIPEREFRESH || loadType == AppConst.REFRESH_TYPE_PARENT_BUTTON) {
                        UIHelper.toast(getActivity(), getString(R.string.str_refresh_ok));
                    }

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
            final TextView tvEditMyStock = ((MainStockFragment) getParentFragment()).getTvMystockEdit();
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
            tvEditMyStock.setTextColor(enable ? Color.BLACK : Color.GRAY);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @OnClick({R.id.img_show_tinymarket_dialog, R.id.tv_mystock_news,
            R.id.tv_mystock_gonggao, R.id.tv_mystock_yanbao})
    void click(View view) {
        switch (view.getId()) {
            case R.id.img_show_tinymarket_dialog:
                ((MainStockFragment) getParentFragment()).showMarketDialog();
                break;

            case R.id.tv_mystock_news:
                intentNews(0);
                break;
            case R.id.tv_mystock_gonggao:
                intentNews(1);
                break;
            case R.id.tv_mystock_yanbao:
                intentNews(2);
                break;
        }
    }

    private void intentNews(int index) {
        Intent intent = new Intent(getActivity(), MyStockNewsActivity.class);
        intent.putExtra("news_title", getString(R.string.title_mtstock_news));
        intent.putExtra("showTabIndex", index);
        startActivity(intent);
    }

    void noData(boolean nadata) {
        editEnable(!nadata);
        try {
            itemMystock.setVisibility(nadata ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.getMessage();
        }
        refreshLayout.setEnabled(!nadata);
        setAppBarLayout(appBarLayout, !nadata);

        if (nadata) {
            myStockAdapter.setEmptyView(R.layout.layout_no_mystock,
                    (ViewGroup) rvMyStock.getParent());
            myStockAdapter.getEmptyView().findViewById(R.id.flag_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), StockSearchActivity.class));
                }
            });
        }
    }

    private void setAppBarLayout(AppBarLayout appBarLayout, boolean scroll) {
        LinearLayout layout = (LinearLayout) appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams)
                layout.getLayoutParams();
        if (!scroll) {
            mParams.setScrollFlags(0);
            appBarLayout.setExpanded(true);
        } else {
            mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        layout.setLayoutParams(mParams);
    }

    @Override
    public void doSort(final View view, final int sortType) {
        ArrayList<MyStockData> cloneData = (ArrayList<MyStockData>) myStockDatas.clone();
        Collections.sort(myStockDatas, new Comparator<MyStockData>() {
            @Override
            public int compare(MyStockData o1, MyStockData o2) {
                if (view.getId() == R.id.tv_mystock_price) {
                    tvMystockRate.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.pareseStringDouble(o2.getPrice()).compareTo(StringUtils.pareseStringDouble(o1.getPrice()));
                    } else if (sortType == 1) {
                        return StringUtils.pareseStringDouble(o1.getPrice()).compareTo(StringUtils.pareseStringDouble(o2.getPrice()));
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
                    if (sortType == -1) {
                        return StringUtils.pareseStringDouble(o2.getChange_rate()).compareTo(StringUtils.pareseStringDouble(o1.getChange_rate()));
                    } else if (sortType == 1) {
                        return StringUtils.pareseStringDouble(o1.getChange_rate()).compareTo(StringUtils.pareseStringDouble(o2.getChange_rate()));
                    } else {
                        try {
                            return Long.compare(CalendarUtils.parseString2Long(o2.getInsertdate()), CalendarUtils.parseString2Long(o1.getInsertdate()));
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

    /**
     * 自选股适配器
     */
    private class MyStockAdapter extends CommonAdapter<MyStockData, BaseViewHolder> {

        private MyStockAdapter(List<MyStockData> datas) {
            super(R.layout.item_my_stock, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockData data, int position) {
            holder.setText(R.id.tv_mystock_stockname, data.getStock_name());
            holder.setText(R.id.tv_mystock_stockcode, data.getStock_code());

            TextView rateView = holder.getView(R.id.tv_mystock_rate);
            TextView priceView = holder.getView(R.id.tv_mystock_price);

            rateView.setClickable(false);
            priceView.setClickable(false);

            priceView.setText(StringUtils.saveSignificand(data.getPrice(), 2));

            //自作主张
            TextView typeView = holder.getView(R.id.tv_stock_type);
            switch (data.getSymbol_type()) {
                case 1:
                    typeView.setVisibility(View.GONE);
                    break;
                case 2:
                    typeView.setVisibility(View.VISIBLE);
                    typeView.setText("指数");
                    break;
                case 3:
                    typeView.setVisibility(View.VISIBLE);
                    typeView.setText("基金");
                    break;
                case 4:
                    typeView.setVisibility(View.VISIBLE);
                    typeView.setText("债券");
                    break;
                default:
                    typeView.setVisibility(View.GONE);
                    break;
            }

            if (data.getStock_type() == 1) {
                rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                priceView.setTextColor(ContextCompat.getColor(getActivity(),
                        StockUtils.getStockRateColor(data.getChange_rate())));
                rateView.setTextColor(ContextCompat.getColor(getActivity(),
                        StockUtils.getStockRateColor(data.getChange_rate())));
            } else {
                rateView.setText(StockUtils.getStockStatus(data.getStock_type()));
                rateView.setTextColor(getResColor(R.color.stock_gray));
            }

            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getSymbol_type() == 1) {
                        NormalIntentUtils.go2StockDetail(v.getContext(),
                                data.getStock_code(), data.getStock_name());
                    } else {
                        WaitDialog waitDialog = WaitDialog.getInstance("暂无 " +
                                        StockUtils.getSympolType(data.getSymbol_type()) + " 详情页面",
                                R.mipmap.login_error, false);
                        waitDialog.show(getActivity().getSupportFragmentManager());
                        waitDialog.dismiss(false);
                    }
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
                                    StockUtils.deleteMyStock(
                                            getContext(),
                                            data.getSource() + data.getStock_code(),//使用股票代码+股票source
                                            new StockUtils.ToggleMyStockCallBack() {
                                                @Override
                                                public void success() {
                                                    MyStockAdapter.this.removeItem(data);
                                                    try {
                                                        new android.os.Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                myStockAdapter.notifyDataSetChanged();
                                                            }
                                                        }, 1000);
                                                    } catch (Exception e) {
                                                        e.getMessage();
                                                    }
                                                }

                                                @Override
                                                public void failed(String msg) {
                                                    UIHelper.toast(getActivity(), msg);
                                                }
                                            });
                                }
                            }).show();
                    return true;
                }
            });
        }
    }
}
