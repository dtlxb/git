package cn.gogoal.im.common.IMHelpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avospush.notification.NotificationCompat;

import java.util.HashMap;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.activity.TypeLoginActivity;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/2/20.
 */
public class MyMessageHandler extends AVIMMessageHandler {

    @Override
    public void onMessage(final AVIMMessage message, AVIMConversation conversation, final AVIMClient client) {
        try {
            final String clientID = AVIMClientManager.getInstance().getClientId();

            AVIMClientManager.getInstance().findConversationById(conversation.getConversationId(), new AVIMClientManager.ChatJoinManager() {
                @Override
                public void joinSuccess(final AVIMConversation conversation) {
                    //接收到消息，发送出去
                    if (clientID.equals(client.getClientId())) {
                        //剔除自己消息
                        if (!message.getFrom().equals(clientID)) {
                            showNotification(message, conversation);
                            final int chatType = (int) conversation.getAttribute("chat_type");
                            switch (chatType) {
                                case AppConst.IM_CHAT_TYPE_SINGLE:
                                    //单聊
                                    sendIMMessage(message, conversation);
                                    //更新通讯录
                                    JSONObject contentObject = JSON.parseObject(message.getContent());
                                    JSONObject lcattrsObject = contentObject.getJSONObject("_lcattrs");
                                    UserInfoUtils.upDateUserInfo(message.getFrom(), lcattrsObject.getString("avatar"), lcattrsObject.getString("username"));
                                    break;
                                case AppConst.IM_CHAT_TYPE_SQUARE:
                                case AppConst.IM_CHAT_TYPE_STOCK_SQUARE:
                                    //群聊，股票群聊
                                    //更新对话
                                    final JSONObject content_object = JSON.parseObject(message.getContent());
                                    final String _lctype = content_object.getString("_lctype");
                                    JSONObject lcattrsGroup = content_object.getJSONObject("_lcattrs");

                                    if (AppConst.IM_MESSAGE_TYPE_SQUARE_ADD.equals(_lctype) || AppConst.IM_MESSAGE_TYPE_SQUARE_DEL.equals(_lctype)) {
                                        conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                //通讯录
                                                JSONArray accountArray = content_object.getJSONObject("_lcattrs").getJSONArray("accountList");
                                                //群数据库加人删人
                                                if (AppConst.IM_MESSAGE_TYPE_SQUARE_ADD.equals(_lctype)) {
                                                    UserInfoUtils.saveGroupUserInfo(conversation.getConversationId(), accountArray);
                                                } else if (AppConst.IM_MESSAGE_TYPE_SQUARE_DEL.equals(_lctype)) {
                                                    UserInfoUtils.deleteGroupUserInfo(conversation.getConversationId(), accountArray);
                                                }
                                                //生成群头像(加人删人时候更改)
                                                if (conversation.getAttribute("avatar") == null || TextUtils.isEmpty((String) conversation.getAttribute("avatar"))) {
                                                    ChatGroupHelper.createGroupImage(conversation, "set_avatar");
                                                }
                                                //发送
                                                sendIMMessage(message, conversation);
                                            }
                                        });
                                    } else if (AppConst.IM_MESSAGE_TYPE_SQUARE_DETAIL.equals(_lctype)) {
                                        //群公告，群简介
                                        sendIMMessage(message, conversation);
                                    } else {
                                        //补全群信息(群信息没有的时候)
                                        List<UserBean> cacheBeans = UserInfoUtils.getAllGroupUserInfo(conversation.getConversationId());
                                        if (cacheBeans == null || cacheBeans.size() == 0) {
                                            UserUtils.getChatGroup(null, conversation.getConversationId(), new UserUtils.SquareInfoCallback() {
                                                @Override
                                                public void squareGetSuccess(JSONObject object) {
                                                }

                                                @Override
                                                public void squareGetFail(String error) {
                                                }
                                            });
                                        }

                                        //更新群通讯录
                                        UserInfoUtils.upDateUserInfo(message.getFrom(), lcattrsGroup.getString("avatar"), lcattrsGroup.getString("username"));
                                        sendIMMessage(message, conversation);
                                    }

                                    break;
                                case AppConst.IM_CHAT_TYPE_LIVE:
                                    //直播
                                    sendIMMessage(message, conversation);
                                    break;
                                case AppConst.IM_CHAT_TYPE_SYSTEM:
                                    //接收到消息，发送出去
                                    sendIMMessage(message, conversation);

                                    //加好友
                                    JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_newFriendList", new JSONArray());
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", message);
                                    jsonObject.put("isYourFriend", false);
                                    jsonArray.add(jsonObject);
                                    SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_newFriendList", jsonArray);
                                    break;
                                case AppConst.IM_CHAT_TYPE_CONTACTS_ACTION:
                                    //好友更新
                                    JSONObject contentObject1 = JSON.parseObject(message.getContent());
                                    JSONObject lcattrsObject1 = contentObject1.getJSONObject("_lcattrs");
                                    String avatar = lcattrsObject1.getString("avatar");
                                    String nickName = lcattrsObject1.getString("nickname");
                                    String conv_id = lcattrsObject1.getString("conv_id");
                                    String duty = lcattrsObject1.getString("duty");
                                    String friend_id = lcattrsObject1.getString("friend_id");

                                    UserBean userBean = new UserBean();
                                    userBean.setConv_id(conv_id);
                                    userBean.setNickname(nickName);
                                    userBean.setFriend_id(friend_id);
                                    userBean.setAvatar(avatar);
                                    userBean.setDuty(duty);
                                    userBean.setInYourContact(true);
                                    userBean.save();
                                    notifyContacts();
                                    break;

                                case AppConst.IM_CHAT_TYPE_CONSULTATION:
                                    //公众号
                                    sendIMMessage(message, conversation);
                                    break;
                                case AppConst.IM_CHAT_TYPE_SQUARE_REQUEST:
                                    //群通知
                                    sendIMMessage(message, conversation);
                                    //加好友
                                    JSONArray unAddArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversation.getConversationId() + "_unadd_accountList_beans", new JSONArray());
                                    JSONObject unAddJsonObject = new JSONObject();
                                    unAddJsonObject.put("message", message);
                                    unAddJsonObject.put("isYourFriend", false);
                                    unAddArray.add(unAddJsonObject);
                                    SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversation.getConversationId() + "_unadd_accountList_beans", unAddArray);
                                    break;
                                case AppConst.IM_CHAT_TYPE_LIVE_MESSAGE:
                                    //直播消息
                                    sendIMMessage(message, conversation);
                                    break;
                                case AppConst.IM_CHAT_TYPE_LIVE_REQUEST:
                                    //直播消息
                                    sendIMMessage(message, conversation);
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        client.close(null);
                    }
                }

                @Override
                public void joinFail(String error) {

                }
            });

        } catch (IllegalStateException e) {
            client.close(null);
        }

    }

    private void notifyContacts() {
        AppManager.getInstance().sendMessage("Change_Contacts");
    }

    private void sendIMMessage(AVIMMessage message, AVIMConversation conversation) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("conversation", conversation);
        int chatType = (int) conversation.getAttribute("chat_type");

        //用于标记已读未读
        if (message instanceof GGAudioMessage) {
            SPTools.saveBoolean(UserUtils.getMyAccountId() + message.getMessageId(), true);
        }

        BaseMessage baseMessage = new BaseMessage("IM_Info", map);
        AppManager.getInstance().sendMessage((chatType == AppConst.IM_CHAT_TYPE_LIVE_REQUEST ||
                chatType == AppConst.IM_CHAT_TYPE_LIVE_MESSAGE) ? "Live_Message" : "IM_Message", baseMessage);
    }


    /**
     * 推送Notification
     */
    private void showNotification(AVIMMessage message, AVIMConversation conversation) {
        boolean noBother = false;
        if (conversation.get("mu") != null) {
            List<String> muList = (List<String>) conversation.get("mu");
            noBother = muList.contains(UserUtils.getMyAccountId());
        }

        if (noBother) {
            return;
        }

        JSONObject content = JSONObject.parseObject(message.getContent());
        JSONObject lcattrs = content.getJSONObject("_lcattrs");
        String push = lcattrs.getString("push");
        if (push != null) {
            if (!AppDevice.isAppOnForeground(MyApp.getAppContext())) {
                try {
                    Intent resultIntent;
                    if (UserUtils.isLogin()) {
                        resultIntent = new Intent(MyApp.getAppContext(), MainActivity.class);
                    } else {
                        resultIntent = new Intent(MyApp.getAppContext(), TypeLoginActivity.class);
                    }

                    PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getAppContext(), 0,
                            resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApp.getAppContext())
                            .setSmallIcon(R.mipmap.logo).setContentTitle("Go-Goal股票")
                            .setContentText(push).setTicker(push);
                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setAutoCancel(true);

                    int mNotificationId = 10001;
                    NotificationManager mNotifyMgr = (NotificationManager) MyApp.getAppContext().
                            getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());

                } catch (Exception e) {
                }
            }
        }
    }
}
