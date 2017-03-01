package com.gogoal.app.common.IMHelpers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.gogoal.app.common.SPTools;

/**
 * Created by huangxx on 2017/2/28.
 */

public class MessageUtils {

    public static void saveMessageInfo(JSONArray thisJsonArray, AVIMConversation conversation) {

        JSONObject jsonObject = new JSONObject();
        if (thisJsonArray != null) {
            for (int i = 0; i < thisJsonArray.size(); i++) {
                if (thisJsonArray.getJSONObject(i).get("conversationID").equals(conversation.getConversationId())) {
                    thisJsonArray.remove(i);
                } else {
                }
            }

            jsonObject.put("conversationID", conversation.getConversationId());
            jsonObject.put("lastTime", conversation.getLastMessageAt());
            jsonObject.put("speakerTo", conversation.getMembers());
            jsonObject.put("lastMessage", conversation.getLastMessage());
            jsonObject.put("unReadCounts", 10 + "");

            thisJsonArray.add(jsonObject);
            SPTools.saveJsonArray("conversation_beans", thisJsonArray);
        } else {

        }
    }
}
