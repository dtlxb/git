package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
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
import cn.gogoal.im.activity.stock.EditMyStockActivity;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.adapter.stock.MyStockMarketAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.MyStockSortInteface;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.SortView;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股.
 */
public class MyStockFragment extends BaseFragment implements MyStockSortInteface {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;

    //左上角编辑按钮
    @BindView(R.id.tv_mystock_edit)
    TextView tvMystockEdit;

    @BindView(R.id.img_mystock_refresh)
    ImageView imgMystockRefresh;

    //搜索框视图按钮
    @BindView(R.id.search_market)
    DrawableCenterTextView searchMarket;

    //大盘滚动
    @BindView(R.id.flipper_mystock_banner)
    AdapterViewFlipper flipperBanner;

    //自选股列表
    @BindView(R.id.rv_mystock)
    RecyclerView rvMystock;

    @BindView(R.id.tv_mystock_price)
    SortView tvMystockPrice;

    @BindView(R.id.tv_mystock_rate)
    SortView tvMystockRate;

    @BindView(R.id.item_mystock)
    PercentRelativeLayout itemMystock;

    @BindView(R.id.flag_layout)
    ViewGroup flagLayout;

    private int errorPadding=0;

    private List<StockMarketBean.DataBean.HangqingBean> myStockMarketDatas = new ArrayList<>();
    private MyStockMarketAdapter myStockMarketAdapter;

