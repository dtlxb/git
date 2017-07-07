package cn.gogoal.im.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by huangxx on 2017/6/6.
 */

public class SquareUserBean extends DataSupport implements Serializable {
    private String conversationId;
    private String friend_id;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public SquareUserBean() {
    }

    public SquareUserBean(String conversationId, String friend_id) {
        this.conversationId = conversationId;
        this.friend_id = friend_id;
    }

    @Override
    public String toString() {
        return "SquareUserBean{" +
                "conversationId='" + conversationId + '\'' +
                ", friend_id=" + friend_id +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        try {
            SquareUserBean other = (SquareUserBean) obj;
            return this.getFriend_id().equals(other.getFriend_id()) &&
                    this.conversationId.equals(other.getConversationId());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(obj);
    }
}
