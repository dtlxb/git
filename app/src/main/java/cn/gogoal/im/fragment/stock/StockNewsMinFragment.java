package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.CopyStockDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.adapter.copy.StockDetailNewsListDataActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import hply.com.niugu.bean.StockDetailNewsBean;
import hply.com.niugu.bean.StockDetailNewsData;
import hply.com.niugu.bean.StockDetailResearchBean;
import hply.com.niugu.bean.StockDetailResearchData;


/**
 * author wangjd on 2017/5/3 0003.
 * Staff_id 1375
 * phone 18930640263
 * description :个股页，简短新闻、研报、公告
 */
public class StockNewsMinFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    /*新闻、公告*/
    private List<StockDetailNewsData> dataListsNews;
    private NewsAdapter newsAdapter;

    /*研报*/
    private ResearchAdapter researchadapter;
    private List<StockDetailResearchData> dataListsResearch;

    //点击查看更多
    private View footView = null;

    private String stockCode;
    private int parentPosition;
    private String stockName;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        stockCode = ((CopyStockDetailActivity) getActivity()).getStockCode();
        stockName = ((CopyStockDetailActivity) getActivity()).getStockName();
    }

    public static StockNewsMinFragment getInstance(int position) {
        StockNewsMinFragment snf = new StockNewsMinFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        snf.setArguments(b);
        return snf;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        parentPosition = getArguments().getInt("position");

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        iniFootView();

        switch (parentPosition) {
            case 0:
                getNews(7);
                break;
            case 1:
                getNews(3);
                break;
            case 2:
                getYanBao();
                break;
        }

        xLayout.setEmptyText("暂无"+getTitle()+"数据");
    }

    //初始化底部"查看更多按钮"
    private void iniFootView() {
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.load_more_tv, null);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StockDetailNewsListDataActivity.class);
                intent.putExtra("type", getNewsType());
                intent.putExtra("title", getTitle());
                intent.putExtra("stockCode", stockCode);
                intent.putExtra("stockName", stockName);
                startActivity(intent);
            }
        });
    }

    void hideFootView() {
        if (parentPosition !=2) {
            newsAdapter.removeFooterView(footView);
        }else {
            researchadapter.addFooterView(footView);
        }
        footView.setVisibility(View.GONE);
    }

    void showFootView() {
        if (parentPosition !=2) {
            newsAdapter.addFooterView(footView);
        }else {
            researchadapter.addFooterView(footView);
        }
        footView.setVisibility(View.VISIBLE);
    }

    /*请求新闻和公告数据*/
    private void getNews(final int type) {

        dataListsNews =new ArrayList<>();
        newsAdapter=new NewsAdapter(dataListsNews);
        recyclerView.setAdapter(newsAdapter);
        xLayout.setStatus(XLayout.Loading);
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("page", "1");
        param.put("type", String.valueOf(type));
        param.put("rows", "3");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    dataListsNews.clear();
                    dataListsNews.addAll(JSONObject.parseObject(responseInfo, StockDetailNewsBean.class).getData());

                    if (dataListsNews.size()<3){
                        hideFootView();
                    }else {
                        showFootView();
                    }

                    newsAdapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);
                }else {
                    xLayout.setStatus(XLayout.Empty);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getContext(), msg, xLayout);
            }
        }).startGet();
    }

    /*请求研报数据*/
    private void getYanBao() {
        xLayout.setStatus(XLayout.Loading);

        dataListsResearch=new ArrayList<>();
        researchadapter=new ResearchAdapter(dataListsResearch);
        recyclerView.setAdapter(researchadapter);

        final Map<String, String> param = new HashMap<String, String>();
        param.put("summary_auth", "1");
        param.put("stock_code", stockCode);
        param.put("token", UserUtils.getToken());
        param.put("first_class", "公司报告");
        param.put("page", "1");
        param.put("rows", "3");

        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e("研报",responseInfo);

                if (JSONObject.parseObject(responseInfo).getIntValue("code")==0){
                    dataListsResearch.addAll(
                            JSONObject.parseObject(
                                    responseInfo, StockDetailResearchBean.class).getData());
                    researchadapter.notifyDataSetChanged();

                    if (dataListsResearch.size()<3){
                        hideFootView();
                    }else {
                        showFootView();
                    }

                    xLayout.setStatus(XLayout.Success);
                }else {
                    xLayout.setStatus(XLayout.Empty);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(),msg,xLayout);
            }
        }).startGet();
    }

    //-----------------------------参数配置区------------------------------------
    private String getTitle() {
        return parentPosition ==0?"新闻":(parentPosition ==1?"公告":"研报");
    }

    private int getNewsType(){
//        7(新闻)、3(公告)、9(看点，去掉)
        return parentPosition ==0?7:(parentPosition ==1?3:9);
    }

    private int getNewsSource(){
        return parentPosition ==0?AppConst.SOURCE_TYPE_NEWS:
                (parentPosition ==1?AppConst.SOURCE_TYPE_GONGGAO:
                AppConst.SOURCE_TYPE_YANBAO);
    }
//-----------------------------参数配置区------------------------------------

    /**
     * 新闻、公告
     */
    private class NewsAdapter extends CommonAdapter<StockDetailNewsData, BaseViewHolder> {

        private NewsAdapter(List<StockDetailNewsData> data) {
            super(R.layout.stock_detail_news_item, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final StockDetailNewsData data, int position) {
            holder.setText(R.id.big_event_tittle_tv,data.getTitle());
            holder.setText(R.id.big_event_date,data.getDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parentPosition==0) {
                        NormalIntentUtils.go2WebActivity(v.getContext(),
                                AppConst.WEB_NEWS + data.getOrigin_id() + "?source=" + getNewsSource(),
                                null, true);
                    }else {
                        NormalIntentUtils.go2PdfDisplayActivity(v.getContext(),data.getOrigin_link(),"");
                    }

                }
            });
        }
    }
    /**
     * 研报
     */
    private class ResearchAdapter extends CommonAdapter<StockDetailResearchData, BaseViewHolder> {

        private ResearchAdapter(List<StockDetailResearchData> data) {
            super(R.layout.stock_detail_news_item, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final StockDetailResearchData data, int position) {
            holder.setText(R.id.big_event_tittle_tv,data.getReport_title());
            holder.setText(R.id.big_event_date,data.getCreate_date());

            TextView tvOrganNameAndAuthor=holder.getView(R.id.big_event_organ_andr_author);
            tvOrganNameAndAuthor.setVisibility(View.VISIBLE);
            tvOrganNameAndAuthor.setText(data.getOrgan_name()+"  "+data.getAuthor());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS+data.getGuid()+"?source="+getNewsSource(),
                            null,true);
                }
            });
        }
    }
}
