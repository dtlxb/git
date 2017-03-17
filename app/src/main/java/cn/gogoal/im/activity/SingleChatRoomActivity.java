package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/2/21.
 */

public class SingleChatRoomActivity extends BaseActivity {

    //聊天对象
    private ChatFragment chatFragment;

    @Override
    public int bindLayout() {
        return R.layout.activity_imsingle_room;
    }

    @Override
    public void doBusiness(Context mContext) {
        String conversation_id = (String) StringUtils.objectNullDeal(this.getIntent().getExtras().getString("conversation_id"), "");
        String nickname = (String) StringUtils.objectNullDeal(this.getIntent().getExtras().getString("nickname"), "");
        initTitle(nickname);

        initAction(conversation_id);
        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);

        //输入聊天对象ID，返回conversation对象
        getSingleConversation(conversation_id);

    }

    private void initTitle(String nickname) {
        XTitle title = setMyTitle(nickname + "聊天窗口", true);
        title.addAction(new XTitle.ImageAction(getResDrawable(R.mipmap.contact_person)) {
            @Override
            public void actionClick(View view) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });
    }

    private void initAction(final String conversation_id) {
        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(SingleChatRoomActivity.this, R.mipmap.chat_person)) {
            @Override
            public void actionClick(View view) {
                Intent intent=new Intent(SingleChatRoomActivity.this,IMPersonActivity.class);
                intent.putExtra("conversation_id",conversation_id);
                startActivity(intent);
            }
        };
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
                UIHelper.toast(SingleChatRoomActivity.this, error);
            }
        });
    }
}
