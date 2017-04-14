package cn.gogoal.im.fragment.copy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;
import cn.gogoal.im.activity.copy.NewsContentActivity;
import cn.gogoal.im.adapter.copy.StockDetailNewsAdapter;
import cn.gogoal.im.adapter.copy.StockDetailNewsListDataActivity;
import cn.gogoal.im.adapter.copy.StockDetailOverviewAdapter;
import cn.gogoal.im.adapter.copy.StockDetailResearchAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.copy.NewsUtils;
import hply.com.niugu.DeviceUtil;
import hply.com.niugu.bean.StockDecisionBean;
import hply.com.niugu.bean.StockDecisionData;
import hply.com.niugu.bean.StockDetailNewsBean;
import hply.com.niugu.bean.StockDetailNewsData;
import hply.com.niugu.bean.StockDetailResearchBean;
import hply.com.niugu.bean.StockDetailResearchData;
import hply.com.niugu.view.InnerListView;


/**
 * Created by wangjd on 2016/9/21 0021.
 */

public class StockDetailNewsFragment extends BaseFragment {

    private String stockName, stockCode;

    //展示列表
    @BindView(R.id.news_lv)
    InnerListView news_lv;

    @BindView(R.id.mProgress)
    ProgressBar mProgressBar;
    //无数据时
    @BindView(R.id.stock_no_data)
    LinearLayout stock_no_data;
    //新闻
    private StockDetailNewsAdapter adapter_news;

    private ArrayList<StockDetailNewsData> list_news = new ArrayList<StockDetailNewsData>();
    private ArrayList<StockDecisionData> list_overview = new ArrayList<StockDecisionData>();
    //研报
    private StockDetailResearchAdapter adapter_research;
    private ArrayList<StockDetailResearchData> list_research = new ArrayList<StockDetailResearchData>();

    private String title = "新闻";
    private int type = 7;
    private int rows = 3;

    //点击查看更多
    private TextView load_more_tv;
    private View footView = null;
    private StockDetailOverviewAdapter adapter_overview;

