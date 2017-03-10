package cn.gogoal.im.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * Created by huangxx on 2017/2/23.
 */

public class IMMessageBean {
    private String conversationID;
    private String lastTime;
    private String unReadCounts;
    private String nickname;
    private String friend_id;
    private String avatar;
    //消息
    private AVIMMessage lastMessage;

    public IMMessageBean() {
    }

    public IMMessageBean(String conversationID, String lastTime, String unReadCounts, String nickname, String friend_id, String avatar, AVIMMessage lastMessage) {
        this.conversationID = conversationID;
        this.lastTime = lastTime;
        this.unReadCounts = unReadCounts;
        this.nickname = nickname;
        this.friend_id = friend_id;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public AVIMMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(AVIMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getUnReadCounts() {
        return unReadCounts;
    }

    public void setUnReadCounts(String unReadCounts) {
        this.unReadCounts = unReadCounts;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime == null ? "0" : lastTime;
    }

    @Override
    public String toString() {
        return "IMMessageBean{" +
                "conversationID='" + conversationID + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", unReadCounts='" + unReadCounts + '\'' +
                ", nickname='" + nickname + '\'' +
                ", friend_id='" + friend_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
