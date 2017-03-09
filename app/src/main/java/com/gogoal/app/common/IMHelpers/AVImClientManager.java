package com.gogoal.app.common.IMHelpers;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.gogoal.app.common.AppConst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangxx on 17/02/20.
 */
public class AVImClientManager {

    private static AVImClientManager imClientManager;

    public AVIMClient avimClient;               //< 连接server
    private String clientId;                    //< 连接serverID

    private AVImClientManager() {
    }

    public synchronized static AVImClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManager();
        }
        return imClientManager;
    }

    /**
     * 打开连接，返回Client对象
     */
    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

    /**
     * 返回用户的基本信息头像，昵称
     */
    public Map<String, String> userBaseInfo() {
        Map<String, String> attr = new HashMap<>();
        attr.put("username", AppConst.LEAN_CLOUD_TOKEN);
        //attr.put("avatar", UserUtils.getUserAvatar());
        return attr;
    }

    // 查找聊天室
    public void findConversationById(String conversationId, final ChatJoinManager mChatJoinManager) {

        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        // 根据conversationId查找房间
        conversationQuery.whereEqualTo("objectId", conversationId);
        // 查找聊天
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {

                if (null == e) {
                    if (null != list && list.size() > 0) {
                        if (null != mChatJoinManager) {
                            mChatJoinManager.joinSuccess(list.get(0));
                        }

                        Log.e("FIND_CONVERSATION", "查找聊天对象成功");
                    } else {
                        if (null != mChatJoinManager) {
                            mChatJoinManager.joinFail("查找聊天对象失败");
                        }

                        Log.e("FIND_CONVERSATION", "查找聊天对象失败");
                    }
                } else {
                    if (null != mChatJoinManager) {
                        mChatJoinManager.joinFail("查找聊天对象失败");
                    }

                    Log.e("FIND_CONVERSATION", "查询条件没有查找到聊天对象" + e.toString());
                }
            }
        });
    }

    /**
     * 返回Conversation管理类
     */
    public interface ChatJoinManager {

        void joinSuccess(AVIMConversation conversation);   ///< 加入房间成功

        void joinFail(String error);      ///< 加入房间失败
    }
}
