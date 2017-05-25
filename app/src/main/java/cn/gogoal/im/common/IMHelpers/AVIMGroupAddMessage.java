package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/24.
 */
@AVIMMessageType(
        type = 5
)
public class AVIMGroupAddMessage extends AVIMSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<AVIMGroupAddMessage> CREATOR = new AVIMMessageCreator(AVIMGroupAddMessage.class);

    public AVIMGroupAddMessage() {
        super();
    }

}
