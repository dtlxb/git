package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/2/21.
 */

public class SingleChatRoomActivity extends BaseActivity implements ChatFragment.MyListener {

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

        initTitle(nickname, conversation_id);

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);

        //输入聊天对象ID，返回conversation对象
        getSingleConversation(conversation_id, need_update);

    }

    private void initTitle(String nickname, final String conversation_id) {
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
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else {
                    UIHelper.toast(getActivity(), "连接断开了");
                }
            }
        };
        title.addAction(personAction, 0);
    }

    public void getSingleConversation(String conversation_id, final boolean need_update) {

        KLog.e(conversation_id);
        //获取聊天conversation
        AVImClientManager.getInstance().findConversationById(conversation_id, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                chatFragment.setConversation(conversation, need_update);
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SingleChatRoomActivity.this, error);
            }
        });
    }

    @Override
    public void setData(ContactBean contactBean) {
        this.contactBean = contactBean;
    }
}
