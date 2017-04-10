package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;

import cn.gogoal.im.base.AppManager;

/**
 * Created by huangxx on 2017/4/10.
 */

public class MyClientEventHandler extends AVIMClientEventHandler {

    @Override
    public void onConnectionPaused(AVIMClient avimClient) {

    }

    @Override
    public void onConnectionResume(AVIMClient avimClient) {

    }

    @Override
    public void onClientOffline(AVIMClient avimClient, int i) {
        AppManager.getInstance().sendMessage("show_client_status");
    }
}
