package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/24.
 */
@AVIMMessageType(
        type = 5
)
public class GGGroupAddMessage extends GGSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<GGGroupAddMessage> CREATOR = new AVIMMessageCreator(GGGroupAddMessage.class);

    public GGGroupAddMessage() {
        super();
    }

}
