package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.socks.library.KLog;
<<<<<<< HEAD

=======
>>>>>>> 6cb43ff6966c4c9061f6000ab3110c278b2da703
import java.util.ArrayList;
import java.util.List;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.ChatFragment;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/2/20.
 */

public class SquareChatRoomActivity extends BaseActivity implements ChatFragment.MyListener {

    //聊天对象
    private ChatFragment chatFragment;
    private ContactBean contactBean;
    private List<String> groupMembers;
    private String squareName;

    @Override
    public int bindLayout() {
        return R.layout.activity_imdemo;
    }

    @Override
    public void doBusiness(Context mContext) {
        squareName = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("squareName"));
        if (!squareName.equals("")) {
            setMyTitle(squareName, false);
        }

        groupMembers = new ArrayList<>();
        final String conversationID = (String) StringUtils.objectNullDeal(this.getIntent().getStringExtra("conversation_id"));
        boolean need_update = this.getIntent().getBooleanExtra("need_update", false);

        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        getSquareConversation(conversationID, need_update);

        initTitle(squareName, conversationID);
    }

    private void initTitle(String squareName, final String conversation_id) {
        XTitle title = setMyTitle(squareName + "(" + ")", true);

        //添加action
        XTitle.ImageAction personAction = new XTitle.ImageAction(ContextCompat.getDrawable(SquareChatRoomActivity.this, R.mipmap.chat_person)) {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(SquareChatRoomActivity.this, IMSquareChatSetActivity.class);
                intent.putStringArrayListExtra("group_members", (ArrayList<String>) groupMembers);
                intent.putExtra("conversation_id", conversation_id);
                startActivity(intent);
            }
        };
        title.addAction(personAction, 0);
    }

    private void getSquareConversation(String conversationId, final boolean need_update) {
        AVImClientManager.getInstance().findConversationById(conversationId, new AVImClientManager.ChatJoinManager() {
            @Override
            public void joinSuccess(AVIMConversation conversation) {
//                joinSquare(conversation);
                chatFragment.setConversation(conversation, need_update);
                groupMembers.addAll(conversation.getMembers());
                if (squareName.equals("")) {
                    setMyTitle(conversation.getName(), false);
                }
                KLog.e(conversation.getName() + "");
            }

            @Override
            public void joinFail(String error) {
                UIHelper.toast(SquareChatRoomActivity.this, error);
            }
        });

    }

    /**
     * 加入聊天室
     */
    /*private void joinSquare(final AVIMConversation conversation) {
        conversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    chatFragment.setConversation(conversation);
                }
            }
        });
    }*/
    @Override
    public void setData(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

}
