package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

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
import cn.gogoal.im.activity.IMAddFriendActivity;
import cn.gogoal.im.activity.IMNewFrienActivity;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
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
import cn.gogoal.im.adapter.recycleviewAdapterHelper.OnItemClickLitener;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.badgeview.Badge;
import cn.gogoal.im.ui.badgeview.BadgeView;
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

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();

        initRecycleView(message_recycler, R.drawable.shape_divider_recyclerview_1px);
    }

    private void initTitle() {
        XTitle xTitle = setFragmentTitle(R.string.title_message);
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

    @Override
    public void onResume() {
        super.onResume();
        jsonArray = SPTools.getJsonArray(AppConst.LEAN_CLOUD_TOKEN + "_conversation_beans", new JSONArray());
        IMMessageBeans.clear();
        if (null != jsonArray) {
            IMMessageBeans.addAll(JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class));
        }

        KLog.e(IMMessageBeans.size());

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
        listAdapter.setOnItemClickListener(new OnItemClickLitener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, View view, final int position) {
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
                                KLog.e(conversation.getConversationId());
                                intent = new Intent(getContext(), SingleChatRoomActivity.class);
                                intent.putExtra("nickname", IMMessageBeans.get(position).getNickname());
                                intent.putExtra("conversation_id", conversation_id);
                                startActivity(intent);
                                break;
                            case "1002":
                                //群聊处理
                                intent = new Intent(getContext(), SquareChatRoomActivity.class);
                                intent.putExtra("conversation_id", conversation_id);
                                intent.putExtra("squareName", IMMessageBeans.get(position).getNickname());
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

            //长按删除
            @Override
            public boolean onItemLongClick(RecyclerView.ViewHolder holder, View view, final int position) {
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

    private class ListAdapter extends CommonAdapter<IMMessageBean> {

        private ListAdapter(Context context, int layoutId, List<IMMessageBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, IMMessageBean messageBean, int position) {
            String dateStr = "";
            String message = "";
            String unRead = "";
            String nickName = "";
            ImageView avatarIv = holder.getView(R.id.head_image);
            Badge badge = new BadgeView(getActivity()).bindTarget(holder.getView(R.id.head_layout));
            badge.setBadgeTextSize(10, true);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            //未读数
            if (messageBean.getUnReadCounts().equals("0")) {
                unRead = "";
                badge.hide(true);
            } else {
                badge.setBadgeText(messageBean.getUnReadCounts());
                unRead = "[" + messageBean.getUnReadCounts() + "条] ";
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
                nickName = messageBean.getNickname();
                switch (_lctype) {
                    case "-1":
                        //文字
                        message = contentObject.getString("_lctext");
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);

                        break;
                    case "-2":
                        //图片
                        message = "[图片]";
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);

                        break;
                    case "-3":
                        //语音
                        message = "[语音]";
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
                    case "3":

                        break;
                    case "4":

                        break;
                    case "5":
                        message = nickName + "加入群聊";
                        break;
                    default:
                        break;
                }
            }

            //holder.setText(R.id.last_message, unRead + message);
            holder.setText(R.id.last_message, message);
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
        String ConversationId = conversation.getConversationId();
        int chatType = (int) conversation.getAttribute("chat_type");


        Long rightNow = CalendarUtils.getCurrentTime();
        String nickName = "";
        String avatar = "";
        int unreadmessage = 0;

        JSONObject contentObject = JSON.parseObject(message.getContent());
        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
        String _lctype = contentObject.getString("_lctype");

        switch (chatType) {
            //单聊,群聊,加好友请求
            case 1001:
                avatar = lcattrsObject.getString("avatar");
                break;
            case 1002:
                //生成群聊头像
                JSONArray accountArray = SPTools.getJsonArray(UserUtils.getToken() + ConversationId + "_accountList_beans", new JSONArray());
                List<String> picUrls = new ArrayList<>();
                for (int i = 0; i < (accountArray.size() < 9 ? accountArray.size() : 9); i++) {
                    JSONObject personObject = accountArray.getJSONObject(i);
                    picUrls.add(personObject.getString("avatar"));
                }
                KLog.e(picUrls);
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
            case "3":

                break;
            case "4":

                break;
            case "5":
                nickName = conversation.getName();
                break;
            default:
                break;
        }

        //已有的会话
        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(ConversationId)) {
                IMMessageBeans.get(i).setLastTime(rightNow);
                IMMessageBeans.get(i).setLastMessage(message);
                unreadmessage = Integer.parseInt(IMMessageBeans.get(i).getUnReadCounts().equals("") ? "0" : IMMessageBeans.get(i).getUnReadCounts()) + 1;
                IMMessageBeans.get(i).setUnReadCounts(unreadmessage + "");
                isTheSame = true;
            } else {
            }
        }

        //新添的会话
        if (!isTheSame) {
            IMMessageBean imMessageBean = new IMMessageBean();
            imMessageBean.setConversationID(ConversationId);
            imMessageBean.setLastMessage(message);
            imMessageBean.setLastTime(rightNow);
            imMessageBean.setNickname(nickName);
            imMessageBean.setAvatar(avatar);
            imMessageBean.setUnReadCounts(1 + "");
            IMMessageBeans.add(imMessageBean);
        }

        KLog.e(message.getContent());
        KLog.e(chatType);

        //保存
        IMMessageBean imMessageBean = new IMMessageBean(ConversationId, chatType, message.getTimestamp(),
                String.valueOf(unreadmessage), nickName, AppConst.LEANCLOUD_APP_ID, avatar, message);
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
