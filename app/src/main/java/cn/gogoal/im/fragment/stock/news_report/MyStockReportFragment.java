package cn.gogoal.im.fragment.stock.news_report;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.bean.stock.MyStockReportBean;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股、公告
 */
public class MyStockReportFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefreshlayout)
    RefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private List<MyStockReportBean.MyStockReport> reportList;
    private MyStockReportAdapter reportAdapter;

    private int defaultPage = 1;
    private int groupId;

    public static MyStockReportFragment newInstance(int groupId) {
        MyStockReportFragment fragment = new MyStockReportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("group_id", groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        groupId = getArguments().getInt("group_id");

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        mContext,
                        LinearLayoutManager.VERTICAL,
                        false));

//        recyclerView.addItemDecoration(new DashlineItemDivider());
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));

        reportList = new ArrayList<>();
        reportAdapter = new MyStockReportAdapter(reportList);
        recyclerView.setAdapter(reportAdapter);
        xLayout.setEmptyText("暂无研报数据\n请添加自选股后重试!");

        getReportDatas(AppConst.REFRESH_TYPE_FIRST);

        swiperefreshlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                defaultPage = 1;
                getReportDatas(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.refreshComplete();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return ((LinearLayoutManager) rvMyStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0 ;
                return !recyclerView.canScrollVertically(-1);
            }
        });

        reportAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    reportAdapter.loadMoreEnd(false);
                    getReportDatas(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    reportAdapter.loadMoreEnd(true);
                    reportAdapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                reportAdapter.loadMoreComplete();
            }
        }, recyclerView);
    }

    private void getReportDatas(final int refreshType) {
        HashMap<String, String> params = UserUtils.getTokenParams();
        if (groupId == 0) {
            xLayout.setStatus(XLayout.Error);
            return;
        }

        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }

        reportAdapter.setEnableLoadMore(false);

        params.put("group_id", String.valueOf(groupId));
        params.put("page", String.valueOf(defaultPage));
        params.put("get_hq", "true");
        params.put("rows", "15");

        new GGOKHTTP(params, GGOKHTTP.MY_STOCK_GET_REPORT, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        reportList.clear();
                    }
                    List<MyStockReportBean.MyStockReport> myStockNewses =
                            JSONObject.parseObject(responseInfo, MyStockReportBean.class).getData();

                    reportList.addAll(myStockNewses);

                    reportAdapter.notifyDataSetChanged();
                    reportAdapter.setEnableLoadMore(true);
                    reportAdapter.loadMoreComplete();
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

    private class MyStockReportAdapter extends CommonAdapter<
            MyStockReportBean.MyStockReport, BaseViewHolder> {

        private MyStockReportAdapter(List<MyStockReportBean.MyStockReport> data) {
            super(R.layout.item_mystock_news, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockReportBean.MyStockReport data, int position) {

            holder.setText(R.id.tv_mystock_news_source,data.getOrgan_name()+"\u3000"+"报告共("+data.getFile_pages()+")");

            holder.setText(R.id.tv_mystock_news_title,data.getReport_title());

            holder.setText(R.id.tv_mystock_news_stockInfo,
                    data.getCode_name() + " (" + data.getCode() + ")");

            holder.setText(R.id.tv_mystock_news_date, CalendarUtils.getStringDate("yyyy-MM-dd HH:mm",
                    data.getCreate_date()));

            UIHelper.setRippBg(holder.itemView);

            holder.setText(R.id.tv_mystock_news_stockRate,
                    StringUtils.save2Significand(data.getPrice()) + "\u3000\u3000" +
                            StockUtils.plusMinus(data.getChange_rate(), true));

            holder.setTextColor(R.id.tv_mystock_news_stockRate,
                    getResColor(StockUtils.getStockRateColor(
                            data.getChange_rate())));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS + data.getGuid() + "?source=" + AppConst.SOURCE_TYPE_YANBAO,
                            null, true);
                }
            });
        }
    }
}
