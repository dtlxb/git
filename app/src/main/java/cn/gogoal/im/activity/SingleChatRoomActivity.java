package cn.gogoal.im.activity;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;

/**
 * Created by huangxx on 2017/2/21.
 */

public class SingleChatRoomActivity extends BaseActivity {

    //聊天对象
    private ChatFragment chatFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_imsingle_room;
    }

    @Override
    public void doBusiness(Context mContext) {
        String conversation_id = (String) StringUtils.objectNullDeal(this.getIntent().getExtras().getString("conversation_id"), "");
        String nickname = (String) StringUtils.objectNullDeal(this.getIntent().getExtras().getString("nickname"), "");
        setMyTitle(nickname + "聊天窗口", false);

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);

        //输入聊天对象ID，返回conversation对象
        getSingleConversation(conversation_id);

    }

    public void getSingleConversation(String conversation_id) {

        KLog.e(conversation_id);
        //获取聊天conversation
        AVImClientManager.getInstance().findConversationById(conversation_id, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                chatFragment.setConversation(conversation);
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SingleChatRoomActivity.this, error);
            }
        });
    }
}
