package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
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

    //自选股集合
    private ArrayList<MyStockData> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my_stock;
    }

    @Override
    public void doBusiness(Context mContext) {
        initSortTitle(mContext);

        iniMyStockList();

        getMyStockData(AppConst.REFRESH_TYPE_FIRST);
    }

    private void iniMyStockList() {
        myStockAdapter=new MyStockAdapter(myStockDatas);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        rvMyStock.setLayoutManager(layoutManager);
        rvMyStock.addItemDecoration(new NormalItemDecoration(getContext()));

        rvMyStock.setItemAnimator(new DefaultItemAnimator());
        rvMyStock.setAdapter(myStockAdapter);
    }

    /**
     * 切换指数缩略
     */
    public void changeIitem(final MyStockMarketBean.MyStockMarketData data) {
        tvTinyMarketName.setText(data.getName());
        tvTinyMarketPrice.setText(StringUtils.saveSignificand(data.getPrice(), 2));
        tvTinyMarketpriceChange$Rate.setText(StockUtils.plusMinus(String.valueOf(data.getPrice_change()) + "", false) + "  " +
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
                KLog.e(responseInfo);

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    myStockDatas.clear();
                    List<MyStockData> parseData = JSONObject.parseObject(responseInfo, MyStockBean.class).getData();
                    for (MyStockData data : parseData) {
                        if (data.getStock_type() == 1) {
                            myStockDatas.add(data);
                        }
                    }
                    myStockAdapter.notifyDataSetChanged();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    if (loadType == AppConst.REFRESH_TYPE_SWIPEREFRESH || loadType == AppConst.REFRESH_TYPE_PARENT_BUTTON) {
                        UIHelper.toast(getActivity(), getString(R.string.str_refresh_ok));
                    }

                } else if (code == 1001) {
                    //没有数据
                } else {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    @OnClick({R.id.img_show_tinymarket_dialog, R.id.tv_mystock_news,
            R.id.tv_mystock_gonggao, R.id.tv_mystock_yanbao})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tv_mystock_edit:
                /*Intent intent = new Intent(view.getContext(), EditMyStockActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("my_stock_edit", myStockDatas);
                intent.putExtras(bundle);
                startActivityForResult(intent, 7);*/
                // TODO: 2017/4/27 0027 编辑自选股，传自选股集合
                break;

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

    private void setAppBarLayout(AppBarLayout appBarLayout,boolean scroll){
        LinearLayout layout= (LinearLayout) appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams)
                layout.getLayoutParams();
        if (!scroll) {
            mParams.setScrollFlags(0);
            appBarLayout.setExpanded(true);
        }else {
            mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        layout.setLayoutParams(mParams);
    }

    @Override
    public void doSort(final View view, final int sortType) {
        ArrayList<MyStockData> cloneData= (ArrayList<MyStockData>) myStockDatas.clone();
        Collections.sort(myStockDatas, new Comparator<MyStockData>() {
            @Override
            public int compare(MyStockData o1, MyStockData o2) {
                if (view.getId() == R.id.tv_mystock_price) {
                    tvMystockRate.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.getStockDouble(o2.getPrice()).compareTo(StringUtils.getStockDouble(o1.getPrice()));
                    } else if (sortType == 1) {
                        return StringUtils.getStockDouble(o1.getPrice()).compareTo(StringUtils.getStockDouble(o2.getPrice()));
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
                        return StringUtils.getStockDouble(o2.getChange_rate()).compareTo(StringUtils.getStockDouble(o1.getChange_rate()));
                    } else if (sortType == 1) {
                        return StringUtils.getStockDouble(o1.getChange_rate()).compareTo(StringUtils.getStockDouble(o2.getChange_rate()));
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
                    NormalIntentUtils.go2StockDetail(v.getContext(),
                            data.getStock_code(), data.getStock_name());
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
                                    StockUtils.reqDelStock(getContext(),
                                            data.getStock_name(),
                                            data.getSource() + data.getStock_code(),
                                            new StockUtils.ToggleMyStockCallBack() {
                                                @Override
                                                public void success() {
                                                    MyStockAdapter.this.removeItem(data);
                                                }

                                                @Override
                                                public void failed(String msg) {
                                                    //TODO 本来应该成功返回code==0才刷新列表，
                                                    // TODO 现在TM的返回的是1001 但是删除成功
                                                    MyStockAdapter.this.removeItem(data);
//                                                    UIHelper.toast(getActivity(), msg);
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
