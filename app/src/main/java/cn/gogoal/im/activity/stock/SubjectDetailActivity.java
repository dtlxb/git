package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
import cn.gogoal.im.bean.stock.bigdata.subject.SubjectDetailBean;
import cn.gogoal.im.bean.stock.bigdata.subject.SubjectDetailData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主题内容
 */
public class SubjectDetailActivity extends BaseActivity {

    @BindView(R.id.tv_opportunity)
    TextView tvOpportunity;

    @BindView(R.id.rv_subject_about_stocks)
    RecyclerView rvSubjectAboutStocks;

    @BindView(R.id.iv_describe)
    ImageView imgDescibe;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.chartView)
    LineChartView chartView;

    private List<BigDataDetailList> subjectContentList;
    private BigDataDetailAdapter subjectContentAdapter;
    private String subjectId;

    @Override
    public int bindLayout() {
        return R.layout.activity_subject;
    }

    @Override
    public void doBusiness(Context mContext) {
        String subject_type = getIntent().getStringExtra("subject_type");
        setMyTitle(subject_type + "产业", true);

        subjectId = getIntent().getStringExtra("subject_id");

        BaseActivity.iniRefresh(refreshLayout);

        subjectContentList = new ArrayList<>();
        subjectContentAdapter = new BigDataDetailAdapter(subjectContentList);
        rvSubjectAboutStocks.setLayoutManager(new LinearLayoutManager(mContext));
        rvSubjectAboutStocks.addItemDecoration(new NormalItemDecoration(mContext));
        rvSubjectAboutStocks.setNestedScrollingEnabled(false);
        rvSubjectAboutStocks.setAdapter(subjectContentAdapter);

        //
        if (StringUtils.isActuallyEmpty(subjectId)) {
            tvOpportunity.setText("获取主题信息失败,请稍后重试");
        } else {
            getSubjectDetail();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubjectDetail();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubjectDetail();
    }

    //请求基本详情数据
    private void getSubjectDetail() {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("id", subjectId);
        new GGOKHTTP(params, GGOKHTTP.GET_RECOMMEND_CONTENT, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    SubjectDetailData subjectDetailData =
                            JSONObject.parseObject(responseInfo, SubjectDetailBean.class).getData();

                    tvOpportunity.setText(
                            Html.fromHtml(subjectDetailData.getTheme_summarize().getDescribe()));

                    RequestOptions options = new RequestOptions();
                    options.centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(getActivity())
                            .load(subjectDetailData.getTheme_summarize().getImg_url())
                            .apply(options)
                            .into(imgDescibe);

                    //
                    getStockPoolDatas(StockUtils.getStockCodes(
                            subjectDetailData.getStocks()), subjectDetailData.getTheme_name());
                    //
                    getThemeWordsAttentionYear(StockUtils.getStockCodes(subjectDetailData.getStocks()),
                            subjectDetailData.getTheme_name());

                } else if (code == 1001) {
                    tvOpportunity.setText("暂无相关描述");
                } else {
                    tvOpportunity.setText("请求出错");
                }
            }

            @Override
            public void onFailure(String msg) {
                if (tvOpportunity != null)
                    tvOpportunity.setText("出错啦" + msg);
            }
        }).startGet();
    }

    //通过详情数据的股票集参数二次请求股票池详细数据
    private void getStockPoolDatas(String stock_codes, String keyword) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("rows", "15");
        params.put("page", "1");
        params.put("get_hq", "1");
        params.put("stock_codes", stock_codes);
        params.put("keyword", keyword);

        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_POOL, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    subjectContentList.clear();
                    List<BigDataDetailList> subjectContents =
                            JSONObject.parseObject(responseInfo, BigDataDetailListBean.class).getData();

                    Collections.sort(subjectContents, new Comparator<BigDataDetailList>() {
                        @Override
                        public int compare(BigDataDetailList o1, BigDataDetailList o2) {
                            return Double.compare(o2.getPrice_change_rate(),o1.getPrice_change_rate());
                        }
                    });
                    subjectContentList.addAll(subjectContents);
                    subjectContentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    //请求主题追踪,绘图
    public void getThemeWordsAttentionYear(String stockCodes, String themeName) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("stock_codes", stockCodes);
        params.put("theme_word", themeName);

        new GGOKHTTP(params, GGOKHTTP.GET_THEME_WORDS_ATTENTION_YEAR, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

}
