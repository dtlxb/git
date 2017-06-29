package cn.gogoal.im.fragment.stock.bigdata;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.EventDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.bean.stock.bigdata.event.EventListBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.TextDrawable;
import cn.gogoal.im.ui.view.XLayout;

import static cn.gogoal.im.R.id.recyclerView;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :事件选股
 */
public class EventChooseStockFragment extends BaseFragment {
    @BindView(recyclerView)
    RecyclerView rvEventList;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private List<EventListBean.EventListData> eventListDatas;
    private EventAdapter eventAdapter;
    private int defaultPage = 1;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        eventListDatas = new ArrayList<>();
        eventAdapter = new EventAdapter(eventListDatas);

        BaseActivity.iniRefresh(swiperefreshlayout);
        rvEventList.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration decoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_divider_15dp));
        rvEventList.addItemDecoration(decoration);
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
                defaultPage=1;
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
                defaultPage=1;
                getEventListData(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
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
                    eventListDatas.addAll(datas);
                    eventAdapter.notifyDataSetChanged();

                    eventAdapter.setEnableLoadMore(true);
                    eventAdapter.loadMoreComplete();

//                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
//                        UIHelper.toast(getActivity(), "数据更新成功!");
//                    }

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

    private class EventAdapter extends CommonAdapter<EventListBean.EventListData, BaseViewHolder> {

        private int[] headColor = {0xffff691d, 0xfff34858, 0xff599efd, 0xfffe9b1a};

        private EventAdapter(List<EventListBean.EventListData> data) {
            super(R.layout.item_big_data_event_list, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final EventListBean.EventListData data, int position) {
            holder.setImageDrawable(R.id.iv_item_event_list_name_icon, getTextDrawable("热点",
                    headColor[position % 4]));

            TextView tvTitleText = holder.getView(R.id.tv_item_event_list_name);
            tvTitleText.setText(data.getEvent_type());


            holder.setText(R.id.tv_item_event_list_date, CalendarUtils.formatDate(
                    "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", data.getDate()));

            holder.setText(R.id.tv_item_event_list_title, data.getTitle());

            RecyclerView rvStock = holder.getView(R.id.rv_inner_event);
            rvStock.setNestedScrollingEnabled(false);
            rvStock.setHasFixedSize(true);
            rvStock.addItemDecoration(new NormalItemDecoration(getContext()));
//            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
//            layoutManager.setAutoMeasureEnabled(true);
//            layoutManager.setSmoothScrollbarEnabled(true);
//            rvStock.setLayoutManager(layoutManager);
            rvStock.addItemDecoration(new NormalItemDecoration(getActivity()));
            rvStock.setAdapter(new CommonAdapter<Stock, BaseViewHolder>(
                    R.layout.item_subject_event_stock_about, data.getCatalog()) {
                @Override
                protected void convert(BaseViewHolder holder, final Stock stockData, int position) {
                    final ImageView ivAddToggle = holder.getView(R.id.iv_item_subject_about_add_stock);

                    ivAddToggle.setImageResource(StockUtils.isMyStock(stockData.getStock_code()) ?
                            R.mipmap.not_choose_stock : R.mipmap.choose_stock);

                    ivAddToggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StockUtils.toggleAddDelStock(stockData.getStock_code(), ivAddToggle);
                        }
                    });

                    holder.setText(R.id.tv_item_subject_about_name, stockData.getStock_name());

                    holder.setText(R.id.tv_item_subject_about_code, stockData.getStock_code());

                    holder.setText(R.id.tv_item_subject_about_price,
                            StringUtils.save2Significand(stockData.getStock_price()));
                    holder.setTextResColor(R.id.tv_item_subject_about_price, StockUtils.getStockRateColor(
                            stockData.getStock_rate()));

                    holder.setText(R.id.tv_item_subject_about_rate,
                            StockUtils.plusMinus(stockData.getStock_rate(), true));
                    holder.setTextResColor(R.id.tv_item_subject_about_rate, StockUtils.getStockRateColor(
                            stockData.getStock_rate()));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NormalIntentUtils.go2StockDetail(v.getContext(),
                                    stockData.getStock_code(), stockData.getStock_name());
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
                    intent.putExtra("event_type_id", data.getId());
                    intent.putExtra("event_type_title", data.getEvent_type());
                    startActivity(intent);
                }
            });
        }

        private Drawable getTextDrawable(String text, int bgColor) {
            TextDrawable.IBuilder iBuilder = TextDrawable
                    .builder()
                    .beginConfig()
                    .fontSize(AppDevice.sp2px(getActivity(), 12))
                    .endConfig().round();
            return iBuilder.build(text, bgColor);
        }

    }

}
