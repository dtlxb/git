package cn.gogoal.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by huangxx on 2017/2/23.
 */

public class IMMessageBean extends DataSupport implements Parcelable {
    private String conversationID;
    private int chatType;
    private Long lastTime;
    private String unReadCounts;
    private String nickname;
    private String friend_id;
    private String avatar;
    //消息
    private String lastMessage;
    //免打扰(静音)
    private boolean mute;

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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
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

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
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
                ", lastMessage='" + lastMessage + '\'' +
                ", mute=" + mute +
                '}';
    }

    public IMMessageBean(String conversationID, int chatType, Long lastTime, String unReadCounts,
                         String nickname, String friend_id, String avatar, String lastMessage, boolean mute) {
        this.conversationID = conversationID;
        this.chatType = chatType;
        this.lastTime = lastTime;
        this.unReadCounts = unReadCounts;
        this.nickname = nickname;
        this.friend_id = friend_id;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.mute = mute;
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
        dest.writeString(this.lastMessage);
        dest.writeByte((byte) (this.mute ? 1 : 0));
    }

    protected IMMessageBean(Parcel in) {
        this.conversationID = in.readString();
        this.chatType = in.readInt();
        this.lastTime = (Long) in.readValue(Long.class.getClassLoader());
        this.unReadCounts = in.readString();
        this.nickname = in.readString();
        this.friend_id = in.readString();
        this.avatar = in.readString();
        this.lastMessage = in.readString();
        this.mute = in.readByte() != 0;
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
