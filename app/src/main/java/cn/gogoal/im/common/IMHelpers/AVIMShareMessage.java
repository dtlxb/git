package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.util.Map;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = 13
)
public class AVIMShareMessage extends AVIMTypedMessage {
    @AVIMMessageField(
            name = "_lctext"
    )
    private String text;

    @AVIMMessageField(
            name = "_lcattrs"
    )
    private Map<String, Object> attrs;

    @SuppressWarnings("unchecked")
    public static final Creator<AVIMShareMessage> CREATOR = new AVIMMessageCreator(AVIMShareMessage.class);

    public AVIMShareMessage() {
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
