package com.gogoal.app.common.IMHelpers;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import java.util.List;

/**
 * Created by huangxx on 17/02/20.
 */
public class AVImClientManager {

    private static AVImClientManager imClientManager;

    public AVIMClient avimClient;               //< 连接server
    private String clientId;                    //< 连接serverID
    private ChatInfoManager chatInfoManager;    //< 聊天信息

    private AVImClientManager() {
    }

    public synchronized static AVImClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManager();
        }
        return imClientManager;
    }

    /**
    * 打开连接，返回Client对象
    * */
    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

    public ChatInfoManager getChatInfoManager() {
        return chatInfoManager;
    }

    public void setChatInfoManager(ChatInfoManager chatInfoManager) {
        this.chatInfoManager = chatInfoManager;
    }

    // 接收文字调用
    public void sendMessageToDelegate(AVIMMessage message, AVIMConversation conversation) {

        if (null != chatInfoManager) chatInfoManager.chatRoomReceiveMessage(conversation, message);
    }

    /**
    * 聊天室代理
    * */
    // 聊天消息接收
    public interface ChatInfoManager {

        void chatRoomMessages(List<AVIMMessage> list);                                       //查询聊天记录回调

        void chatRoomReceiveMessage(AVIMConversation conversation, AVIMMessage message);     //聊天室实时推送消息
    }
}
