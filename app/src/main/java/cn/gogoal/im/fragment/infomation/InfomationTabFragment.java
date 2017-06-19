package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯模块每个tab
 */
public class InfomationTabFragment extends BaseFragment {

    public static final int INFOMATION_TYPE_GET_ASK_NEWS = 116;//资讯——要闻

    public static final int INFOMATION_TYPE_SUN_BUSINESS = 112;//资讯——朝阳会务

    public static final int INFOMATION_TYPE_PRIVATE_VIEW_POINT = 113;//资讯——私募观点

    public static final int INFOMATION_TYPE_SKY_VIEW_POINT = 114;//资讯——天高视点

    public static final int INFOMATION_TYPE_POLICY_DYNAMICS = 115;//资讯——政策动态

    @BindView(R.id.recyclerView)
    RecyclerView mRvInfomation;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout refreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindArray(R.array.info_arrar)
    String[] tabArrays;

    private int defaultPage = 1;

    private int tabType;//tab

    private SparseArray<String> apis;

    private NormalInfoTabAdapter adapter;

    private List<InfomationData.Data> dataList;

    public static InfomationTabFragment newInstance(int tabType) {
        InfomationTabFragment tabFragment = new InfomationTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab_type", tabType);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.iniRefresh(refreshlayout);

        mRvInfomation.setBackgroundColor(Color.WHITE);
        mRvInfomation.setLayoutManager(new LinearLayoutManager(mContext));
        mRvInfomation.addItemDecoration(new NormalItemDecoration(mContext));

        dataList = new ArrayList<>();
        adapter = new NormalInfoTabAdapter(dataList);
        mRvInfomation.setAdapter(adapter);

        tabArrays = Arrays.copyOfRange(tabArrays, 2, 7);

        tabType = getArguments().getInt("tab_type");
        apis = new SparseArray<>();

        apis.put(INFOMATION_TYPE_GET_ASK_NEWS, GGOKHTTP.GET_ASK_NEWS);
        apis.put(INFOMATION_TYPE_SUN_BUSINESS, GGOKHTTP.SUN_BUSINESS);
        apis.put(INFOMATION_TYPE_PRIVATE_VIEW_POINT, GGOKHTTP.PRIVATE_VIEW_POINT);
        apis.put(INFOMATION_TYPE_SKY_VIEW_POINT, GGOKHTTP.SKY_VIEW_POINT);
        apis.put(INFOMATION_TYPE_POLICY_DYNAMICS, GGOKHTTP.POLICY_DYNAMICS);

        getInfomation(AppConst.REFRESH_TYPE_FIRST, tabType);

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfomation(AppConst.REFRESH_TYPE_SWIPEREFRESH, tabType);
            }
        });

        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    adapter.loadMoreEnd(false);
                    getInfomation(AppConst.REFRESH_TYPE_LOAD_MORE, tabType);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                adapter.loadMoreComplete();
            }
        }, mRvInfomation);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getInfomation(AppConst.REFRESH_TYPE_RELOAD, tabType);
            }
        });
    }

    /**
     * @param refreshType 刷新类型
     * @param tabType     加载类型：要闻（@see INFOMATION_TYPE_GET_ASK_NEWS）、
     *                    朝阳会务（INFOMATION_TYPE_SUN_BUSINESS）等等
     */
    private void getInfomation(final int refreshType, final int tabType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }
        if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH){
            defaultPage=1;
        }

        HashMap<String, String> params = new HashMap<>();
//        params.put("end_time", CalendarUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));

        KLog.e(StringUtils.map2ggParameter(params));

        new GGOKHTTP(params, apis.get(tabType), new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        dataList.clear();
                    }

                    List<InfomationData.Data> datas =
                            JSONObject.parseObject(responseInfo, InfomationData.class).getData();

                    dataList.addAll(datas);
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();

                    adapter.notifyDataSetChanged();

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getActivity(), tabArrays[tabType - (
                                tabType == INFOMATION_TYPE_GET_ASK_NEWS ? 116 : 111
                        )] + "数据刷新成功");
                    }

                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
                refreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                xLayout.setStatus(XLayout.Error);
                refreshlayout.setRefreshing(false);
            }
        }).startGet();
    }

    private class NormalInfoTabAdapter extends CommonAdapter<InfomationData.Data, BaseViewHolder> {

        private NormalInfoTabAdapter(List<InfomationData.Data> data) {
            super(R.layout.item_infomation_normal, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final InfomationData.Data data, int position) {
            holder.setText(R.id.tv_item_normal_info_title, tabType == INFOMATION_TYPE_SKY_VIEW_POINT ?
                    data.getSummary() : data.getTitle());

            holder.setText(R.id.tv_item_normal_info_info,
                    String.format(Locale.getDefault(),
                            getString(R.string.infomation_bottom),
                            data.getDate(),
                            data.getSource()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS + data.getId() + "?source=" + tabType, "");
                }
            });
        }
    }
}
