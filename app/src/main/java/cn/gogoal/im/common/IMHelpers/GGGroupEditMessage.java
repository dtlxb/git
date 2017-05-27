package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = 8
)
public class GGGroupEditMessage extends GGSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<GGGroupEditMessage> CREATOR = new AVIMMessageCreator(GGGroupEditMessage.class);

    public GGGroupEditMessage() {
        super();
    }
}
