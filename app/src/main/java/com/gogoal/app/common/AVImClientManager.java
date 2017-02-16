package com.gogoal.app.common;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by huangxx on 17/2/16.
 */
public class AVImClientManager {

    private static AVImClientManager imClientManager;

    private AVIMClient avimClient;  ///< 连接server
    private String clientId;        ///< 连接者id
    private Activity conversationAc;

    private Boolean isShotOff = false;  ///< 是否被踢出

    public synchronized static AVImClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManager();
        }
        return imClientManager;
    }

    private AVImClientManager() {

    }

    public void setIsShotOff(Boolean isShot) {
        this.isShotOff = isShot;
    }

    // 通过id启动连接
    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.close(null);
        avimClient.open(callback);
    }

    // 关闭启动连接
    public void close() {

        if (isShotOff == true) {  // 被踢出不能关闭连接
            isShotOff = false;
            return;
        }

        if (null == avimClient) return;

        avimClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null != e) Log.e("LEAN_CLOUD", e.toString());
            }
        });
    }

    // 获取server
    public AVIMClient getClient() {
        return avimClient;
    }

    // 获取连接者id
    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

    // 聊天页面
    public void setConversationActiviy(Activity conversationActiviy) {
        this.conversationAc = conversationActiviy;
    }

    public Activity getConversationActiviy() {
        return conversationAc;
    }

    /**************************
     * 聊天室
     **************************/

    private AVIMConversation conversation;          ///< 聊天室对象
    private LinkedList<AVIMMessage> messageList;    ///< 当前聊天类聊天信息
    private ChatInfoManager chatInfoManager;        ///< 聊天信息
    private ChatJoinManager chatJoinManager;        ///< 加入聊天室
    private ChatSendManager chatSendManager;        ///< 发送信息
    private ChatErrorManager chatErrorManager;      ///< 错误信息
    private ChatHistoryMessage chatHistoryMessage;  ///< 查询历史消息

    private Boolean isSelfLogin = true;             ///< 是否自己登陆


    public Boolean getSelfLogin() {
        return isSelfLogin;
    }

    public void setSelfLogin(Boolean selfLogin) {
        isSelfLogin = selfLogin;
    }

    // 发送消息代理
//    private final YFOKHTTP.GGHttpInterface sendMessage = new YFOKHTTP.GGHttpInterface() {
//        @Override
//        public void onSuccess(String responseInfo) {
//
//            Log.e("LEAN_CLOUD", "聊天发送成功");
//
//            if (chatSendManager != null) chatSendManager.sendSuccess();
//        }
//
//        @Override
//        public void onFailure(String msg) {
//
//            if (chatSendManager != null) chatSendManager.sendFail();
//
//            Log.e("LEAN_CLOUD", "聊天信息发送失败" + msg);
//        }
//    };

    /**************************
     * 设置代理
     **************************/

    public void setChatHistoryMessage(ChatHistoryMessage chatHistoryMessage) {
        this.chatHistoryMessage = chatHistoryMessage;
    }

    public void setChatInfoManager(ChatInfoManager chatInfoManager) {
        this.chatInfoManager = chatInfoManager;
    }

    public void setChatSendManager(ChatSendManager chatSendManager) {
        this.chatSendManager = chatSendManager;
    }

    public void setChatJoinManager(ChatJoinManager chatJoinManager) {
        this.chatJoinManager = chatJoinManager;
    }

    /**************************
     * 聊天室方法
     **************************/

    public String getCreateID() {

        if (null == conversation.getCreator()) {
            return "";
        }

        return conversation.getCreator();
    }

    // 接收文字调用
    public void sendMessageToDelegate(AVIMMessage message, AVIMConversation conversation) {

        // 判断id是否一致
        if (conversation.getConversationId().equals(conversation.getConversationId())) {

            if (null != chatInfoManager) chatInfoManager.chatRoomReceiveMessage(message);
        }
    }


    /**************************
     * 查询历史记录
     **************************/

    // 查询消息
    public void queryChatMessage(int limit) {

        if (messageList == null) {
            messageList = new LinkedList<AVIMMessage>();
        }

        if (messageList.size() > 0) {   // 查询历史信息
            // 获取历史最旧一条Message
            AVIMMessage message = messageList.get(0);
//            queryOldMessages(message, limit);
        } else {
            queryNewMessages(limit);
        }
    }

    // 查询历史记录(自己接口)
