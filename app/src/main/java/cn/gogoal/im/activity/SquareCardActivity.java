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
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.RecommendBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.RectangleView;
import cn.gogoal.im.ui.view.SelectorButton;
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
    @BindView(R.id.jion_group)
    SelectorButton jion_group;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private List<ContactBean> PersonContactBeens = new ArrayList<>();
    private String conversationId;
    private String squareName;
    private String squareCreater;
    private List<RecommendBean.DataBean.MBean> mBeanList;
    private boolean isIn;

    @Override
    public int bindLayout() {
        return R.layout.activity_square_card;
    }

    @Override
    public void doBusiness(Context mContext) {
        //初始化数据
        mBeanList = new ArrayList<>();
        personlistRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        conversationId = getIntent().getExtras().getString("conversation_id");
        squareName = getIntent().getExtras().getString("square_name");
        squareCreater = getIntent().getExtras().getString("square_creater");
        mBeanList = (List<RecommendBean.DataBean.MBean>) getIntent().getExtras().getSerializable("square_members");
        mPersonInfoAdapter = new IMPersonSetAdapter(1002, SquareCardActivity.this, R.layout.item_square_chat_set, squareCreater, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
        //初始化界面
        initTitle();
        //群成员
        getAllContacts(mBeanList);
        //拉取数据
        getGroupInfo();
    }

    @OnClick({R.id.look_more_person, R.id.jion_group})
    void fuction(View view) {
        switch (view.getId()) {
            case R.id.look_more_person:
                Intent intent = new Intent(getActivity(), IMGroupContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("square_creater", squareCreater);
                bundle.putSerializable("chat_group_contacts", (Serializable) PersonContactBeens);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.jion_group:
                if (isIn) {
                    Intent intent1 = new Intent(SquareCardActivity.this, SquareChatRoomActivity.class);
                    intent1.putExtra("conversation_id", conversationId);
                    intent1.putExtra("squareName", squareName);
                    intent1.putExtra("need_update", true);
                    startActivity(intent1);
                } else {
                    applyIntoGroup();
                }
                break;
        }
    }

    private void initTitle() {
        xTitle = setMyTitle("群名片", true);
        tvSquareName.setText(squareName);
        tvTeamSize.setText(String.valueOf(mBeanList.size()));
        iv_square_head.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap_avatar"));
        /*XTitle.ImageAction imageAction = new XTitle.ImageAction(getResDrawable(R.mipmap.arrows_white)) {
            @Override
            public void actionClick(View view) {

            }
        };
        xTitle.addAction(imageAction, 0);*/
    }

    private void getAllContacts(List<RecommendBean.DataBean.MBean> MBeanList) {
        KLog.e(MBeanList);
        for (int i = 0; i < MBeanList.size(); i++) {
            RecommendBean.DataBean.MBean mBean = mBeanList.get(i);
            contactBeens.add(addNomoralFuns(mBean.getNickname(), mBean.getAccount_id(), mBean.getAvatar(), mBean.getAccount_name()));
        }
        PersonContactBeens.addAll(contactBeens);
        contactBeens.clear();
        contactBeens.addAll(squareShowSix(PersonContactBeens));
        mPersonInfoAdapter.notifyDataSetChanged();
    }

    private ContactBean<String> addNomoralFuns(String name, int friend_id, String avatar, String accountName) {
        ContactBean<String> nomoralbean = new ContactBean<>();
        nomoralbean.setNickname(name);
        nomoralbean.setFriend_id(friend_id);
        nomoralbean.setContactType(ContactBean.ContactType.PERSION_ITEM);
        nomoralbean.setAvatar(avatar);
        nomoralbean.setAccount_name(accountName);
        return nomoralbean;
    }

    //拉取群详情
    public void getGroupInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);
        jion_group.setClickable(false);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                Log.e("=====notice", responseInfo);
                jion_group.setClickable(true);
                JSONObject result = JSONObject.parseObject(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject dataJsonObject = (JSONObject) result.get("data");
                    isIn = dataJsonObject.getBoolean("is_in");
                    tvSquareName.setText((dataJsonObject).get("name") == null ? "" : (dataJsonObject).getString("name"));
                    JSONObject jsonObject = (JSONObject) (dataJsonObject).get("attr");
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
                    if (isIn) {
                        jion_group.setText("进入聊天");
                    } else {
                        jion_group.setText("我要报名");
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                jion_group.setClickable(true);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_GROUP_INFO, ggHttpInterface).startGet();
    }

    public void applyIntoGroup() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("conv_id", conversationId);
        KLog.e(params);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if ((int) result.get("code") == 0) {
                    JSONObject dataJson = result.getJSONObject("data");
                    if (dataJson.getBoolean("success")) {
                        UIHelper.toast(SquareCardActivity.this, "入群申请发送成功");
                    } else {
                        UIHelper.toast(SquareCardActivity.this, "入群申请发送失败");
                    }
                } else {
                    UIHelper.toast(SquareCardActivity.this, "入群申请发送失败");
                }
            }


            @Override
            public void onFailure(String msg) {
                UIHelper.toast(SquareCardActivity.this, R.string.network_busy);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.APPLY_INTO_GROUP, ggHttpInterface).startGet();
    }

    //取六个人
    public List<ContactBean> squareShowSix(List<ContactBean> contactBeanList) {
        List<ContactBean> newContactBeanList = new ArrayList<>();
        int msize;
        if (contactBeanList.size() > 6) {
            msize = 6;
        } else {
            msize = contactBeanList.size();
        }
        for (int i = 0; i < msize; i++) {
            newContactBeanList.add(contactBeanList.get(i));
        }
        return newContactBeanList;
    }
}
