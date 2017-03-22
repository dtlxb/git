package cn.gogoal.im.common.IMHelpers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.socks.library.KLog;

import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;

/**
 * Created by huangxx on 2017/2/28.
 */

public class MessageUtils {

    public static void saveMessageInfo(JSONArray thisJsonArray, IMMessageBean imMessageBean) {

        JSONObject jsonObject = new JSONObject();
        if (thisJsonArray != null) {
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
            SPTools.saveJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", thisJsonArray);
        } else {

        }
    }

    public static void removeMessageInfo(int position) {
        JSONArray jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
        if (null != jsonArray) {
            jsonArray.remove(position);
        }
        SPTools.saveJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", jsonArray);
    }
}
