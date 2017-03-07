package com.gogoal.app.servise;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.socks.library.KLog;

/**
 * Created by dave.
 * Date: 2017/3/7.
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class IntentService extends GTIntentService {

    public IntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        KLog.json("onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        KLog.json("onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        KLog.json("onReceiveCommandResult -> " + new String(msg.getPayload()));
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        KLog.json("onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }
}
