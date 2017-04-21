package cn.gogoal.im.common.IMHelpers;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

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
                public void joinSuccess(final AVIMConversation conversation) {

                    //接收到消息，发送出去
                    if (clientID.equals(client.getClientId())) {
                        //剔除自己消息
                        if (!message.getFrom().equals(clientID)) {
                            final int chatType = (int) conversation.getAttribute("chat_type");
                            switch (chatType) {
                                case 1001:
                                    //单聊
                                    sendIMMessage(message, conversation);
                                    //更新通讯录
                                    JSONObject contentObject = JSON.parseObject(message.getContent());
                                    JSONObject lcattrsObject = contentObject.getJSONObject("_lcattrs");
                                    UserUtils.upDataContactInfo(Integer.parseInt(message.getFrom()), lcattrsObject.getString("avatar"),
                                            lcattrsObject.getString("username"), lcattrsObject.getString("conv_id"));
                                    break;
                                case 1002:
                                    //群聊
                                    //更新对话
                                    final JSONObject content_object = JSON.parseObject(message.getContent());
                                    final String _lctype = content_object.getString("_lctype");
                                    JSONObject lcattrsGroup = content_object.getJSONObject("_lcattrs");
                                    if ("5".equals(_lctype) || "6".equals(_lctype)) {
                                        conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                //通讯录
                                                JSONArray accountArray = content_object.getJSONObject("_lcattrs").getJSONArray("accountList");
                                                MessageUtils.changeSquareInfo(conversation.getConversationId(), accountArray, _lctype);

                                                //生成群头像
                                                ChatGroupHelper.createGroupImage(conversation.getConversationId(), conversation.getMembers());

                                                //发送
                                                sendIMMessage(message, conversation);
                                            }
                                        });
                                    } else {
                                        //更新群通讯录
                                        ChatGroupHelper.upDataGroupContactInfo(conversation.getConversationId(), Integer.parseInt(message.getFrom()),
                                                lcattrsGroup.getString("avatar"), lcattrsGroup.getString("username"));
                                        sendIMMessage(message, conversation);
                                    }

                                    break;
                                case 1003:
                                    //直播
                                    sendIMMessage(message, conversation);
                                    break;
                                case 1004:
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
                                case 1005:
                                    //好友更新
                                    JSONObject contentObjec1 = JSON.parseObject(message.getContent());
                                    JSONObject lcattrsObject1 = contentObjec1.getJSONObject("_lcattrs");
                                    String avatar = lcattrsObject1.getString("avatar");
                                    String nickName = lcattrsObject1.getString("nickname");
                                    String conv_id = lcattrsObject1.getString("conv_id");
                                    int friend_id = lcattrsObject1.getInteger("friend_id");
                                    ContactBean<String> contactBean = new ContactBean<>();
                                    contactBean.setNickname(nickName);
                                    contactBean.setAvatar(avatar);
                                    contactBean.setFriend_id(friend_id);
                                    contactBean.setContactType(ContactBean.ContactType.PERSION_ITEM);
                                    contactBean.setConv_id(conv_id);
                                    String friendList = UserUtils.updataFriendList(JSONObject.toJSONString(contactBean));
                                    SPTools.saveString(UserUtils.getMyAccountId() + "_contact_beans", friendList);
                                    KLog.e(SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", ""));
                                    break;

                                case 1006:
                                    //公众号
                                    sendIMMessage(message, conversation);
                                    break;
                                case 1007:
                                    //群通知
                                    sendIMMessage(message, conversation);
                                    //加好友
                                    JSONArray unAddArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversation.getConversationId() + "_unadd_accountList_beans", new JSONArray());
                                    JSONObject unAddjsonObject = new JSONObject();
                                    unAddjsonObject.put("message", message);
                                    unAddjsonObject.put("isYourFriend", false);
                                    unAddArray.add(unAddjsonObject);
                                    SPTools.saveJsonArray(UserUtils.getMyAccountId() + conversation.getConversationId() + "_unadd_accountList_beans", unAddArray);
                                    break;
                                case 1008:
                                    //直播消息
                                    sendIMMessage(message, conversation);
                                    break;
                                case 1009:
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

    private void sendIMMessage(AVIMMessage message, AVIMConversation conversation) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("conversation", conversation);
        int chatType = (int) conversation.getAttribute("chat_type");

        BaseMessage baseMessage = new BaseMessage("IM_Info", map);
        AppManager.getInstance().sendMessage((chatType == 1009 || chatType == 1008) ? "Live_Message" : "IM_Message", baseMessage);
    }
}
