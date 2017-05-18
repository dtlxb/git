package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/5/18.
 */

public class SearchBean<T> {
    private T avatar;//头像URL
    private String nickname;//昵称
    private String intro;//简介

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

    public SearchBean(T avatar, String nickname, String intro) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "avatar=" + avatar +
                ", nickname='" + nickname + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
