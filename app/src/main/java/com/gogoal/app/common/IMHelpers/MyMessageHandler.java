package com.gogoal.app.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

/**
 * Created by huangxx on 2017/2/20.
 */

public class MyMessageHandler extends AVIMMessageHandler {
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        AVImClientManager.getInstance().sendMessageToDelegate(message, conversation);
    }
}
