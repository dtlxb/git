package com.gogoal.app.common.IMHelpers;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;

import java.util.List;

/**
 * Created by huangxx on 2017/2/20.
 */

public class MyConversationHandler extends AVIMConversationEventHandler {

    @Override
    public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
        Log.e("LEAN_CLOUD", s + "离开聊天室" + avimConversation.getConversationId());
    }

    @Override
    public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
        Log.e("LEAN_CLOUD", s + "加入聊天室" + avimConversation.getConversationId());
    }

    @Override
    public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {
        Log.e("LEAN_CLOUD", avimClient.getClientId() + "被踢下线");
    }

    @Override
    public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }
}
