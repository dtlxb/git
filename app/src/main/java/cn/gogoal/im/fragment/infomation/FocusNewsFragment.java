package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static cn.gogoal.im.fragment.infomation.InfomationTabFragment.INFOMATION_TYPE_GET_ASK_NEWS;

/**
 * author wangjd on 2017/7/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :要闻
 */
public class FocusNewsFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.swipeRefreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.rv_focus_news)
    RecyclerView rvNews;

    private ArrayList<InfomationData.Data> dataList;
    private FocusNewsAdapter adapter;
    private int defaultPage = 1;

    private String dateString;

    @Override
    public int bindLayout() {
        return R.layout.fragment_focus_news;
    }

    @Override
    public void doBusiness(final Context mContext) {
        rvNews.setBackgroundColor(Color.WHITE);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvNews.addItemDecoration(new DashlineItemDivider());
        rvNews.addItemDecoration(new NormalItemDecoration(mContext));

        dataList = new ArrayList<>();
        adapter = new FocusNewsAdapter(dataList);
        rvNews.setAdapter(adapter);

        getNewsData(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                defaultPage = 1;
                dateString = null;
                getNewsData(AppConst.REFRESH_TYPE_SWIPEREFRESH);
            }
        });

        xLayout.setEmptyText("该日没有更多要闻了!\n点击查看全部要闻");
        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                defaultPage = 1;
                dateString=null;
                getNewsData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
        xLayout.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultPage = 1;
                dateString=null;
                getNewsData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });

        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    adapter.loadMoreEnd(false);
                    getNewsData(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                adapter.loadMoreComplete();
            }
        }, rvNews);
    }

    @Subscriber(tag = "focus_news_in_date")
    void focusNewsInDate(String msg) {
        dateString=msg;
        defaultPage=1;
        getNewsData(AppConst.REFRESH_TYPE_RELOAD);
    }

    private void getNewsData(final int refreshType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(defaultPage));
        if (dateString != null) {
            params.put("start_date", dateString);
            params.put("end_date", dateString);
        }
        params.put("rows", String.valueOf(15));

        new GGOKHTTP(params, GGOKHTTP.GET_ASK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JsonUtils.getIntValue(responseInfo, "code");

                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH ||
                            refreshType == AppConst.REFRESH_TYPE_RELOAD) {
                        dataList.clear();
                    }

                    List<InfomationData.Data> datas =
                            JsonUtils.parseJsonObject(responseInfo, InfomationData.class).getData();

                    dataList.addAll(datas);
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();

                    adapter.notifyDataSetChanged();
                    if (xLayout != null)
                        xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    if (refreshType!=AppConst.REFRESH_TYPE_LOAD_MORE) {
                        if (xLayout != null)
                            xLayout.setStatus(XLayout.Empty);
                    }else {
                        UIHelper.toast(getActivity(),"该日没有更多要闻了!");
                    }
                } else {
                    if (xLayout != null)
                        xLayout.setStatus(XLayout.Error);
                }
                if (refreshLayout!=null)
                refreshLayout.refreshComplete();
            }

            @Override
            public void onFailure(String msg) {
//                KLog.e(msg);
//                if (xLayout != null && refreshLayout != null) {
//                    xLayout.setStatus(XLayout.Error);
//                    re
//                }
            }
        }).startGet();
    }

    @Subscriber(tag = "double_click_2_top")
    void doubleClick2Top(String index){
        if (StringUtils.parseStringDouble(index)==1){//是本Tab
            rvNews.smoothScrollToPosition(0);
        }
    }

    /*适配器*/
    private class FocusNewsAdapter extends CommonAdapter<InfomationData.Data, BaseViewHolder> {

        private FocusNewsAdapter(List<InfomationData.Data> data) {
            super(R.layout.item_infomation_normal, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final InfomationData.Data data, int position) {
            holder.setText(R.id.tv_item_normal_info_title, data.getTitle());

            holder.setText(R.id.tv_item_normal_header_title,
                    TextUtils.isEmpty(data.getSummary()) ? "" : data.getSummary());

            ImageView newsImage = holder.getView(R.id.iv_news_iamge);

            if (StringUtils.isActuallyEmpty(data.getImage())) {
                newsImage.setVisibility(View.GONE);
                holder.setText(R.id.tv_item_normal_info_info,
                        data.getDate() + "|" + data.getOrigin());
            } else {
                newsImage.setVisibility(View.VISIBLE);
                ImageDisplay.loadImage(getContext(),data.getImage(),newsImage);
                holder.setText(R.id.tv_item_normal_info_info,
                        data.getDate()+" | "+(AppDevice.isLowDpi() ? "\n" : "") +
                                StringUtils.getNotNullString(data.getOrigin()));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS + data.getId() + "?source=" + INFOMATION_TYPE_GET_ASK_NEWS, "");
                }
            });

        }
    }
}
