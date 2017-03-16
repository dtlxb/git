package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.IMAddFriendActivity;
import cn.gogoal.im.activity.IMNewFrienActivity;
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
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XTitle;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;
    private List<IMMessageBean> IMMessageBeans = new ArrayList<>();
    private ListAdapter listAdapter;
    private JSONArray jsonArray;
    private XTitle xTitle;


    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        xTitle = setFragmentTitle(R.string.title_message);
        initTitle();
    }

    @Override
    public void onResume() {
        super.onResume();

        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
        IMMessageBeans.clear();

        initRecycleView(message_recycler, R.drawable.shape_divider_recyclerview_1px);

        if (null != jsonArray) {
            IMMessageBeans = JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class);
        }

        if (null != IMMessageBeans && IMMessageBeans.size() > 0) {
            //按照时间排序
            Collections.sort(IMMessageBeans, new Comparator<IMMessageBean>() {
                @Override
                public int compare(IMMessageBean object1, IMMessageBean object2) {
                    return Long.compare(object2.getLastTime(), object1.getLastTime());
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
                            case "1003":
                                //直播处理
                                intent = new Intent(getContext(), SquareChatRoomActivity.class);
                                intent.putExtra("conversation_id", conversation_id);
                                startActivity(intent);
                                break;
                            case "1004":
                                //系统处理
                                intent = new Intent(getContext(), IMNewFrienActivity.class);
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
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {
                DialogHelp.getSelectDialog(getActivity(), "", new String[]{"标为未读", "置顶聊天", "删除聊天"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            IMMessageBeans.remove(position);
                            MessageUtils.removeMessageInfo(position);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }, false).show();
                return false;
            }
        });

    }

    private void initTitle() {
        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(getContext(), R.mipmap.contact_person)) {
            @Override
            public void actionClick(View view) {

            }
        };
        XTitle.ImageAction addAction = new XTitle.ImageAction(ContextCompat.getDrawable(getContext(), R.mipmap.contact_add_message)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(), IMAddFriendActivity.class));
            }
        };
        xTitle.addAction(personAction, 0);
        xTitle.addAction(addAction, 1);
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
            String nickName = "";
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

            //时间
            if (null != messageBean.getLastTime()) {
                dateStr = CalendarUtils.parseDateIMMessageFormat(messageBean.getLastTime());
            }

            //SDK定义的消息类型
            if (messageBean.getLastMessage() != null) {
                String content = messageBean.getLastMessage().getContent();
                KLog.e(content);
                JSONObject contentObject = JSON.parseObject(content);
                String _lctype = contentObject.getString("_lctype");
                switch (_lctype) {
                    case "-1":
                        //文字
                        message = contentObject.getString("_lctext");
                        nickName = messageBean.getNickname();
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);

                        break;
                    case "-2":
                        //图片
                        message = "[图片]";
                        nickName = messageBean.getNickname();
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);

                        break;
                    case "-3":
                        //语音
                        message = "[语音]";
                        nickName = messageBean.getNickname();
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);

                        break;
                    case "1":

                        break;

                    case "2":
                        //加好友
                        nickName = "新的好友";
                        message = messageBean.getNickname() + "请求添加你为好友";
                        ImageDisplay.loadResImage(getActivity(), R.mipmap.chat_new_friend, avatarIv);

                        break;
                    default:
                        break;
                }
            }

            holder.setText(R.id.last_message, unRead + message);
            holder.setText(R.id.whose_message, nickName);
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

        Long rightNow = CalendarUtils.getCurrentTime();
        int chatType = (int) conversation.getAttribute("chat_type");
        String nickName = "";

        JSONObject contentObject = JSON.parseObject(message.getContent());
        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
        String _lctype = contentObject.getString("_lctype");
        String avatar = lcattrsObject.getString("avatar");
        switch (_lctype) {
            case "-1":
                //文字
            case "-2":
                //图片
            case "-3":
                //语音
                nickName = lcattrsObject.getString("username");
                break;
            case "1":

                break;

            case "2":
                //加好友
                nickName = lcattrsObject.getString("nickname");
                break;
            default:
                break;
        }

        KLog.e(message.getContent());

        //已有的会话
        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(conversation.getConversationId())) {
                IMMessageBeans.get(i).setLastTime(rightNow);
                IMMessageBeans.get(i).setLastMessage(message);
                int unreadmessage = Integer.parseInt(IMMessageBeans.get(i).getUnReadCounts()) + 1;
                IMMessageBeans.get(i).setUnReadCounts(unreadmessage + "");
                isTheSame = true;
            } else {
            }
        }

        //新添的会话
        if (!isTheSame) {
            IMMessageBean imMessageBean = new IMMessageBean();
            imMessageBean.setConversationID(conversation.getConversationId());
            imMessageBean.setLastMessage(message);
            imMessageBean.setLastTime(rightNow);
            imMessageBean.setNickname(nickName);
            imMessageBean.setAvatar(avatar);
            int unRead = Integer.parseInt(imMessageBean.getUnReadCounts() == null ? "0" : imMessageBean.getUnReadCounts()) + 1;
            imMessageBean.setUnReadCounts(unRead + "");
            IMMessageBeans.add(imMessageBean);
        }
        switch (chatType) {
            //单聊,群聊,加好友请求
            case 1001:
            case 1002:
                break;
            case 1004:
                break;
            //直播
            case 1003:
                break;
            //操纵通讯录
            case 1005:
                break;
            //公众号模块
            case 1006:
                break;
            default:
                break;
        }

        //保存
        IMMessageBean imMessageBean = new IMMessageBean(conversation.getConversationId(), (int) conversation.getAttribute("chat_type"), message.getTimestamp(),
                "0", nickName, AppConst.LEANCLOUD_APP_ID, avatar, message);
        MessageUtils.saveMessageInfo(jsonArray, imMessageBean);

        //按照时间排序
        if (null != IMMessageBeans && IMMessageBeans.size() > 0) {
            Collections.sort(IMMessageBeans, new Comparator<IMMessageBean>() {
                @Override
                public int compare(IMMessageBean object1, IMMessageBean object2) {
                    return Long.compare(object2.getLastTime(), object1.getLastTime());
                }
            });
        }

        listAdapter.notifyDataSetChanged();

    }

}
