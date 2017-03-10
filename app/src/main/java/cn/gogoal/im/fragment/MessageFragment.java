package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.MultiItemTypeAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;

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
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                final String conversation_id = IMMessageBeans.get(position).getConversationID();

                KLog.e(conversation_id);

                AVImClientManager.getInstance().findConversationById(conversation_id, new AVImClientManager.ChatJoinManager() {
                    @Override
                    public void joinSuccess(AVIMConversation conversation) {
                        String chat_type = conversation.getAttribute("chat_type") == null ? "1001" : conversation.getAttribute("chat_type").toString();
                        Intent intent;
                        switch (chat_type) {
                            case "1001":
                                //单聊处理
                                intent = new Intent(getContext(), SingleChatRoomActivity.class);
                                intent.putExtra("nickname", IMMessageBeans.get(position).getNickname());
                                intent.putExtra("conversation_id", conversation_id);
                                startActivity(intent);
                                break;
                            case "1002":
                                //群聊处理
                                intent = new Intent(getContext(), SquareChatRoomActivity.class);
                                intent.putExtra("conversation_id", conversation_id);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void joinFail(String error) {
                        UIHelper.toast(getActivity(), "获取聊天房间失败");
                    }
                });

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
            String unRead = "";
            View unReadTag = holder.getView(R.id.unread_tag);
            ImageView avatarIv = holder.getView(R.id.head_image);

            //未读数
            if (messageBean.getUnReadCounts().equals("0")) {
                unRead = "";
                unReadTag.setVisibility(View.GONE);
            } else {
                unRead = "[" + messageBean.getUnReadCounts() + "条] ";
                unReadTag.setVisibility(View.VISIBLE);
            }

            if (null != messageBean.getLastTime()) {
                dateStr = CalendarUtils.parseStampToDate(messageBean.getLastTime());
            }

            //消息类型
            if (messageBean.getLastMessage() != null) {
                String content = messageBean.getLastMessage().getContent();
                JSONObject contentObject = JSON.parseObject(content);
                String _lctype = contentObject.getString("_lctype");
                switch (_lctype) {
                    case "-1":
                        message = contentObject.getString("_lctext");
                        break;
                    case "-2":
                        message = "[图片]";
                        break;
                    case "-3":
                        message = "[语音]";
                        break;
                    default:
                        break;
                }
            } else {
                message = "";
            }

            ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);
            holder.setText(R.id.whose_message, messageBean.getNickname());
            holder.setText(R.id.last_message, unRead + message);
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
        String rightNow = String.valueOf(CalendarUtils.getCurrentTime());

        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversation.getConversationId())) {
                IMMessageBeans.get(i).setLastTime(rightNow);
                IMMessageBeans.get(i).setLastMessage(message);
                int unreadmessage = Integer.parseInt(IMMessageBeans.get(i).getUnReadCounts()) + 1;
                KLog.e(unreadmessage);
                IMMessageBeans.get(i).setUnReadCounts(unreadmessage + "");

                //头像暂时未保存
                IMMessageBean imMessageBean = new IMMessageBean(conversation.getConversationId(), String.valueOf(CalendarUtils.getCurrentTime()),
                        "0", message.getFrom(), AppConst.LEANCLOUD_APP_ID, "", message);

                isTheSame = true;
                MessageUtils.saveMessageInfo(jsonArray, imMessageBean);
            } else {
            }
        }

        if (!isTheSame) {
            IMMessageBean imMessageBean = new IMMessageBean();
            imMessageBean.setConversationID(conversation.getConversationId());
            imMessageBean.setLastMessage(message);
            imMessageBean.setLastTime(rightNow);
            imMessageBean.setNickname(message.getFrom());
            int unRead = Integer.parseInt(imMessageBean.getUnReadCounts() == null ? "0" : imMessageBean.getUnReadCounts()) + 1;
            imMessageBean.setUnReadCounts(unRead + "");

            //头像暂时未保存
            IMMessageBean unKonwimMessageBean = new IMMessageBean(conversation.getConversationId(), String.valueOf(CalendarUtils.getCurrentTime()),
                    "0", message.getFrom(), AppConst.LEANCLOUD_APP_ID, "", message);
            IMMessageBeans.add(imMessageBean);
            MessageUtils.saveMessageInfo(jsonArray, unKonwimMessageBean);
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
