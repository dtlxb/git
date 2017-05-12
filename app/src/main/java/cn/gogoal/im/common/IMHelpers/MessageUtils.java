package cn.gogoal.im.common.IMHelpers;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.socks.library.KLog;

import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/2/28.00
 */

public class MessageUtils {

    //消息列表：保存消息
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

            AVIMMessage message = imMessageBean.getLastMessage();
            //判断消息类型
            if (message instanceof AVIMImageMessage) {
                int mWidth = ((AVIMImageMessage) message).getFileMetaData().get("width") == null ?
                        0 : ((Number) ((AVIMImageMessage) message).getFileMetaData().get("width")).intValue();

                int mHeight = ((AVIMImageMessage) message).getFileMetaData().get("height") == null ?
                        0 : ((Number) ((AVIMImageMessage) message).getFileMetaData().get("height")).intValue();

                long mSize = ((AVIMImageMessage) message).getFileMetaData().get("size") == null ?
                        0L : ((Number) ((AVIMImageMessage) message).getFileMetaData().get("size")).longValue();

                ((AVIMImageMessage) message).getFileMetaData().put("width", mWidth);
                ((AVIMImageMessage) message).getFileMetaData().put("height", mHeight);
                ((AVIMImageMessage) message).getFileMetaData().put("size", mSize);

            } else if (message instanceof AVIMAudioMessage) {
                long mSize = ((AVIMAudioMessage) message).getFileMetaData().get("size") == null ?
                        0L : ((Number) ((AVIMAudioMessage) message).getFileMetaData().get("size")).longValue();
                ((AVIMAudioMessage) message).getFileMetaData().put("size", mSize);
            }

            thisJsonArray.add(jsonObject);
            SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", thisJsonArray);
        } else {

        }
    }

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

    //消息列表：移除消息
    public static void removeMessageInfo(String conversationID) {
        JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", new JSONArray());
        KLog.e(conversationID);
        for (int i = 0; i < jsonArray.size(); i++) {
            if (((JSONObject) jsonArray.get(i)).getString("conversationID").equals(conversationID)) {
                jsonArray.remove(jsonArray.get(i));
                break;
            }
        }
        KLog.e(jsonArray);
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", jsonArray);
    }

    public static int getAllMessageUnredCount(JSONArray jsonArray) {
        int count = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            count += jsonObject.getInteger("unReadCounts");
        }
        return count;
    }

    //根据conversationID移除
    public static void removeByID(String conv_id) {
        JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", new JSONArray());
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).get("conversationID").equals(conv_id)) {
                jsonArray.remove(i);
                break;
            } else {
            }
        }
        SPTools.saveJsonArray(UserUtils.getMyAccountId() + "_conversation_beans", jsonArray);
    }

    //群聊拉人加人(5:建群，拉人   6:踢人)
    public static void changeSquareInfo(String conversationID, JSONArray accountArray, String messageType) {
        JSONArray spAccountArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", new JSONArray());
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

    //从群消息提取出人名
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

    //提取聊天对方头像或者昵称
    public static String getContactWhatedInfo(String whated, int friendId, int chatType, String conversationID) {
        JSONArray jsonArray;
        JSONObject object;
        if (chatType == AppConst.IM_CHAT_TYPE_SINGLE) {
            object = SPTools.getJsonObject((friendId + ""), null);
            if (null != object) {
                return object.getString(whated);
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
                                return oldObject.getString(whated);
                            }
                        }
                    }
                }
            }
        } else if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
            object = SPTools.getJsonObject(UserUtils.getMyAccountId() + conversationID + friendId, null);
            if (null != object) {
                return object.getString(whated);
            } else {
                jsonArray = SPTools.getJsonArray(UserUtils.getMyAccountId() + conversationID + "_accountList_beans", new JSONArray());
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject oldObject = (JSONObject) jsonArray.get(i);
                        if (oldObject.getInteger("friend_id") == friendId) {
                            return oldObject.getString(whated);
                        }
                    }
                }
            }
        }
        return "";
    }

}
