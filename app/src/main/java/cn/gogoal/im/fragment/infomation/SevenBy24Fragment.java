package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
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
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.infomation.Sevenby24;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.ExpandableLayout;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯——7*24
 */
public class SevenBy24Fragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int defaultPage = 1;

    private Sevenby24Adapter adapter;
    private List<Sevenby24.Data> dataList;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.iniRefresh(refreshlayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        dataList = new ArrayList<>();
        adapter = new Sevenby24Adapter(dataList);
        recyclerView.setAdapter(adapter);

        get7by24Datas(AppConst.REFRESH_TYPE_FIRST);

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get7by24Datas(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                defaultPage=1;
            }
        });
        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    adapter.loadMoreEnd(false);
                    get7by24Datas(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                adapter.loadMoreComplete();
            }
        },recyclerView);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                get7by24Datas(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
    }

    private void get7by24Datas(final int refreshType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST || refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }

        if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH){
            defaultPage=1;//重置回1
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));

        new GGOKHTTP(params, GGOKHTTP.GET_FULL_TIME_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    xLayout.setStatus(XLayout.Success);
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        dataList.clear();
                    }

                    List<Sevenby24.Data> datas =
                            JSONObject.parseObject(responseInfo, Sevenby24.class).getData();
                    if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH){
                        UIHelper.toast(getContext(),"7×24数据更新成功");
                    }
                    dataList.addAll(datas);
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
                refreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                refreshlayout.setRefreshing(false);
            }
        }).startGet();
    }

    private class Sevenby24Adapter extends CommonAdapter<Sevenby24.Data, BaseViewHolder> {

        private Sevenby24Adapter(List<Sevenby24.Data> data) {
            super(R.layout.item_infomation_7by24, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, Sevenby24.Data data, int position) {
            ExpandableLayout expandableLayout = holder.getView(R.id.expanded_item_layout);
            expandableLayout.setText(data.getTitle(), position);

            holder.setText(R.id.tv_item_time, CalendarUtils.formatDate(
                    "yyyy-MM-dd HH:mm:ss", "HH:mm", data.getDate()));

            holder.setText(R.id.tv_item_date, CalendarUtils.formatDate(
                    "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", data.getDate()));
        }
    }
}
