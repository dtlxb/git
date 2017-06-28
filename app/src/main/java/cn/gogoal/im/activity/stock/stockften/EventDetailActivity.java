package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.BigDataDetailAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.bigdata.BigDataDetailList;
import cn.gogoal.im.bean.stock.bigdata.BigDataDetailListBean;
import cn.gogoal.im.bean.stock.bigdata.event.EventDetailBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :事件选股详情
 */
public class EventDetailActivity extends BaseActivity {

    @BindView(R.id.tv_opportunity)
    TextView tvOpportunity;

    @BindView(R.id.rv_subject_about_stocks)
    RecyclerView rvAboutStocks;

    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int typeId;

    private BigDataDetailAdapter dataDetailAdapter;

    private List<BigDataDetailList> dataDetailLists;

    @Override
    public int bindLayout() {
        return R.layout.activity_event_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
//        GET_CONTENT
        String typeTitle = getIntent().getStringExtra("event_type_title");
        setMyTitle(typeTitle, true);
        typeId = getIntent().getIntExtra("event_type_id", -1080);

        BaseActivity.iniRefresh(refreshlayout);

        dataDetailLists = new ArrayList<>();
        dataDetailAdapter = new BigDataDetailAdapter(dataDetailLists);

        rvAboutStocks.setLayoutManager(new LinearLayoutManager(mContext));
        rvAboutStocks.addItemDecoration(new NormalItemDecoration(mContext));
        rvAboutStocks.setNestedScrollingEnabled(false);
        rvAboutStocks.setAdapter(dataDetailAdapter);

        //
        if (typeId == -1080) {
            tvOpportunity.setText("获取事件信息失败,请稍后重试");
        } else {
            getEventDetailData(AppConst.REFRESH_TYPE_FIRST);
        }

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventDetailData(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshlayout.setRefreshing(false);
            }
        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getEventDetailData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventDetailData(-1);
    }

    private void getEventDetailData(int refreshType) {
        if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
            xLayout.setStatus(XLayout.Loading);
        }
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("id", String.valueOf(typeId));
        new GGOKHTTP(params, GGOKHTTP.GET_CONTENT, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    EventDetailBean.EventDetailData eventDetailData =
                            JSONObject.parseObject(responseInfo, EventDetailBean.class).getData();

                    tvOpportunity.setText(Html.fromHtml(eventDetailData.getContent()));

                    getStockPoolDatas(StockUtils.getStockCodes(
                            eventDetailData.getStocks()), eventDetailData.getSource());
                    //
                    xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null)
                    xLayout.setStatus(XLayout.Error);
            }
        }).startGet();
    }

    private void getStockPoolDatas(String stock_codes, String content) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("rows", "15");
        params.put("page", "1");
        params.put("get_hq", "1");
        params.put("stock_codes", stock_codes);
        params.put("keyword", content);

        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_POOL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    dataDetailLists.clear();
                    List<BigDataDetailList> bigDataDetailLists =
                            JSONObject.parseObject(responseInfo, BigDataDetailListBean.class).getData();

                    Collections.sort(bigDataDetailLists, new Comparator<BigDataDetailList>() {
                        @Override
                        public int compare(BigDataDetailList o1, BigDataDetailList o2) {
                            return Double.compare(o2.getPrice_change_rate(),o1.getPrice_change_rate());
                        }
                    });
                    dataDetailLists.addAll(bigDataDetailLists);
                    dataDetailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

}
