package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/24.
 */
@AVIMMessageType(
        type = 6
)
public class AVIMGroupDelMessage extends AVIMSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<AVIMGroupDelMessage> CREATOR = new AVIMMessageCreator(AVIMGroupDelMessage.class);

    public AVIMGroupDelMessage() {
        super();
    }
}
