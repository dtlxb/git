package cn.gogoal.im.common.IMHelpers;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;


import org.litepal.crud.DataSupport;

import java.util.List;

import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.bean.UserBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/2/28.00
 * 消息列表缓存处理
 */

public class MessageListUtils {

    /**
     * 消息列表：保存消息
     */
    /*public static void saveMessageInfo(JSONArray thisJsonArray, IMMessageBean imMessageBean) {

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
    }*/

    /**
     * 消息列表：保存消息
     */
    public static void saveMessageInfo(IMMessageBean imMessageBean) {
        List<IMMessageBean> cacheBeans = DataSupport.where("conversationID = ?",
                imMessageBean.getConversationID()).find(IMMessageBean.class);
        if (null != cacheBeans && cacheBeans.size() > 0) {
            cacheBeans.get(0).delete();
        }
        imMessageBean.save();
    }

    /**
     * 根据对话id寻找消息
     */
    public static IMMessageBean getIMMessageBeanById(String conversationID) {

        List<IMMessageBean> cacheBeans = DataSupport.where("conversationID = ?",
                conversationID).find(IMMessageBean.class);

        if (null != cacheBeans && cacheBeans.size() > 0) {
            return cacheBeans.get(0);
        }
        return new IMMessageBean(conversationID, AppConst.IM_CHAT_TYPE_SQUARE, System.currentTimeMillis(), "0", "", "", "", null);
    }

    /**
     * 消息列表：移除消息
     */
    public static void removeMessageInfo(String conversationID) {
        List<IMMessageBean> cacheBeans = DataSupport.where("conversationID = ?",
                conversationID).find(IMMessageBean.class);

        if (null != cacheBeans && cacheBeans.size() > 0) {
            cacheBeans.get(0).delete();
        }
    }

    /**
     * 消息列表：获取未读数
     */
    public static int getAllMessageUnreadCount() {
        return DataSupport.sum(IMMessageBean.class, "unReadCounts", int.class);
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
     * 获取所需要的字段(头像，昵称)
     */
    public static String getContactWantedInfo(String wanted, int friendId) {
        List<UserBean> userBeanList = DataSupport.where("friend_id = ? ", friendId + "").find(UserBean.class);
        if (null != userBeanList && userBeanList.size() > 0) {
            if (wanted.equals("avatar")) {
                return userBeanList.get(0).getAvatar();
            } else if (wanted.equals("nickname")) {
                return userBeanList.get(0).getNickname();
            }
        }
        return "";
    }

}
