package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/5/18.
 */

public class SearchBean<T> {
    private T avatar;//头像URL
    private String nickname;//昵称
    private String intro;//简介
    private String conversationId;
    private int chatType;


    public SearchBean(T avatar, String nickname, String intro, String conversationId, int chatType) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.intro = intro;
        this.conversationId = conversationId;
        this.chatType = chatType;
    }

    public T getAvatar() {
        return avatar;
    }

    public void setAvatar(T avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "avatar=" + avatar +
                ", nickname='" + nickname + '\'' +
                ", intro='" + intro + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", chatType=" + chatType +
                '}';
    }
}
