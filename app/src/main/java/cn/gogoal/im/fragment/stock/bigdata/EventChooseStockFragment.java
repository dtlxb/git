package cn.gogoal.im.fragment.stock.bigdata;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.EventDetailActivity;
import cn.gogoal.im.adapter.EventStcokAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.SectionEventStockData;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.bean.stock.bigdata.event.EventListBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.TextDrawable;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :事件选股
 */
public class EventChooseStockFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView rvEventList;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private List<SectionEventStockData> eventListDatas;
    private EventStcokAdapter eventAdapter;
    private int defaultPage = 1;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        eventListDatas = new ArrayList<>();
        eventAdapter = new EventStcokAdapter(mContext, eventListDatas);

        BaseActivity.iniRefresh(swiperefreshlayout);
        rvEventList.setLayoutManager(new LinearLayoutManager(mContext));
        rvEventList.setAdapter(eventAdapter);

        getEventListData(AppConst.REFRESH_TYPE_FIRST);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getEventListData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                defaultPage = 1;
                getEventListData(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.setRefreshing(false);
            }
        });

        eventAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    getEventListData(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    eventAdapter.loadMoreEnd(true);
                    eventAdapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), "没有更多数据");
                }
                eventAdapter.loadMoreComplete();
            }
        }, rvEventList);

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                defaultPage = 1;
                getEventListData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getEventListData(-1);
    }

    private void getEventListData(final int refreshType) {
        if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
            xLayout.setStatus(XLayout.Loading);
        }
        eventAdapter.setEnableLoadMore(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("get_related_stock", "1");
        map.put("rows", String.valueOf(15));
        map.put("page", String.valueOf(defaultPage));

        new GGOKHTTP(map, GGOKHTTP.GET_HOTS_HEADLINES_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType != AppConst.REFRESH_TYPE_LOAD_MORE) {
                        eventListDatas.clear();
                    }
                    List<EventListBean.EventListData> datas =
                            JSONObject.parseObject(responseInfo, EventListBean.class).getData();

                    for (int i=0;i<datas.size();i++) {
                        //json array
                        EventListBean.EventListData eventData = datas.get(i);
                        //section title
                        SectionEventStockData.EventTitle eventTitle =
                                new SectionEventStockData.EventTitle(i,eventData.getEvent_type(),
                                        eventData.getDate(),eventData.getTitle());
                        eventTitle.setId(eventData.getId());
                        //title put in section
                        eventListDatas.add(
                                new SectionEventStockData(true, eventTitle));
                        List<Stock> stockList = eventData.getCatalog();
                        //stock list put in section
                        for (Stock stock : stockList) {
                            eventListDatas.add(new SectionEventStockData(stock));
                        }
                    }

                    eventAdapter.notifyDataSetChanged();

                    eventAdapter.setEnableLoadMore(true);
                    eventAdapter.loadMoreComplete();

//                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
//                        UIHelper.toast(getActivity(), "数据更新成功!");
//                    }

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
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null)
                    xLayout.setStatus(XLayout.Error);
            }
        }).startGet();
    }

}
