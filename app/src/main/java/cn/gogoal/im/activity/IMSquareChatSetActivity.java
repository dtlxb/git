package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.NineGridImageViewAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.OnItemClickLitener;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.NineGridImageView;

/**
 * Created by huangxx on 2017/3/20.
 */

public class IMSquareChatSetActivity extends BaseActivity {


    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;

    @BindView(R.id.iv_square_head)
    NineGridImageView iv_square_head;

    @BindView(R.id.tv_square_name)
    TextView tvSquareName;

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
    Switch saveGroup;

    private List<String> imageList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();

    private IMPersonSetAdapter mPersonInfoAdapter;
    NineGridImageViewAdapter<String> mAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private String conversationId;
    private String squareCreater;
    private List<String> groupMembers;
    private String squareName;

    @Override
    public int bindLayout() {
        return R.layout.item_imsquare_set;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        mPersonInfoAdapter = new IMPersonSetAdapter(IMSquareChatSetActivity.this, R.layout.item_square_chat_set, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
        groupMembers = new ArrayList<>();
        //正式流程走完后
        conversationId = getIntent().getExtras().getString("conversation_id");
        squareCreater = getIntent().getExtras().getString("square_creater");
        squareName = getIntent().getExtras().getString("squareName");
        tvSquareName.setText(squareName);
        the_square.setText(squareName);

        if (null != getIntent().getExtras().getStringArrayList("group_members")) {
            groupMembers.addAll(getIntent().getExtras().getStringArrayList("group_members"));
            tvTeamSize.setText(groupMembers.size() + "人");
        }
        JSONArray accountArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + conversationId + "_accountList_beans", null);
        //缓存中没有群信息则向后台拉取
        if (null != accountArray) {
            getAllContacts(accountArray);
        } else {
            getChatGroup(groupMembers);
        }

        final JSONArray groupsArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_groups_saved", new JSONArray());
        JSONObject thisGroup = null;
        for (int i = 0; i < groupsArray.size(); i++) {
            if (((JSONObject) groupsArray.get(i)).getString("conv_id").equals(conversationId)) {
                saveGroup.setChecked(true);
                thisGroup = (JSONObject) groupsArray.get(i);
            }
        }
        //保存群
        final JSONObject finalThisGroup = thisGroup;
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
                    collcetGroup();
                } else {
                    groupsArray.remove(finalThisGroup);
                    deleteGroup();
                }
                SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_groups_saved", groupsArray);
            }
        });

        mPersonInfoAdapter.setOnItemClickListener(new OnItemClickLitener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Intent intent;
                Bundle mBundle = new Bundle();
                if ((position == contactBeens.size() - 2 && squareCreater.equals(UserUtils.getUserAccountId())) ||
                        (!squareCreater.equals(UserUtils.getUserAccountId()) && position == contactBeens.size() - 1)) {
                    intent = new Intent(IMSquareChatSetActivity.this, ChooseContactActivity.class);
                    mBundle.putInt("square_action", AppConst.SQUARE_ROOM_ADD_ANYONE);
                    mBundle.putString("conversation_id", conversationId);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_ADD_ANYONE);
                } else if (squareCreater.equals(UserUtils.getUserAccountId()) && position == contactBeens.size() - 1) {
                    //删除人
                    intent = new Intent(IMSquareChatSetActivity.this, ChooseContactActivity.class);
                    mBundle.putInt("square_action", AppConst.SQUARE_ROOM_DELETE_ANYONE);
                    mBundle.putString("conversation_id", conversationId);
                    intent.putExtras(mBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_DELETE_ANYONE);
                } else {
                    intent = new Intent(IMSquareChatSetActivity.this, IMPersonDetailActivity.class);
                    mBundle.putInt("friend_id", contactBeens.get(position).getFriend_id());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(RecyclerView.ViewHolder holder, View view, int position) {
                return false;
            }
        });

        //群头像
        mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                ImageDisplay.loadNetImage(context, url, imageView);
            }

            @Override
            public ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };
        getGroupInfo();
        iv_square_head.setAdapter(mAdapter);
        iv_square_head.setImagesData(imageList);
    }

    private ContactBean<Integer> addFunctionHead(String name, @DrawableRes int iconId) {
        ContactBean<Integer> bean = new ContactBean<>();
        bean.setRemark(name);
        bean.setContactType(ContactBean.ContactType.FUNCTION_ITEM);
        bean.setAvatar(iconId);
        return bean;
    }

    private ContactBean<String> addNomoralFuns(String name, int friend_id, String avatar) {
        ContactBean<String> nomoralbean = new ContactBean<>();
        nomoralbean.setNickname(name);
        nomoralbean.setFriend_id(friend_id);
        nomoralbean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        nomoralbean.setAvatar(avatar);
        return nomoralbean;
    }

    private void getAllContacts(JSONArray aarray) {
        for (int i = 0; i < aarray.size(); i++) {
            JSONObject accountObject = aarray.getJSONObject(i);
            contactBeens.add(addNomoralFuns(accountObject.getString("nickname"), accountObject.getInteger("friend_id"), accountObject.getString("avatar")));
        }
        //测试代码,没有数据的时候拉通讯录建群
        for (int i = 0; i < contactBeens.size(); i++) {
            if (i < 4) {
                imageList.add(contactBeens.get(i).getAvatar().toString());
                idList.add(String.valueOf(contactBeens.get(i).getFriend_id()));
            }
        }
        idList.add(UserUtils.getUserAccountId());
        contactBeens.add(addFunctionHead("", R.mipmap.person_add));
        if (squareCreater.equals(UserUtils.getUserAccountId())) {
            contactBeens.add(addFunctionHead("", R.mipmap.chat_reduce));
        }

        mPersonInfoAdapter.notifyDataSetChanged();
        KLog.e(contactBeens);
    }

    //删除群成员
    public void deleteAnyone(final List<Integer> idSet) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(idSet));
        params.put("conv_id", conversationId);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    if (idSet.size() == 1 && idSet.get(0) == (Integer.parseInt(UserUtils.getUserAccountId()))) {
                        UIHelper.toast(IMSquareChatSetActivity.this, "退群并删除群成功");
                        //群列表删除
                        SPTools.clearItem(UserUtils.getUserAccountId() + conversationId + "_accountList_beans");
                        MessageUtils.removeByID(conversationId);
                        finish();
                        AppManager.getInstance().finishActivity(SquareChatRoomActivity.class);
                    } else {
                        UIHelper.toast(IMSquareChatSetActivity.this, "群成员删除成功");
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.e("++++responseInfo", msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.DELETE_MEMBER, ggHttpInterface).startGet();
    }

    //添加群成员
    public void addAnyone(List<Integer> idSet) {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("id_list", JSONObject.toJSONString(idSet));
        params.put("conv_id", conversationId);
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(IMSquareChatSetActivity.this, "群成员添加成功!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.ADD_MEMBER, ggHttpInterface).startGet();
    }

    //收藏群
    public void collcetGroup() {
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
                    UIHelper.toast(IMSquareChatSetActivity.this, "群收藏成功!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.COLLECT_GROUP, ggHttpInterface).startGet();
    }

    //取消群收藏
    public void deleteGroup() {
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
                    UIHelper.toast(IMSquareChatSetActivity.this, "群已取消收藏!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CANCEL_COLLECT_GROUP, ggHttpInterface).startGet();
    }

    //拉取群组信息
    public void getChatGroup(List<String> groupMembers) {
        UserUtils.getChatGroup(groupMembers, conversationId, new UserUtils.getSquareInfo() {
            @Override
            public void squareGetSuccess(JSONObject object) {
                getAllContacts(object.getJSONArray("accountList"));
            }

            @Override
            public void squareGetFail(String error) {

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
                Log.e("=====notice", responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                the_square.setText(((JSONObject) result.get("data")).get("name") == null ? "" : ((JSONObject) result.get("data")).getString("name"));
                if ((int) result.get("code") == 0) {
                    JSONObject jsonObject = (JSONObject) ((JSONObject) result.get("data")).get("attr");
                    the_brief.setText(jsonObject.get("intro") == null ? "" : jsonObject.getString("intro"));
                    the_square_notice.setText(jsonObject.get("notice") == null ? "" : jsonObject.getString("notice"));
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
                //startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.square_message_tv:
                //群公告
                intent = new Intent(getActivity(), EditSquareBriefActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putBoolean("is_creater", squareCreater.equals(UserUtils.getUserAccountId()));
                mBundle.putString("conversation_id", conversationId);
                mBundle.putBoolean("is_notice", true);
                intent.putExtras(mBundle);
                startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_NOTICE);
                break;
            case R.id.tv_what_square:
                //群名称
                if (squareCreater.equals(UserUtils.getUserAccountId())) {
                    intent = new Intent(getActivity(), EditSquareNameActivity.class);
                    Bundle nameBundle = new Bundle();
                    nameBundle.putString("square_name", squareName);
                    nameBundle.putString("conversation_id", conversationId);
                    nameBundle.putBoolean("is_notice", false);
                    intent.putExtras(nameBundle);
                    startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_NAME);
                }
                break;
            case R.id.square_brief:
                //群简介
                intent = new Intent(getActivity(), EditSquareBriefActivity.class);
                Bundle briefBundle = new Bundle();
                briefBundle.putBoolean("is_creater", squareCreater.equals(UserUtils.getUserAccountId()));
                briefBundle.putString("conversation_id", conversationId);
                intent.putExtras(briefBundle);
                startActivityForResult(intent, AppConst.SQUARE_ROOM_EDIT_BRIEF);
                break;
            case R.id.tv_search_history_tv:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tv_delete_square:
                //删除并退出群
                List<Integer> quitList = new ArrayList<>();
                quitList.add(Integer.parseInt(UserUtils.getUserAccountId()));
                deleteAnyone(quitList);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {

            switch (requestCode) {
                case AppConst.SQUARE_ROOM_ADD_ANYONE:
                    List<ContactBean> addContactBeens = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                    List<Integer> addIdList = new ArrayList<>();
                    for (int i = 0; i < addContactBeens.size(); i++) {
                        addIdList.add(addContactBeens.get(i).getFriend_id());
                    }
                    //添加群成员
                    addAnyone(addIdList);
                    contactBeens.addAll(contactBeens.size() - 2, addContactBeens);
                    mPersonInfoAdapter.notifyDataSetChanged();
                    break;
                case AppConst.SQUARE_ROOM_DELETE_ANYONE:
                    List<ContactBean> changeContactBeens = (List<ContactBean>) data.getSerializableExtra("choose_friend_array");
                    List<Integer> idList = new ArrayList<>();
                    for (int i = 0; i < changeContactBeens.size(); i++) {
                        idList.add(changeContactBeens.get(i).getFriend_id());
                    }
                    //删除群成员
                    deleteAnyone(idList);
                    contactBeens.removeAll(changeContactBeens);
                    mPersonInfoAdapter.notifyDataSetChanged();
                    break;
                case AppConst.SQUARE_ROOM_EDIT_NAME:
                    String correctedSquareName = data.getStringExtra("corrected_square_name");
                    tvSquareName.setText(correctedSquareName);
                    the_square.setText(correctedSquareName);
                    break;
                case AppConst.SQUARE_ROOM_EDIT_BRIEF:
                    String stringBrief = data.getStringExtra("group_intro");
                    the_brief.setText(stringBrief);
                    break;
                case AppConst.SQUARE_ROOM_EDIT_NOTICE:
                    String stringNotice = data.getStringExtra("group_notice");
                    the_square_notice.setText(stringNotice);
                    break;
            }
        }

    }
}
