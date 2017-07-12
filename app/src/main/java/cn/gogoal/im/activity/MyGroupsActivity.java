package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.dialog.ShareMessageDialog;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/5/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :我的群组
 */
public class MyGroupsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.tv_to_search)
    DrawableCenterTextView tv_to_search;

    private ListAdapter listAdapter;
    private List<GroupData> dataBeans;

    private int actionType;
    private GGShareEntity entity;

    @Override
    public int bindLayout() {
        return R.layout.activity_my_group;
    }

    @Override
    public void doBusiness(final Context mContext) {
        actionType = getIntent().getIntExtra("action_type", 0);
        XTitle xTitle = setMyTitle(R.string.title_square_collcet, true).setLeftText(R.string.tv_cancle);
        if (actionType == 0) {
            xTitle.addAction(new XTitle.TextAction(getString(R.string.square_collcet_add)) {
                @Override
                public void actionClick(View view) {
                    Intent intent = new Intent(mContext, SearchPersonSquareActivity.class);
                    intent.putExtra("search_index", 1);
                    startActivity(intent);
                }
            });
        } else {
            entity = getIntent().getParcelableExtra("share_web_data");
        }
        xLayout.setEmptyText("你还没有群组\n\r赶快找到属于你的组织吧");

        recyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));

        dataBeans = new ArrayList<>();
        listAdapter = new ListAdapter(dataBeans);
        recyclerView.setAdapter(listAdapter);
        getGroupList(AppConst.REFRESH_TYPE_FIRST);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupList(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.setRefreshing(false);
            }
        });

        tv_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IMSearchLocalActivity.class);
                startActivity(intent);
            }
        });

    }

    //收藏群列表
    public void getGroupList(final int type) {
        UserUtils.getMyGroupList(new Impl<String>() {
            @Override
            public void response(int code, String jsonData) {
                switch (code) {
                    case Impl.RESPON_DATA_SUCCESS:
                        dataBeans.clear();

                        List<GroupData> data =
                                JsonUtils.parseJsonArray(jsonData, GroupData.class);

//                                Arrays.asList(new Gson().fromJson(jsondata, GroupData[].class));

                        dataBeans.addAll(data);

                        listAdapter.notifyDataSetChanged();

                        xLayout.setStatus(XLayout.Success);

                        if (type == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                            UIHelper.toast(getActivity(), "更新群组数据成功");
                        }
                        break;
                    case Impl.RESPON_DATA_EMPTY:
                        xLayout.setStatus(XLayout.Empty);
                        break;
                    case Impl.RESPON_DATA_ERROR:
                        xLayout.setStatus(XLayout.Error);
                        UIHelper.toastError(getActivity(), jsonData, xLayout);
                        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                            @Override
                            public void onReload(View v) {
                                getGroupList(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                            }
                        });
                        break;
                }
            }
        });

    }

    private class ListAdapter extends CommonAdapter<GroupData, BaseViewHolder> {

        private Object groupAvatar;

        private ListAdapter(List<GroupData> datas) {
            super(R.layout.item_my_group_list, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final GroupData data, final int position) {
            final RoundedImageView imgAvatar = holder.getView(R.id.img_my_group_avatar);
//            final Bitmap[] groupAvatar = new Bitmap[1];

            final String groupUrl = data.getAttr().getAvatar();
            if (!TextUtils.isEmpty(groupUrl)) {//
                ImageDisplay.loadRoundedRectangleImage(getActivity(), groupUrl, imgAvatar);
                groupAvatar = groupUrl;
            } else {
                ChatGroupHelper.setGroupAvatar(data.getConv_id(), new AvatarTakeListener() {
                    @Override
                    public void success(final Bitmap bitmap) {
                        MyGroupsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgAvatar.setImageBitmap(bitmap);
                                groupAvatar = bitmap;
                            }
                        });
                    }

                    public void failed(Exception e) {
                    }
                });

            }

            holder.setText(R.id.tv_my_group_name_member_count, "(" + data.getM().size() + ")");
            holder.setText(R.id.tv_my_group_name, StringUtils.getNotNullString(data.getName()));
            holder.setText(R.id.tv_my_group_intro, StringUtils.getNotNullString(data.getAttr().getIntro()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionType == 0) {
                        //群聊处理
                        Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("conversation_id", data.getConv_id());
                        bundle.putString("squareName", data.getName());
                        bundle.putBoolean("need_update", false);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        IMMessageBean imMessageBean = new IMMessageBean(data.getConv_id(), AppConst.IM_CHAT_TYPE_SQUARE, new Date().getTime(), "0",
                                data.getName(), "", "", "",false);
                        ShareItemInfo shareItemInfo = new ShareItemInfo<>(groupAvatar, data.getName(), entity, imMessageBean);
                        ShareMessageDialog.newInstance(shareItemInfo).show(getSupportFragmentManager());
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (actionType == 0) {
                        DialogHelp.getSelectDialog(getActivity(), "", new String[]{"从通讯录移除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListAdapter.this.removeItem(position);
                                deleteGroup(data.getConv_id());
                            }
                        }, false).show();
                    }
                    return true;
                }
            });
        }

    }

    //取消群收藏
    public void deleteGroup(String conversationId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(MyGroupsActivity.this, "群已取消收藏!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CANCEL_COLLECT_GROUP, ggHttpInterface).startGet();
    }
}
