package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/2/20.;
 */

public class SquareChatRoomActivity extends BaseActivity {

    //聊天对象
    private ChatFragment chatFragment;
    XTitle xTitle;
    private List<String> groupMembers;
    private String squareName;
    private String squareCreater;
    private String headAvatar;

    @Override
    public int bindLayout() {
        return R.layout.activity_imdemo;
    }

    @Override
    public void doBusiness(Context mContext) {
        squareName = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("squareName"));
        groupMembers = new ArrayList<>();
        final String conversationID = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("conversation_id"));
        boolean need_update = this.getIntent().getBooleanExtra("need_update", false);
        int actionType = this.getIntent().getIntExtra("square_action", 0);
        List<AVIMMessage> messageChooesed = this.getIntent().getParcelableArrayListExtra("messages_chooesed");

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        if (!squareName.equals("")) {
            initTitle(squareName, conversationID);
        }
        getSquareConversation(conversationID, need_update, actionType, messageChooesed);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTitle(final String squareName, final String conversation_id) {
        xTitle = setMyTitle(squareName, true);
        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(SquareChatRoomActivity.this, R.mipmap.chat_person)) {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(SquareChatRoomActivity.this, IMSquareChatSetActivity.class);
                intent.putStringArrayListExtra("group_members", (ArrayList<String>) groupMembers);
                intent.putExtra("conversation_id", conversation_id);
                intent.putExtra("head_avatar", headAvatar);
                intent.putExtra("square_creater", squareCreater);
                intent.putExtra("squareName", squareName);
                startActivity(intent);
            }
        };
        xTitle.addAction(personAction, 0);
    }

    private void getSquareConversation(String conversationId, final boolean need_update, final int actionType, final List<AVIMMessage> messageList) {
        AVIMClientManager.getInstance().findConversationById(conversationId, new AVIMClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
                chatFragment.setConversation(conversation, need_update, actionType, null, messageList);
                groupMembers.addAll(conversation.getMembers());
                if (squareName.equals("")) {
                    initTitle(conversation.getName(), conversation.getConversationId());
                }
                headAvatar = (String) conversation.getAttribute("avatar");
                squareCreater = conversation.getCreator();
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SquareChatRoomActivity.this, error);
            }
        });

    }

    @Subscriber(tag = "correct_square_name")
    private void correctName(String msg) {
        //群改名字
        if (!TextUtils.isEmpty(msg)) {
            xTitle.setTitle(msg);
        }
    }

}
