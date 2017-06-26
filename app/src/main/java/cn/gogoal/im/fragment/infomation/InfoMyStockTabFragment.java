package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.infomation.InfoMyStock;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯-自选股
 */
public class InfoMyStockTabFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int defaultPage = 1;

    private InfoMyStockAdapter adapter;
    private List<InfoMyStock.Data> dataList;


    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.iniRefresh(refreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DashlineItemDivider());

        dataList = new ArrayList<>();
        adapter = new InfoMyStockAdapter(dataList);
        recyclerView.setAdapter(adapter);

        getMyStockInfo(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyStockInfo(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                defaultPage = 1;
            }
        });
        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getMyStockInfo(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
        xLayout.setEmptyText("暂无自选股资讯，多添加一些自选股吧！");

        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    adapter.loadMoreEnd(false);
                    getMyStockInfo(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                adapter.loadMoreComplete();
            }
        }, recyclerView);
    }

    @Subscriber(tag = "double_click_2_top")
    void doubleClick2Top(String index) {
        if (StringUtils.parseStringDouble(index) == 0) {//是本Tab
            recyclerView.smoothScrollToPosition(0);
        }
    }

    private void getMyStockInfo(final int refreshType) {
        adapter.setEnableLoadMore(false);
        if (refreshType == AppConst.REFRESH_TYPE_FIRST || refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }
        if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
            defaultPage = 1;
        }
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCK_NEWS_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        dataList.clear();
                    }
                    List<InfoMyStock.Data> datas =
                            JSONObject.parseObject(responseInfo, InfoMyStock.class).getData();

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getActivity(), "自选资讯更新成功");
                    }

                    dataList.addAll(datas);

                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);


                } else if (code == 1001) {
                    if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
                        xLayout.setStatus(XLayout.Empty);
                    }
                } else {
                    xLayout.setStatus(XLayout.Error);
                }

                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
                    xLayout.setStatus(XLayout.Error);
                }
                refreshLayout.setRefreshing(false);
            }
        }).startGet();
    }

    private class InfoMyStockAdapter extends CommonAdapter<InfoMyStock.Data, BaseViewHolder> {

        private InfoMyStockAdapter(List<InfoMyStock.Data> data) {
            super(R.layout.item_infomation_mystock, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final InfoMyStock.Data data, int position) {
            holder.setText(R.id.tv_item_info_date$origin,
                    data.getDate() + "\u3000" + data.getOrigin());

            holder.setText(R.id.tv_item_info_content, data.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(
                            v.getContext(),
                            AppConst.WEB_NEWS + data.getOrigin_id() + "?source=111", "");
                }
            });
        }
    }
}
