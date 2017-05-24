package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.ChatFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/2/21.
 */

public class SingleChatRoomActivity extends BaseActivity {

    //聊天对象
    private ChatFragment chatFragment;
    private ContactBean contactBean;

    @Override
    public int bindLayout() {
        return R.layout.activity_imsingle_room;
    }

    @Override
    public void doBusiness(Context mContext) {
        String conversation_id = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("conversation_id"));
        String nickname = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("nickname"));
        boolean need_update = this.getIntent().getBooleanExtra("need_update", false);
        List<AVIMMessage> messageChooesed = this.getIntent().getParcelableArrayListExtra("messages_chooesed");
        initTitle(nickname, conversation_id);

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);

        //输入聊天对象ID，返回conversation对象
        getSingleConversation(conversation_id, need_update, messageChooesed);

    }

    private void initTitle(final String nickname, final String conversation_id) {
        XTitle title = setMyTitle(nickname, true);

        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(SingleChatRoomActivity.this, R.mipmap.chat_person)) {
            @Override
            public void actionClick(View view) {
                if (null != contactBean) {
                    Intent intent = new Intent(SingleChatRoomActivity.this, IMPersonActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("seri", contactBean);
                    mBundle.putString("conversation_id", conversation_id);
                    mBundle.putString("nickname", nickname);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else {
                    UIHelper.toast(getActivity(), "连接断开了");
                }
            }
        };
        title.addAction(personAction, 0);
    }

    public void getSingleConversation(String conversation_id, final boolean need_update, final List<AVIMMessage> messageList) {

        KLog.e(conversation_id);
        //获取聊天conversation
        AVIMClientManager.getInstance().findConversationById(conversation_id, new AVIMClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                contactBean = getYourSpeaker(conversation);
                chatFragment.setConversation(conversation, need_update, 0, contactBean, messageList);
            }

            @Override
            public void joinFail(String error) {
                KLog.e(error);
                UIHelper.toast(SingleChatRoomActivity.this, error);
            }
        });
    }

    private ContactBean getYourSpeaker(AVIMConversation conversation) {
        //拿到对方
        String speakTo = "";
        List<String> members = new ArrayList<>();
        members.addAll(conversation.getMembers());
        if (members.size() > 0) {
            if (members.size() == 2) {
                if (members.contains(UserUtils.getMyAccountId())) {
                    members.remove(UserUtils.getMyAccountId());
                    speakTo = members.get(0);
                }
            } else {
            }
        } else {
        }
        JSONObject object = SPTools.getJsonObject(speakTo + "", null);
        if (null != object) {
            return JSON.parseObject(String.valueOf(object), ContactBean.class);
        } else {
            String responseInfo = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", "");
            List<ContactBean<String>> contactBeanList = new ArrayList<>();
            if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                BaseBeanList<ContactBean<String>> beanList = JSONObject.parseObject(
                        responseInfo,
                        new TypeReference<BaseBeanList<ContactBean<String>>>() {
                        });
                List<ContactBean<String>> list = beanList.getData();

                for (ContactBean<String> bean : list) {
                    bean.setContactType(ContactBean.ContactType.PERSION_ITEM);
                }

                contactBeanList.addAll(list);
            }
            for (int i = 0; i < contactBeanList.size(); i++) {
                if ((contactBeanList.get(i).getFriend_id() + "").equals(speakTo)) {
                    return contactBeanList.get(i);
                }
            }
        }
        return null;
    }

}
