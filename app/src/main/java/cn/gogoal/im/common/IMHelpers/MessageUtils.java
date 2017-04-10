package cn.gogoal.im.common.IMHelpers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.okio.Buffer;
import com.socks.library.KLog;

import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/2/28.
 */

public class MessageUtils {

    //消息列表：保存消息
    public static void saveMessageInfo(JSONArray thisJsonArray, IMMessageBean imMessageBean) {

        JSONObject jsonObject = new JSONObject();
        if (thisJsonArray != null && imMessageBean != null) {
            for (int i = 0; i < thisJsonArray.size(); i++) {
                if (thisJsonArray.getJSONObject(i).get("conversationID").equals(imMessageBean.getConversationID())) {
                    thisJsonArray.remove(i);
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
            KLog.e(jsonObject);
            SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", thisJsonArray);
        } else {

        }
    }

    //消息列表：移除消息
    public static void removeMessageInfo(int position) {
        JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", new JSONArray());
        if (null != jsonArray) {
            jsonArray.remove(position);
        }
        SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", jsonArray);
    }

    //根据conversationID移除
    public static void removeByID(String conv_id) {
        JSONArray jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", new JSONArray());
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).get("conversationID").equals(conv_id)) {
                jsonArray.remove(i);
            } else {
            }
        }
        SPTools.saveJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", jsonArray);
    }

    //群聊拉人加人(5:建群，拉人   6:踢人)
    public static void changeSquareInfo(String conversationID, JSONArray accountArray, String messageType) {
        JSONArray spAccountArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + conversationID + "_accountList_beans", new JSONArray());
        spAccountArray.addAll(accountArray);
        if (null != accountArray) {
            if (messageType.equals("5")) {
            } else if (messageType.equals("6")) {
                spAccountArray.removeAll(accountArray);
            }
            KLog.e(spAccountArray.toString());
            SPTools.saveJsonArray(UserUtils.getUserAccountId() + conversationID + "_accountList_beans", spAccountArray);
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

}
