package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/3/29.
 */

public class SquareCollectActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView squareRoomRecycler;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private ListAdapter listAdapter;

    private List<JSONObject> groupList;
    private JSONArray groupsArray;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(final Context mContext) {

        setMyTitle(R.string.title_square_collcet, true).setLeftText(R.string.tv_cancle).addAction(new XTitle.TextAction(getString(R.string.square_collcet_add)) {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(mContext, SearchPersonSquareActivity.class);
                intent.putExtra("search_index", 1);
                startActivity(intent);
            }
        });

        xLayout.setEmptyText("你还没有群组\n\r赶快找到属于你的组织吧");
        initRecycleView(squareRoomRecycler, R.drawable.shape_divider_1px);

        groupsArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_groups_saved", new JSONArray());
        Boolean needRefresh = SPTools.getBoolean("squareNeedRefresh", false);
        KLog.e(groupsArray.toString());

        groupList = new ArrayList<>();
        if (needRefresh) {
            getGroupList();
        } else {
            if (groupsArray.size() > 0) {
                for (int i = 0; i < groupsArray.size(); i++) {
                    groupList.add((JSONObject) groupsArray.get(i));
                }
                xLayout.setStatus(XLayout.Success);
            } else {
                xLayout.setStatus(XLayout.Empty);
            }
        }
        KLog.e(groupList);
        listAdapter = new ListAdapter(SquareCollectActivity.this, R.layout.item_square_collect, groupList);
        squareRoomRecycler.setAdapter(listAdapter);

        //短按跳转
        listAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                //群聊处理
                Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("conversation_id", groupList.get(position).getString("conv_id"));
                bundle.putString("squareName", groupList.get(position).getString("name"));
                bundle.putBoolean("need_update", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //长按删除
        listAdapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(CommonAdapter adapter, View view, final int position) {
                DialogHelp.getSelectDialog(getActivity(), "", new String[]{"标为未读", "置顶聊天", "删除聊天"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            JSONObject jsonObject = groupList.get(position);
                            deleteGroup(jsonObject.getString("conv_id"));

                            groupsArray.remove(position);
                            groupList.remove(position);
                            SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_groups_saved", groupsArray);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }, false).show();
                return false;
            }
        });

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
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(SquareCollectActivity.this, "群已取消收藏!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CANCEL_COLLECT_GROUP, ggHttpInterface).startGet();
    }

    //收藏群列表
    public void getGroupList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        xLayout.setStatus(XLayout.Loading);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                SPTools.clearItem("squareNeedRefresh");
                if ((int) result.get("code") == 0) {
                    JSONArray newGroupArray = (JSONArray) result.get("data");
                    for (int i = 0; i < newGroupArray.size(); i++) {
                        groupList.add((JSONObject) newGroupArray.get(i));
                    }
                    xLayout.setStatus(XLayout.Success);
                    listAdapter.notifyDataSetChanged();
                    SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_groups_saved", newGroupArray);
                    groupsArray.addAll(newGroupArray);
                } else {
                    xLayout.setStatus(XLayout.Empty);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_LIST, ggHttpInterface).startGet();
    }

    private class ListAdapter extends CommonAdapter<JSONObject, BaseViewHolder> {

        private ListAdapter(Context context, int layoutId, List<JSONObject> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, JSONObject groupObject, int position) {
            ImageView avatarIv = holder.getView(R.id.head_image);
            TextView timeView = holder.getView(R.id.last_time);
            TextView nameView = holder.getView(R.id.whose_message);
            nameView.setMaxWidth(AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 120));
            KLog.e(groupObject.toString());
            if (ImageUtils.getBitmapFilePaht(groupObject.getString("conv_id"), "imagecache").equals("")) {
                JSONArray accountArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + groupObject.getString("conv_id") + "_accountList_beans", null);
                if (null == accountArray) {
                    getNicePicture(groupObject.getString("conv_id"), position);
                } else {
                    List<String> urls = new ArrayList<>();
                    for (int i = 0; i < accountArray.size(); i++) {
                        JSONObject accountObject = accountArray.getJSONObject(i);
                        urls.add(accountObject.getString("avatar"));
                    }
                    getNicePicture(urls, groupObject.getString("conv_id"), String.valueOf(position));
                }
            } else {
                ImageDisplay.loadImage(getActivity(), new File(ImageUtils.getBitmapFilePaht(groupObject.getString("conv_id"), "imagecache")), avatarIv);
            }
            holder.setText(R.id.last_message, groupObject.getJSONObject("attr").getString("intro") != null ? groupObject.getJSONObject("attr").getString("intro") : "");
            nameView.setText(groupObject.getString("name"));
            timeView.setText("(" + groupObject.getString("m_size") + ")");
        }
    }

    private void getNicePicture(final String ConversationId, final int position) {
        AVImClientManager.getInstance().findConversationById(ConversationId, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                UserUtils.getChatGroup(AppConst.CHAT_GROUP_CONTACT_BEANS, conversation.getMembers(), ConversationId, new UserUtils.getSquareInfo() {
                    @Override
                    public void squareGetSuccess(JSONObject object) {
                        JSONArray array = object.getJSONArray("accountList");
                        if (null != array && array.size() > 0) {
                            List<String> urls = new ArrayList<>();
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject accountObject = array.getJSONObject(i);
                                urls.add(accountObject.getString("avatar"));
                            }
                            getNicePicture(urls, ConversationId, String.valueOf(position));
                        }
                    }

                    @Override
                    public void squareGetFail(String error) {

                    }
                });
            }

            @Override
            public void joinFail(String error) {

            }
        });
    }

    //生成九宫图
    private void getNicePicture(List<String> picUrls, final String conversationId, final String positon) {
        GroupFaceImage.getInstance(getActivity(), picUrls).load(new GroupFaceImage.OnMatchingListener() {
            @Override
            public void onSuccess(Bitmap mathingBitmap) {
                String groupFaceImageName = "_" + conversationId + ".png";
                ImageUtils.cacheBitmapFile(getActivity(), mathingBitmap, "imagecache", groupFaceImageName);
                AppManager.getInstance().sendMessage("set_squarecollcet_avatar", positon);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    /**
     * 群头像
     */
    @Subscriber(tag = "set_squarecollcet_avatar")
    public void setAvatar(String code) {
        KLog.e(code);
        listAdapter.notifyItemChanged(Integer.parseInt(code));
    }

}
