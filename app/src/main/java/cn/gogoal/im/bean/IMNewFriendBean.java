package cn.gogoal.im.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by huangxx on 2017/3/15.
 */

public class IMNewFriendBean {
    AVIMMessage message;
    Boolean isYourFriend;

    public IMNewFriendBean(AVIMMessage message, Boolean isYourFriend) {
        this.message = message;
        this.isYourFriend = isYourFriend;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }

    public Boolean getYourFriend() {
        return isYourFriend;
    }

    public void setYourFriend(Boolean yourFriend) {
        isYourFriend = yourFriend;
    }

    @Override
    public String toString() {
        return "IMNewFriendBean{" +
                "message=" + message +
                ", isYourFriend=" + isYourFriend +
                '}';
    }
}
