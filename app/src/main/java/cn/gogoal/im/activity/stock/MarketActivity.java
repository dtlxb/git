package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hply.imagepicker.view.StatusBarUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.BaseBeanObject;
import cn.gogoal.im.bean.HangQing;
import cn.gogoal.im.bean.HotIndustrylist;
import cn.gogoal.im.bean.MarketData;
import cn.gogoal.im.bean.StockBannerData;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.ClickUtils;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/3/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 行情
 */
public class MarketActivity extends BaseActivity {

//    大盘
    @BindView(R.id.rv_market)
    RecyclerView rvMarket;

//    广告
    @BindView(R.id.flipper_ad)
    ViewFlipper flipperAd;

//    热门行业
    @BindView(R.id.rv_hot)
    RecyclerView rvHotIndustry;

//    涨、跌、振、换列表
    @BindView(R.id.rv_sort_list)
    RecyclerView rvSortList;

    @Override
    public int bindLayout() {
        return R.layout.activity_market;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();
        getMarketInformation();
        getMarketAd();

        AppDevice.get
    }

    //初始化标题
    private void iniTitle() {
        StatusBarUtil.with(this).setColor(ContextCompat.getColor(this, R.color.colorAccent));
        XTitle title = setMyTitle(R.string.title_stock_market, true)
                .setTitleColor(Color.WHITE)
                .setLeftTextColor(Color.WHITE)
                .setLeftImageResource(R.mipmap.image_title_back_255);
        title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        //添加action

        XTitle.ImageAction action = new XTitle.ImageAction(ContextCompat.getDrawable(getContext(), R.mipmap.refresh_white)) {
            @Override
            public void actionClick(View view) {
                AnimationUtils.getInstance().setLoadingAnime((ImageView) view, R.mipmap.loading_fresh);
                UIHelper.toast(view.getContext(), "刷新");
            }
        };

        title.addAction(action, 0);

        title.getViewByTitle().setOnClickListener(new ClickUtils(new ClickUtils.OnSuperClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(getContext(), "single click");
            }

            @Override
            public void onDoubleClick(View v) {
                UIHelper.toast(getContext(), "double click");
            }
        }));
    }

    /**获取[大盘]、[热门行业]、[涨跌振换]列表*/
    private void getMarketInformation() {
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
        param.put("category_type", "1");
//        if (!isRefresh) {
//            String oldValue = PersistTool.getString("stockData", null);
//            if (oldValue != null) {
//                InformationBean bean = JSONObject.parseObject(oldValue, InformationBean.class);
//                parseMarketBean(bean);
//            }
//        }
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                FileUtil.writeSDcard(responseInfo,"大盘行情_"+ CalendarUtils.getCurrentTime("yyyyMMddHHmmss")+".txt");
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    BaseBeanObject<MarketData> marketData = JSONObject.parseObject(responseInfo, new TypeReference<BaseBeanObject<MarketData>>() {
                    });

                    //设置大盘
                    List<HangQing> hangqing = marketData.getData().getHangqing();
                    rvMarket.setAdapter(new MarketAdapter(hangqing));
                    //设置热门行业
                    List<HotIndustrylist> hotIndustrylist = marketData.getData().getHotIndustrylist();
                    rvHotIndustry.setAdapter(new HotIndustryAdapter(hotIndustrylist));

                } else {
                    UIHelper.toast(getContext(), JSONObject.parseObject(responseInfo).getString("message"));
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getContext(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    private void getMarketAd() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("product", "4");
//        if (!isRefresh) {
//            JSONObject oldValue = PersistTool.getJsonObject("goroundData", null);
//            if (oldValue != null) {
//                parseGoroundData(oldValue);
//            }
//        }
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code")==0) {
                    SPTools.saveJsonArray("marketAdData", JSONObject.parseObject(responseInfo).getJSONArray("data"));
//                parseGoroundData(result);
                    BaseBeanList<StockBannerData> beanListBean = JSONObject.parseObject(responseInfo, new TypeReference<BaseBeanList<StockBannerData>>() {
                    });
                    List<StockBannerData> bannerData = beanListBean.getData();
                    for (int i = 0; i < bannerData.size(); i++) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        TextView tView = new TextView(getContext());
                        tView.setText(bannerData.get(i).getData().getNewstitle());
                        tView.setSingleLine();
                        tView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                        tView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        tView.setTextColor(Color.BLACK);
                        tView.setGravity(Gravity.CENTER_VERTICAL);
                        tView.setLayoutParams(params);
//                        tView.setOnClickListener(new StockBannerClickListener(bannerData.get(i).getTarget_url()));
                        flipperAd.addView(tView);
                    }
                    flipperAd.setInAnimation(
                            android.view.animation.AnimationUtils.loadAnimation(
                                    getContext(), R.anim.down_to_center));
                    flipperAd.setOutAnimation(
                            android.view.animation.AnimationUtils.loadAnimation(
                                    getContext(), R.anim.center_to_up));
                    flipperAd.startFlipping();

                }else {
                    UIHelper.toastResponseError(getContext(),responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getContext(),msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.BANNER_LIST, ggHttpInterface).startGet();
    }

    /**大盘适配器*/
    private class MarketAdapter extends CommonAdapter<HangQing>{

        private MarketAdapter(List<HangQing> datas) {
            super(MarketActivity.this, R.layout.item_stock_market, datas);
        }

        @Override
        protected void convert(ViewHolder holder, HangQing hangQing, int position) {
            // TODO: 2017/3/10 0010 字体颜色，文字为0时
            View holderView = holder.getView(R.id.item_stock_market);
            ViewGroup.LayoutParams params = holderView.getLayoutParams();
            params.width= (AppDevice.getWidth(getContext())-AppDevice.dp2px(getContext(),1))/2;
            holder.setText(R.id.tv_stock_market_name,hangQing.getName());
            holder.setText(R.id.tv_stock_market_price, StringUtils.saveSignificand(hangQing.getPrice(),2));
            holder.setText(R.id.tv_stock_market_price_change, StringUtils.saveSignificand(hangQing.getPrice_change(),2));
            holder.setText(R.id.tv_stock_market_price_change_rate, StringUtils.saveSignificand(hangQing.getPrice_change_rate(),2)+"%");
        }
    }

    /**热门行业数据*/
    private class HotIndustryAdapter extends CommonAdapter<HotIndustrylist>{

        public HotIndustryAdapter(List<HotIndustrylist> datas) {
            super(MarketActivity.this, R.layout.item_stock_hotindustry, datas);
        }

        @Override
        protected void convert(ViewHolder holder, HotIndustrylist hotIndustrylist, int position) {
        }
    }
}
