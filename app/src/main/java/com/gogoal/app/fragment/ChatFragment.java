package com.gogoal.app.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gogoal.app.R;
import com.gogoal.app.adapter.IMChatAdapter;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.BaseMessage;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.IMHelpers.MessageUtils;
import com.gogoal.app.common.SPTools;
import com.gogoal.app.common.UIHelper;
import com.gogoal.app.ui.view.MessageSendView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by huangxx on 2017/2/21.
 */

public class ChatFragment extends BaseFragment {

    @BindView(R.id.message_list)
    RecyclerView message_recycler;

    @BindView(R.id.message_send_view)
    MessageSendView message_send_view;

    private AVIMConversation imConversation;

    private List<AVIMMessage> messageList = new ArrayList<>();

    private IMChatAdapter imChatAdapter;

    private JSONArray jsonArray;

    @Override
    public int bindLayout() {
        return R.layout.fragment_imchat;
    }

    @Override
    public void doBusiness(Context mContext) {

        initRecycleView(message_recycler, null);

        imChatAdapter = new IMChatAdapter(getContext(), messageList);

        message_recycler.setAdapter(imChatAdapter);

        //发送消息(之后会改成向公司服务器发送消息，然后后台再处理给LeanCloud发送消息)
        message_send_view.setDelegate(new MessageSendView.LiveMessageDelegate() {
            @Override
            public void sendMessage(String text) {
                HashMap<String, Object> attrsMap = new HashMap<String, Object>();
                attrsMap.put("username", AppConst.LEAN_CLOUD_TOKEN);
                final AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText(text);
                msg.setAttrs(attrsMap);

                imChatAdapter.addItem(msg);
                message_recycler.smoothScrollToPosition(messageList.size());

                imConversation.sendMessage(msg, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        UIHelper.showSnack(getActivity(), "发送成功");
                        MessageUtils.saveMessageInfo(jsonArray, imConversation);
                    }
                });
            }

            @Override
            public void onClick(int type) {

            }
        });

    }

    private void getHistoryMessage() {
        if (null != imConversation) {
            imConversation.queryMessages(20, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (null == e) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.e("+++AVIMMessage", list.get(i).getFrom() + "");
                        }
                        messageList.addAll(list);
                        imChatAdapter.notifyDataSetChanged();
                        message_recycler.smoothScrollToPosition(messageList.size());
                    }
                }
            });
        }
    }

    public void setConversation(AVIMConversation conversation) {
        Log.e("+++conversation", conversation.getConversationId() + "");
        if (null != conversation) {
            imConversation = conversation;
            //拉取历史记录(直接从LeanCloud拉取)
            getHistoryMessage();

            jsonArray = SPTools.getJsonArray("conversation_beans", new JSONArray());

            MessageUtils.saveMessageInfo(jsonArray, conversation);
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

            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                //AVIMTextMessage msg = (AVIMTextMessage) message;
                imChatAdapter.addItem(message);
                message_recycler.smoothScrollToPosition(messageList.size());
                MessageUtils.saveMessageInfo(jsonArray, conversation);
            }
        }
    }

}
