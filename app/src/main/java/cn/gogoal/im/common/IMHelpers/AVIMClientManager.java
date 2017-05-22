package cn.gogoal.im.common.IMHelpers;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.common.UserUtils;

import static com.avos.avoscloud.AVQuery.CachePolicy.CACHE_THEN_NETWORK;
import static com.avos.avoscloud.AVQuery.CachePolicy.NETWORK_ONLY;

/**
 * Created by huangxx on 17/02/20.
 */
public class AVIMClientManager {

    private volatile static AVIMClientManager imClientManager;

    public AVIMClient avimClient;               //< 连接server
    private String clientId;                    //< 连接serverID

    private AVIMClientManager() {
    }

    public static AVIMClientManager getInstance() {
        if (null == imClientManager) {
            synchronized (AVIMClientManager.class) {
                if (null == imClientManager) {
                    imClientManager = new AVIMClientManager();
                }
            }
        }
        return imClientManager;
    }

    /**
     * 打开连接，返回Client对象
     */
    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId, clientId);
        avimClient.setMessageQueryCacheEnable(true);
        avimClient.open(callback);
    }

    /**
     * 关闭连接，返回Client对象
     */
    public void close(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId, clientId);
        avimClient.close(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVIMClientManager.open first");
        }
        return clientId;
    }

    //设置互相怼监听
    public static void setEventHandler() {
        getInstance().getClient().setClientEventHandler(new MyClientEventHandler());
    }

    /**
     * 返回用户的基本信息头像，昵称
     */
    public Map<String, String> userBaseInfo() {
        Map<String, String> attr = new HashMap<>();
        attr.put("username", UserUtils.getNickname());
        attr.put("avatar", UserUtils.getUserAvatar());
        return attr;
    }

    // 查找聊天室
    public void findConversationById(String conversationId, final ChatJoinManager mChatJoinManager) {

        if (avimClient == null) {
            return;
        }
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        // 根据conversationId查找房间
        conversationQuery.whereEqualTo("objectId", conversationId);
        //查找策略
        conversationQuery.setQueryPolicy(CACHE_THEN_NETWORK);
        // 查找聊天
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (null == e) {
                    if (null != list && (!list.isEmpty())) {
                        if (null != mChatJoinManager) {
                            mChatJoinManager.joinSuccess(list.get(0));
                        }
                    } else {
                        if (null != mChatJoinManager) {
                            mChatJoinManager.joinFail("没有会话数据");
                        }
                    }
                } else {
                    if (null != mChatJoinManager) {
                        mChatJoinManager.joinFail(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 刷新Conversation
     */
    public void refreshConversation(String conversationId) {
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        // 根据conversationId查找房间
        conversationQuery.whereEqualTo("objectId", conversationId);
        //查找策略
        conversationQuery.setQueryPolicy(NETWORK_ONLY);
        // 查找聊天
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(final List<AVIMConversation> list, AVIMException e) {

                if (null == e) {
                    if (null != list && (!list.isEmpty())) {
                        list.get(0).fetchInfoInBackground(new AVIMConversationCallback() {
                            @Override
                            public void done(AVIMException e) {
                                KLog.e(list.get(0).getMembers());
                            }
                        });
                    }
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
