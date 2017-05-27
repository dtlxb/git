package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = 14
)
public class GGUnReadMessage extends GGSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<GGUnReadMessage> CREATOR = new AVIMMessageCreator(GGUnReadMessage.class);

    public GGUnReadMessage() {
        super();
    }

}
