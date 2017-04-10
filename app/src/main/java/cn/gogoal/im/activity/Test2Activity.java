package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.adapter.stock.MyStockMarketAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.MarkteBean;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.DrawableCenterTextView;


public class Test2Activity extends BaseActivity {

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
    TextView tvMystockPrice;

    @BindView(R.id.tv_mystock_rate)
    TextView tvMystockRate;

    @BindView(R.id.item_mystock)
    PercentRelativeLayout itemMystock;

    private List<MarkteBean.MarketItemData> myStockMarketDatas = new ArrayList<>();
    private MyStockMarketAdapter myStockMarketAdapter;

    private List<MyStockBean.DataBean> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;
    private int sortType;

    @Override
    public int bindLayout() {
        return R.layout.activity_test2;
    }

    @Override
    public void doBusiness(final Context mContext) {
        initMarketBanner(mContext);
        initSortTitle(mContext);
        myStockAdapter = new MyStockAdapter(myStockDatas);
        rvMystock.setLayoutManager(new LinearLayoutManager(mContext));
        rvMystock.addItemDecoration(new NormalItemDecoration(mContext));
        rvMystock.setAdapter(myStockAdapter);

        getMyStockData();
        getMarketBanner();
    }

    private void initSortTitle(Context ctx) {
        itemMystock.setPadding(0,AppDevice.dp2px(ctx,5),0,AppDevice.dp2px(ctx,5));
        findViewById(R.id.flag_layout).setVisibility(View.INVISIBLE);
        AppDevice.setViewWidth$Height(itemMystock,-1,AppDevice.dp2px(ctx,40));
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/sort.ttf");
        tvMystockPrice.setTypeface(iconfont);
        tvMystockRate.setTypeface(iconfont);

        setViewStateNormal(tvMystockPrice);
        setViewStateNormal(tvMystockRate);

        sortType=0;
        tvMystockPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType==0) {
                    setViewStateUp(tvMystockPrice);
                    sortType++;
                }else if (sortType==1){
                    setViewStateDown(tvMystockPrice);
                    sortType++;
                }else if (sortType==2){
                    setViewStateNormal(tvMystockPrice);
                    sortType=0;
                }
            }
        });
    }

    /**排序算法*/
    private void doSort(){

    }

    private void setViewStateNormal(TextView textView){
        textView.setText(textView.getId()==R.id.tv_mystock_price?getString(R.string.mystock_sort_price_normal):
        getString(R.string.mystock_sort_rate_normal));
        textView.setTextColor(getResColor(R.color.textColor_333333));
    }
    private void setViewStateUp(TextView textView){
        textView.setText(textView.getId()==R.id.tv_mystock_price?getString(R.string.mystock_sort_price_up):
                getString(R.string.mystock_sort_rate_up));
        textView.setTextColor(getResColor(R.color.colorPrimary));
    }
    private void setViewStateDown(TextView textView){
        textView.setText(textView.getId()==R.id.tv_mystock_price?getString(R.string.mystock_sort_price_down):
                getString(R.string.mystock_sort_rate_down));
        textView.setTextColor(getResColor(R.color.colorPrimary));
    }
    /**
     * 获取自选股数据
     */
    private void getMyStockData() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "200");
        params.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
//                    layoutNoData.setVisibility(View.GONE);
                    List<MyStockBean.DataBean> dataBeen;
                    myStockDatas.addAll(dataBeen = JSONObject.parseObject(responseInfo, MyStockBean.class).getData());
                    myStockAdapter.notifyDataSetChanged();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                } else if (code == 1001) {
//                    layoutNoData.setVisibility(View.VISIBLE);
                } else {
//                    layoutNoData.setVisibility(View.VISIBLE);
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
//                layoutNoData.setVisibility(View.VISIBLE);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    /**
     * 获取大盘数据
     */
    private void getMarketBanner() {
        myStockMarketDatas.clear();
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
        param.put("category_type", "1");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
//                    layoutNoData.setVisibility(View.GONE);
                    StockMarketBean.DataBean marketData = JSONObject.parseObject(responseInfo, StockMarketBean.class).getData();
                    List<StockMarketBean.DataBean.HangqingBean> hangqing = marketData.getHangqing();
                    for (StockMarketBean.DataBean.HangqingBean hangqingBean : hangqing) {
                        myStockMarketDatas.add(new MarkteBean.MarketItemData(
                                hangqingBean.getName(),
                                hangqingBean.getPrice(),
                                hangqingBean.getPrice_change(),
                                hangqingBean.getPrice_change_rate(), 0, null, null,
                                StockUtils.getStockRateColor(hangqingBean.getPrice_change())));
                    }
                    myStockMarketAdapter.notifyDataSetChanged();
                } else if (code == 1001) {
//                    layoutNoData.setVisibility(View.VISIBLE);
                } else {
//                    layoutNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String msg) {
//                layoutNoData.setVisibility(View.VISIBLE);
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    //init
    private void initMarketBanner(Context mContext) {
        myStockMarketAdapter = new MyStockMarketAdapter(myStockMarketDatas);
        flipperBanner.setAdapter(myStockMarketAdapter);
//        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(flipperBanner, "translationY", AppDevice.dp2px(mContext, 70), 0).setDuration(900);
//        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(flipperBanner, "translationY", 0, -AppDevice.dp2px(mContext, 70)).setDuration(900);
//        flipperBanner.setInAnimation(inAnimator);
//        flipperBanner.setOutAnimation(outAnimator);
//        flipperBanner.startFlipping();
    }

    @OnClick({R.id.tv_mystock_news, R.id.tv_mystock_gonggao, R.id.tv_mystock_yanbao
//            ,R.id.layout_mystock_price_head, R.id.layout_mystock_rate_head
    })
    void onClick(View view) {
        switch (view.getId()) {
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
        intent.putExtra("showTabIndex", index);
        startActivity(intent);
    }

    private class MyStockAdapter extends CommonAdapter<MyStockBean.DataBean> {

        private MyStockAdapter(List<MyStockBean.DataBean> datas) {
            super(getActivity(), R.layout.item_my_stock, datas);
        }

        @Override
        protected void convert(ViewHolder holder, MyStockBean.DataBean data, int position) {
            holder.setText(R.id.tv_mystock_stockname, data.getStock_name());
            holder.setText(R.id.tv_mystock_stockcode, data.getStock_code());
            holder.setText(R.id.tv_mystock_price, StringUtils.saveSignificand(data.getPrice(), 2));

            holder.setText(R.id.tv_mystock_rate, StockUtils.plusMinus(data.getChange_rate()));
            holder.setTextColor(R.id.tv_mystock_rate, ContextCompat.getColor(getActivity(),
                    StockUtils.getStockRateColor(data.getChange_rate())));
            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
