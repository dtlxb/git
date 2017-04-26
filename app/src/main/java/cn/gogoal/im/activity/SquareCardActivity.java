package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.RectangleView;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/26.
 */

public class SquareCardActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.iv_square_head)
    RectangleView iv_square_head;
    @BindView(R.id.personlist_recycler)
    RecyclerView personlistRecycler;
    @BindView(R.id.square_notice)
    TextView square_notice;
    @BindView(R.id.tv_square_name)
    TextView tvSquareName;
    @BindView(R.id.tv_square_detail)
    TextView tvSquareDetail;
    @BindView(R.id.team_size)
    TextView tvTeamSize;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private List<ContactBean> PersonContactBeens = new ArrayList<>();
    private String conversationId;
    private String squareName;
    private List<String> groupMembers;

    @Override
    public int bindLayout() {
        return R.layout.activity_square_card;
    }

    @Override
    public void doBusiness(Context mContext) {
        //初始化数据
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        conversationId = getIntent().getExtras().getString("conversation_id");
        squareName = getIntent().getExtras().getString("square_name");
        groupMembers = new ArrayList<>();
        //拉取数据
        getGroupInfo();
        if (null != getIntent().getExtras().getStringArrayList("group_members")) {
            groupMembers.addAll(getIntent().getExtras().getStringArrayList("group_members"));
            tvTeamSize.setText(groupMembers.size() + "人");
            getChatGroup(groupMembers);
        }
        //初始化界面
        initTitle();
        mPersonInfoAdapter = new IMPersonSetAdapter(1002, SquareCardActivity.this, R.layout.item_square_chat_set, "square_card", contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
    }

    @OnClick({R.id.look_more_person, R.id.jion_group})
    void fuction(View view) {
        switch (view.getId()) {
            case R.id.look_more_person:
                Intent intent = new Intent(getActivity(), IMGroupContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("square_creater", "square_card");
                bundle.putSerializable("chat_group_contacts", (Serializable) PersonContactBeens);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.jion_group:
                break;
        }
    }

    private void initTitle() {
        xTitle = setMyTitle("群名片", true);
        tvSquareName.setText(squareName);
        iv_square_head.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap_avatar"));
        XTitle.ImageAction imageAction = new XTitle.ImageAction(getResDrawable(R.mipmap.arrows_white)) {
            @Override
            public void actionClick(View view) {

            }
        };
        xTitle.addAction(imageAction, 0);
    }

    //拉取群组信息
    public void getChatGroup(List<String> groupMembers) {
        UserUtils.getChatGroup(AppConst.CHAT_GROUP_CONTACT_BEANS, groupMembers, conversationId, new UserUtils.getSquareInfo() {
            @Override
            public void squareGetSuccess(JSONObject object) {
                getAllContacts(object.getJSONArray("accountList"));
            }

            @Override
            public void squareGetFail(String error) {

            }
        });
    }

    private void getAllContacts(JSONArray aarray) {
        for (int i = 0; i < aarray.size(); i++) {
            JSONObject accountObject = aarray.getJSONObject(i);
            contactBeens.add(addNomoralFuns(accountObject.getString("nickname"), accountObject.getInteger("friend_id"), accountObject.getString("avatar")));
        }
        PersonContactBeens.addAll(contactBeens);
        contactBeens.clear();
        contactBeens.addAll(squareShowSix(PersonContactBeens));
        mPersonInfoAdapter.notifyDataSetChanged();
    }

    private ContactBean<String> addNomoralFuns(String name, int friend_id, String avatar) {
        ContactBean<String> nomoralbean = new ContactBean<>();
        nomoralbean.setNickname(name);
        nomoralbean.setFriend_id(friend_id);
        nomoralbean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        nomoralbean.setAvatar(avatar);
        return nomoralbean;
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
                if ((int) result.get("code") == 0) {
                    tvSquareName.setText(((JSONObject) result.get("data")).get("name") == null ? "" : ((JSONObject) result.get("data")).getString("name"));
                    JSONObject jsonObject = (JSONObject) ((JSONObject) result.get("data")).get("attr");
                    if (jsonObject.get("intro") != null && !TextUtils.isEmpty(jsonObject.getString("intro"))) {
                        tvSquareDetail.setText(jsonObject.get("intro") == null ? "" : jsonObject.getString("intro"));
                    } else {
                        tvSquareDetail.setText("暂无群简介");
                    }

                    if (jsonObject.get("notice") != null && !TextUtils.isEmpty(jsonObject.getString("notice"))) {
                        square_notice.setVisibility(View.VISIBLE);
                        square_notice.setText(jsonObject.get("notice") == null ? "" : jsonObject.getString("notice"));
                    } else {
                        square_notice.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, ggHttpInterface).startGet();
    }

    //排序将群主放置第一位
    public List<ContactBean> squareShowSix(List<ContactBean> contactBeanList) {
        List<ContactBean> newContactBeanList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newContactBeanList.add(contactBeanList.get(i));
        }
        return newContactBeanList;
    }
}
