package cn.gogoal.im.bean;

import java.io.Serializable;

/**
 * author wangjd on 2017/5/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :分享数据
 */
public class ShareItemInfo<T> implements Serializable{
    private T avatar;//分享目标用户头像
    private String name;//分享目标用户名字
    private String conversationId;//与分享目标用户会话Id
    private GGShareEntity entity;//分享的数据,包含url,title,icon,desc

    public ShareItemInfo(T avatar, String name, GGShareEntity entity,String conversationId) {
        this.avatar = avatar;
        this.name = name;
        this.entity = entity;
        this.conversationId=conversationId;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
