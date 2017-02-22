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
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.BaseMessage;
import com.gogoal.app.common.UIHelper;

import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //发送消息(之后会改成向公司服务器发送消息，然后后台再处理给LeanCloud发送消息)
        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> attrsMap = new HashMap<String, Object>();
                attrsMap.put("username", "Bjergsen");
                AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText(message_input.getText().toString());
                msg.setAttrs(attrsMap);

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

    private void getHistoryMessage() {
        if (null != imConversation) {
            imConversation.queryMessages(20, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (null != e) {

                    }
                }
            });
        }
    }

    public void setConversation(AVIMConversation conversation) {
        if (null != conversation) {
            imConversation = conversation;
            //拉取历史记录(直接从LeanCloud拉取)
            getHistoryMessage();
        }
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");
            Log.e("+++leancloud", imConversation.getConversationId() + "");
            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                AVIMTextMessage msg = (AVIMTextMessage) message;
                message_show.setText(msg.getText());
            }
        }
    }
}
