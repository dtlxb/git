package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.UIHelper;

/**
 * Created by huangxx on 2017/2/20.
 */

public class IMRegisterActivity extends BaseActivity {

    @BindView(R.id.login_et_username)
    EditText login_et_username;

    @BindView(R.id.chat_room_login)
    Button chat_room_login;

    @Override
    public int bindLayout() {
        return R.layout.activity_imregister;
    }

    @Override
    public void doBusiness(Context mContext) {
        chat_room_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClient(login_et_username.getText().toString().trim());
            }
        });
    }

    private void openClient(String MyId) {
        if (TextUtils.isEmpty(MyId)) {
            UIHelper.toast(getActivity(),"昵称不能为空");
            return;
        }

        chat_room_login.setEnabled(false);
        login_et_username.setEnabled(false);

        /*AVImClientManager.getInstance().getClient().createConversation(Collections.<String>emptyList(), "HelloKitty PK 加菲猫", null, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                Log.e("+++ConversationId", avimConversation.getConversationId());
                avimConversation.getConversationId();
            }
        });*/

        //单聊页面
        Intent intent = new Intent(IMRegisterActivity.this, SingleChatRoomActivity.class);
        intent.putExtra("friend_id", MyId);
        //66,77
        //intent.putExtra("conversation_id", AppConst.LEAN_CLOUD_CONVERSATION_ID_66_77);
        //66,99
        //intent.putExtra("conversation_id", AppConst.LEAN_CLOUD_CONVERSATION_ID_66_99);


        //群聊
        /*Intent intent = new Intent(IMRegisterActivity.this, SquareChatRoomActivity.class);
        //ntent.putExtra("conversation_id", "58b5452561ff4b006b1e3353");
        intent.putExtra("conversation_id", "58aaa02d8d6d8100636e8be9");
        intent.putExtra("userName", MyId);*/
        startActivity(intent);
        finish();
    }

}
