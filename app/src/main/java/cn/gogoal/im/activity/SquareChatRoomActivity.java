package cn.gogoal.im.activity;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;

/**
 * Created by huangxx on 2017/2/20.
 */

public class SquareChatRoomActivity extends BaseActivity {

    //聊天对象
    private ChatFragment chatFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_imdemo;
    }

    @Override
    public void doBusiness(Context mContext) {
        String userName = this.getIntent().getExtras().getString("userName");
        setMyTitle(userName, false);
        final String conversationID = this.getIntent().getExtras().getString("conversation_id");

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        getSquareConversation(conversationID);

    }

    private void getSquareConversation(String conversationId) {
        AVImClientManager.getInstance().findConversationById(conversationId, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                joinSquare(conversation);
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SquareChatRoomActivity.this, error);
            }
        });

    }

    /**
     * 加入聊天室
     */
    private void joinSquare(final AVIMConversation conversation) {
        conversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    chatFragment.setConversation(conversation);
                }
            }
        });
    }
}
