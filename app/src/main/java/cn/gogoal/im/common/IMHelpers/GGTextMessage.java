package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import cn.gogoal.im.common.AppConst;

/**
 * Created by huangxx on 2017/5/26.
 */

@AVIMMessageType(
        type = -1
)
public class GGTextMessage extends AVIMTextMessage {
    private int messageSendStatus = AppConst.MESSAGE_SEND_STATUS_SUCCESS;

    public int getMessageSendStatus() {
        return messageSendStatus;
    }

    public void setMessageSendStatus(int messageSendStatus) {
        this.messageSendStatus = messageSendStatus;
    }

    public GGTextMessage() {
        super();
    }

}
