package cn.gogoal.im.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.GGQrCode;

/**
 * Created by huangxx on 2017/3/20.
 */

public class IMSquareChatSetActivity extends BaseActivity {


    @BindView(R.id.personlist_recycler)
    RecyclerView personListRecycler;

    @BindView(R.id.iv_square_head)
    RoundedImageView iv_square_head;

    @BindView(R.id.tv_square_name)
    TextView tvSquareName;

    @BindView(R.id.tv_square_detail)
    TextView tvSquareDetail;

    @BindView(R.id.team_size)
    TextView tvTeamSize;

    @BindView(R.id.the_square)
    TextView the_square;

    @BindView(R.id.the_brief)
    TextView the_brief;

    @BindView(R.id.the_square_notice)
    TextView the_square_notice;

    @BindView(R.id.square_message_tv)
    RelativeLayout the_notice;

    @BindView(R.id.save_switch)
    SwitchCompat saveGroup;

    @BindView(R.id.bother_switch)
    SwitchCompat botherSwitch;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeans = new ArrayList<>();
    private List<ContactBean> PersonContactBeans = new ArrayList<>();
    private String conversationId;
    private String squareCreater;
    private List<String> groupMembers;
    private String squareName;
    private String headAvatar;
    private List<String> urls;

    private List<UserBean> userBeans = new ArrayList<>();
    private JSONObject finalThisGroup;

