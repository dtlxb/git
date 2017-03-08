package cn.gogoal.im.common.IMHelpers;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by huangxx on 17/02/20.
 */
public class AVImClientManager {

    private static AVImClientManager imClientManager;

    public AVIMClient avimClient;               //< 连接server
    private String clientId;                    //< 连接serverID

    private AVImClientManager() {
    }

    public synchronized static AVImClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManager();
        }
        return imClientManager;
    }

    /**
    * 打开连接，返回Client对象
    * */
    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }
}
