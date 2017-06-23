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
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.IMPersonSetAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.group.GroupData;
import cn.gogoal.im.bean.group.GroupMemberInfo;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import cn.gogoal.im.ui.view.SelectorButton;

/**
 * Created by huangxx on 2017/4/26.
 */

public class SquareCardActivity extends BaseActivity {

    @BindView(R.id.iv_square_head)
    ImageView iv_square_head;

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
    @BindView(R.id.join_group)
    SelectorButton join_group;

    private IMPersonSetAdapter mPersonInfoAdapter;
    private List<ContactBean> contactBeens = new ArrayList<>();
    private ArrayList<ContactBean> PersonContactBeens = new ArrayList<>();
    private String conversationId;
    private String squareName;
    private String squareCreater;
    private List<GroupMemberInfo> mBeanList;

    private boolean isIn;
    private int chatType;

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
        mBeanList = getIntent().getParcelableArrayListExtra("square_members");

        mPersonInfoAdapter = new IMPersonSetAdapter(1002, SquareCardActivity.this, R.layout.item_square_chat_set, squareCreater, contactBeens);
        personlistRecycler.setAdapter(mPersonInfoAdapter);
        //初始化界面
        initTitle();
        //群成员
        getAllContacts(mBeanList);
        //拉取数据
        getGroupInfo();
    }

    @OnClick({R.id.look_more_person, R.id.join_group, R.id.layout_square_qrcode})
    void fuction(View view) {
        switch (view.getId()) {
            case R.id.look_more_person:
                Intent intent = new Intent(getActivity(), IMGroupContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("square_creater", squareCreater);
                bundle.putSerializable("chat_group_contacts", PersonContactBeens);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.join_group:
                if (isIn) {
                    Intent intent1 = new Intent(SquareCardActivity.this, SquareChatRoomActivity.class);
                    intent1.putExtra("conversation_id", conversationId);
                    intent1.putExtra("squareName", squareName);
                    intent1.putExtra("need_update", true);
                    startActivity(intent1);
                } else if (chatType==1010){

                    List<Integer> addIdList = new ArrayList<>();
                    addIdList.add(Integer.parseInt(UserUtils.getMyAccountId()));
                    //添加群成员
                    ChatGroupHelper.addAnyone(addIdList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                        @Override
                        public void groupActionSuccess(JSONObject object) {
                            Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                            intent.putExtra("conversation_id", conversationId);
                            intent.putExtra("need_update", false);
                            startActivity(intent);
                        }

                        @Override
                        public void groupActionFail(String error) {
                            UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                            Log.e("TAG", error);
                        }
                    });

                }else {
                    ChatGroupHelper.applyIntoGroup(getActivity(), conversationId);
                }
                break;
            case R.id.layout_square_qrcode:
                intent = new Intent(view.getContext(), QrCodeActivity.class);
                intent.putExtra("qr_code_type", GGQrCode.QR_CODE_TYPE_GROUP);
                intent.putExtra("qrcode_name", squareName);
                intent.putExtra("qrcode_info", "(" + mBeanList.size() + ")人");
                intent.putExtra("qrcode_content_id", conversationId);
                startActivity(intent);
                break;
        }
    }

    private void initTitle() {
        setMyTitle("群名片", true);
        tvSquareName.setText(squareName);
        tvTeamSize.setText(String.valueOf(mBeanList.size()));

        ChatGroupHelper.setGroupAvatar(conversationId, new AvatarTakeListener() {
            @Override
            public void success(final Bitmap bitmap) {
                SquareCardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_square_head.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void failed(Exception e) {

            }
        });
    }

    private void getAllContacts(List<GroupMemberInfo> MBeanList) {
        KLog.e(MBeanList);
        for (int i = 0; i < MBeanList.size(); i++) {
            GroupMemberInfo mBean = mBeanList.get(i);
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
        join_group.setClickable(false);
        ChatGroupHelper.getGroupInfo(conversationId, new Impl<String>() {
            @Override
            public void response(int code, String data) {
                switch (code) {
                    case Impl.RESPON_DATA_SUCCESS:
                        GroupData groupData = JSONObject.parseObject(data, GroupData.class);
                        //群名称
                        tvSquareName.setText(TextUtils.isEmpty(groupData.getName())?"":groupData.getName());
                        //群详情
                        tvSquareDetail.setText(TextUtils.isEmpty(groupData.getAttr().getIntro())?
                        "暂无群简介":groupData.getAttr().getIntro());
                        //群简介
                        if (StringUtils.isActuallyEmpty(groupData.getAttr().getNotice())){
                            square_notice.setVisibility(View.GONE);
                        }else {
                            square_notice.setVisibility(View.VISIBLE);
                            square_notice.setText(groupData.getAttr().getNotice());
                        }
                        //加入按钮
                        isIn=groupData.is_in();
                        chatType=groupData.getAttr().getChat_type();
                        if (groupData.is_in() || groupData.getAttr().getChat_type()==1010){
                            join_group.setText("进入聊天");
                        }else {
                            join_group.setText("我要报名");
                        }
                        join_group.setClickable(true);
                        break;
                    case Impl.RESPON_DATA_ERROR:
                        UIHelper.toast(getActivity(),"没有信息");
                        break;
                }
            }
        });
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
