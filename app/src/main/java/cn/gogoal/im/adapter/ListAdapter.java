//package cn.gogoal.im.adapter;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.style.ForegroundColorSpan;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.avos.avoscloud.im.v2.AVIMConversation;
//import com.bumptech.glide.Glide;
//import com.socks.library.KLog;
//
//import java.util.List;
//
//import cn.gogoal.im.R;
//import cn.gogoal.im.activity.IMNewFrienActivity;
//import cn.gogoal.im.activity.OfficialAccountsActivity;
//import cn.gogoal.im.activity.SingleChatRoomActivity;
//import cn.gogoal.im.activity.SquareChatRoomActivity;
//import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
//import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
//import cn.gogoal.im.bean.IMMessageBean;
//import cn.gogoal.im.common.AppConst;
//import cn.gogoal.im.common.CalendarUtils;
//import cn.gogoal.im.common.DialogHelp;
//import cn.gogoal.im.common.IMHelpers.AVImClientManager;
//import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
//import cn.gogoal.im.common.IMHelpers.MessageListUtils;
//import cn.gogoal.im.common.ImageUtils.ImageDisplay;
//import cn.gogoal.im.common.ImageUtils.ImageUtils;
//import cn.gogoal.im.common.SPTools;
//import cn.gogoal.im.common.StringUtils;
//import cn.gogoal.im.common.UIHelper;
//import cn.gogoal.im.common.UserUtils;
//import cn.gogoal.im.fragment.main.MessageFragment;
//
//import static java.security.AccessController.getContext;
//
///**
// * author wangjd on 2017/5/10 0010.
// * Staff_id 1375
// * phone 18930640263
// * description :${annotated}.
// */
//public class ListAdapter extends CommonAdapter<IMMessageBean, BaseViewHolder> {
//
//    private Context context;
//
//    public ListAdapter(Context context, List<IMMessageBean> datas) {
//        super(R.layout.item_fragment_message, datas);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder holder,final IMMessageBean messageBean, final int position) {
//        String dateStr = "";
//        String message = "";
//        String unRead = "";
//        String nickName = "";
//        String squareMessageFrom = "";
//        int chatType = messageBean.getChatType();
//        ImageView avatarIv = holder.getView(R.id.head_image);
//        TextView messageTv = holder.getView(R.id.last_message);
//        TextView countTv = holder.getView(R.id.count_tv);
//        //未读数
//        setCountTag(countTv, messageBean.getUnReadCounts());
//        if (messageBean.getUnReadCounts().equals("0")) {
//            unRead = "";
//        } else {
//            unRead = "[" + messageBean.getUnReadCounts() + "条] ";
//        }
//        //时间
//        if (null != messageBean.getLastTime()) {
//            dateStr = CalendarUtils.parseDateIMMessageFormat(messageBean.getLastTime());
//        }
//
//        //SDK定义的消息类型
//        if (messageBean.getLastMessage() != null) {
//            String content = messageBean.getLastMessage().getContent();
//            JSONObject contentObject = JSON.parseObject(content);
//            JSONObject lcattrsObject = JSON.parseObject(contentObject.getString("_lcattrs"));
//            String _lctype = contentObject.getString("_lctype");
//            nickName = messageBean.getNickname();
//            //头像设置
//            if (chatType == AppConst.IM_CHAT_TYPE_SQUARE) {
//                if (lcattrsObject != null && lcattrsObject.get("username") != null) {
//                    squareMessageFrom = UserUtils.getUserName().equals(lcattrsObject.get("username")) ? "" : lcattrsObject.getString("username");
//                }
//                if (ImageUtils.getBitmapFilePaht(messageBean.getConversationID(), "imagecache").equals("")) {
//                    ChatGroupHelper.createGroupImage(messageBean.getConversationID(), gruopMemberMap.get(messageBean.getConversationID()), "set_avatar");
//                } else {
//                    ImageDisplay.loadRoundedRectangleImage(context, ImageUtils.getBitmapFilePaht(messageBean.getConversationID(), "imagecache"), avatarIv);
//                }
//            } else if (chatType == AppConst.IM_CHAT_TYPE_SYSTEM) {
//                ImageDisplay.loadRoundedRectangleImage(context, R.mipmap.chat_new_friend, avatarIv);
//            } else {
//                ImageDisplay.loadRoundedRectangleImage(context,
//                        messageBean.getAvatar(), avatarIv);
//            }
//
//            switch (_lctype) {
//                case AppConst.IM_MESSAGE_TYPE_TEXT:
//                    //文字
//                    message = contentObject.getString("_lctext");
//                    if (StringUtils.StringFilter(message, "@*[\\S]*[ \r\n]")) {
//                        message = "[有人@了你]" + contentObject.getString("_lctext");
//                    }
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_PHOTO:
//                    //图片
//                    message = "[图片]";
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_AUDIO:
//                    //语音
//                    message = "[语音]";
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_FRIEND_ADD:
//
//                    break;
//
//                case AppConst.IM_MESSAGE_TYPE_FRIEND_DEL:
//                    //加好友
//                    nickName = "新的好友";
//                    message = messageBean.getNickname() + "请求添加你为好友";
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_CONTACT_ADD:
//
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_CONTACT_DEL:
//
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_SQUARE_ADD:
//                case AppConst.IM_MESSAGE_TYPE_SQUARE_DEL:
//                    //群加人，踢人
//                    JSONArray accountArray;
//                    String squareMessage;
//                    if (null != lcattrsObject && null != lcattrsObject.getJSONArray("accountList")) {
//                        accountArray = lcattrsObject.getJSONArray("accountList");
//                        squareMessage = MessageListUtils.findSquarePeople(accountArray, _lctype);
//                        //群消息记录
//                        SPTools.saveString(UserUtils.getMyAccountId() + messageBean.getConversationID() + "_square_message", squareMessage);
//                    } else {
//                        squareMessage = SPTools.getString(UserUtils.getMyAccountId() + messageBean.getConversationID() + "_square_message", "");
//                    }
//                    message = squareMessage;
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_SQUARE_REQUEST:
//                    //群通知
//                    nickName = "申请入群";
//                    message = messageBean.getNickname() + "请求加入" + messageBean.getFriend_id();
//                    Glide.with(context).load(messageBean.getAvatar()).into(avatarIv);
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_SQUARE_DETIAL:
//                    //群公告,群简介
//                    message = contentObject.getString("_lctext");
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_PUBLIC:
//                    message = "公众号消息";
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_STOCK:
//                    //股票消息
//                    message = "发来一条股票消息";
//                    break;
//                case AppConst.IM_MESSAGE_TYPE_SHARE:
//                    message = "发来一条分享消息";
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        if (chatType == 1002) {
//            if (message.length() > 7 && message.substring(0, 7).equals("[有人@了你]")) {
//                SpannableStringBuilder sb = new SpannableStringBuilder(message); // 包装字体内容
//                ForegroundColorSpan fcs = new ForegroundColorSpan(getResColor(R.color.stock_red)); // 设置字体颜色
//                sb.setSpan(fcs, 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                messageTv.setText(sb);
//            } else {
//                messageTv.setText(squareMessageFrom.equals("") ? message : squareMessageFrom + ": " +
//                        message);
//            }
//        } else {
//            messageTv.setText(message);
//        }
//        //holder.setText(R.id.last_message, unRead + message);
//        holder.setText(R.id.whose_message, nickName);
//        holder.setText(R.id.last_time, dateStr);
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                DialogHelp.getSelectDialog(context, "", new String[]{"删除聊天"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MessageListUtils.removeMessageInfo(messageBean.getConversationID());
//                        removeItem(position);
//                    }
//                }, false).show();
//                return true;
//            }
//        });
//        final String conversation_id = messageBean.getConversationID();
//
//        KLog.e(conversation_id);
//
//        AVIMClientManager.getInstance().findConversationById(conversation_id, new AVIMClientManager.ChatJoinManager() {
//            @Override
//            public void joinSuccess(AVIMConversation conversation) {
//                int chat_type = (int) conversation.getAttribute("chat_type");
//                Intent intent;
//                Bundle bundle = new Bundle();
//                switch (chat_type) {
//                    case AppConst.IM_CHAT_TYPE_SINGLE:
//                        //单聊处理
//                        intent = new Intent(context, SingleChatRoomActivity.class);
//                        bundle.putString("conversation_id", conversation_id);
//                        bundle.putString("nickname", messageBean.getNickname());
//                        bundle.putBoolean("need_update", true);
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//                        break;
//                    case AppConst.IM_CHAT_TYPE_SQUARE:
//                        //群聊处理
//                        intent = new Intent(context, SquareChatRoomActivity.class);
//                        bundle.putString("conversation_id", conversation_id);
//                        bundle.putString("squareName", messageBean.getNickname());
//                        bundle.putBoolean("need_update", true);
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//                        break;
//                    case AppConst.IM_CHAT_TYPE_LIVE:
//                        //直播处理
//                        intent = new Intent(context, SquareChatRoomActivity.class);
//                        intent.putExtra("conversation_id", conversation_id);
//                        context.startActivity(intent);
//                        break;
//                    case AppConst.IM_CHAT_TYPE_SYSTEM:
//                        //系统处理
//                        intent = new Intent(context, IMNewFrienActivity.class);
//                        intent.putExtra("conversation_id", conversation_id);
//                        intent.putExtra("add_type", 0x01);
//                        context.startActivity(intent);
//                        break;
//                    case AppConst.IM_CHAT_TYPE_CONSULTATION:
//                        //公众号(直播)
//                        intent = new Intent(context, OfficialAccountsActivity.class);
//                        intent.putExtra("conversation_id", conversation_id);
//                        context.startActivity(intent);
//                        break;
//                    case AppConst.IM_CHAT_TYPE_SQUARE_REQUEST:
//                        //入群申请
//                        intent = new Intent(context, IMNewFrienActivity.class);
//                        intent.putExtra("conversation_id", conversation_id);
//                        intent.putExtra("add_type", 0x02);
//                        context.startActivity(intent);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void joinFail(String error) {
//                UIHelper.toast(context, error);
//            }
//        });
//
//    }
//
//    private void setCountTag(TextView view, String count) {
//        if (count.equals("0")) {
//            view.setVisibility(View.GONE);
//        } else if (count.length() == 1) {
//            view.setVisibility(View.VISIBLE);
//            view.setText(count);
//            view.setBackgroundResource(R.drawable.shape_message_circle_tag);
//        } else if (count.length() == 2) {
//            view.setVisibility(View.VISIBLE);
//            view.setText(count);
//            view.setBackgroundResource(R.drawable.shape_message_tag);
//        } else {
//            view.setVisibility(View.VISIBLE);
//            view.setText("...");
//            view.setBackgroundResource(R.drawable.shape_message_tag);
//        }
//    }
//}
