package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.GGShareEntity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.ShareItemInfo;
import cn.gogoal.im.bean.ShareListBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.ShareMessageDialog;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/5/4 0004.
 * Staff_id 1375
 * phone 18930640263
 * description :${分享选择页面}.
 */
public class ShareMessageActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.title_bar)
    XTitle titleBar;

    private List<ShareListBean> datas;
    private ShareListAdapter adapter;
    private GGShareEntity shareEntity;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        BaseActivity.initRecycleView(recyclerView, 0);

        shareEntity = getIntent().getParcelableExtra("share_web_data");//分享的数据

        datas = new ArrayList<>();
        adapter = new ShareListAdapter(datas);

        recyclerView.setAdapter(adapter);

        datas.add(new ShareListBean(ShareListBean.LIST_TYPE_SEARCH));
        datas.add(addFunctionHead("我的朋友", R.mipmap.contacts_new_friend));
        datas.add(addFunctionHead("我的群组", R.mipmap.group_contacts));

        getRecentConversation();
    }

    private void initTitle() {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("选择联系人").setLeftText("\u3000取消");
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setTitleColor(Color.BLACK);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ShareListBean addFunctionHead(String name, int iconId) {
        ShareListBean<Integer> bean = new ShareListBean<>(ShareListBean.LIST_TYPE_ITEM);
        bean.setItemImage(iconId);
        bean.setText(name);
        return bean;
    }

    /**
     * 获取最近会话
     */
    public void getRecentConversation() {
        JSONArray recentArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", null);
        if (recentArray != null) {
            datas.add(new ShareListBean(ShareListBean.LIST_TYPE_SECTION));
            List<IMMessageBean> messageBeen = JSON.parseArray(recentArray.toJSONString(), IMMessageBean.class);
            for (IMMessageBean bean : messageBeen) {
                if (bean.getChatType() == 1001) {
                    ShareListBean<String> shareListFriend = new ShareListBean<>(
                            ShareListBean.LIST_TYPE_ITEM, bean.getAvatar(), bean.getNickname(), bean);
                    datas.add(shareListFriend);
                } else if (bean.getChatType() == 1002) {
                    final ShareListBean group = new ShareListBean<>(ShareListBean.LIST_TYPE_ITEM);
                    group.setText(bean.getNickname());
                    group.setBean(bean);

                    ChatGroupHelper.setGroupAvatar(bean.getConversationID(),  new AvatarTakeListener() {
                        @Override
                        public void success(Bitmap bitmap) {
                            group.setItemImage(bitmap);
                        }

                        @Override
                        public void failed(Exception e) {

                        }
                    });
//                    String imagecache = ChatGroupHelper.getBitmapFilePaht(bean.getConversationID());
//                    if (!StringUtils.isActuallyEmpty(imagecache)) {
//                        group.setItemImage(imagecache);
//                    } else {//没有缓存就拼
//                        final List<String> avatarString = new ArrayList<>();
//                        ChatGroupHelper.createGroupImage(bean.getConversationID(), new ChatGroupHelper.GroupInfoResponse() {
//                            @Override
//                            public void getInfoSuccess(JSONObject groupInfo) {
//                                JSONArray accountList = groupInfo.getJSONArray("accountList");
//                                for (int i = 0; i < accountList.size(); i++) {
//                                    avatarString.add(((JSONObject) accountList.get(i)).getString("avatar"));
//                                }
//                                GroupFaceImage.getInstance(getActivity(), avatarString).load(new GroupFaceImage.OnMatchingListener() {
//                                    @Override
//                                    public void onSuccess(Bitmap mathingBitmap) {
//                                        group.setItemImage(mathingBitmap);
//                                    }
//
//                                    @Override
//                                    public void onError(Exception e) {
//                                        KLog.e(e.getMessage());
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void getInfoFailed(Exception e) {
//                                KLog.e(e.getMessage());
//                            }
//                        });
//                    }

                    datas.add(group);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 分享选择列表
     */
    private class ShareListAdapter extends BaseMultiItemQuickAdapter<ShareListBean, BaseViewHolder> {
        private ShareListAdapter(List<ShareListBean> data) {
            super(data);
            addItemType(ShareListBean.LIST_TYPE_SEARCH, R.layout.include_search_edit);
            addItemType(ShareListBean.LIST_TYPE_SECTION, android.R.layout.simple_list_item_1);
            addItemType(ShareListBean.LIST_TYPE_ITEM, R.layout.item_contacts);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ShareListBean data, final int position) {
            switch (holder.getItemViewType()) {
                case ShareListBean.LIST_TYPE_SEARCH:
                    EditText etSearch = holder.getView(R.id.layout_2search);
                    // TODO: 2017/5/8 0008 搜索匹配
                    break;
                case ShareListBean.LIST_TYPE_SECTION:
                    TextView sectionView = holder.getView(android.R.id.text1);
                    sectionView.setText("最近会话");
                    AppDevice.setViewWidth$Height(sectionView,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            AppDevice.dp2px(ShareMessageActivity.this, 30));
                    sectionView.setTextColor(0xff888888);
                    sectionView.setBackgroundColor(0xffe8e8e8);
                    break;
                case ShareListBean.LIST_TYPE_ITEM:
                    holder.setText(R.id.item_contacts_tv_nickname, data.getText());
                    final AppCompatImageView icon = holder.getView(R.id.item_contacts_iv_icon);
                    ImageDisplay.loadRoundedRectangleImage(ShareMessageActivity.this, data.getItemImage(), icon);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (position) {
                                case 1://好友列表
                                    Intent intent = new Intent(v.getContext(), ChooseContactActivity.class);
                                    intent.putExtra("square_action", AppConst.SQUARE_ROOM_AT_SHARE_MESSAGE);
                                    intent.putExtra("share_web_data", shareEntity);
                                    startActivity(intent);
                                    break;
                                case 2://我的群组列表
                                    Intent intent1 = new Intent(v.getContext(), MyGroupsActivity.class);
                                    intent1.putExtra("action_type", AppConst.SQUARE_ROOM_AT_SHARE_MESSAGE);
                                    intent1.putExtra("share_web_data", shareEntity);
                                    startActivity(intent1);
                                    break;
                                default:
                                    ShareMessageDialog.newInstance(
                                            new ShareItemInfo(data.getItemImage(),
                                                    data.getText(),
                                                    shareEntity, data.getBean())).show(getSupportFragmentManager());
                                    break;
                            }
                        }
                    });

                    break;
            }
        }
    }
}
