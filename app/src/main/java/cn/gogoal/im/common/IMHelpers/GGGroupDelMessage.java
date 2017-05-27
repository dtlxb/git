package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by huangxx on 2017/5/24.
 */
@AVIMMessageType(
        type = 6
)
public class GGGroupDelMessage extends GGSystemMessage {

    @SuppressWarnings("unchecked")
    public static final Creator<GGGroupDelMessage> CREATOR = new AVIMMessageCreator(GGGroupDelMessage.class);

    public GGGroupDelMessage() {
        super();
    }
}
