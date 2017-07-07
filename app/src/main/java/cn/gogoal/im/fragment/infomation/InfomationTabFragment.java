package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

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
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static cn.gogoal.im.R.id.recyclerView;

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

    @BindView(recyclerView)
    RecyclerView mRvInfomation;

    @BindView(R.id.swiperefreshlayout)
    RefreshLayout refreshlayout;

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
        mRvInfomation.setBackgroundColor(Color.WHITE);
        mRvInfomation.setLayoutManager(new LinearLayoutManager(mContext));
        mRvInfomation.addItemDecoration(new DashlineItemDivider());

        dataList = new ArrayList<>();
        adapter = new NormalInfoTabAdapter(dataList);
        mRvInfomation.setAdapter(adapter);

        tabArrays = Arrays.copyOfRange(tabArrays, 2, 7);

        tabType = getArguments().getInt("tab_type");

        apis = new SparseArray<>();

//        apis.put(INFOMATION_TYPE_GET_ASK_NEWS, GGOKHTTP.GET_ASK_NEWS);
        apis.put(INFOMATION_TYPE_SUN_BUSINESS, GGOKHTTP.SUN_BUSINESS);
        apis.put(INFOMATION_TYPE_PRIVATE_VIEW_POINT, GGOKHTTP.PRIVATE_VIEW_POINT);
        apis.put(INFOMATION_TYPE_SKY_VIEW_POINT, GGOKHTTP.SKY_VIEW_POINT);
        apis.put(INFOMATION_TYPE_POLICY_DYNAMICS, GGOKHTTP.POLICY_DYNAMICS);

        getInfomation(AppConst.REFRESH_TYPE_FIRST, tabType);

        refreshlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                defaultPage = 1;
                getInfomation(AppConst.REFRESH_TYPE_SWIPEREFRESH, tabType);
                refreshlayout.refreshComplete();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return ((LinearLayoutManager) rvMyStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0 ;
                return !mRvInfomation.canScrollVertically(-1);
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

        HashMap<String, String> params = new HashMap<>();
//        if (tabType == INFOMATION_TYPE_GET_ASK_NEWS) {
//            params.put("contains_top", String.valueOf(false));
//        }
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));

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

                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
                if (xLayout != null && refreshlayout != null) {
                    xLayout.setStatus(XLayout.Error);
                }
            }
        }).startGet();
    }

    @Subscriber(tag = "double_click_2_top")
    void doubleClick2Top(String index) {
        KLog.e(index);
        if (StringUtils.parseStringDouble(index) == tabType - 110) {//是本Tab
            mRvInfomation.smoothScrollToPosition(0);
        }
    }

    private class NormalInfoTabAdapter extends CommonAdapter<InfomationData.Data, BaseViewHolder> {

        private NormalInfoTabAdapter(List<InfomationData.Data> data) {
            super(R.layout.item_infomation_normal, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final InfomationData.Data data, int position) {
            holder.setText(R.id.tv_item_normal_info_title, data.getTitle());

            holder.setText(R.id.tv_item_normal_sub_title,
                    TextUtils.isEmpty(data.getSummary()) ? "" : data.getSummary());

            holder.setVisible(R.id.tv_item_normal_sub_title, tabType == INFOMATION_TYPE_SKY_VIEW_POINT);

            holder.setText(R.id.tv_item_normal_info_info,
                    String.format(Locale.getDefault(),
                            getString(R.string.infomation_bottom),
                            getMyDate(tabType, data.getDate()),
                            data.getSource()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS + data.getId() + "?source=" + tabType, "");
                }
            });
        }

        private String getMyDate(int tabType, String dateString) {
            if (StringUtils.isActuallyEmpty(dateString)) {
                return "";
            }
            switch (tabType) {
                case INFOMATION_TYPE_SUN_BUSINESS://朝阳会务
                    return dateString;
                case INFOMATION_TYPE_PRIVATE_VIEW_POINT://私募观点
                    return CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", dateString);
                case INFOMATION_TYPE_SKY_VIEW_POINT://天高视点
//                    SimpleDateFormat.getDateTimeInstance().parse()
                    return CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", dateString);

                case INFOMATION_TYPE_POLICY_DYNAMICS://政策动态
                    return dateString;
                default:
                    return dateString;
            }
        }
    }
}
