package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;

import java.io.File;
import java.io.IOException;

import cn.gogoal.im.common.AppConst;

/**
 * Created by huangxx on 2017/5/26.
 */

@AVIMMessageType(
        type = -2
)
public class GGImageMessage extends AVIMImageMessage {

    private int messageSendStatus = AppConst.MESSAGE_SEND_STATUS_SUCCESS;

    public int getMessageSendStatus() {
        return messageSendStatus;
    }

    public void setMessageSendStatus(int messageSendStatus) {
        this.messageSendStatus = messageSendStatus;
    }

    public GGImageMessage() {
        super();
    }

    public GGImageMessage(String localPath) throws IOException {
        super(localPath);
    }

    public GGImageMessage(File localFile) throws IOException {
        super(localFile);
    }

    public GGImageMessage(AVFile file) {
        super(file);
    }
}
