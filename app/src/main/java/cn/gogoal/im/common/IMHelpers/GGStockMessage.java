package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.util.Map;

import cn.gogoal.im.common.AppConst;

/**
 * Created by huangxx on 2017/5/24.
 */
@AVIMMessageType(
        type = 11
)
public class GGStockMessage extends AVIMTypedMessage {
    @AVIMMessageField(
            name = "_lctext"
    )
    private String text;

    @AVIMMessageField(
            name = "_lcattrs"
    )
    private Map<String, Object> attrs;

    private int messageSendStatus = AppConst.MESSAGE_SEND_STATUS_SUCCESS;

    @SuppressWarnings("unchecked")
    public static final Creator<GGStockMessage> CREATOR = new AVIMMessageCreator(GGStockMessage.class);

    public GGStockMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public int getMessageSendStatus() {
        return messageSendStatus;
    }

    public void setMessageSendStatus(int messageSendStatus) {
        this.messageSendStatus = messageSendStatus;
    }
}
