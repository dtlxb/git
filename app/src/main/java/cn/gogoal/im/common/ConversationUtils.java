package cn.gogoal.im.common;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

import java.util.List;

/**
 * author wangjd on 2017/3/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :消息处理.
 */
public class ConversationUtils {

    private static final ConversationUtils ourInstance = new ConversationUtils();

    public static ConversationUtils getInstance() {
        return ourInstance;
    }

    private ConversationUtils() {

    }

    public void test(final int limit, AVIMConversation conversation) {
        conversation.queryMessagesFromCache(limit, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
            }
        });
    }
}
