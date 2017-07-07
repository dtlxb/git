package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.infomation.Sevenby24;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.ExpandableLayout;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯——7*24
 */
public class SevenBy24Fragment extends BaseFragment {

    private static final long INTERVAL_TIME = 5000;//定时刷新间隔(毫秒)

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefreshlayout)
    RefreshLayout refreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.tv_new_message)
    TextView tvNewMessageCount;

    //定时刷新
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, INTERVAL_TIME);

                get7by24NewsCount();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private int defaultPage = 1;

    private Sevenby24Adapter adapter;
    private List<Sevenby24.Data> dataList;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        dataList = new ArrayList<>();
        adapter = new Sevenby24Adapter(dataList);
        recyclerView.setAdapter(adapter);

        get7by24Datas(AppConst.REFRESH_TYPE_FIRST);

        refreshlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                defaultPage = 1;
                get7by24Datas(AppConst.REFRESH_TYPE_SWIPEREFRESH);
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return ((LinearLayoutManager) rvMyStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0 ;
                return !recyclerView.canScrollVertically(-1);
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
        }, recyclerView);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                get7by24Datas(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, INTERVAL_TIME);//启动定时刷新
    }

    private void get7by24Datas(final int refreshType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST || refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));
//        params.put("end_time", CalendarUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        new GGOKHTTP(params, GGOKHTTP.GET_FULL_TIME_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {

                    get7by24NewsCount();//查询总条数

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        dataList.clear();
                    }

                    List<Sevenby24.Data> datas =
                            JSONObject.parseObject(responseInfo, Sevenby24.class).getData();
//                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
//                        UIHelper.toast(getContext(), "7×24数据更新成功");
//                    }
                    dataList.addAll(datas);
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    if (refreshType != AppConst.REFRESH_TYPE_LOAD_MORE) {
                        xLayout.setStatus(XLayout.Empty);
                    } else {
                        UIHelper.toast(getActivity(), "没有更多");
                    }
                } else {
                    xLayout.setStatus(XLayout.Error);
                }

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.refreshComplete();
                    }
                },800);

            }

            @Override
            public void onFailure(String msg) {
                if (refreshlayout!=null)
                refreshlayout.refreshComplete();
                UIHelper.toastError(getContext(), msg, xLayout);
            }
        }).startGet();
    }

    private void get7by24NewsCount() {
        HashMap<String, String> map = new HashMap<>();
        map.put("get_count", "1");
        new GGOKHTTP(map, GGOKHTTP.GET_FULL_TIME_INFO, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
                if (JsonUtils.getIntValue(responseInfo, "code") == 0) {
                    JsonObject data = JsonUtils.getJsonArray(responseInfo, "data")
                            .get(0).getAsJsonObject();
                    long count = JsonUtils.getLongValue(data, "count");

                    long oldCount = SPTools.getLong("new_message_count", 0);
                    if (oldCount > 0 && count > oldCount) {
                        tvNewMessageCount.setVisibility(View.VISIBLE);
                        tvNewMessageCount.setText("有" + (count - oldCount) + "条更新");
                        SPTools.saveLong("new_message_count", count);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    @Subscriber(tag = "double_click_2_top")
    void doubleClick2Top(String index) {
        if (StringUtils.parseStringDouble(index) == 0) {//是本Tab
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @OnClick(R.id.tv_new_message)
    void click(View view) {
        recyclerView.smoothScrollToPosition(0);
        defaultPage = 1;
        get7by24Datas(AppConst.REFRESH_TYPE_SWIPEREFRESH);
        tvNewMessageCount.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
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
