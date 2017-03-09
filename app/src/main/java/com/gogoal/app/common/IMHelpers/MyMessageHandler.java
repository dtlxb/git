package com.gogoal.app.common.IMHelpers;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.gogoal.app.activity.SingleChatRoomActivity;
import com.gogoal.app.base.AppManager;
import com.gogoal.app.bean.BaseMessage;
import com.gogoal.app.common.UIHelper;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by huangxx on 2017/2/20.
 */

public class MyMessageHandler extends AVIMMessageHandler {
    @Override
    public void onMessage(final AVIMMessage message, AVIMConversation conversation, final AVIMClient client) {

        try {
            final String clientID = AVImClientManager.getInstance().getClientId();

            AVImClientManager.getInstance().findConversationById(conversation.getConversationId(), new AVImClientManager.ChatJoinManager() {
                @Override
                public void joinSuccess(AVIMConversation conversation) {
                    //接收到消息，发送出去
                    if (clientID.equals(client.getClientId())) {
                        //剔除自己消息
                        if (!message.getFrom().equals(clientID)) {
                            //接收到消息，发送出去
                            sendIMMessage(message, conversation);
                            KLog.e(conversation.getConversationId());
                        }
                    } else {
                        client.close(null);
                    }
                    sendIMMessage(message, conversation);
                    KLog.e(conversation.getConversationId());
                }

                @Override
                public void joinFail(String error) {

                }
            });

        } catch (IllegalStateException e) {
            client.close(null);
        }

    }


    private void sendIMMessage(AVIMMessage message, AVIMConversation conversation) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("conversation", conversation);

        BaseMessage baseMessage = new BaseMessage("IM_Info", map);
        AppManager.getInstance().sendMessage("IM_Message", baseMessage);
    }
}
