package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * Created by huangxx on 2017/2/23.
 */

public class IMMessageBean implements Parcelable{
    private String conversationID;
    private int chatType;
    private Long lastTime;
    private String unReadCounts;
    private String nickname;
    private String friend_id;
    private String avatar;
    //消息
    private AVIMMessage lastMessage;

    public IMMessageBean() {
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

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime == null ? 0L : lastTime;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }


    @Override
    public String toString() {
        return "IMMessageBean{" +
                "conversationID='" + conversationID + '\'' +
                ", chatType=" + chatType +
                ", lastTime=" + lastTime +
                ", unReadCounts='" + unReadCounts + '\'' +
                ", nickname='" + nickname + '\'' +
                ", friend_id='" + friend_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", lastMessage=" + lastMessage +
                '}';
    }

    public IMMessageBean(String conversationID, int chatType, Long lastTime, String unReadCounts, String nickname, String friend_id, String avatar, AVIMMessage lastMessage) {
        this.conversationID = conversationID;
        this.chatType = chatType;
        this.lastTime = lastTime;
        this.unReadCounts = unReadCounts;
        this.nickname = nickname;
        this.friend_id = friend_id;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationID);
        dest.writeInt(this.chatType);
        dest.writeValue(this.lastTime);
        dest.writeString(this.unReadCounts);
        dest.writeString(this.nickname);
        dest.writeString(this.friend_id);
        dest.writeString(this.avatar);
        dest.writeParcelable(this.lastMessage, flags);
    }

    protected IMMessageBean(Parcel in) {
        this.conversationID = in.readString();
        this.chatType = in.readInt();
        this.lastTime = (Long) in.readValue(Long.class.getClassLoader());
        this.unReadCounts = in.readString();
        this.nickname = in.readString();
        this.friend_id = in.readString();
        this.avatar = in.readString();
        this.lastMessage = in.readParcelable(AVIMMessage.class.getClassLoader());
    }

    public static final Creator<IMMessageBean> CREATOR = new Creator<IMMessageBean>() {
        @Override
        public IMMessageBean createFromParcel(Parcel source) {
            return new IMMessageBean(source);
        }

        @Override
        public IMMessageBean[] newArray(int size) {
            return new IMMessageBean[size];
        }
    };
}