    @Override
    public int bindLayout() {
        return R.layout.item_imsquare_set;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        //初始化
        squareCreater = getIntent().getExtras().getString("square_creater");
        headAvatar = getIntent().getExtras().getString("head_avatar");
        personListRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        mPersonInfoAdapter = new IMPersonSetAdapter(1002, IMSquareChatSetActivity.this, R.layout.item_square_chat_set, squareCreater, contactBeans);
        personListRecycler.setAdapter(mPersonInfoAdapter);
        groupMembers = new ArrayList<>();
        urls = new ArrayList<>();
        //正式流程走完后
        conversationId = getIntent().getExtras().getString("conversation_id");
        squareName = getIntent().getExtras().getString("squareName");
        tvSquareName.setText(squareName);
        the_square.setText(squareName);

        //初始化打扰设置
        boolean noBother = SPTools.getBoolean(UserUtils.getMyAccountId() + conversationId + "noBother", false);
        botherSwitch.setChecked(noBother);

        if (null != getIntent().getExtras().getStringArrayList("group_members")) {
            groupMembers.addAll(getIntent().getExtras().getStringArrayList("group_members"));
            tvTeamSize.setText(groupMembers.size() + "人");
        }

        userBeans.addAll(UserInfoUtils.getAllGroupUserInfo(conversationId));

        if (null != userBeans && userBeans.size() > 1) {
            getAllContacts(userBeans);
        } else {
            getChatGroup();
        }

        final JSONArray groupsArray = UserUtils.getLocalMyGooupList();

        //保存群
        if (null != groupsArray && groupsArray.size() > 0) {
            for (int i = 0; i < groupsArray.size(); i++) {
                if (((JSONObject) groupsArray.get(i)).getString("conv_id").equals(conversationId)) {
                    saveGroup.setChecked(true);
                    finalThisGroup = (JSONObject) groupsArray.get(i);
                    break;
                }
            }
        }

        //消息打扰设置
        botherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChatGroupHelper.controlMute(isChecked, conversationId);
            }
        });

        saveGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject groupObject = new JSONObject();
                    JSONObject attrObject = new JSONObject();
                    groupObject.put("conv_id", conversationId);
                    groupObject.put("name", squareName);
                    groupObject.put("m_size", groupMembers.size() + "");

                    attrObject.put("intro", "股市行情，畅所欲言");
                    attrObject.put("notice", "股票有大涨趋势");
                    groupObject.put("attr", attrObject);

                    groupsArray.add(groupObject);
                    ChatGroupHelper.collectGroup(conversationId, new ChatGroupHelper.ChatGroupManager() {
                        @Override
                        public void groupActionSuccess(JSONObject object) {
                            UIHelper.toast(IMSquareChatSetActivity.this, "群收藏成功!!!");
                        }

                        @Override
                        public void groupActionFail(String error) {

                        }
                    });
                } else {
                    groupsArray.remove(finalThisGroup);
                    ChatGroupHelper.deleteGroup(conversationId, new ChatGroupHelper.ChatGroupManager() {
                        @Override
                        public void groupActionSuccess(JSONObject object) {
                            UIHelper.toast(IMSquareChatSetActivity.this, "群已取消收藏!!!");
                        }

                        @Override
                        public void groupActionFail(String error) {

                        }
                    });
                }
                SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_groups_saved", groupsArray);
            }
        });

        mPersonInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, int position) {
                Intent intent;
                Bundle mBundle = new Bundle();
                if (position == contactBeans.size() - 2 && squareCreater.equals(UserUtils.getMyAccountId())) {
                    intent = new Intent(IMSquareChatSetActivity.this, ChooseContactActivity.class);
                    mBundle.putInt("square_action", AppConst.SQUARE_ROOM_ADD_ANYONE);
                    mBundle.putString("conversation_id", conversationId);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_ADD_ANYONE);
                } else if (squareCreater.equals(UserUtils.getMyAccountId()) && position == contactBeans.size() - 1) {
                    //删除人
                    intent = new Intent(IMSquareChatSetActivity.this, ChooseContactActivity.class);
                    mBundle.putInt("square_action", AppConst.SQUARE_ROOM_DELETE_ANYONE);
                    mBundle.putString("conversation_id", conversationId);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_DELETE_ANYONE);
                } else {
//                    intent = new Intent(IMSquareChatSetActivity.this, IMPersonDetailActivity.class);
//                    mBundle.putInt("account_id", contactBeans.get(position).getFriend_id());
//                    intent.putExtras(mBundle);
//                    startActivity(intent);

                    NormalIntentUtils.go2PersionDetail(IMSquareChatSetActivity.this,
                            contactBeans.get(position).getFriend_id());
                }
            }
        });
        getGroupInfo();
    }

    @OnClick(R.id.layout_square_qrcode)
    void click(View view){
        Intent intent = new Intent(view.getContext(), QrCodeActivity.class);
        intent.putExtra("qr_code_type", GGQrCode.QR_CODE_TYPE_GROUP);
        intent.putExtra("qrcode_name", squareName);
        intent.putExtra("qrcode_info", "(" + groupMembers.size() + ")人");
        intent.putExtra("qrcode_content_id", conversationId);
        startActivity(intent);
    }

    private ContactBean<Integer> addFunctionHead(String name, @DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setRemark(name);
        bean.setContactType(ContactBean.ContactType.FUNCTION_ITEM);
        bean.setAvatar(iconId);
        return bean;
    }

    private ContactBean<String> addNormalFans(String name, int friend_id, String avatar) {
        ContactBean<String> normalBean = new ContactBean<>();
        normalBean.setNickname(name);
        normalBean.setFriend_id(friend_id);
        normalBean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        normalBean.setAvatar(avatar);
        return normalBean;
    }

    @SuppressLint("SetTextI18n")
    private void getAllContacts(List<UserBean> list) {
        List<String> memberList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            contactBeans.add(addNormalFans(list.get(i).getNickname(), list.get(i).getFriend_id(), list.get(i).getAvatar()));
            memberList.add(String.valueOf(list.get(i).getFriend_id()));
            urls.add(list.get(i).getAvatar());
        }
        //获取群头像
        if (!TextUtils.isEmpty(headAvatar)) {
            ImageDisplay.loadRoundedRectangleImage(IMSquareChatSetActivity.this, headAvatar, iv_square_head);
        } else {
            if (memberList.size() > 0) {
                groupMembers.clear();
                groupMembers.addAll(memberList);
                getNicePicture(urls);
            }
        }
        tvTeamSize.setText(groupMembers.size() + "人");
        PersonContactBeans.addAll(contactBeans);
        contactBeans.clear();
        contactBeans.addAll(squareCreaterFirst(PersonContactBeans));
        mPersonInfoAdapter.notifyDataSetChanged();
    }

    //生成九宫图
    private void getNicePicture(List<String> picUrls) {
        GroupFaceImage.getInstance(getActivity(), picUrls).load(new GroupFaceImage.OnMatchingListener() {
            @Override
            public void onSuccess(Bitmap matchingBitmap) {
                ChatGroupHelper.cacheGroupAvatar(conversationId, matchingBitmap);
                HashMap<String, Bitmap> map = new HashMap<>();
                map.put("matching_bitmap", matchingBitmap);
                BaseMessage<Bitmap> baseMessage = new BaseMessage<>("Bitmap_Info", map);
                AppManager.getInstance().sendMessage("set_square_avatar", baseMessage);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    /**
     * 群头像
     */
    @Subscriber(tag = "set_square_avatar")
    public void setAvatar(BaseMessage<Bitmap> baseMessage) {
        Map<String, Bitmap> map = baseMessage.getOthers();
        Bitmap bitmap = map.get("matching_bitmap");
        iv_square_head.setImageBitmap(bitmap);
    }

    //删除群成员后
    public void afterDeleteAnyone(List<Integer> idList) {
        if (idList.size() == 1 && idList.get(0) == (Integer.parseInt(UserUtils.getMyAccountId()))) {
            UIHelper.toast(IMSquareChatSetActivity.this, "退群并删除群成功");
            //群列表删除
            UserInfoUtils.deleteTheGroup(conversationId);
            MessageListUtils.removeMessageInfo(conversationId);
            finish();
            AppManager.getInstance().finishActivity(SquareChatRoomActivity.class);
        } else {
            UIHelper.toast(IMSquareChatSetActivity.this, "群成员删除成功");
        }
    }

    //拉取群组信息
    public void getChatGroup() {
        UserUtils.getChatGroup(null, conversationId, new UserUtils.SquareInfoCallback() {
            @Override
            public void squareGetSuccess(JSONObject object) {
                if (null != object.get("accountList")) {
                    List<UserBean> userBeanList = JSON.parseArray(object.getJSONArray("accountList").toJSONString(), UserBean.class);
                    getAllContacts(userBeanList);
                } else {
                }
            }

            @Override
            public void squareGetFail(String error) {
                KLog.e(error);
            }
        });
    }

    //拉取群详情
    public void getGroupInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    the_square.setText(((JSONObject) result.get("data")).get("name") == null ? "" : ((JSONObject) result.get("data")).getString("name"));
                    tvSquareName.setText(((JSONObject) result.get("data")).get("name") == null ? "" : ((JSONObject) result.get("data")).getString("name"));
                    JSONObject jsonObject = (JSONObject) ((JSONObject) result.get("data")).get("attr");
                    if (jsonObject.get("intro") != null && !TextUtils.isEmpty(jsonObject.getString("intro"))) {
                        the_brief.setText(jsonObject.get("intro") == null ? "" : jsonObject.getString("intro"));
                        tvSquareDetail.setText(jsonObject.get("intro") == null ? "" : jsonObject.getString("intro"));
                    } else {
                        the_brief.setText("暂无群简介");
                        tvSquareDetail.setText("暂无群简介");
                    }

                    if (jsonObject.get("notice") != null && !TextUtils.isEmpty(jsonObject.getString("notice"))) {
                        the_square_notice.setText(jsonObject.get("notice") == null ? "" : jsonObject.getString("notice"));
                    } else {
                        the_square_notice.setText("暂无群公告");
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, ggHttpInterface).startGet();
    }

    @OnClick({R.id.look_more_person, R.id.square_message_tv, R.id.tv_what_square, R.id.square_brief, R.id.tv_search_history_tv, R.id.tv_delete_square})
    void function(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.look_more_person:
                intent = new Intent(getActivity(), IMGroupContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("square_creater", squareCreater);
                bundle.putSerializable("chat_group_contacts", (Serializable) PersonContactBeans);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.square_message_tv:
                //群公告
                intent = new Intent(getActivity(), EditSquareBriefActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putBoolean("is_creater", squareCreater.equals(UserUtils.getMyAccountId()));
                mBundle.putString("conversation_id", conversationId);
                mBundle.putBoolean("is_notice", true);
                intent.putExtras(mBundle);
                startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_NOTICE);
                break;
            case R.id.tv_what_square:
                //群名称
                if (squareCreater.equals(UserUtils.getMyAccountId())) {
                    intent = new Intent(getActivity(), EditSquareNameActivity.class);
                    Bundle nameBundle = new Bundle();
                    nameBundle.putString("square_name", squareName);
                    nameBundle.putString("conversation_id", conversationId);
                    intent.putExtras(nameBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_NAME);
                }
                break;
            case R.id.square_brief:
                //群简介
                intent = new Intent(getActivity(), EditSquareBriefActivity.class);
                Bundle briefBundle = new Bundle();
                briefBundle.putBoolean("is_creater", squareCreater.equals(UserUtils.getMyAccountId()));
                briefBundle.putString("conversation_id", conversationId);
                briefBundle.putBoolean("is_notice", false);
                intent.putExtras(briefBundle);
                startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_BRIEF);
                break;
            case R.id.tv_search_history_tv:
                intent = new Intent(getActivity(), SearchMessagesActivity.class);
                intent.putExtra("conversation_id", conversationId);
                startActivity(intent);
                break;
            case R.id.tv_delete_square:
                //删除并退出群
                final List<Integer> quitList = new ArrayList<>();
                quitList.add(Integer.parseInt(UserUtils.getMyAccountId()));
                ChatGroupHelper.deleteAnyone(quitList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                    @Override
                    public void groupActionSuccess(JSONObject object) {
                        afterDeleteAnyone(quitList);
                    }

                    @Override
                    public void groupActionFail(String error) {

                    }
                });
                //查看是收藏这个群了然后删除
                JSONArray groupsArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_groups_saved", new JSONArray());
                for (int i = 0; i < groupsArray.size(); i++) {
                    JSONObject object = (JSONObject) groupsArray.get(i);
                    if (object.get("conv_id").equals(conversationId)) {
                        groupsArray.remove(i);
                        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_groups_saved", groupsArray);
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {

            switch (requestCode) {
                case AppConst.SQUARE_ROOM_ADD_ANYONE:
                    List<ContactBean> addContactBeans = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                    List<Integer> addIdList = new ArrayList<>();
                    for (int i = 0; i < addContactBeans.size(); i++) {
                        addIdList.add(addContactBeans.get(i).getFriend_id());
                        urls.add((String) addContactBeans.get(i).getAvatar());
                    }
                    //添加群成员
                    ChatGroupHelper.addAnyone(addIdList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                        @Override
                        public void groupActionSuccess(JSONObject object) {
                            UIHelper.toast(IMSquareChatSetActivity.this, "群成员添加成功!!!");
                        }

                        @Override
                        public void groupActionFail(String error) {

                        }
                    });
                    PersonContactBeans.addAll(addContactBeans);
                    getNicePicture(urls);
                    contactBeans.clear();
                    contactBeans.addAll(squareCreaterFirst(PersonContactBeans));
                    tvTeamSize.setText(PersonContactBeans.size() + "人");
                    mPersonInfoAdapter.notifyDataSetChanged();
                    break;
                case AppConst.SQUARE_ROOM_DELETE_ANYONE:
                    List<ContactBean> changeContactBeans = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                    final List<Integer> idList = new ArrayList<>();
                    for (int i = 0; i < changeContactBeans.size(); i++) {
                        idList.add(changeContactBeans.get(i).getFriend_id());
                        urls.remove((String) changeContactBeans.get(i).getAvatar());
                    }
                    //删除群成员
                    ChatGroupHelper.deleteAnyone(idList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                        @Override
                        public void groupActionSuccess(JSONObject object) {
                            afterDeleteAnyone(idList);
                        }

                        @Override
                        public void groupActionFail(String error) {

                        }
                    });
                    PersonContactBeans.removeAll(changeContactBeans);
                    getNicePicture(urls);
                    contactBeans.clear();
                    contactBeans.addAll(squareCreaterFirst(PersonContactBeans));
                    tvTeamSize.setText(PersonContactBeans.size() + "人");
                    mPersonInfoAdapter.notifyDataSetChanged();
                    break;
                case AppConst.SQUARE_ROOM_EDIT_NAME:
                    String correctedSquareName = data.getStringExtra("corrected_square_name");
                    tvSquareName.setText(correctedSquareName);
                    the_square.setText(correctedSquareName);
                    break;
                case AppConst.SQUARE_ROOM_EDIT_BRIEF:
                    String stringBrief = data.getStringExtra("group_intro");
                    if (TextUtils.isEmpty(stringBrief)) {
                        stringBrief = "暂无群简介";
                    }
                    the_brief.setText(stringBrief);
                    tvSquareDetail.setText(stringBrief);
                    break;
                case AppConst.SQUARE_ROOM_EDIT_NOTICE:
                    String stringNotice = data.getStringExtra("group_notice");
                    if (TextUtils.isEmpty(stringNotice)) {
                        stringNotice = "暂无群公告";
                    }
                    the_square_notice.setText(stringNotice);
                    break;
            }
        }

    }

    //排序将群主放置第一位
    public List<ContactBean> squareCreaterFirst(List<ContactBean> contactBeanList) {
        List<ContactBean> newContactBeanList = new ArrayList<>();
        int size;
        ContactBean contactBean;
        for (int i = 0; i < contactBeanList.size(); i++) {
            if (String.valueOf(contactBeanList.get(i).getFriend_id()).equals(squareCreater)) {
                contactBean = contactBeanList.get(i);
                contactBeanList.remove(i);
                contactBeanList.add(0, contactBean);
            }
        }
        if (squareCreater.equals(UserUtils.getMyAccountId())) {
            if (contactBeanList.size() > 4) {
                size = 4;
            } else {
                size = contactBeanList.size();
            }
            for (int i = 0; i < size; i++) {
                newContactBeanList.add(contactBeanList.get(i));
            }
            newContactBeanList.add(addFunctionHead("", R.mipmap.person_add));
            newContactBeanList.add(addFunctionHead("", R.mipmap.chat_reduce));
        } else {
            if (contactBeanList.size() > 6) {
                size = 6;
            } else {
                size = contactBeanList.size();
            }
            for (int i = 0; i < size; i++) {
                newContactBeanList.add(contactBeanList.get(i));
            }
        }
        return newContactBeanList;
    }
}
