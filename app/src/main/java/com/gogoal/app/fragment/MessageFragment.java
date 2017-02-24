package com.gogoal.app.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.gogoal.app.R;
import com.gogoal.app.adapter.recycleviewAdapterHelper.CommonAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.base.ViewHolder;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.BaseMessage;
import com.gogoal.app.bean.IMMessageBean;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.CalendarUtils;
import com.gogoal.app.common.SPTools;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
    private ListAdapter listAdapter;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_message);
    }

    @Override
    public void onResume() {
        super.onResume();

        JSONArray jsonArray = SPTools.getJsonArray("conversation_beans", null);
        IMMessageBeans.clear();

        initRecycleView(message_recycler, 0);

        if (null != jsonArray) {
            IMMessageBeans = JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class);
        }

        Log.e("+++IMMessageBeans", IMMessageBeans + "");

        listAdapter = new ListAdapter(getContext(), R.layout.item_fragment_message, IMMessageBeans);

        message_recycler.setAdapter(listAdapter);

        //长按删除
        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    class ListAdapter extends CommonAdapter<IMMessageBean> {

        public ListAdapter(Context context, int layoutId, List<IMMessageBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, IMMessageBean messageBean, int position) {
            String dateStr = "";
            String message = "";
            String speaker = "";
            List<String> members = new ArrayList<String>();
            if (null != messageBean.getLastTime()) {
                dateStr = CalendarUtils.parseStampToDate(messageBean.getLastTime());
            }

            /*if (jsonObject.get("lastMessage") instanceof AVIMTextMessage) {
                message = ((AVIMTextMessage) jsonObject.get("lastMessage")).getText();
            } else if (jsonObject.get("lastMessage") instanceof AVIMAudioMessage) {
                message = "语音信息";
            }else {
                message = "图片信息";
            }*/

            members.clear();
            members.addAll(messageBean.getSpeakerTo());
            Log.e("+++members", members + "");
            if (members.size() > 0) {
                if (members.size() == 2) {
                    //硬代码
                    if (members.contains(AppConst.LEAN_CLOUD_TOKEN)) {
                        members.remove(AppConst.LEAN_CLOUD_TOKEN);
                        speaker = members.get(0);
                        Log.e("+++members2", members + "");
                    }
                } else {
                    speaker = "群聊房间";
                }
            } else {
            }


            Log.e("+++message", messageBean.getLastMessage() + "");
            if (messageBean.getLastMessage() == null) {
                message = "";
            } else {
                String content = messageBean.getLastMessage().getContent();
                JSONObject contentObject = JSON.parseObject(content);
                message = contentObject.getString("_lctext");
            }

            holder.setText(R.id.whose_message, speaker);
            holder.setText(R.id.last_message, message);
            holder.setText(R.id.last_time, dateStr);
        }
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        Map<String, Object> map = baseMessage.getOthers();
        AVIMMessage message = (AVIMMessage) map.get("message");
        AVIMConversation conversation = (AVIMConversation) map.get("conversation");


        Log.e("+++thisMessage", message + "");

        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversation.getConversationId())) {
                IMMessageBeans.get(i).setLastTime(String.valueOf(conversation.getLastMessageAt().getTime()));
                IMMessageBeans.get(i).setLastMessage(message);
            }
        }
        listAdapter.notifyDataSetChanged();
        /*//判断房间一致然后做消息接收处理
        if (imConversation.getConversationId().equals(conversation.getConversationId())) {
            AVIMTextMessage msg = (AVIMTextMessage) message;
            message_show.setText(msg.getText());
        }*/
    }

}
