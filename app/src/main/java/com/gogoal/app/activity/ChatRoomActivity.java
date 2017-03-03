package com.gogoal.app.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.ui.view.VoiceView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author wangjd on 2017/3/2 0002.
 * Staff_id 1375
 * phone 18930640263
 */
public class ChatRoomActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.img_voice)
    ImageButton imgVoice;

    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.img_emoji)
    ImageButton imgEmoji;

    @BindView(R.id.img_function)
    ImageButton btnFunction;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.voiveView)
    VoiceView voiceView;

    private int CHAT_VOICE_IMG = R.mipmap.cache_chat_img_voice;

    private int CHAT_KEYBORD_IMG = R.mipmap.cache_chat_img_keyboard;

    private int chatLeft = CHAT_VOICE_IMG;

    @Override
    public int bindLayout() {
        return R.layout.activity_chatroom;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.img_voice, R.id.img_emoji, R.id.img_function, R.id.btn_send})
    void chatClick(View view) {
        switch (view.getId()) {
            case R.id.img_voice:
                if (chatLeft == CHAT_VOICE_IMG) {
                    chatLeft = CHAT_KEYBORD_IMG;
                    AppDevice.hideSoftKeyboard(etInput);
                    voiceView.setVisibility(View.VISIBLE);
                    etInput.setVisibility(View.GONE);
                } else {
                    chatLeft = CHAT_VOICE_IMG;
                    voiceView.setVisibility(View.GONE);
                    etInput.setVisibility(View.VISIBLE);
                }
                imgVoice.setImageResource(chatLeft);
                break;
            case R.id.img_emoji:
                break;
            case R.id.img_function:
                break;
            case R.id.btn_send:
                break;
        }
    }
}
