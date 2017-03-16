package cn.gogoal.im.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by huangxx on 2017/3/15.
 */

public class IMNewFriendBean {
    AVIMMessage message;
    Boolean isYourFriend;

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }

    public Boolean getIsYourFriend() {
        return isYourFriend;
    }

    public void setIsYourFriend(Boolean yourFriend) {
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
