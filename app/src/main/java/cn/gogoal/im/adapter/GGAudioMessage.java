package cn.gogoal.im.adapter;

import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;

import java.io.File;
import java.io.IOException;

/**
 * Created by huangxx on 2017/5/25.
 */

@AVIMMessageType(
        type = -3
)

public class GGAudioMessage extends AVIMAudioMessage {
    private RecyclerView.ViewHolder listItem;

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
}
