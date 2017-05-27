package cn.gogoal.im.common.IMHelpers;

import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;

import java.io.File;
import java.io.IOException;

import cn.gogoal.im.common.AppConst;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = -3
)
public class GGAudioMessage extends AVIMAudioMessage {
    private RecyclerView.ViewHolder listItem;

    private Integer audioStatus = 0;        // 是否播放动画 0 动画停止, 1 动画开始
    private Boolean isPlaying = false;      // 是否正在播放

    private int messageSendStatus = AppConst.MESSAGE_SEND_STATUS_SUCCESS;

    public int getMessageSendStatus() {
        return messageSendStatus;
    }

    public void setMessageSendStatus(int messageSendStatus) {
        this.messageSendStatus = messageSendStatus;
    }

    public RecyclerView.ViewHolder getListItem() {
        return listItem;
    }

    public void setListItem(RecyclerView.ViewHolder listItem) {
        this.listItem = listItem;
    }

    public void refreshItem() {

        this.listItem = null;
    }

    public GGAudioMessage() {
    }

    public GGAudioMessage(String localPath) throws IOException {
        super(localPath);
    }

    public GGAudioMessage(File localFile) throws IOException {
        super(localFile);
    }

    public GGAudioMessage(AVFile file) {
        super(file);
    }

    public Integer getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(Integer audioStatus) {
        this.audioStatus = audioStatus;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
