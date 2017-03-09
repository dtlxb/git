package cn.gogoal.im.activity;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;

/**
 * Created by huangxx on 2017/2/21.
 */

public class SingleChatRoomActivity extends BaseActivity {

    //聊天对象
    private AVIMConversation imConversation;
    private ChatFragment chatFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_imsingle_room;
    }

    @Override
    public void doBusiness(Context mContext) {
        String memberID = this.getIntent().getExtras().getString("member_id");
        String conversation_id = this.getIntent().getExtras().getString("conversation_id");
        setMyTitle(memberID + "聊天窗口", false);

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        //输入聊天对象ID，返回conversation对象
        getSingleConversation(conversation_id);

    }

    public void getSingleConversation(String conversation_id) {

        KLog.e(conversation_id);
        //获取聊天conversation
        AVImClientManager.getInstance().findConversationById(conversation_id, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                chatFragment.setConversation(conversation);
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SingleChatRoomActivity.this, "获取聊天房间失败");
            }
        });
       /* conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {

                if (null == e) {
                    if (null != list && list.size() > 0) {
                        imConversation = list.get(0);
                        Log.e("LEAN_CLOUD1", "find conversation success" + " : " + imConversation.getConversationId());
                    } else {
                        //HashMap<String, Object> attributes = new HashMap<String, Object>();
                        //attributes.put("customConversationType", 1);
                        avimClient.createConversation(memberList, null, null, false, true, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation avimConversation, AVIMException e) {
                                if (null == e) {
                                    imConversation = avimConversation;
                                    chatFragment.setConversation(imConversation);
                                    Log.e("LEAN_CLOUD2", "find conversation success" + " : " + imConversation.getConversationId());
                                }
                            }
                        });
                    }
                }
            }
        });*/

    }
}
