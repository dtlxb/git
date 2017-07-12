package cn.gogoal.im.servise;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
<<<<<<< HEAD
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
=======
>>>>>>> af8a20b39e1f5f215d9331b9628ed189f167b346

import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.IMMessageBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.IMHelpers.GGSystemMessage;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/19.
 */

public class MessageSaveService extends Service {
    private List<IMMessageBean> IMMessageBeans;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);

        IMMessageBeans = new ArrayList<>();
        IMMessageBeans.clear();
        //查找到消息列表按时间排序
        IMMessageBeans.addAll(DataSupport.findAll(IMMessageBean.class));
        return super.onStartCommand(intent, START_FLAG_REDELIVERY, startId);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 消息查看一次更改未读数
     */
    @Subscriber(tag = "Cache_change")
    public void changeCache(String code) {
        IMMessageBeans.clear();
        IMMessageBeans.addAll(DataSupport.findAll(IMMessageBean.class));
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        Map map = baseMessage.getOthers();
        AVIMMessage message = (AVIMMessage) map.get("message");
        AVIMConversation conversation = (AVIMConversation) map.get("conversation");
        boolean isTheSame = false;
        final String ConversationId = conversation.getConversationId();

        int chatType = (int) conversation.getAttribute("chat_type");
        //获取免打扰
        boolean noBother = false;
        if (conversation.get("mu") != null) {
            List<String> muList = (List<String>) conversation.get("mu");
            noBother = muList.contains(UserUtils.getMyAccountId());
        }
        SPTools.saveBoolean(UserUtils.getMyAccountId() + ConversationId + "noBother", noBother);
        //股票消息不缓存
        if (chatType != AppConst.IM_CHAT_TYPE_STOCK_SQUARE) {
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
                    GGSystemMessage systemMessage = (GGSystemMessage) message;
                    JSONArray accountArray = (JSONArray) systemMessage.getAttrs().get("accountList");
                    String _lctext = MessageListUtils.findSquarePeople(accountArray, String.valueOf(systemMessage.getMessageType()));
                    systemMessage.setText(_lctext);
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
                //单聊,群聊,股票群聊,加好友请求
                case AppConst.IM_CHAT_TYPE_SINGLE:
                    avatar = lcattrsObject.getString("avatar");
                    break;
                case AppConst.IM_CHAT_TYPE_SQUARE:
                case AppConst.IM_CHAT_TYPE_STOCK_SQUARE:
                    nickName = conversation.getName();
                    avatar = (String) conversation.getAttribute("avatar");
                    break;
                case AppConst.IM_CHAT_TYPE_SYSTEM:
                    break;
                //直播
                case AppConst.IM_CHAT_TYPE_LIVE:
                    break;
                //操纵通讯录
                case AppConst.IM_CHAT_TYPE_CONTACTS_ACTION:
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
                    IMMessageBeans.get(i).setMute(noBother);
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
                imMessageBean.setMute(noBother);
                imMessageBean.setChatType(chatType);
                unreadMessage = 1;
                imMessageBean.setUnReadCounts(unreadMessage + "");
                IMMessageBeans.add(imMessageBean);
            }

            //保存
            IMMessageBean imMessageBean = new IMMessageBean(ConversationId, chatType, message.getTimestamp(),
                    String.valueOf(unreadMessage), nickName, friend_id, avatar, JSON.toJSONString(message), noBother);
            MessageListUtils.saveMessageInfo(imMessageBean);
        }

    }
}
