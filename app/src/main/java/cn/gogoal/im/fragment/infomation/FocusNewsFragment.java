package cn.gogoal.im.fragment.infomation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.calender.CaledarAdapter;
import cn.gogoal.im.ui.calender.CalendarBean;
import cn.gogoal.im.ui.calender.CalendarDateView;
import cn.gogoal.im.ui.calender.CalendarUtil;
import cn.gogoal.im.ui.calender.CalendarView;
import cn.gogoal.im.ui.view.XLayout;

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

//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;

    @BindView(R.id.rv_focus_news)
    RecyclerView rvNews;

    @BindView(R.id.tv_current_month)
    TextView mTitle;
    private ArrayList<InfomationData.Data> dataList;
    private FocusNewsAdapter adapter;
    private int defaultPage=1;

    @Override
    public int bindLayout() {
        return R.layout.fragment_focus_news;
    }

    @Override
    public void doBusiness(final Context mContext) {
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            AppDevice.dp2px(mContext, 48), AppDevice.dp2px(mContext, 48));
                    convertView.setLayoutParams(params);
                }

                view = (TextView) convertView.findViewById(R.id.text);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }

                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                mTitle.setText(bean.year + "/" + getDisPlayNumber(bean.moth) + "/" + getDisPlayNumber(bean.day));
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        mTitle.setText(data[0] + "/" + data[1] + "/" + data[2]);

        iniList();

    }

    private void iniList() {
        rvNews.setBackgroundColor(Color.WHITE);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.addItemDecoration(new DashlineItemDivider());

        dataList = new ArrayList<>();
        adapter = new FocusNewsAdapter(dataList);
        rvNews.setAdapter(adapter);

        getNewsData(AppConst.REFRESH_TYPE_FIRST);

//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                defaultPage=1;
//                getNewsData(AppConst.REFRESH_TYPE_SWIPEREFRESH);
//            }
//        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                defaultPage=1;
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

    //双位
    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }


    private void getNewsData(final int refreshType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", String.valueOf(15));

        new GGOKHTTP(params, GGOKHTTP.GET_ASK_NEWS, new GGOKHTTP.GGHttpInterface() {
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
                    if (xLayout!=null)
                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    if (xLayout!=null)
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    if (xLayout!=null)
                    xLayout.setStatus(XLayout.Error);
                }
//                if (refreshLayout!=null)
//                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);
//                if (xLayout != null && refreshLayout != null) {
//                    xLayout.setStatus(XLayout.Error);
//                    re
//                }
            }
        }).startGet();
    }

    /*适配器*/
    private class FocusNewsAdapter extends CommonAdapter<InfomationData.Data, BaseViewHolder> {

        private FocusNewsAdapter(List<InfomationData.Data> data) {
            super(R.layout.item_infomation_normal, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final InfomationData.Data data, int position) {
            holder.setText(R.id.tv_item_normal_info_title, data.getTitle());

            holder.setText(R.id.tv_item_normal_sub_title,
                    TextUtils.isEmpty(data.getSummary()) ? "" : data.getSummary());

            holder.setText(R.id.tv_item_normal_info_info,
                    String.format(Locale.getDefault(),
                            getString(R.string.infomation_bottom),
                            data.getDate(),
                            data.getSource()));

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
