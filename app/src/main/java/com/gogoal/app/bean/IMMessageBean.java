package com.gogoal.app.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * Created by huangxx on 2017/2/23.
 */

public class IMMessageBean {
    private String conversationID;
    private String lastTime;
    private String unReadCounts;
    private List<String> speakerTo;
    //消息
    private AVIMMessage lastMessage;


    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public AVIMMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(AVIMMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getSpeakerTo() {
        return speakerTo;
    }

    public void setSpeakerTo(List<String> speakerTo) {
        this.speakerTo = speakerTo;
    }

    public String getUnReadCounts() {
        return unReadCounts;
    }

    public void setUnReadCounts(String unReadCounts) {
        this.unReadCounts = unReadCounts;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "IMMessageBean{" +
                "conversationID='" + conversationID + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", unReadCounts='" + unReadCounts + '\'' +
                ", speakerTo=" + speakerTo +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
