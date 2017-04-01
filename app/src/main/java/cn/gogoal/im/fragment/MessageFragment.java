package cn.gogoal.im.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChooseContactActivity;
import cn.gogoal.im.activity.IMNewFrienActivity;
import cn.gogoal.im.activity.SearchPersonSquareActivity;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.OnItemClickLitener;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.ImageUtils.GroupFaceImage;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
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

    private List<String> groupMembers;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        groupMembers = new ArrayList<>();
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
                popuMore(view);
            }
        };
        xTitle.addAction(personAction, 0);
        xTitle.addAction(addAction, 1);

    }

    @Override
    public void onResume() {
        super.onResume();
        jsonArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + "_conversation_beans", new JSONArray());
        IMMessageBeans.clear();
        IMMessageBeans.addAll(JSON.parseArray(String.valueOf(jsonArray), IMMessageBean.class));

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
                        Bundle bundle = new Bundle();
                        switch (chat_type) {
                            case "1001":
                                //单聊处理
                                intent = new Intent(getContext(), SingleChatRoomActivity.class);
                                bundle.putString("conversation_id", conversation_id);
                                bundle.putString("nickname", IMMessageBeans.get(position).getNickname());
                                bundle.putBoolean("need_update", true);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case "1002":
                                //群聊处理
                                intent = new Intent(getContext(), SquareChatRoomActivity.class);
                                bundle.putString("conversation_id", conversation_id);
                                bundle.putString("squareName", IMMessageBeans.get(position).getNickname());
                                bundle.putBoolean("need_update", true);
                                intent.putExtras(bundle);
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
                                intent.putExtra("add_type", 0x01);
                                startActivity(intent);
                                break;
                            case "1007":
                                //入群申请
                                intent = new Intent(getContext(), IMNewFrienActivity.class);
                                intent.putExtra("conversation_id", conversation_id);
                                intent.putExtra("add_type", 0x02);
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

    /**
     * 点击弹窗
     */
    private void popuMore(View clickView) {
        final View popuView = LayoutInflater.from(clickView.getContext()).
                inflate(R.layout.chat_message_more,
                        new LinearLayout(getActivity().getApplicationContext()), false);


        PopupWindow popuWindow = new PopupWindow(popuView,
                (int) (AppDevice.getWidth(getContext()) * 0.4), -2, //// 弹窗宽、高
                true);// 是否获取焦点（能使用返回键能关闭而不是退出）
        initPopu(popuView, popuWindow);
        popuWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.chat_popu_more));
        popuWindow.setOutsideTouchable(true);// 点击周边可关闭
        popuWindow.showAsDropDown(clickView, AppDevice.dp2px(getContext(), 5), AppDevice.dp2px(getContext(), 6));

    }

    //初始化弹窗
    private void initPopu(View popuView, PopupWindow popupWindow) {
        RelativeLayout find_man_layout = (RelativeLayout) popuView.findViewById(R.id.find_man_layout);
        RelativeLayout find_square_layout = (RelativeLayout) popuView.findViewById(R.id.find_square_layout);
        RelativeLayout take_square_layout = (RelativeLayout) popuView.findViewById(R.id.take_square_layout);
        RelativeLayout sweep_twocode_layout = (RelativeLayout) popuView.findViewById(R.id.sweep_twocode_layout);
        find_man_layout.setOnClickListener(new PopuClick(popupWindow));
        find_square_layout.setOnClickListener(new PopuClick(popupWindow));
        take_square_layout.setOnClickListener(new PopuClick(popupWindow));
        sweep_twocode_layout.setOnClickListener(new PopuClick(popupWindow));
    }

    //弹窗监听事件
    private class PopuClick implements View.OnClickListener {

        PopupWindow popupWindow;

        public PopuClick(PopupWindow popupWindow) {
            this.popupWindow = popupWindow;
        }

        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            Intent intent;
            switch (v.getId()) {
                case R.id.find_man_layout:
                    intent = new Intent(getContext(), SearchPersonSquareActivity.class);
                    intent.putExtra("search_index", 0);
                    startActivity(intent);
                    break;
                case R.id.find_square_layout:
                    intent = new Intent(getContext(), SearchPersonSquareActivity.class);
                    intent.putExtra("search_index", 1);
                    startActivity(intent);
                    break;
                case R.id.take_square_layout:
                    intent = new Intent(getContext(), ChooseContactActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("square_action", AppConst.CREATE_SQUARE_ROOM_BUILD);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    break;
                case R.id.sweep_twocode_layout:
                    break;
            }
        }
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
            String squareMessageFrom = "";
            int chatType = messageBean.getChatType();
            ImageView avatarIv = holder.getView(R.id.head_image);
            TextView messageTv = holder.getView(R.id.last_message);
            Badge badge = new BadgeView(getActivity()).bindTarget(holder.getView(R.id.head_layout));
            badge.setBadgeTextSize(10, true);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);

            KLog.e(messageBean);

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
                JSONObject contentObject = JSON.parseObject(content);
                JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                String _lctype = contentObject.getString("_lctype");
                nickName = messageBean.getNickname();

                //头像设置
                if (chatType == 1001) {
                    ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);
                } else if (chatType == 1002) {
                    if (lcattrsObject != null) {
                        squareMessageFrom = (lcattrsObject.get("username")) == null ? "" : lcattrsObject.getString("username");
                    }
                    if (ImageUtils.getBitmapFilePaht(messageBean.getConversationID(), "imagecache").equals("")) {
                        createGroupImage(messageBean.getConversationID(), position);
                    } else {
                        ImageDisplay.loadFileImage(getmContext(), new File(ImageUtils.getBitmapFilePaht(messageBean.getConversationID(), "imagecache")), avatarIv);
                    }
                } else if (chatType == 1004) {
                    ImageDisplay.loadResImage(getActivity(), R.mipmap.chat_new_friend, avatarIv);
                }

                switch (_lctype) {
                    case "-1":
                        //文字
                        message = contentObject.getString("_lctext");
                        if (StringUtils.StringFilter(message, "@*[\\S]*[ \r\n]")) {
                            message = "[有人@了你]" + contentObject.getString("_lctext");
                        }
                        break;
                    case "-2":
                        //图片
                        message = "[图片]";
                        break;
                    case "-3":
                        //语音
                        message = "[语音]";
                        break;
                    case "1":

                        break;

                    case "2":
                        //加好友
                        nickName = "新的好友";
                        message = messageBean.getNickname() + "请求添加你为好友";
                        break;
                    case "3":

                        break;
                    case "4":

                        break;
                    case "5":
                    case "6":
                        //群加人，踢人
                        JSONArray accountArray;
                        String squareMessage;
                        if (null != lcattrsObject && null != lcattrsObject.getJSONArray("accountList")) {
                            accountArray = lcattrsObject.getJSONArray("accountList");
                            squareMessage = MessageUtils.findSquarePeople(accountArray, _lctype);
                            //群消息记录
                            SPTools.saveString(UserUtils.getUserAccountId() + messageBean.getConversationID() + "_square_message", squareMessage);
                        } else {
                            squareMessage = SPTools.getString(UserUtils.getUserAccountId() + messageBean.getConversationID() + "_square_message", "");
                        }
                        message = squareMessage;

                        //群头像拼接
                        createGroupImage(messageBean.getConversationID(), position);
                        break;
                    case "7":
                        //群通知
                        nickName = "申请入群";
                        message = messageBean.getNickname() + "请求加入" + messageBean.getFriend_id();
                        ImageDisplay.loadNetImage(getActivity(), messageBean.getAvatar(), avatarIv);
                        break;
                    default:
                        break;
                }
            }

            if (chatType == 1002) {
                if (message.length() > 7 && message.substring(0, 7).equals("[有人@了你]")) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(message); // 包装字体内容
                    ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(R.color.stock_red)); // 设置字体颜色
                    sb.setSpan(fcs, 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    messageTv.setText(sb);
                } else {
                    messageTv.setText(squareMessageFrom + ":" +
                            message);
                }
            } else {
                messageTv.setText(message);
            }
            //holder.setText(R.id.last_message, unRead + message);
            holder.setText(R.id.whose_message, nickName);
            holder.setText(R.id.last_time, dateStr);
        }
    }

    /**
     * 群头像
     */
    @Subscriber(tag = "set_avatar")
    public void setAvatar(String code) {
        KLog.e(code);
        listAdapter.notifyItemChanged(Integer.parseInt(code));
    }

    private void createGroupImage(final String ConversationId, final int position) {
        //群删除好友(每次删除后重新生成群头像)
        JSONArray accountArray = SPTools.getJsonArray(UserUtils.getUserAccountId() + ConversationId + "_accountList_beans", new JSONArray());
        if (null != accountArray && accountArray.size() > 0) {
            getNinePic(accountArray, ConversationId, position);
        } else {
            UserUtils.getChatGroup(groupMembers, ConversationId, new UserUtils.getSquareInfo() {
                @Override
                public void squareGetSuccess(JSONObject object) {
                    JSONArray array = object.getJSONArray("accountList");
                    if (null != array && array.size() > 0)
                        getNinePic(array, ConversationId, position);
                }

                @Override
                public void squareGetFail(String error) {

                }
            });
        }
    }

    private void getNinePic(JSONArray array, final String ConversationId, final int position) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject personObject = array.getJSONObject(i);
            picUrls.add(personObject.getString("avatar"));
        }
        //九宫图拼接
        GroupFaceImage.getInstance(getContext(), picUrls).load(new GroupFaceImage.OnMatchingListener() {
            @Override
            public void onSuccess(Bitmap mathingBitmap) {
                String groupFaceImageName = "_" + ConversationId + ".png";
                ImageUtils.saveBitmapFile(mathingBitmap, "imagecache", groupFaceImageName);

                AppManager.getInstance().sendMessage("set_avatar", position + "");
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        Map<String, Object> map = baseMessage.getOthers();
        AVIMMessage message = (AVIMMessage) map.get("message");
        AVIMConversation conversation = (AVIMConversation) map.get("conversation");
        groupMembers.clear();
        groupMembers.addAll(conversation.getMembers());
        boolean isTheSame = false;
        final String ConversationId = conversation.getConversationId();
        int chatType = (int) conversation.getAttribute("chat_type");
        Long rightNow = CalendarUtils.getCurrentTime();
        String nickName = "";
        String avatar = "";
        String friend_id = UserUtils.getUserAccountId();
        int unreadmessage = 0;

        JSONObject contentObject = JSON.parseObject(message.getContent());
        JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
        String _lctype = contentObject.getString("_lctype");

        KLog.e(message.getContent());

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
                //好友加入通讯录
                break;
            case "4":
                //好友从通讯录移除
                break;
            case "5":
            case "6":
                //好友入群
                //群删除好友(每次删除后重新生成群头像)
                break;
            case "7":
                //申请入群
                nickName = lcattrsObject.getString("nickname");
                friend_id = lcattrsObject.getString("group_name");
                break;
            default:
                break;
        }

        switch (chatType) {
            //单聊,群聊,加好友请求
            case 1001:
                avatar = lcattrsObject.getString("avatar");
                break;
            case 1002:
                nickName = conversation.getName();
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
            //群通知
            case 1007:
                avatar = lcattrsObject.getString("avatar");
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
            imMessageBean.setChatType(chatType);
            imMessageBean.setUnReadCounts(1 + "");
            IMMessageBeans.add(imMessageBean);
        }

        //保存
        IMMessageBean imMessageBean = new IMMessageBean(ConversationId, chatType, message.getTimestamp(),
                isTheSame ? String.valueOf(unreadmessage) : "1", nickName, friend_id, avatar, message);
        KLog.e(imMessageBean);
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