//    public void queryGGMessage(int limit, String conversationId, int page,boolean is_only,int order_type) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put("token", UserUtils.getToken());
//        map.put("live_id", conversationId);
//        map.put("rows", limit + "");
//        map.put("page", page + "");
//        map.put("is_only", is_only + "");
//        map.put("order_type", order_type + "");
//
//        YFOKHTTP.GGHttpInterface message = new YFOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//                KLog.json(responseInfo);
//                if (null != chatHistoryMessage) {
//                    chatHistoryMessage.chatRoomHistoryMessage(responseInfo);
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//                Log.e("LEAN_CLOUD", "历史消息获取失败" + msg);
//            }
//        };
//
//        new YFOKHTTP(map, YFOKHTTP.LIVE_HISTORY_MESSAGE, message).startGet();
//    }

    // 查询最新几条聊天记录
    private void queryNewMessages(final int limit) {

        if (conversation != null) {

            conversation.queryMessages(limit, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {

                    if (null != e) {

                        Log.e("LEAN_CLOUD", "查询最新聊天记录错误" + e.toString());
                        if (null != chatErrorManager) chatErrorManager.queryMessageError();
                        return;
                    }

                    // 查找成功
                    messageList = new LinkedList<AVIMMessage>(list);   // 得到最新记录
                    if (null != chatInfoManager) chatInfoManager.chatRoomMessages(list);
                }
            });
        }
    }

    // 查询条信息之前的记录
//    private void queryOldMessages(final AVIMMessage message, int limit) {
//
//        if (conversation != null) {
//
//            conversation.queryMessages(message.getConversationId(), message.getTimestamp(), limit, new AVIMMessagesQueryCallback() {
//                @Override
//                public void done(List<AVIMMessage> list, AVIMException e) {
//
//                    if (e != null) {
//
//                        Log.e("LEAN_CLOUD", "查询最新聊天记录错误" + e.toString());
//
//                        if (null != chatErrorManager) chatErrorManager.queryMessageError();
//
//                        return;
//                    }
//
//                    // 查找成功
//                    messageList = Global.listAddList(new LinkedList<AVIMMessage>(list), messageList);
//                    if (null != chatInfoManager) chatInfoManager.chatRoomMessages(list);
//                }
//            });
//        }
//    }

    /************************
     * 发送聊天室消息
     *************************/

    // 发送聊天室消息
//    public void sendChatMessage(String text, String convsation_id, final ChatSendManager sendManager) {
//
//        Log.e("LEAN_CLOUD", "预备发送消息");
//
//        if (null != conversation) {
//
//            Log.e("LEAN_CLOUD", "发送消息接口发送");
//
//            Map<String, String> map = new HashMap<>();
//            map.put("conv_id", convsation_id);
//            map.put("token", UserUtils.getToken());
//            map.put("message", Global.getTextMessageString(text));
//
//            YFOKHTTP.GGHttpInterface sendMessage = new YFOKHTTP.GGHttpInterface() {
//                @Override
//                public void onSuccess(String responseInfo) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送成功");
//
//                    if (sendManager != null) sendManager.sendSuccess();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送失败");
//
//                    if (sendManager != null) sendManager.sendFail();
//                }
//            };
//
//            new YFOKHTTP(map, YFOKHTTP.SEND_LIVE_MESSAGE, sendMessage).startGet();
//        }
//    }

    // 发送语音信息
//    public void sendVoiceMessage(String url, Integer answerSec, String text, String speakerId, String convsation_id, final ChatSendManager sendManager) {
//
//        if (null != conversation) {
//
//            Map<String, String> map = new HashMap<>();
//            map.put("conv_id", convsation_id);
//            map.put("token", UserUtils.getToken());
//            map.put("message", Global.getVoiceMessageString(url, answerSec, text,speakerId));
//
//            YFOKHTTP.GGHttpInterface sendMessage = new YFOKHTTP.GGHttpInterface() {
//                @Override
//                public void onSuccess(String responseInfo) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送成功");
//
//                    if (sendManager != null) sendManager.sendSuccess();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送失败");
//
//                    if (sendManager != null) sendManager.sendFail();
//                }
//            };
//
//            new YFOKHTTP(map, YFOKHTTP.SEND_LIVE_MESSAGE, sendMessage).startGet();
//        }
//    }

    // 发送图片信息