    private ArrayList<MyStockData> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;
    private RotateAnimation rotateAnimation;
    private static final long INTERVAL_TIME=10000;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my_stock;
    }

    @Override
    public void doBusiness(final Context mContext) {
        BaseActivity.iniRefresh(refreshLayout);

        initMarketBanner(mContext);
        initSortTitle(mContext);
        initRecyclerView(mContext);

        refreshAll(AppConst.REFRESH_TYPE_FIRST);//请求

        handler.postDelayed(runnable,INTERVAL_TIME);

        initXLayout();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {refreshAll(AppConst.REFRESH_TYPE_SWIPEREFRESH);}
        });

        imgMystockRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                getMyStockData(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                getMarketLittle();
            }
        });

    }

    private void refreshAll(int refreshType){
        startAnimation();//刷新按钮动画
        getMyStockData(refreshType);
        getMarketLittle();
        getMarketLittle();
    }

    private void initXLayout() {
        xLayout.setEmptyText(getString(R.string.str_add_my_stock));
        xLayout.setEmptyImage(R.mipmap.img_mystock_no_data);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getMyStockData(AppConst.REFRESH_TYPE_FIRST);
            }
        });

        searchMarket.post(new Runnable() {
            @Override
            public void run() {
                if (searchMarket.getBottom() > 0) {
                    errorPadding=(AppDevice.getHeight(getContext()) - searchMarket.getBottom())/ 3;
                }
            }
        });
    }

    //init
    private void initMarketBanner(Context mContext) {
        myStockMarketAdapter = new MyStockMarketAdapter(getContext(),myStockMarketDatas);
        flipperBanner.setAdapter(myStockMarketAdapter);
//        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(flipperBanner, "translationY", AppDevice.dp2px(mContext, 70), 0).setDuration(900);
//        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(flipperBanner, "translationY", 0, -AppDevice.dp2px(mContext, 70)).setDuration(900);
//        flipperBanner.setInAnimation(inAnimator);
//        flipperBanner.setOutAnimation(outAnimator);
//        flipperBanner.startFlipping();
    }

    private void initSortTitle(Context ctx) {
        itemMystock.setPadding(0, AppDevice.dp2px(ctx, 5), 0, AppDevice.dp2px(ctx, 5));
        flagLayout.setVisibility(View.INVISIBLE);
        AppDevice.setViewWidth$Height(itemMystock, -1, AppDevice.dp2px(ctx, 40));

        iniSortBar();

        tvMystockPrice.seOntSortListener(this);
        tvMystockRate.seOntSortListener(this);

    }

    private void iniSortBar() {
        tvMystockPrice.setDefaultText("最新价");
        tvMystockRate.setDefaultText("涨跌幅");
        tvMystockPrice.setViewStateNormal();
        tvMystockRate.setViewStateNormal();
    }

    private void initRecyclerView(Context mContext) {
        myStockAdapter = new MyStockAdapter(myStockDatas);
        //------------------------
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvMystock.setLayoutManager(layoutManager);
        rvMystock.setHasFixedSize(true);
        rvMystock.setNestedScrollingEnabled(false);
        //------------------------
        rvMystock.setLayoutManager(layoutManager);
        rvMystock.addItemDecoration(new NormalItemDecoration(mContext));
        rvMystock.setAdapter(myStockAdapter);
    }

    @Override
    public void doSort(final View view, final int sortType) {

        KLog.e(sortType);
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
                        getMyStockData(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                        return 0;
                    }
                } else if (view.getId() == R.id.tv_mystock_rate) {
                    tvMystockPrice.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.getStockDouble(o2.getChange_rate()).compareTo(StringUtils.getStockDouble(o1.getChange_rate()));
                    } else if (sortType == 1) {
                        return StringUtils.getStockDouble(o1.getChange_rate()).compareTo(StringUtils.getStockDouble(o2.getChange_rate()));
                    } else {
                        getMyStockData(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                        return 0;
                    }
                }
                return 0;
            }
        });

        myStockAdapter.notifyDataSetChanged();
    }

    /**
     * 获取自选股数据
     */
    private void getMyStockData(final int loadType) {
        iniSortBar();
        if (loadType == AppConst.REFRESH_TYPE_FIRST) {
            xLayout.setStatus(XLayout.Loading);
        }
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "200");
        params.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {

                    xLayout.setPadding(0,0,0,0);
                    myStockDatas.clear();
                    myStockDatas.addAll(JSONObject.parseObject(responseInfo, MyStockBean.class).getData());
                    myStockAdapter.notifyDataSetChanged();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    xLayout.setStatus(XLayout.Success);
                    refreshLayout.setRefreshing(false);
                    stopAnimation();
                    if (loadType != AppConst.REFRESH_TYPE_FIRST) {
                        UIHelper.toast(getActivity(), getString(R.string.str_refresh_ok));
                    }

                } else if (code == 1001) {
                    xLayout.setPadding(0,errorPadding,0,0);
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                    xLayout.setPadding(0,0,0,0);
                    refreshLayout.setRefreshing(false);
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                xLayout.setPadding(0,errorPadding,0,0);
                refreshLayout.setRefreshing(false);
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    /***/
    private void getMarketLittle() {
        myStockMarketDatas.clear();
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000300");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    String hanhqing = JSONObject.parseObject(responseInfo).getJSONArray("data").get(0).toString();
                    myStockMarketDatas.add(JSONObject.parseObject(hanhqing, StockMarketBean.DataBean.HangqingBean.class));
                }
                myStockMarketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_GET, ggHttpInterface).startGet();
    }

    @OnClick({R.id.tv_mystock_edit, R.id.tv_mystock_news, R.id.tv_mystock_gonggao,
            R.id.tv_mystock_yanbao})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tv_mystock_edit:
                Intent intent = new Intent(view.getContext(), EditMyStockActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("my_stock_edit", myStockDatas);
                intent.putExtras(bundle);
                startActivityForResult(intent, 7);
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
        intent.putExtra("news_title",getString(R.string.title_mtstock_news));
        intent.putExtra("showTabIndex", index);
        startActivity(intent);
    }

    private void stopAnimation() {
        if (rotateAnimation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, imgMystockRefresh, R.mipmap.img_refresh);
        } else {
            imgMystockRefresh.clearAnimation();
            imgMystockRefresh.setImageResource(R.mipmap.img_refresh);
        }
    }

    private void startAnimation() {
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(imgMystockRefresh, R.mipmap.img_loading_refresh);
        rotateAnimation.startNow();
    }

    private class MyStockAdapter extends CommonAdapter<MyStockData,BaseViewHolder> {

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

            if (data.getStock_type()==1) {
                rateView.setText(StockUtils.plusMinus(data.getChange_rate(),true));
                rateView.setTextColor(ContextCompat.getColor(getActivity(),
                        StockUtils.getStockRateColor(data.getChange_rate())));
            }else {
                rateView.setText(StockUtils.getStockStatus(data.getStock_type()));
                rateView.setTextColor(getResColor(R.color.stock_gray));
            }

            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StockUtils.go2StockDetail(v.getContext(),
                            data.getStock_code(),data.getStock_name());
                }
            });
        }
    }

    //定时刷新
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, INTERVAL_TIME);
                refreshAll(AppConst.REFRESH_TYPE_AUTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        handler.postDelayed(runnable,INTERVAL_TIME);
    };

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && data!=null) {
            List<MyStockData> myStockdata = (List<MyStockData>) data.getSerializableExtra("my_stock_edit");
            myStockDatas.clear();
            myStockDatas.addAll(myStockdata);
            myStockAdapter.notifyDataSetChanged();
        }
    }
}

