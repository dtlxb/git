package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = 14
)
public class AVIMUnReadMessage extends AVIMSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<AVIMUnReadMessage> CREATOR = new AVIMMessageCreator(AVIMUnReadMessage.class);

    public AVIMUnReadMessage() {
        super();
    }

}
