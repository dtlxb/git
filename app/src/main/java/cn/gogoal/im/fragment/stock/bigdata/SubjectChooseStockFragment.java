package cn.gogoal.im.fragment.stock.bigdata;

import android.content.Context;
import android.content.Intent;
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
import cn.gogoal.im.activity.stock.SubjectDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.bigdata.subject.SubjectListBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主题选股
 */
public class SubjectChooseStockFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView rvSubject;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private SubjectChooseStockAdapter subjectChooseStockAdapter;
    private List<SubjectListBean.SubjectData> subjectDataList;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        rvSubject.setLayoutManager(new LinearLayoutManager(mContext));
        rvSubject.addItemDecoration(new NormalItemDecoration(mContext));
        subjectDataList = new ArrayList<>();
        subjectChooseStockAdapter = new SubjectChooseStockAdapter(subjectDataList);
        rvSubject.setAdapter(subjectChooseStockAdapter);

        getSubjectList(AppConst.REFRESH_TYPE_FIRST);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubjectList(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.setRefreshing(false);
            }
        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getSubjectList(AppConst.REFRESH_TYPE_RELOAD);
            }
        });
    }

    private void getSubjectList(final int refreshType) {
        if (refreshType != AppConst.REFRESH_TYPE_SWIPEREFRESH) {
            xLayout.setStatus(XLayout.Loading);
        }
        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("type", "0");
        params.put("get_hq", "1");
        new GGOKHTTP(params, GGOKHTTP.GET_RECOMMEND_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        subjectDataList.clear();
                    }

                    List<SubjectListBean.SubjectData> subjectDatas =
                            JSONObject.parseObject(responseInfo, SubjectListBean.class).getData();
                    subjectDataList.addAll(subjectDatas);
                    subjectChooseStockAdapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);

                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getActivity(), "更新数据成功");
                    }
                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null) {
                    xLayout.setStatus(XLayout.Error);
                }
            }
        }).startGet();
    }

    private class SubjectChooseStockAdapter extends CommonAdapter<SubjectListBean.SubjectData, BaseViewHolder> {

        private SubjectChooseStockAdapter(List<SubjectListBean.SubjectData> data) {
            super(R.layout.item_big_data_subject_list, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final SubjectListBean.SubjectData data, int position) {
            int value = (int) (data.getPrice());
            holder.setText(R.id.tv_item_subject_list_value,
                    value == 0 ? "--" : String.valueOf(value));

            holder.setBackgroundColor(R.id.layout_item_subject_list_head,
                    value >= 1000 ? 0xffff9233 : 0xfffebd23);

            holder.setText(R.id.tv_item_subject_list_rate,
                    data.getPrice_change_rate() == 0 ? "--" : StockUtils.plusMinus(data.getPrice_change_rate(), false));

            holder.setText(R.id.tv_item_subject_list_theme_name, data.getTheme_name());
            holder.setText(R.id.tv_item_subject_list_date, "发现时间：" + data.getInsert_time());
            holder.setText(R.id.tv_item_subject_list_title, data.getTitle());
            holder.setText(R.id.tv_item_subject_list_tag1, data.getTags().get(0));
            holder.setText(R.id.tv_item_subject_list_tag2, data.getTags().get(1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SubjectDetailActivity.class);
                    intent.putExtra("subject_id", String.valueOf(data.getId()));
                    intent.putExtra("subject_type", data.getTheme_name());
                    startActivity(intent);
                }
            });
        }
    }
}
