package cn.gogoal.im.common.IMHelpers;

import android.util.Log;

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

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.IMNewFriendBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

import java.util.HashMap;

import static cn.gogoal.im.common.UserUtils.getToken;

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
                                    break;
                                case 1002:
                                    //群聊
                                    //更新对话
                                    conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                                        @Override
                                        public void done(AVIMException e) {
                                            KLog.e(conversation.getName());
                                            //保存
                                            JSONObject contentObject = JSON.parseObject(message.getContent());
                                            String _lctype = contentObject.getString("_lctype");
                                            KLog.e(contentObject);
                                            if ("5".equals(_lctype) || "6".equals(_lctype)) {
                                                JSONArray messageListJsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
                                                IMMessageBean imMessageBean = new IMMessageBean(conversation.getConversationId(), chatType, message.getTimestamp(),
                                                        "0", conversation.getName(), AppConst.LEANCLOUD_APP_ID, "", message);
                                                MessageUtils.saveMessageInfo(messageListJsonArray, imMessageBean);

                                                //通讯录
                                                JSONArray accountArray = contentObject.getJSONObject("_lcattrs").getJSONArray("accountList");
                                                if (null != accountArray) {
                                                    SPTools.saveJsonArray(UserUtils.getToken() + conversation.getConversationId() + "_accountList_beans", accountArray);
                                                    KLog.e(accountArray.toString());
                                                }
                                            } else {
                                                //发送
                                                sendIMMessage(message, conversation);
                                            }
                                        }
                                    });

                                    break;
                                case 1003:
                                    //直播
                                    sendIMMessage(message, conversation);
                                    break;
                                case 1004:
                                    //接收到消息，发送出去
                                    sendIMMessage(message, conversation);

                                    //加好友
                                    JSONArray jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_newFriendList", new JSONArray());
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", message);
                                    jsonObject.put("isYourFriend", false);
                                    jsonArray.add(jsonObject);
                                    SPTools.saveJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_newFriendList", jsonArray);
                                    break;
                                case 1005:
                                    //好友更新
                                    JSONObject contentObject = JSON.parseObject(message.getContent());
                                    JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                                    String avatar = lcattrsObject.getString("avatar");
                                    String nickName = lcattrsObject.getString("nickname");
                                    String conv_id = lcattrsObject.getString("conv_id");
                                    int friend_id = lcattrsObject.getInteger("friend_id");
                                    ContactBean<String> contactBean = new ContactBean<>();
                                    contactBean.setRemark("");
                                    contactBean.setNickname(nickName);
                                    contactBean.setAvatar(avatar);
                                    contactBean.setFriend_id(friend_id);
                                    contactBean.setContactType(ContactBean.ContactType.PERSION_ITEM);
                                    contactBean.setConv_id(conv_id);
                                    String friendList = UserUtils.updataFriendList(JSONObject.toJSONString(contactBean));
                                    SPTools.saveString(getToken() + "_contact_beans", friendList);

                                    break;
                                case 1006:
                                    //公众号
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

        BaseMessage baseMessage = new BaseMessage("IM_Info", map);
        AppManager.getInstance().sendMessage("IM_Message", baseMessage);
    }
}
