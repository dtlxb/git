package com.gogoal.app.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gogoal.app.R;
import com.gogoal.app.adapter.recycleviewAdapterHelper.CommonAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.base.ViewHolder;
import com.gogoal.app.base.BaseActivity;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.recording.MediaManager;
import com.gogoal.app.common.recording.Recorder;
import com.gogoal.app.ui.view.SwitchImageView;
import com.gogoal.app.ui.view.VoiceButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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
    SwitchImageView imgVoice;

    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.img_emoji)
    ImageButton imgEmoji;

    @BindView(R.id.img_function)
    ImageButton imgFunction;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.voiveView)
    VoiceButton voiceView;

    private AudioAdapter mAdapter;
    private List<Recorder> mDatas = new ArrayList<>();
    private View animView;

    private LinearLayoutManager layoutManager;
    @Override
    public int bindLayout() {
        return R.layout.activity_chatroom;
    }

    @Override
    public void doBusiness(Context mContext) {

        imgVoice.setOnSwitchListener(new SwitchImageView.OnSwitchListener() {
            @Override
            public void onSwitch(View view, int state) {
                switch (state) {
                    case 0:
                        AppDevice.hideSoftKeyboard(etInput);
                        voiceView.setVisibility(View.VISIBLE);
                        etInput.setVisibility(View.GONE);
                        break;
                    case 1:
                        voiceView.setVisibility(View.GONE);
                        etInput.setVisibility(View.VISIBLE);
                        etInput.requestFocus();
                        AppDevice.showSoftKeyboard(etInput);
                        break;
                }
                imgVoice.setImageResource(state == 0 ? R.mipmap.cache_chat_img_voice : R.mipmap.cache_chat_img_keyboard);
            }
        });

        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        voiceView.setAudioFinishRecorderListener(new VoiceButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds,filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();//通知更新的内容
                layoutManager.scrollToPosition(mAdapter.getItemCount()-1);
            }
        });
        mAdapter=new AudioAdapter(mDatas);
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.img_emoji, R.id.img_function, R.id.btn_send})
    void chatClick(View view) {
        switch (view.getId()) {
            case R.id.img_emoji:

                break;
            case R.id.img_function:
                break;
            case R.id.btn_send:
                break;
        }
    }

    @OnTextChanged(R.id.et_input)
    void onTextChanged(CharSequence s) {
        btnSend.setVisibility(s.length()>0?View.VISIBLE:View.INVISIBLE);
        imgFunction.setVisibility(s.length()>0?View.INVISIBLE:View.VISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    class AudioAdapter extends CommonAdapter<Recorder>{

        public AudioAdapter(List<Recorder> datas) {
            super(getContext(), R.layout.item_recorder, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final Recorder recorder, final int position) {
            holder.setText(R.id.recorder_time,Math.round(recorder.getDuration())+"\"");

            View holderLengthView = holder.getView(R.id.recorder_length);

            ViewGroup.LayoutParams params = holderLengthView.getLayoutParams();
            int mMaxItemWidth = (int) (AppDevice.getWidth(getContext()) * 0.7f);
            int mMinItemWidth = (int) (AppDevice.getWidth(getContext()) * 0.16f);
            params.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f)* recorder.getDuration());
            holderLengthView.setLayoutParams(params);

            holderLengthView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(animView != null) {
                        animView.setBackgroundResource(R.drawable.v_anim3);
                        animView = null;
                    }
                    //播放动画
                    animView = view.findViewById(R.id.recorder_anim);
                    animView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable anim = (AnimationDrawable) animView.getBackground();
                    anim.start();
                    //播放音频
                    MediaManager.playSound(recorder.getFilePath(), new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            animView.setBackgroundResource(R.drawable.v_anim3);
                        }
                    });
                }
            });
        }
    }
}