//    public void sendPhotoMessage(String url, String convsation_id, final ChatSendManager sendManager,int width,int height) {
//
//        if (null != conversation) {
//
//            Map<String, String> map = new HashMap<>();
//            map.put("conv_id", convsation_id);
//            map.put("token", UserUtils.getToken());
//            map.put("message", Global.getPhotoMessageString(url,width,height));
//
//            YFOKHTTP.GGHttpInterface sendMessage = new YFOKHTTP.GGHttpInterface() {
//                @Override
//                public void onSuccess(String responseInfo) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送成功");
//
//                    if (sendManager != null) sendManager.sendSuccess();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//
//                    Log.e("LEAN_CLOUD", "聊天发送失败");
//
//                    if (sendManager != null) sendManager.sendFail();
//                }
//            };
//
//            new YFOKHTTP(map, YFOKHTTP.SEND_LIVE_MESSAGE, sendMessage).startGet();
//        }
//    }

    /**************************
     * 退出聊天室
     **************************/
  /*
   * 注意: 退出失败需要进行再次退出,
   * 因为LeanCloud本地会有缓存文件,
   * 再次启动长连接会存在
   */
    // 退出当前聊天室
    public void quit() {

//    if (isShotOff == true) {  // 被踢出不能关闭连接
//      isShotOff = false;
//      return;
//    }

        if (conversation == null) return;

        Log.e("LEAN_CLOUD", "启动推出");

        conversation.quit(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                // 退出成功
                messageList = new LinkedList<AVIMMessage>();

                if (e != null) {
                    Log.e("LEAN_CLOUD", "推出失败" + e.toString());
                } else {
                    conversation = null;
                }
            }
        });
    }

    /**************************
     * 进入聊天室
     **************************/

    // 加入聊天室(共有方法)
    public void joinChatRoom(String room_id) {

        if (null == avimClient) Log.e("LEAN_CLOUD", "连接为空");

        if (null != avimClient) {

            Log.e("LEAN_CLOUD", "加入聊天第一步");

            searchChatRoom(room_id);
        }
    }

    // 查找聊天室
    private void searchChatRoom(String conversationId) {

        AVIMConversationQuery conversationQuery = avimClient.getQuery();
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

                        conversation = list.get(0);
                        joinWithConversation(conversation);
                        Log.e("LEAN_CLOUD", "查找聊天对象成功");
                    }
                } else {

                    if (null != chatJoinManager) {
                        chatJoinManager.joinFail();
                    }
                    Log.e("LEAN_CLOUD", "查询条件没有查找到聊天对象" + e.toString());
                }
            }
        });
    }

    // 加入聊天室 （先踢自己在加入）
    private void joinWithConversation(final AVIMConversation cov) {

//    List<String> list = new LinkedList<>();
//
//    Boolean cankick = false;
//
//    for (int z = 0; z < list.size(); z++) {
//
//      if (UserUtils.getUserAccountId().equals(list.get(z))) {
//
//        cankick = true;
//
//        Log.e("TTT", "进入踢人");
//      }
//    }

        //if (cankick) {

//      cov.kickMembers(list, new AVIMConversationCallback() {
//        @Override
//        public void done(AVIMException e) {
//
//          Log.e("TTT", "踢人成功");
//
//          joinToConversation(cov);
//        }
//      });
        //}
        //else {

        //joinToConversation(cov);
        //}

        setSelfLogin(true);
        joinToConversation(cov);
    }

    // 加入聊天室（直接加入）
    private void joinToConversation(AVIMConversation cov) {

        cov.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (null == e) {
                    messageList = new LinkedList<AVIMMessage>();
                    Log.e("LEAN_CLOUD", "加入聊天室成功");
                    if (null != chatJoinManager) {
                        chatJoinManager.joinSuccess();
                    }
                } else {

                    if (null != chatJoinManager) {
                        chatJoinManager.joinFail();
                    }
                    Log.e("LEAN_CLOUD", "加入聊天室失败" + e.toString());
                }
            }
        });
    }

    /**************************
     * 聊天室代理
     **************************/

    // 查询当前聊天室
    public interface ChatInfoManager {

        void chatRoomMessages(List<AVIMMessage> list);        ///< 查询聊天记录回调

        void chatRoomReceiveMessage(AVIMMessage message);     ///< 聊天室实时推送消息
    }

    public interface ChatErrorManager {

        void queryMessageError();     ///< 查询信息错误
    }

    public interface ChatSendManager {

        void sendSuccess();           ///< 发送成功

        void sendFail();              ///< 发送失败
    }

    public interface ChatJoinManager {

        void joinSuccess();   ///< 加入房间成功

        void joinFail();      ///< 加入房间失败
    }

    public interface ChatHistoryMessage {

        void chatRoomHistoryMessage(String request);
    }
}
