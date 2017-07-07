package cn.gogoal.im.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by huangxx on 2017/6/2.
 */

public class UserBean extends DataSupport implements Serializable {

    @Column(unique = true)
    private String friend_id;//账号

    private String nickname;//昵称

    private String avatar;//头像URL

    private String duty;//职务

    private String conv_id;//对话ID

    private boolean inYourContact;

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getConv_id() {
        return conv_id;
    }

    public void setConv_id(String conv_id) {
        this.conv_id = conv_id;
    }

    public boolean isInYourContact() {
        return inYourContact;
    }

    public void setInYourContact(boolean inYourContact) {
        this.inYourContact = inYourContact;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "friend_id=" + friend_id +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", duty='" + duty + '\'' +
                ", conv_id='" + conv_id + '\'' +
                ", inYourContact=" + inYourContact +
                '}';
    }
}
