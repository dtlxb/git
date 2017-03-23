package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.adapter.NineGridImageViewAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.OnItemClickLitener;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
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
    private List<String> imageList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private String conversationId;
    private List<String> groupMembers;

    @Override
    public int bindLayout() {
        return R.layout.activity_imsquare_set;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle(R.string.title_chat_person_detial, true);
        //初始化
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 5));
        mPersonInfoAdapter = new IMPersonSetAdapter(IMSquareChatSetActivity.this, R.layout.item_grid_foundfragment, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
        //正式流程走完后
        conversationId = getIntent().getExtras().getString("conversation_id");
        groupMembers = new ArrayList<>();
        if (null != getIntent().getExtras().getStringArrayList("group_members")) {
            groupMembers.addAll(getIntent().getExtras().getStringArrayList("group_members"));
        }
        Log.e("++++conversationId", conversationId);
        JSONArray accountArray = SPTools.getJsonArray(UserUtils.getToken() + conversationId + "_accountList_beans", null);
        //缓存中没有群信息则向后台拉取
        if (null != accountArray) {
            Log.e("++++accountArray1", accountArray.toString());
            getAllContacts(accountArray);
        } else {
            Log.e("++++accountArray2", accountArray.toString());
            getChatGroup(groupMembers);
        }


        mPersonInfoAdapter.setOnItemClickListener(new OnItemClickLitener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
                Intent intent;
                if (position == contactBeens.size() - 1) {
                    //createChatGroup();
                    //删除人
                    for (int i = 0; i < idList.size(); i += 2) {
                        idList.add(idList.get(i));
                    }
                    deleteAnyone(idList);

                } else if (position == contactBeens.size() - 2) {

                } else {
                    intent = new Intent(IMSquareChatSetActivity.this, IMPersonDetailActivity.class);
                    intent.putExtra("friend_id", contactBeens.get(position).getFriend_id());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(RecyclerView.ViewHolder holder, View view, int position) {
                return false;
            }
        });

        //群头像
        NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                ImageDisplay.loadNetImage(context, url, imageView);
            }

            @Override
            public ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };

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
        //parseContactDatas(SPTools.getString(AppConst.LEAN_CLOUD_TOKEN + "_contact_beans", ""));
        for (int i = 0; i < contactBeens.size(); i++) {
            imageList.add(contactBeens.get(i).getAvatar().toString());
            idList.add(String.valueOf(contactBeens.get(i).getFriend_id()));
        }
        idList.add(AppConst.LEAN_CLOUD_TOKEN);
        contactBeens.add(addFunctionHead("", R.mipmap.person_add));
        contactBeens.add(addFunctionHead("", R.mipmap.chat_reduce));

        mPersonInfoAdapter.notifyDataSetChanged();
        KLog.e(contactBeens);
    }

    //群头像测试
    private void parseContactDatas(String responseInfo) {
        if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
            BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                    responseInfo,
                    new TypeReference<BaseBeanList<ContactBean<String>>>() {
                    });
            List<ContactBean<String>> list = beanList.getData();

            for (ContactBean<String> bean : list) {
                bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
            }

            contactBeens.addAll(list);
        }
    }

    //删除群成员
    public void deleteAnyone(List<String> idList) {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(idList));
        params.put("conv_id", conversationId);
        Log.e("++++params", params.toString());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                Log.e("++++responseInfo", responseInfo);
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(IMSquareChatSetActivity.this, "群成员删除成功!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.DELETE_MEMBER, ggHttpInterface).startGet();
    }

    //删除群成员
    public void addAnyone(List<String> idList) {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(idList));
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

    //拉取群组消息
    public void getChatGroup(List<String> groupMembers) {
        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("conv_id", conversationId);
        params.put("id_list", JSONObject.toJSONString(groupMembers));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                Log.e("++++responseInfo", responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    if (null != result.getJSONObject("data")) {
                        getAllContacts(result.getJSONObject("data").getJSONArray("accountList"));
                        SPTools.saveJsonArray(UserUtils.getToken() + conversationId + "_accountList_beans", result.getJSONObject("data").getJSONArray("accountList"));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MEMBER_INFO, ggHttpInterface).startGet();
    }


    //创建群组
    public void createChatGroup() {

        Map<String, String> params = new HashMap<>();
        params.put("token", AppConst.LEAN_CLOUD_TOKEN);
        params.put("id_list", JSONObject.toJSONString(idList));
        KLog.e(params);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(result.get("code"));
                if ((int) result.get("code") == 0) {
                    UIHelper.toast(IMSquareChatSetActivity.this, "群组创建成功!!!");
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.CREATE_GROUP_CHAT, ggHttpInterface).startGet();
    }

    /*@OnClick({R.id.tv_do_search_conversation, R.id.getmessage_swith})
    void function(View view) {
        switch (view.getId()) {
            case R.id.tv_do_search_conversation:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.getmessage_swith:
                break;
            case R.id.tv_delete_square:
                break;
        }
    }*/

}
