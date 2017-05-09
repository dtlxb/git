package cn.gogoal.im.bean;

import java.io.Serializable;

/**
 * author wangjd on 2017/5/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ShareItemInfo<T> implements Serializable{
    private T avatar;
    private String name;
    private GGShareEntity entity;

    public ShareItemInfo(T avatar, String name, GGShareEntity entity) {
        this.avatar = avatar;
        this.name = name;
        this.entity = entity;
    }

    public T getAvatar() {
        return avatar;
    }

    public void setAvatar(T avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GGShareEntity getEntity() {
        return entity;
    }

    public void setEntity(GGShareEntity entity) {
        this.entity = entity;
    }
}
