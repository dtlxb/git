package cn.gogoal.im.fragment.main;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.bumptech.glide.Glide;
import com.hply.qrcode_lib.activity.CodeUtils;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChooseContactActivity;
import cn.gogoal.im.activity.ContactsActivity;
import cn.gogoal.im.activity.Face2FaceActivity;
import cn.gogoal.im.activity.IMNewFriendActivity;
import cn.gogoal.im.activity.IMSearchLocalActivity;
import cn.gogoal.im.activity.OfficialAccountsActivity;
import cn.gogoal.im.activity.ScanQRCodeActivity;
import cn.gogoal.im.activity.SearchPersonSquareActivity;
import cn.gogoal.im.activity.SingleChatRoomActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AvatarTakeListener;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.UFileImageHelper;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.ggqrcode.GGQrCode;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.PopupMoreMenu;
import cn.gogoal.im.ui.widget.NoAlphaItemAnimator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;
import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_CAMERA_PERM;
import static com.hply.qrcode_lib.activity.CodeUtils.REQUEST_CODE;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.tv_to_search)
    DrawableCenterTextView tv_to_search;

    @BindView(R.id.recyclerView)
    RecyclerView message_recycler;

    @BindView(R.id.iv_go_contacts)
    ImageView goContacts;

    @BindView(R.id.iv_add_person)
    ImageView addPerson;

    @BindView(R.id.tv_xtitle)
    TextView xTitle;

    private List<IMMessageBean> IMMessageBeans;

    private ListAdapter listAdapter;

    private int allCount;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {

        initTitle();
        initRecycleView(message_recycler, R.drawable.shape_divider_1px);
        message_recycler.setItemAnimator(new NoAlphaItemAnimator());
        IMMessageBeans = new ArrayList<>();
        listAdapter = new ListAdapter(R.layout.item_fragment_message, IMMessageBeans);
        message_recycler.setAdapter(listAdapter);
    }

    private void initTitle() {
        goContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactsActivity.class));
            }
        });
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuMore(v);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IMMessageBeans.clear();
        //查找到消息列表按时间排序
        IMMessageBeans.addAll(DataSupport.order("lastTime desc").find(IMMessageBean.class));
        allCount = MessageListUtils.getAllMessageUnreadCount();
        sendUnreadCount(allCount);

        listAdapter.notifyDataSetChanged();

        listAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommonAdapter adapter, View view, final int position) {
                String conversation_id = IMMessageBeans.get(position).getConversationID();
                int chat_type = IMMessageBeans.get(position).getChatType();
                Intent intent;
                Bundle bundle = new Bundle();
                switch (chat_type) {
                    case AppConst.IM_CHAT_TYPE_SINGLE:
                        //单聊处理
                        intent = new Intent(getContext(), SingleChatRoomActivity.class);
                        bundle.putString("conversation_id", conversation_id);
                        bundle.putString("nickname", IMMessageBeans.get(position).getNickname());
                        bundle.putBoolean("need_update", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case AppConst.IM_CHAT_TYPE_SQUARE:
                        //群聊处理
                        intent = new Intent(getContext(), SquareChatRoomActivity.class);
                        bundle.putString("conversation_id", conversation_id);
                        bundle.putString("squareName", IMMessageBeans.get(position).getNickname());
                        bundle.putBoolean("need_update", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case AppConst.IM_CHAT_TYPE_LIVE:
                        //直播处理
                        intent = new Intent(getContext(), SquareChatRoomActivity.class);
                        intent.putExtra("conversation_id", conversation_id);
                        startActivity(intent);
                        break;
                    case AppConst.IM_CHAT_TYPE_SYSTEM:
                        //系统处理
                        intent = new Intent(getContext(), IMNewFriendActivity.class);
                        intent.putExtra("conversation_id", conversation_id);
                        intent.putExtra("add_type", 0x01);
                        startActivity(intent);
                        break;
                    case AppConst.IM_CHAT_TYPE_CONSULTATION:
                        //公众号(直播)
                        intent = new Intent(getContext(), OfficialAccountsActivity.class);
                        intent.putExtra("conversation_id", conversation_id);
                        startActivity(intent);
                        break;
                    case AppConst.IM_CHAT_TYPE_SQUARE_REQUEST:
                        //入群申请
                        intent = new Intent(getContext(), IMNewFriendActivity.class);
                        intent.putExtra("conversation_id", conversation_id);
                        intent.putExtra("add_type", 0x02);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        listAdapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(CommonAdapter adapter, View view, final int position) {
                DialogHelp.getSelectDialog(getActivity(), "", new String[]{"删除聊天"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        allCount = allCount - Integer.parseInt(IMMessageBeans.get(position).getUnReadCounts());
                        sendUnreadCount(allCount);
                        MessageListUtils.removeMessageInfo(IMMessageBeans.get(position).getConversationID());
                        listAdapter.removeItem(position);
                    }
                }, false).show();
                return false;
            }
        });
        tv_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IMSearchLocalActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * TODO 点击弹窗
     */
    private void popuMore(View clickView) {
        PopupMoreMenu popuMoreMenu = new PopupMoreMenu(getActivity());
        List<BaseIconText<Integer, String>> popuData = new ArrayList<>();
        popuData.add(new BaseIconText<>(R.mipmap.chat_find_man, "找人"));
        popuData.add(new BaseIconText<>(R.mipmap.chat_find_square, "发起群聊"));
        popuData.add(new BaseIconText<>(R.mipmap.chat_find_square, "扫一扫"));
        popuData.add(new BaseIconText<>(R.mipmap.chat_find_square, "面对面建群"));
        popuMoreMenu.dimBackground(false)
                .needAnimationStyle(true)
                .addMenuList(popuData)
                .setOnMenuItemClickListener(new PopupMoreMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        Intent intent;
                        switch (position) {
                            case 0://找人
                                intent = new Intent(getContext(), SearchPersonSquareActivity.class);
                                intent.putExtra("search_index", 0);
                                startActivity(intent);
                                break;
                            case 1://发起群聊
                                intent = new Intent(getContext(), ChooseContactActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putInt("square_action", AppConst.CREATE_SQUARE_ROOM_BUILD);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                                break;
                            case 2://扫一扫
                                cameraTask();
                                break;

                            case 3://扫一扫
                                startActivity(new Intent(getContext(), Face2FaceActivity.class));
                                break;
                        }
                    }
                })
                .showAsDropDown(clickView,0, 0);


    }

    @AfterPermissionGranted(CodeUtils.REQUEST_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            onClick();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    CodeUtils.REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    private void onClick() {
        Intent intent = new Intent(getContext(), ScanQRCodeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    private class ListAdapter extends CommonAdapter<IMMessageBean, BaseViewHolder> {

        private ListAdapter(int layoutId, List<IMMessageBean> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, IMMessageBean messageBean, int position) {
            String dateStr = "";
            String message = "";
            String unRead = "";
            String nickName = "";
            String squareMessageFrom = "";
            int chatType = messageBean.getChatType();
            boolean noBother = SPTools.getBoolean(UserUtils.getMyAccountId() + messageBean.getConversationID() + "noBother", false);
            final RoundedImageView avatarIv = holder.getView(R.id.head_image);
            TextView messageTv = holder.getView(R.id.last_message);
            TextView countTv = holder.getView(R.id.count_tv);
            ImageView botherIv = holder.getView(R.id.iv_no_bother);
            //消息免打扰
            if (noBother) {
                botherIv.setVisibility(View.VISIBLE);
            } else {
                botherIv.setVisibility(View.GONE);
            }

            //未读数
            setCountTag(countTv, messageBean.getUnReadCounts());
            if (messageBean.getUnReadCounts().equals("0")) {
                unRead = "";
            } else {
                unRead = "[" + messageBean.getUnReadCounts() + "条] ";
            }
            //时间
            if (null != messageBean.getLastTime()) {
                dateStr = CalendarUtils.parseDateIMMessageFormat(messageBean.getLastTime());
            }

            //SDK定义的消息类型
            if (messageBean.getLastMessage() != null) {
                AVIMMessage cacheMessage = JSON.parseObject(messageBean.getLastMessage(), AVIMMessage.class);
                String content = cacheMessage.getContent();
                JSONObject contentObject = JSON.parseObject(content);
                JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
                String _lctype = contentObject.getString("_lctype");
                nickName = messageBean.getNickname();
                //头像设置
                if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
                    if (lcattrsObject != null && lcattrsObject.get("username") != null) {
                        squareMessageFrom = UserUtils.getUserName().equals(lcattrsObject.get("username")) ? "" : lcattrsObject.getString("username");
                    }

                    if (messageBean.getAvatar() != null && !TextUtils.isEmpty(messageBean.getAvatar())) {
                        ImageDisplay.loadRoundedRectangleImage(getActivity(),
                                UFileImageHelper.load(messageBean.getAvatar()).compress(20).get(), avatarIv);
                    } else {
                        ChatGroupHelper.setGroupAvatar(messageBean.getConversationID(), new AvatarTakeListener() {
                            @Override
                            public void success(final Bitmap bitmap) {
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            avatarIv.setImageBitmap(bitmap);
                                        }
                                    });
                                }

                            }

                            @Override
                            public void failed(Exception e) {

                            }
                        });
                    }

                } else if (chatType == AppConst.IM_CHAT_TYPE_SYSTEM) {
                    ImageDisplay.loadRoundedRectangleImage(getActivity(), R.mipmap.chat_new_friend, avatarIv);
                } else {
                    ImageDisplay.loadRoundedRectangleImage(getActivity(),
                            UFileImageHelper.load(messageBean.getAvatar()).compress(20).get(), avatarIv);
                }

                switch (_lctype) {
                    case AppConst.IM_MESSAGE_TYPE_TEXT:
                        //文字
                        message = contentObject.getString("_lctext");
                        if (StringUtils.StringFilter(message, "@*[\\S]*[ \r\n]")) {
                            message = "[有人@了你]" + squareMessageFrom + ":" + contentObject.getString("_lctext");
                        }
                        break;
                    case AppConst.IM_MESSAGE_TYPE_PHOTO:
                        //图片
                        message = "[图片]";
                        break;
                    case AppConst.IM_MESSAGE_TYPE_AUDIO:
                        //语音
                        message = "[语音]";
                        break;
                    case AppConst.IM_MESSAGE_TYPE_FRIEND_DEL:

                        break;
                    case AppConst.IM_MESSAGE_TYPE_FRIEND_ADD:
                        //加好友
                        nickName = "新的好友";
                        message = messageBean.getNickname() + "请求添加你为好友";
                        break;
                    case AppConst.IM_MESSAGE_TYPE_CONTACT_ADD:

                        break;
                    case AppConst.IM_MESSAGE_TYPE_CONTACT_DEL:

                        break;
                    case AppConst.IM_MESSAGE_TYPE_SQUARE_ADD:
                    case AppConst.IM_MESSAGE_TYPE_SQUARE_DEL:
                        //群加人，踢人
                        JSONArray accountArray;
                        String squareMessage;
                        if (null != lcattrsObject && null != lcattrsObject.getJSONArray("accountList")) {
                            accountArray = lcattrsObject.getJSONArray("accountList");
                            squareMessage = MessageListUtils.findSquarePeople(accountArray, _lctype);
                            //群消息记录
                            SPTools.saveString(UserUtils.getMyAccountId() + messageBean.getConversationID() + "_square_message", squareMessage);
                        } else {
                            squareMessage = SPTools.getString(UserUtils.getMyAccountId() + messageBean.getConversationID() + "_square_message", "");
                        }
                        message = squareMessage;
                        break;
                    case AppConst.IM_MESSAGE_TYPE_SQUARE_REQUEST:
                        //群通知
                        nickName = "申请入群";
                        message = messageBean.getNickname() + "请求加入" + messageBean.getFriend_id();
                        Glide.with(getActivity()).load(UFileImageHelper.load(messageBean.getAvatar()).compress(20).get()).into(avatarIv);
                        break;
                    case AppConst.IM_MESSAGE_TYPE_SQUARE_DETAIL:
                        //群公告,群简介
                        message = contentObject.getString("_lctext");
                        break;
                    case AppConst.IM_MESSAGE_TYPE_PUBLIC:
                        message = "公众号消息";
                        break;
                    case AppConst.IM_MESSAGE_TYPE_STOCK:
                        //股票消息
                        message = "发来一条股票消息";
                        break;
                    case AppConst.IM_MESSAGE_TYPE_SHARE:
                        message = "发来一条分享消息";
                        break;
                    default:
                        break;
                }
            }

            if (chatType == 1002) {
                if (message.length() > 7 && message.startsWith("[有人@了你]")) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(message); // 包装字体内容
                    ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(R.color.stock_red)); // 设置字体颜色
                    sb.setSpan(fcs, 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    messageTv.setText(sb);
                } else if (message.length() > 5 && message.startsWith("[草稿] ")) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(message);
                    ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(R.color.stock_red));
                    sb.setSpan(fcs, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    messageTv.setText(sb);
                } else {
                    messageTv.setText(squareMessageFrom.equals("") ? message : squareMessageFrom + ": " +
                            message);
                }
            } else {
                if (message.length() > 5 && message.startsWith("[草稿] ")) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(message);
                    ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(R.color.stock_red));
                    sb.setSpan(fcs, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    messageTv.setText(sb);
                } else {
                    messageTv.setText(message);
                }
            }
            //holder.setText(R.id.last_message, unRead + message);
            holder.setText(R.id.whose_message, nickName);
            holder.setText(R.id.last_time, dateStr);
        }
    }

    private void setCountTag(TextView view, String count) {
        if (count.equals("0")) {
            view.setVisibility(View.GONE);
        } else if (count.length() == 1) {
            view.setVisibility(View.VISIBLE);
            view.setText(count);
            view.setBackgroundResource(R.drawable.shape_message_circle_tag);
        } else if (count.length() == 2) {
            view.setVisibility(View.VISIBLE);
            view.setText(count);
            view.setBackgroundResource(R.drawable.shape_message_tag);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText("...");
            view.setBackgroundResource(R.drawable.shape_message_tag);
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

    /**
     * 判断client连接状态
     */
    @Subscriber(tag = "connection_status")
    public void clientStatus(String msg) {
        if (msg.equals("connection_paused")) {
            xTitle.setText(R.string.title_status);
        } else if (msg.equals("connection_resume")) {
            xTitle.setText(R.string.title_message);
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
        final String ConversationId = conversation.getConversationId();
        //获取免打扰
        KLog.e(conversation.get("mu"));
        if (conversation.get("mu") != null) {
            boolean canBother = (boolean) conversation.get("mu");
            SPTools.saveBoolean(UserUtils.getMyAccountId() + ConversationId + "noBother", canBother);
        } else {
            SPTools.saveBoolean(UserUtils.getMyAccountId() + ConversationId + "noBother", false);
        }

        int chatType = (int) conversation.getAttribute("chat_type");
        Long rightNow = CalendarUtils.getCurrentTime();
        String nickName = "";
        String avatar = "";
        String friend_id = UserUtils.getMyAccountId();
        int unreadMessage = 0;

        JSONObject contentObject = JSON.parseObject(message.getContent());
        JSONObject lcattrsObject = contentObject.getJSONObject("_lcattrs");
        String _lctype = contentObject.getString("_lctype");
        switch (_lctype) {
            case AppConst.IM_MESSAGE_TYPE_TEXT:
                //文字
            case AppConst.IM_MESSAGE_TYPE_PHOTO:
                //图片
            case AppConst.IM_MESSAGE_TYPE_AUDIO:
                //语音
            case AppConst.IM_MESSAGE_TYPE_CONTACT_ADD:
            case AppConst.IM_MESSAGE_TYPE_SHARE:
                //好友加入通讯录
                nickName = lcattrsObject.getString("username");
                break;
            case AppConst.IM_MESSAGE_TYPE_FRIEND_DEL:
            case AppConst.IM_MESSAGE_TYPE_FRIEND_ADD:
                //加好友
                nickName = lcattrsObject.getString("nickname");
                break;
            case AppConst.IM_MESSAGE_TYPE_CONTACT_DEL:
                //好友从通讯录移除
                break;
            case AppConst.IM_MESSAGE_TYPE_SQUARE_ADD:
            case AppConst.IM_MESSAGE_TYPE_SQUARE_DEL:
                //好友入群
                break;
            case AppConst.IM_MESSAGE_TYPE_SQUARE_REQUEST:
                //申请入群
                nickName = lcattrsObject.getString("nickname");
                friend_id = lcattrsObject.getString("group_name");
                break;
            case AppConst.IM_MESSAGE_TYPE_SQUARE_DETAIL:
                //群公告,群简介
                nickName = lcattrsObject.getString("nickname");
                friend_id = lcattrsObject.getString("group_name");
                break;
            case AppConst.IM_MESSAGE_TYPE_PUBLIC:
                //公众号
                avatar = (String) conversation.getAttribute("avatar");
                nickName = conversation.getName();
                break;
            case AppConst.IM_MESSAGE_TYPE_STOCK:
                //股票消息
                break;
            default:
                break;
        }

        switch (chatType) {
            //单聊,群聊,加好友请求
            case AppConst.IM_CHAT_TYPE_SINGLE:
                avatar = lcattrsObject.getString("avatar");
                break;
            case AppConst.IM_CHAT_TYPE_SQUARE:
                nickName = conversation.getName();
                //avatar = (String) conversation.getAttribute("avatar");
                avatar = (String) conversation.getAttribute("avatar");
                break;
            case AppConst.IM_CHAT_TYPE_SYSTEM:
                break;
            //直播
            case AppConst.IM_CHAT_TYPE_LIVE:
                break;
            //操纵通讯录
            case AppConst.IM_CHAT_TYPE_SQUARE_ACTION:
                break;
            //公众号模块
            case AppConst.IM_CHAT_TYPE_CONSULTATION:
                break;
            //群通知
            case AppConst.IM_CHAT_TYPE_SQUARE_REQUEST:
                avatar = lcattrsObject.getString("avatar");
                break;
            default:
                break;
        }

        //已有的会话
        for (int i = 0; i < IMMessageBeans.size(); i++) {
            if (IMMessageBeans.get(i).getConversationID().equals(ConversationId)) {
                IMMessageBeans.get(i).setLastTime(rightNow);
                IMMessageBeans.get(i).setLastMessage(JSON.toJSONString(message));
                unreadMessage = Integer.parseInt(IMMessageBeans.get(i).getUnReadCounts().equals("") ? "0" : IMMessageBeans.get(i).getUnReadCounts()) + 1;
                IMMessageBeans.get(i).setUnReadCounts(unreadMessage + "");
                isTheSame = true;
            }
        }

        //新添的会话
        if (!isTheSame) {
            IMMessageBean imMessageBean = new IMMessageBean();
            imMessageBean.setConversationID(ConversationId);
            imMessageBean.setLastMessage(JSON.toJSONString(message));
            imMessageBean.setLastTime(rightNow);
            imMessageBean.setNickname(nickName);
            imMessageBean.setAvatar(avatar);
            imMessageBean.setChatType(chatType);
            imMessageBean.setUnReadCounts(1 + "");
            IMMessageBeans.add(imMessageBean);
        }

        //保存
        IMMessageBean imMessageBean = new IMMessageBean(ConversationId, chatType, message.getTimestamp(),
                isTheSame ? String.valueOf(unreadMessage) : "1", nickName, friend_id, avatar, JSON.toJSONString(message));
        MessageListUtils.saveMessageInfo(imMessageBean);
        allCount++;
        sendUnreadCount(allCount);
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

    private void sendUnreadCount(int count) {
        //发送消息更改消息总数
        HashMap<String, Object> countMap = new HashMap<>();
        countMap.put("index", 0);
        countMap.put("number", count);
        BaseMessage countMessage = new BaseMessage("message_count", countMap);
        AppManager.getInstance().sendMessage("correct_allmessage_count", countMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            KLog.e("==========" + data.getExtras().getString(CodeUtils.RESULT_STRING) + "===========");
        } catch (Exception e) {
            e.getMessage();
        }

        KLog.e("requestCode==" + requestCode);

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    String codeBody = GGQrCode.getUserQrCodeBody(result);

                    try {
                        JSONObject scanBody = JSONObject.parseObject(codeBody);
                        if (scanBody.getString("qrType").equalsIgnoreCase("0")) {
                            //TODO 跳转个人详情
                            NormalIntentUtils.go2PersionDetail(getContext(),
                                    Integer.parseInt(scanBody.getString("account_id")));

                        } else if (scanBody.getString("qrType").equalsIgnoreCase("1")) {
                            //TODO 跳转群名片
                        }

                    } catch (Exception e) {
                        e.getMessage();
                        KLog.e("结果不是json");
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    UIHelper.toast(getActivity(), "解析出错");
                }
            }
        }
    }
}
