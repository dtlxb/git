package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.SquareCardActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.RecommendBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

public class SearchTeamFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private RecommendAdapter adapter;

    private ArrayList<RecommendBean.DataBean> dataBeanList;
    private List<String> groupMembers;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {

        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        dataBeanList = new ArrayList<>();
        adapter = new RecommendAdapter(dataBeanList);
        groupMembers = new ArrayList<>();

        TextView headView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headView.setGravity(Gravity.CENTER_VERTICAL);
        headView.setPadding(AppDevice.dp2px(mContext, 15), AppDevice.dp2px(mContext, 10), 0,
                AppDevice.dp2px(mContext, 10));
        headView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        headView.setTextColor(getResColor(R.color.textColor_333333));
        headView.setText("推荐");
        headView.setLayoutParams(params);
        adapter.addHeaderView(headView);

        recyclerView.setAdapter(adapter);

        getRecommendGroup("");

    }

    @Subscriber(tag = "SEARCH_TEAM_TAG")
    private void getRecommendGroup(final String keyword) {
        dataBeanList.clear();
        xLayout.setStatus(XLayout.Loading);
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());
        if (!TextUtils.isEmpty(keyword)) {
            map.put("keyword", keyword);
        }
        map.put("is_recommend", String.valueOf(TextUtils.isEmpty(keyword)));

        new GGOKHTTP(map, GGOKHTTP.SEARCH_GROUP, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    RecommendBean recommendBean = JSONObject.parseObject(responseInfo, RecommendBean.class);
                    if (null != recommendBean.getData()) {
                        dataBeanList.addAll(recommendBean.getData());
                        adapter.notifyDataSetChanged();
                        xLayout.setStatus(XLayout.Success);
                    }
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code") == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                    xLayout.setEmptyText(String.format(getString(R.string.str_result), keyword) + "群组");
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                    @Override
                    public void onReload(View v) {
                        getRecommendGroup(keyword);
                    }
                });

                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }

    private class RecommendAdapter extends CommonAdapter<RecommendBean.DataBean, BaseViewHolder> {

        RecommendAdapter(List<RecommendBean.DataBean> datas) {
            super(R.layout.item_search_type_persion, datas);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final RecommendBean.DataBean data, final int position) {
            TextView addView = holder.getView(R.id.btn_search_group_add);

            final View itemView = holder.itemView;

            if (data.getM() == null || data.getM().isEmpty()) {
                itemView.setClickable(false);
                itemView.setEnabled(false);
            } else {
                itemView.setClickable(true);
                itemView.setEnabled(true);
            }

            addView.setVisibility(View.VISIBLE);
            if (data.isIs_in()) {
                addView.setBackgroundColor(Color.TRANSPARENT);
                addView.setText("已加入");
                addView.setTextColor(Color.parseColor("#a9a9a9"));
                addView.setClickable(false);
                addView.setEnabled(false);
            }else {
                addView.setBackgroundResource(R.drawable.shape_search_group_add_btn);
                addView.setText("加入");
                addView.setTextColor(Color.parseColor("#a9a9a9"));
                addView.setClickable(true);
                addView.setEnabled(true);
            }
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 申请加入
                }
            });

            Glide.get(getContext()).clearMemory();


            GroupFaceImage.getInstance(getActivity(), getImageAvatar(data.getM())
            ).load(new GroupFaceImage.OnMatchingListener() {
                @Override
                public void onSuccess(final Bitmap mathingBitmap) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.setImageBitmap(R.id.item_user_avatar, mathingBitmap);
                        }
                    });

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(v.getContext(), SquareChatRoomActivity.class);
                            if (data.isIs_in()) {//我在群里
                                // TODO: 进入聊天
                                in.putExtra("squareName", data.getName());
                                in.putExtra("conversation_id", data.getConv_id());
                                startActivity(in);
                            } else {//TODO: 申请加群
                                in = new Intent(getActivity(), SquareCardActivity.class);
                                if (position > 0) {
                                    for (int i = 0; i < dataBeanList.get(position - 1).getM().size(); i++) {
                                        groupMembers.add(String.valueOf(dataBeanList.get(position - 1).getM().get(i).getAccount_id()));
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("conversation_id", dataBeanList.get(position - 1).getConv_id());
                                    bundle.putString("square_name", dataBeanList.get(position - 1).getName());
                                    bundle.putParcelable("bitmap_avatar", mathingBitmap);
                                    bundle.putString("square_creater", dataBeanList.get(position - 1).getC());
                                    bundle.putSerializable("square_members", (Serializable) dataBeanList.get(position - 1).getM());
                                    in.putExtras(bundle);
                                    startActivity(in);
                                }
                            }
                        }
                    });

                }

                @Override
                public void onError(Exception e) {

                }
            });

            UIHelper.setRippBg(itemView);

            holder.setText(R.id.item_tv_search_result_intro, TextUtils.isEmpty(data.getAttr().getIntro()) ? "暂无群简介" : data.getAttr().getIntro());

            holder.setText(R.id.item_tv_search_result_name, data.getName() +
                    String.format(getString(R.string.str_group_count), null == data.getM() ? 0 : data.getM().size()));

        }
    }

    private List<String> getImageAvatar(List<RecommendBean.DataBean.MBean> datas) {
        List<String> li = new ArrayList<>();
        if (null != datas && !datas.isEmpty()) {
            for (RecommendBean.DataBean.MBean b : datas) {
                li.add(b.getAvatar());
            }
            return li;
        }
        return new ArrayList<>();
    }
}
