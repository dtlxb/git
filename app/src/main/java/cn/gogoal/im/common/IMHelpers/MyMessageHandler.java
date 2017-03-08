package cn.gogoal.im.common.IMHelpers;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;

import java.util.HashMap;

/**
 * Created by huangxx on 2017/2/20.
 */

public class MyMessageHandler extends AVIMMessageHandler {
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        String clientID = "";
        try {
            clientID = AVImClientManager.getInstance().getClientId();

            if (clientID.equals(client.getClientId())) {
                if (!message.getFrom().equals(clientID)) {
                    //接收到消息，发送出去
                    sendIMMessage(message, conversation);
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }

    }


    private void sendIMMessage(AVIMMessage message, AVIMConversation conversation) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("message",message);
        map.put("conversation",conversation);

        BaseMessage baseMessage=new BaseMessage("IM_Info",map);
        AppManager.getInstance().sendMessage("IM_Message",baseMessage);
    }
}
