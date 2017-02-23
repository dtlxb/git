package com.gogoal.app.activity;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.IMHelpers.AVImClientManager;
import com.gogoal.app.common.UIHelper;
import com.gogoal.app.fragment.ChatFragment;

import java.util.List;

/**
 * Created by huangxx on 2017/2/20.
 */

public class SquareChatRoomActivity extends BaseActivity {

    //聊天对象
    private AVIMConversation imConversation;
    private ChatFragment chatFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_imdemo;
    }

    @Override
    public void doBusiness(Context mContext) {
        String userName = this.getIntent().getExtras().getString("userName");
        setMyTitle(userName + "聊天窗口", false);
        final String conversationID = this.getIntent().getExtras().getString("conversation_id");

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        getSquareConversation(conversationID);

    }

    private void getSquareConversation(String conversationId) {
        AVIMConversationQuery conversationQuery = AVImClientManager.getInstance().getClient().getQuery();
        // 根据room_id查找房间
        conversationQuery.whereEqualTo("objectId", conversationId);

        Log.e("LEAN_CLOUD", "查找聊天室" + conversationId);

        // 查找聊天
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {

                if (null == e) {
                    // 查询列表取第一个
                    if (null != list && list.size() > 0) {

                        imConversation = list.get(0);
                        joinSquare(imConversation);
                        Log.e("LEAN_CLOUD", "search chatroom success");
                    } else {
                        Log.e("LEAN_CLOUD", "search chatroom fail ");
                    }
                } else {
                    UIHelper.showSnack(SquareChatRoomActivity.this, "当前聊天房间不存在");
                    Log.e("LEAN_CLOUD", "查询条件没有查找到聊天对象" + e.toString());
                }
            }
        });

    }

    /**
     * 加入聊天室
     */
    private void joinSquare(AVIMConversation conversation) {
        conversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    chatFragment.setConversation(imConversation);
                }
            }
        });
    }
}
