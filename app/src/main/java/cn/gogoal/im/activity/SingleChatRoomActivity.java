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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.IMHelpers.UserInfoUtils;
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
    private UserBean userBean;
    private XTitle xTitle;

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
        xTitle = setMyTitle(nickname, true);
        xTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleChatRoomActivity.this, MessageHolderActivity.class);
                startActivity(intent);
            }
        });

        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(SingleChatRoomActivity.this, R.mipmap.chat_person)) {
            @Override
            public void actionClick(View view) {
                if (null != userBean) {
                    Intent intent = new Intent(SingleChatRoomActivity.this, IMPersonActivity.class);
                    Bundle mBundle = new Bundle();

                    ContactBean contactBean = new ContactBean();
                    contactBean.setFriend_id(userBean.getFriend_id());
                    contactBean.setConv_id(userBean.getConv_id());
                    contactBean.setAvatar(userBean.getAvatar());
                    contactBean.setNickname(userBean.getNickname());

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
        xTitle.addAction(personAction, 0);
    }

    public void getSingleConversation(String conversation_id, final boolean need_update, final List<AVIMMessage> messageList) {

        //获取聊天conversation
        AVIMClientManager.getInstance().findConversationById(conversation_id, new AVIMClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                userBean = getYourSpeaker(conversation);
                chatFragment.setConversation(conversation, need_update, 0, userBean, messageList);
            }

            @Override
            public void joinFail(String error) {
                KLog.e(error);
                UIHelper.toast(SingleChatRoomActivity.this, error);
            }
        });
    }

    private UserBean getYourSpeaker(AVIMConversation conversation) {
        //拿到对方
        String speakTo = "";
        UserBean userBean = null;
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
        userBean = UserInfoUtils.getSomeone(Integer.parseInt(speakTo));
        return userBean;
    }

}
