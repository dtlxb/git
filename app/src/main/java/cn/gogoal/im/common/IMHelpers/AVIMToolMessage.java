package cn.gogoal.im.common.IMHelpers;


import java.util.Map;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by huangxx on 2017/5/23.
 */

@AVIMMessageType(
        type = 13
)
public class AVIMToolMessage extends AVIMTypedMessage {

    @AVIMMessageField(
            name = "_lctext"
    )
    String text;
    @AVIMMessageField(
            name = "_lcattrs"
    )
    Map<String, Object> attrs;

    @SuppressWarnings("unchecked")
    public static final Creator<AVIMToolMessage> CREATOR = new AVIMMessageCreator(AVIMToolMessage.class);

    public AVIMToolMessage() {
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
}