    public StockDetailNewsFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        stockName = ((CopyStockDetailActivity) getActivity()).getStockName();
        stockCode = ((CopyStockDetailActivity) getActivity()).getStockCode();
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_stock_detail_news;
    }

    public static StockDetailNewsFragment newInstance(int pos, String keyWord) {
        StockDetailNewsFragment sef = new StockDetailNewsFragment();
        Bundle b = new Bundle();
        b.putString("keyWord", keyWord);
        b.putInt("position", pos);
        sef.setArguments(b);
        return sef;
    }

    @Override
    public void doBusiness(Context mContext) {
        //添加底部
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.load_more_tv, null);
        load_more_tv = (TextView) footView.findViewById(R.id.load_more_tv);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StockDetailNewsListDataActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("title", title);
                intent.putExtra("stockCode", stockCode);
                intent.putExtra("stockName", stockName);
                startActivity(intent);
            }
        });
        news_lv.addFooterView(footView);

        list_news.clear();
        getNewsData(stockCode, type, 3);

        news_lv.setOnItemClickListener(new MyItemListener());

    }

    /**
     * 设置监听 以及百度统计
     */
    @Subscriber(tag = "StockDetailNewsFragment_TAB")
    public void setTabChange(BaseMessage msg) {
        switch (msg.getCode()) {
            case "0":
                setNewsData();
                break;
            case "1":
                setNoticeList();
                break;
            case "2":
                setYanBaoList();
                break;
            case "3":
                setViewPointList();
                break;
        }
    }

    private void setNewsData() {
        type = 7;
        title = "新闻";
        list_news.clear();
        list_overview.clear();
        list_research.clear();
        getNewsData(stockCode, type, 3);
    }


    private void setNoticeList() {
        type = 3;
        title = "公告";
        list_news.clear();
        list_overview.clear();
        list_research.clear();
        getNewsData(stockCode, type, 3);
    }

    private void setYanBaoList() {
        title = "研报";
        type = 0;
        adapter_research = new StockDetailResearchAdapter(list_research);
        news_lv.setAdapter(adapter_research);
        list_research.clear();
        list_news.clear();
        list_overview.clear();
        getYanbaoData(stockCode, 3);
    }

    private void setViewPointList() {
        title = "看点";
        type = 9;
        list_overview.clear();
        list_news.clear();
        list_research.clear();
        getViewPointData(1, stockCode);
    }

    public void showProgress(final boolean show, final View mProgressView, final View dataView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            dataView.setVisibility(show ? View.GONE : View.VISIBLE);
            dataView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dataView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            dataView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    /**
     * 获取新闻 公告数据
     */
    private void getNewsData(String stockCode, int type, int rows) {
        showProgress(false, mProgressBar, news_lv);
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        param.put("type", type + "");
        param.put("page", "1");
        param.put("rows", rows + "");

        adapter_news = new StockDetailNewsAdapter(list_news);
        news_lv.setAdapter(adapter_news);

        final GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                try {
                    showProgress(false, mProgressBar, news_lv);
                }catch (Exception e){}
                
                StockDetailNewsBean bean = JSONObject.parseObject(responseInfo, StockDetailNewsBean.class);
                if (bean.getCode().equals("0")) {
                    try {
                        news_lv.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                    try {
                        stock_no_data.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
                    ArrayList<StockDetailNewsData> info = bean.getData();
                    list_news.clear();
                    list_news.addAll(info);
                    adapter_news.notifyDataSetChanged();
                    if (info.size() < 3) {
                        load_more_tv.setVisibility(View.GONE);
                    } else {
                        load_more_tv.setVisibility(View.VISIBLE);
                    }
                } else if (bean.getCode().equals("1001")) {
                    if (news_lv!=null) {
                        news_lv.setVisibility(View.GONE);
                    }
                    showNoDataView();
                }
            }

            @Override
            public void onFailure(String msg) {
                try {
                    news_lv.setVisibility(View.GONE);
                } catch (Exception e) {
                }

                showNoDataView();
                if (isAdded()) {
                    UIHelper.toast(getActivity(), R.string.net_erro_hint);
                    try {
                        showProgress(false, mProgressBar, news_lv);
                    }catch (Exception e){}
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, httpInterface).startGet();
    }

    /**
     * 获取研报数据
     */
    private void getYanbaoData(String stockCode, int rows) {
        showProgress(true, mProgressBar, news_lv);
        final Map<String, String> param = new HashMap<String, String>();
        //判断是否登录
        if (!UserUtils.isLogin()) {
            param.put("stock_code", stockCode);
            param.put("first_class", "公司报告");
            param.put("page", "1");
            param.put("rows", rows + "");
        } else {
            param.put("summary_auth", "1");
            param.put("stock_code", stockCode);
            param.put("token", UserUtils.getToken());
            param.put("first_class", "公司报告");
            param.put("page", "1");
            param.put("rows", rows + "");
        }

        final GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                showProgress(false, mProgressBar, news_lv);
                StockDetailResearchBean bean = JSONObject.parseObject(responseInfo, StockDetailResearchBean.class);
                if (bean.getCode().equals("0")) {
                    news_lv.setVisibility(View.VISIBLE);
                    stock_no_data.setVisibility(View.GONE);
                    ArrayList<StockDetailResearchData> info = bean.getData();
                    list_research.clear();
                    list_research.addAll(info);
                    adapter_research.notifyDataSetChanged();
                    if (info.size() < 3) {
                        load_more_tv.setVisibility(View.GONE);
                    } else {
                        load_more_tv.setVisibility(View.VISIBLE);
                    }
                } else {
                    news_lv.setVisibility(View.GONE);
                    showNoDataView();
                }
            }

            @Override
            public void onFailure(String msg) {
                if (isAdded()) {
                    UIHelper.toast(getActivity(), "请检查网络");
                    showProgress(false, mProgressBar, news_lv);
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, httpInterface).startGet();
    }

    /**
     * 获取看点数据
     */
    private void getViewPointData(int page, String stockCode) {
        showProgress(true, mProgressBar, news_lv);
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        param.put("keyword", "主要看点");
        param.put("page", page + "");
        param.put("rows", "2");

        adapter_overview = new StockDetailOverviewAdapter(list_overview);
        news_lv.setAdapter(adapter_overview);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String respornseInfo) {
                showProgress(false, mProgressBar, news_lv);
                if (respornseInfo != null) {
                    StockDecisionBean bean = JSONObject.parseObject(respornseInfo, StockDecisionBean.class);
                    if (bean.getCode() == 0) {
                        news_lv.setVisibility(View.VISIBLE);
                        stock_no_data.setVisibility(View.GONE);
                        ArrayList<StockDecisionData> info = bean.getData();
                        list_overview.clear();
                        list_overview.addAll(info);
                        adapter_overview.notifyDataSetChanged();
                        if (info.size() < 2) {
                            load_more_tv.setVisibility(View.GONE);
                        } else {
                            load_more_tv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        news_lv.setVisibility(View.GONE);
                        showNoDataView();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (isAdded()) {
                    UIHelper.toast(getActivity(), R.string.net_erro_hint);
                    showProgress(false, mProgressBar, news_lv);
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.REPORT_RM, httpInterface).startGet();
    }

    /**
     * 设置无数据时的显示
     */
    private void showNoDataView() {
        stock_no_data.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams myParams = stock_no_data.getLayoutParams();
        myParams.height = DeviceUtil.dp2px(getActivity(), 220);
        stock_no_data.setLayoutParams(myParams);

        load_more_tv.setVisibility(View.GONE);
    }

    class MyItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            JSONObject new_id = new JSONObject();
            switch (type) {
                case 7:
                    if (list_news != null) {
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", list_news.get(position).getOrigin_id());
                        intent.putExtra("type", "100");
                        intent.putExtra("favor_type", "1");
                        intent.putExtra("newstitle", list_news.get(position).getTitle());
                        intent.putExtra("favor_count", list_news.get(position).getFavor_sum());
                        intent.putExtra("praise_count", list_news.get(position).getPraise_sum());
                        intent.putExtra("share_count", list_news.get(position).getShare_sum());
                        startActivity(intent);
                        new_id.put("id", list_news.get(position).getOrigin_id());
                        NewsUtils.addReadNewsId(new_id);
                        adapter_news.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    if (list_news != null) {
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", list_news.get(position).getOrigin_id());
                        intent.putExtra("type", "105");
                        intent.putExtra("newstitle", list_news.get(position).getTitle());
                        intent.putExtra("favor_count", list_news.get(position).getFavor_sum());
                        intent.putExtra("praise_count", list_news.get(position).getPraise_sum());
                        intent.putExtra("share_count", list_news.get(position).getShare_sum());
                        startActivity(intent);
                        new_id.put("id", list_news.get(position).getOrigin_id());
                        NewsUtils.addReadNewsId(new_id);
                        adapter_news.notifyDataSetChanged();
                    }
                    break;
                case 0:
                    if (list_research != null) {
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", list_research.get(position).getGuid());
                        intent.putExtra("type", "102");
                        intent.putExtra("newstitle", list_research.get(position).getReport_title());
                        intent.putExtra("favor_count", list_research.get(position).getFavor_sum());
                        intent.putExtra("praise_count", list_research.get(position).getPraise_sum());
                        intent.putExtra("share_count", list_research.get(position).getShare_sum());
                        startActivity(intent);
                        new_id.put("id", list_research.get(position).getGuid());
                        NewsUtils.addReadNewsId(new_id);
                        adapter_research.notifyDataSetChanged();
                    }
                    break;
                case 9:
                    if (list_overview != null) {
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", list_overview.get(position).getGuid());
                        intent.putExtra("type", "102");
                        intent.putExtra("favor_type", "5");
                        intent.putExtra("favor_count", list_overview.get(position).getFavor_sum());
                        intent.putExtra("praise_count", list_overview.get(position).getPraise_sum());
                        intent.putExtra("share_count", list_overview.get(position).getShare_sum());
                        startActivity(intent);
                        new_id.put("id", list_overview.get(position).getGuid());
                        NewsUtils.addReadNewsId(new_id);
                        adapter_overview.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
