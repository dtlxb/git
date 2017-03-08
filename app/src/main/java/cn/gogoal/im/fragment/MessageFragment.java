package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.SPTools;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
    private ListAdapter listAdapter;
    private JSONArray jsonArray;

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

        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
        IMMessageBeans.clear();

        initRecycleView(message_recycler, 0);

        if (null != jsonArray) {
            IMMessageBeans = JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class);
        }
        Log.e("+++IMMessageBeans", IMMessageBeans + "");

        if (null != IMMessageBeans && IMMessageBeans.size() > 0) {
            //按照时间排序
            Collections.sort(IMMessageBeans, new Comparator<IMMessageBean>() {
                @Override
                public int compare(IMMessageBean object1, IMMessageBean object2) {
                    return Long.compare(Long.parseLong(object2.getLastTime()), Long.parseLong(object1.getLastTime()));
                }
            });
        }


        listAdapter = new ListAdapter(getContext(), R.layout.item_fragment_message, IMMessageBeans);

        message_recycler.setAdapter(listAdapter);

        //长按删除
        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String conversation_id = IMMessageBeans.get(position).getConversationID();
                String member_id = "";
                Intent intent;

                //群聊处理
                intent = new Intent(getContext(), SquareChatRoomActivity.class);
                intent.putExtra("conversation_id", conversation_id);
                startActivity(intent);

                //单聊处理
                /*List<String> members = new ArrayList<>();
                intent = new Intent(getContext(), SingleChatRoomActivity.class);
                members.clear();
                members.addAll(IMMessageBeans.get(position).getSpeakerTo());
                if (members.size() == 2) {
                    if (members.contains(AppConst.LEAN_CLOUD_TOKEN)) {
                        members.remove(AppConst.LEAN_CLOUD_TOKEN);
                        member_id = members.get(0);
                    }
                }
                intent.putExtra("member_id", member_id);
                startActivity(intent);*/
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
        boolean isTheSame = false;

        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversation.getConversationId())) {
                IMMessageBeans.get(i).setLastTime(String.valueOf(conversation.getLastMessageAt().getTime()));
                IMMessageBeans.get(i).setLastMessage(message);
                MessageUtils.saveMessageInfo(jsonArray, conversation);
                isTheSame = true;
            } else {
            }
        }

        if (!isTheSame) {
            IMMessageBean imMessageBean = new IMMessageBean();
            imMessageBean.setConversationID(conversation.getConversationId());
            imMessageBean.setLastMessage(message);
            imMessageBean.setLastTime(String.valueOf(conversation.getLastMessageAt().getTime()));
            imMessageBean.setSpeakerTo(conversation.getMembers());
            imMessageBean.setUnReadCounts("99");

            IMMessageBeans.add(imMessageBean);
            MessageUtils.saveMessageInfo(jsonArray, conversation);
        }

        if (null != IMMessageBeans && IMMessageBeans.size() > 0) {
            Collections.sort(IMMessageBeans, new Comparator<IMMessageBean>() {
                @Override
                public int compare(IMMessageBean object1, IMMessageBean object2) {
                    return Long.compare(Long.parseLong(object2.getLastTime()), Long.parseLong(object1.getLastTime()));
                }
            });
        }

        listAdapter.notifyDataSetChanged();

    }

}
