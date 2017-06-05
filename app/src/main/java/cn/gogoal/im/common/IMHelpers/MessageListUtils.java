package cn.gogoal.im.common.IMHelpers;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/2/28.00
 * 消息列表缓存处理
 */

public class MessageListUtils {

    /**
     * 消息列表：保存消息
     */
    public static void saveMessageInfo(JSONArray thisJsonArray, IMMessageBean imMessageBean) {

        JSONObject jsonObject = new JSONObject();
        if (thisJsonArray != null && imMessageBean != null) {
            for (int i = 0; i < thisJsonArray.size(); i++) {
                if (thisJsonArray.getJSONObject(i).get("conversationID").equals(imMessageBean.getConversationID())) {
                    thisJsonArray.remove(i);
                    break;
                } else {
                }
            }

            jsonObject.put("conversationID", imMessageBean.getConversationID());
            jsonObject.put("lastTime", imMessageBean.getLastTime());
            jsonObject.put("lastMessage", imMessageBean.getLastMessage());
            jsonObject.put("unReadCounts", imMessageBean.getUnReadCounts());
            jsonObject.put("nickname", imMessageBean.getNickname());
            jsonObject.put("chatType", imMessageBean.getChatType());
            jsonObject.put("friend_id", imMessageBean.getFriend_id());
            jsonObject.put("avatar", imMessageBean.getAvatar());

            AVIMMessage message = JSON.parseObject(imMessageBean.getLastMessage(), AVIMMessage.class);
            //判断消息类型
            if (message instanceof GGImageMessage) {
                double width = StringUtils.pareseStringDouble(String.valueOf(((GGImageMessage) message).getFileMetaData().get("width")));
                int mWidth = (int) width;
                double height = StringUtils.pareseStringDouble(String.valueOf(((GGImageMessage) message).getFileMetaData().get("height")));
                int mHeight = (int) height;
                double size = StringUtils.pareseStringDouble(String.valueOf(((GGImageMessage) message).getFileMetaData().get("size")));
                int mSize = (int) size;

                ((GGImageMessage) message).getFileMetaData().put("width", mWidth);
                ((GGImageMessage) message).getFileMetaData().put("height", mHeight);
                ((GGImageMessage) message).getFileMetaData().put("size", mSize);

            } else if (message instanceof GGAudioMessage) {
                long mSize = ((GGAudioMessage) message).getFileMetaData().get("size") == null ?
                        0L : ((Number) ((GGAudioMessage) message).getFileMetaData().get("size")).longValue();
                ((GGAudioMessage) message).getFileMetaData().put("size", mSize);
            }

            thisJsonArray.add(jsonObject);
            UserUtils.saveMessageListInfo(thisJsonArray);

        } else {

        }
    }


    /**
     * 根据对话id寻找消息
     */
    public static IMMessageBean getIMMessageBeanById(JSONArray thisJsonArray, String conv_id) {
        if (thisJsonArray != null && !TextUtils.isEmpty(conv_id)) {
            for (int i = 0; i < thisJsonArray.size(); i++) {
                if (thisJsonArray.getJSONObject(i).get("conversationID").equals(conv_id)) {
                    return JSON.parseObject(String.valueOf(thisJsonArray.get(i)), IMMessageBean.class);
                } else {
                }
            }
        }
        return new IMMessageBean(conv_id, AppConst.IM_CHAT_TYPE_SQUARE, System.currentTimeMillis(), "0", "", "", "", null);
    }

    /**
     * 消息列表：移除消息
     */
    public static void removeMessageInfo(String conversationID) {
        JSONArray jsonArray = UserUtils.getMessageListInfo();
        KLog.e(conversationID);
        for (int i = 0; i < jsonArray.size(); i++) {
            if (((JSONObject) jsonArray.get(i)).getString("conversationID").equals(conversationID)) {
                jsonArray.remove(jsonArray.get(i));
                break;
            }
        }
        KLog.e(jsonArray);
        UserUtils.saveMessageListInfo(jsonArray);
    }

    /**
     * 消息列表：获取未读数
     */
    public static int getAllMessageUnreadCount(JSONArray jsonArray) {
        int count = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            count += jsonObject.getInteger("unReadCounts");
        }
        return count;
    }

    /**
     * 消息列表：获取未读数
     */
    public static int getAllMessageUnreadCount(List IMMessages) {
        int count = 0;
        for (int i = 0; i < IMMessages.size(); i++) {
            IMMessageBean bean = (IMMessageBean) IMMessages.get(i);
            count += Integer.parseInt(bean.getUnReadCounts());
        }
        return count;
    }

    /**
     * 从群消息提取出人名
     */
    public static String findSquarePeople(JSONArray accountArray, String messageType) {
        StringBuilder mSB = new StringBuilder();
        String ms = "";
        for (int i = 0; i < accountArray.size(); i++) {
            JSONObject object = (JSONObject) accountArray.get(i);
            boolean needIt = object.getString("nickname").equals(UserUtils.getUserName());
            if (!needIt) {
                mSB.append(object.getString("nickname"));
                if (accountArray.size() > 1 && i < accountArray.size() - 1) {
                    mSB.append("、");
                }
            }
        }
        if (messageType.equals("5")) {
            ms = "加入了群聊";
        } else if (messageType.equals("6")) {
            ms = "离开了群聊";
        }
        return mSB + ms;
    }

    /**
     * 获取所需要的字段
     */
    public static String getContactWantedInfo(String wanted, int friendId, int chatType, String conversationID) {
        JSONArray jsonArray;
        JSONObject object;
        if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
            object = SPTools.getJsonObject(UserUtils.getMyAccountId() + friendId + "", null);
            if (null != object) {
                return object.getString(wanted);
            } else {
                String string = SPTools.getString(UserUtils.getMyAccountId() + "_contact_beans", null);
                KLog.e(string);
                if (!TextUtils.isEmpty(string)) {
                    JSONObject jsonObject = JSON.parseObject(string);
                    KLog.e(jsonObject.toString());
                    if (jsonObject.get("data") != null) {
                        jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject oldObject = (JSONObject) jsonArray.get(i);
                            if (oldObject.getInteger("friend_id") == friendId) {
                                return oldObject.getString(wanted);
                            }
                        }
                    }
                }
            }
        } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
            object = SPTools.getJsonObject(UserUtils.getMyAccountId() + conversationID + friendId, null);
            if (null != object) {
                return object.getString(wanted);
            } else {
                jsonArray = UserUtils.getGroupContactInfo(conversationID);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject oldObject = (JSONObject) jsonArray.get(i);
                        if (oldObject.getInteger("friend_id") == friendId) {
                            return oldObject.getString(wanted);
                        }
                    }
                }
            }
        }
        return "";
    }

    //群聊拉人加人(5:建群，拉人   6:踢人)
    public static void changeSquareInfo(String conversationID, JSONArray accountArray, String messageType) {
        JSONArray spAccountArray = UserUtils.getGroupContactInfo(conversationID);
        if (null != accountArray) {
            if (messageType.equals("5")) {
                spAccountArray.addAll(accountArray);
            } else if (messageType.equals("6")) {
                spAccountArray.removeAll(accountArray);
            }
            UserUtils.saveGroupContactInfo(conversationID, spAccountArray);
        } else {

        }
    }

}
