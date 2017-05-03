package cn.gogoal.im.adapter.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.copy.NewsUtils;
import hply.com.niugu.bean.StockDecisionData;
import hply.com.niugu.bean.StockDetailNewsBean;
import hply.com.niugu.bean.StockDetailNewsData;
import hply.com.niugu.bean.StockDetailResearchBean;
import hply.com.niugu.bean.StockDetailResearchData;
import hply.com.niugu.view.GGListView;


/*
* daiwei
* */
public class StockDetailNewsListDataActivity extends BaseActivity {
    //返回
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    //标题
    @BindView(R.id.textHeadTitle)
    TextView textHeadTitle;
    //列表
    @BindView(R.id.data_list)
    GGListView data_list;
    //页面加载动画
    @BindView(R.id.load_animation)
    RelativeLayout load_animation;

    private String stockName, title, stockCode;
    private int type, page = 1;
    //新闻
    private StockDetailNewsAdapter adapter_news;
    private ArrayList<StockDetailNewsData> list_news = new ArrayList<StockDetailNewsData>();
    //看点
    private StockDetailOverviewAdapter adapter_overview;
    private ArrayList<StockDecisionData> list_overview = new ArrayList<StockDecisionData>();
    //研报
    private StockDetailResearchAdapter adapter_research;
    private ArrayList<StockDetailResearchData> list_research = new ArrayList<StockDetailResearchData>();

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_detail_news_list_data;
    }

    @Override
    public void doBusiness(Context mContext) {
        init();
        //设置状态栏
        StatusBarUtil.with(this).setColor(Color.WHITE);
    }

    private void init() {
        //返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stockName = getIntent().getStringExtra("stockName");
        stockCode = getIntent().getStringExtra("stockCode");
        title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);

        textHeadTitle.setText(stockName + "-" + title);
        if (type == 3 || type == 7) {
            adapter_news = new StockDetailNewsAdapter(list_news);
            data_list.setAdapter(adapter_news);
            list_news.clear();
            list_overview.clear();
            list_research.clear();
            InitList(stockCode, type, page);
        } else if (type == 9) {
            adapter_overview=new StockDetailOverviewAdapter(list_overview);
            data_list.setAdapter(adapter_overview);
            list_news.clear();
            list_overview.clear();
            list_research.clear();
//            InitPoint(page,stockCode);
        } else {
            adapter_research = new StockDetailResearchAdapter(list_research);
            data_list.setAdapter(adapter_research);
            list_news.clear();
            list_overview.clear();
            list_research.clear();
            InitResearch(stockCode, page);
        }

        data_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject new_id = new JSONObject();
                switch (type) {
                    case 7:
                        if (list_news != null) {
                            Intent intent = new Intent(StockDetailNewsListDataActivity.this, FunctionActivity.class);
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
                            Intent intent = new Intent(StockDetailNewsListDataActivity.this, FunctionActivity.class);
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
                            Intent intent = new Intent(StockDetailNewsListDataActivity.this, FunctionActivity.class);
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
                            Intent intent = new Intent(StockDetailNewsListDataActivity.this, FunctionActivity.class);
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
        });

        data_list.setPullBottomListener(new GGListView.PullBottomListener() {
            @Override
            public void Run() {
                if (page < 500) {
                    page++;
                    if (type == 3 || type == 7) {
                        InitList(stockCode, type, page);
                    }
//                    else if (type == 9) {
//                        InitPoint(page, stockCode);
//                    }
                    else {
                        InitResearch(stockCode, page);
                    }
                } else {
                    data_list.loadMoreComplate();
                }
            }
        });
    }

    private void InitList(String stock_code, final int type, final int page) {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stock_code);
        param.put("type", type + "");
        param.put("page", page + "");
        param.put("rows", "20");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                StockDetailNewsBean bean = JSONObject.parseObject(responseInfo, StockDetailNewsBean.class);
                if (bean.getCode().equals("0")) {
                    ArrayList<StockDetailNewsData> info = bean.getData();
                    list_news.addAll(info);
                    adapter_news.notifyDataSetChanged();
                } else if(bean.getCode().equals("1001")){
                    if(page!=1) {
                        UIHelper.toast(StockDetailNewsListDataActivity.this, R.string.nomoredata_hint);
                    }
                }
                refresh();
            }

            @Override
            public void onFailure(String msg) {
                refresh();
                UIHelper.toast(StockDetailNewsListDataActivity.this, "请检查网络");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, ggHttpInterface).startGet();
    }

    private void InitResearch(String stock_code,final int page) {
        final Map<String, String> param = new HashMap<String, String>();
        //判断是否登录
        if (!UserUtils.isLogin()) {
            param.put("stock_code", stock_code);
            param.put("first_class", "公司报告");
            param.put("page", page + "");
            param.put("rows", "20");
        } else {
            param.put("summary_auth", "1");
            param.put("stock_code", stock_code);
            param.put("token", UserUtils.getToken());
            param.put("first_class", "公司报告");
            param.put("page", page + "");
            param.put("rows", "20");
        }

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                StockDetailResearchBean bean = JSONObject.parseObject(responseInfo, StockDetailResearchBean.class);
                if (bean.getCode().equals("0")) {
                    ArrayList<StockDetailResearchData> info = bean.getData();
                    list_research.addAll(info);
                    adapter_research.notifyDataSetChanged();
                } else if(bean.getCode().equals("1001")){
                    if(page!=1) {
                        UIHelper.toast(StockDetailNewsListDataActivity.this, R.string.nomoredata_hint);
                    }
                }
                refresh();
            }

            @Override
            public void onFailure(String msg) {
                refresh();
                UIHelper.toast(StockDetailNewsListDataActivity.this, "请检查网络");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, ggHttpInterface).startGet();
    }

//    private void InitPoint(final int page, String stockCode) {
//        final Map<String, String> param = new HashMap<String, String>();
//        param.put("stock_code", stockCode);
//        param.put("keyword", "主要看点");
//        param.put("page", page + "");
//        param.put("rows", "20");
//
//        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//
//            @Override
//            public void onSuccess(String respornseInfo) {
//                if (respornseInfo != null) {
//                    StockDecisionBean bean = JSONObject.parseObject(respornseInfo, StockDecisionBean.class);
//                    if (bean.getCode()==0) {
//                        ArrayList<StockDecisionData> info = bean.getData();
//                        list_overview.addAll(info);
//                        adapter_overview.notifyDataSetChanged();
//                    }else if(bean.getCode()==0){
//                        if(page!=1){
//                            UIHelper.toast(StockDetailNewsListDataActivity.this, R.string.nomoredata_hint);
//                        }
//                    }
//                }
//                refresh();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                refresh();
//                UIHelper.toast(StockDetailNewsListDataActivity.this, R.string.net_erro_hint);
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.REPORT_RM, ggHttpInterface).startGet();
//    }

    private void refresh(){
        data_list.loadMoreComplate();
        load_animation.setVisibility(View.GONE);
    }
}