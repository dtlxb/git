package cn.gogoal.im.common.IMHelpers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;

import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;

/**
 * Created by huangxx on 2017/2/28.
 */

public class MessageUtils {

    public static void saveMessageInfo(JSONArray thisJsonArray, AVIMConversation conversation, String messageAt, AVIMMessage message, String unReadCount) {

        JSONObject jsonObject = new JSONObject();
        if (thisJsonArray != null) {
            for (int i = 0; i < thisJsonArray.size(); i++) {
                if (thisJsonArray.getJSONObject(i).get("conversationID").equals(conversation.getConversationId())) {
                    thisJsonArray.remove(i);
                } else {
                }
            }

            jsonObject.put("conversationID", conversation.getConversationId());
            jsonObject.put("speakerTo", conversation.getMembers());
            jsonObject.put("lastTime", messageAt);
            jsonObject.put("lastMessage", message);
            jsonObject.put("unReadCounts", unReadCount);

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
            SPTools.saveJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", thisJsonArray);
        } else {

        }
    }
}
