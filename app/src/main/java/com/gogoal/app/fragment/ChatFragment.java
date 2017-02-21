package com.gogoal.app.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.IMHelpers.AVImClientManager;
import com.gogoal.app.common.UIHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by huangxx on 2017/2/21.
 */

public class ChatFragment extends BaseFragment {

    @BindView(R.id.message_show)
    TextView message_show;

    @BindView(R.id.message_input)
    EditText message_input;

    @BindView(R.id.message_send)
    Button message_send;

    private AVIMConversation imConversation;

    @Override
    public int bindLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    public void doBusiness(Context mContext) {
        //接收消息
        loadConvertManager();
        //发送消息(之后会改成向公司服务器发送消息，然后后台再处理给LeanCloud发送消息)
        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> attrsMap = new HashMap<String, Object>();
                attrsMap.put("username", "Bjergsen");
                AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText(message_input.getText().toString());
                //msg.setAttrs(attrsMap);

                imConversation.sendMessage(msg, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        message_input.setText("");
                        UIHelper.showSnack(getActivity(), "发送成功");
                    }
                });
            }
        });
    }

    public void setConversation(AVIMConversation conversation) {
        if (null != conversation) {
            imConversation = conversation;
        }
    }

    private void loadConvertManager() {
        AVImClientManager.getInstance().setChatInfoManager(new AVImClientManager.ChatInfoManager() {
            @Override
            public void chatRoomMessages(List<AVIMMessage> list) {

            }

            @Override
            public void chatRoomReceiveMessage(AVIMConversation conversation, AVIMMessage message) {
                //判断房间是否一样
                //if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                    AVIMTextMessage textMessage = (AVIMTextMessage) message;
                    message_show.setText(textMessage.getText());
                    Log.e("LEAN_CLOUD", "get this message");
                //}
            }
        });
    }
}
