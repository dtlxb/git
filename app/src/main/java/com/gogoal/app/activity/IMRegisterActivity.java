package com.gogoal.app.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.IMHelpers.AVImClientManager;

import butterknife.BindView;

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
            Toast.makeText(IMRegisterActivity.this, "昵称不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        chat_room_login.setEnabled(false);
        login_et_username.setEnabled(false);
        //连接服务器
        AVImClientManager.getInstance().open(MyId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                chat_room_login.setEnabled(true);
                login_et_username.setEnabled(true);

                Intent intent = new Intent(IMRegisterActivity.this, SingleChatRoomActivity.class);
                intent.putExtra("conversation_id", "58aaa02d8d6d8100636e8be9");
                intent.putExtra("userName", login_et_username.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });
    }
}
