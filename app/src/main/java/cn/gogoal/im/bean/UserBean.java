package cn.gogoal.im.bean;


import cn.gogoal.im.common.database.crud.DataSupport;

/**
 * Created by huangxx on 2017/6/2.
 */

public class UserBean<T> extends DataSupport {

    private int friend_id;//账号

    private String nickname;//昵称

    private T avatar;//头像URL

    private String duty;//职务

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public T getAvatar() {
        return avatar;
    }

    public void setAvatar(T avatar) {
        this.avatar = avatar;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "friend_id=" + friend_id +
                ", nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                ", duty='" + duty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        try {
            ContactBean other = (ContactBean) o;
            return this.getFriend_id() == other.getFriend_id();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

}
