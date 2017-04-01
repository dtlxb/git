package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.wrapper.HeaderAndFooterWrapper;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.RecommendBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;

public class SearchTeamFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecommendAdapter adapter;

    private ArrayList<RecommendBean.DataBean> dataBeanList;
    private HeaderAndFooterWrapper wrapper;

    @Override
    public int bindLayout() {
        return R.layout.fragment_search_team;
    }

    @Override
    public void doBusiness(Context mContext) {
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        dataBeanList=new ArrayList<>();
        adapter=new RecommendAdapter(dataBeanList);

        wrapper=new HeaderAndFooterWrapper(adapter);
        TextView headView= new TextView(mContext);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headView.setGravity(Gravity.CENTER_VERTICAL);
        headView.setPadding(AppDevice.dp2px(mContext,15),AppDevice.dp2px(mContext,10),0,
                AppDevice.dp2px(mContext,10));
        headView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        headView.setTextColor(getResColor(R.color.textColor_333333));
        headView.setText("推荐");
        headView.setLayoutParams(params);
        wrapper.addHeaderView(headView);

        recyclerView.setAdapter(wrapper);

        getRecommendGroup("");
    }

    @Subscriber(tag = "SEARCH_TEAM_TAG")
    private void getRecommendGroup(String keyword) {
        dataBeanList.clear();

        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        if (!TextUtils.isEmpty(keyword)) {
            map.put("keyword", keyword);
        }
        map.put("is_recommend", "true");

        KLog.e("token=" + UserUtils.getToken() + "&");

        new GGOKHTTP(map, GGOKHTTP.SEARCH_GROUP, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    RecommendBean recommendBean = JSONObject.parseObject(responseInfo, RecommendBean.class);
                    if (null!=recommendBean.getData()){
                        dataBeanList.addAll(recommendBean.getData());
                        adapter.notifyDataSetChanged();
                        wrapper.notifyDataSetChanged();
                    }

                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        }).startGet();
    }

    private class RecommendAdapter extends CommonAdapter<RecommendBean.DataBean>{

        RecommendAdapter(List<RecommendBean.DataBean> datas) {
            super(getActivity(), R.layout.item_search_type_persion, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final RecommendBean.DataBean data, int position) {
            TextView addView = holder.getView(R.id.btn_search_group_add);
            addView.setVisibility(View.VISIBLE);
            if (data.isIs_in()){
                addView.setBackgroundColor(Color.TRANSPARENT);
                addView.setText("已加入");
                addView.setTextColor(Color.parseColor("#a9a9a9"));
            }

            UIHelper.setRippBg(holder.itemView);
            holder.setText(R.id.item_tv_search_result_name,data.getName());
            holder.setText(R.id.item_tv_search_result_count,
                    String.format(getString(R.string.str_group_count),data.getM().size()));
            holder.setText(R.id.item_tv_search_result_intro,TextUtils.isEmpty(data.getAttr().getIntro())?"暂无群简介":data.getAttr().getIntro());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.isIs_in()){//我在群里
                        // TODO: 进入聊天
                        Intent in=new Intent(v.getContext(), SquareChatRoomActivity.class);
                        in.putExtra("squareName",data.getName());
                        in.putExtra("conversation_id",data.getConv_id());
                        startActivity(in);
                    }else {//TODO: 申请加群

                    }
                }
            });

        }
    }
}
