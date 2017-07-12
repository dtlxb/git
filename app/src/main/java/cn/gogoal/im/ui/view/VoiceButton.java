package cn.gogoal.im.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.permission.IPermissionListner;
import cn.gogoal.im.common.recording.AudioManager;
import cn.gogoal.im.common.recording.DialogManager;


public class VoiceButton extends AppCompatButton implements AudioManager.AudioStateListener {

    private static final int DISTANCE_Y_CANCEL = 50; //正常开发情况下,应该使用dp,然后将dp转换为px

    private static final int STATE_NORMAL = 1; //默认状态

    private static final int STATE_RECORDING = 2; //正在录音

    private static final int STATE_WANT_TO_CANCEL = 3; //希望取消

    private static final int MSG_AUDIO_PREPARED = 0X110;

    private static final int MSG_VOICE_CHANGED = 0X111;

    private static final int MSG_DIALOG_DIMISS = 0X112;

    private static final int MSG_AUDIO_STOP = 0X113; //时间过长取消录音

    private int mCurState = STATE_NORMAL; //当前的状态

    //已经开始录音
    private boolean isRecording = false; //是否已经开始录音

    private DialogManager mDialogManger;

    private AudioManager mAudioManger;

    private float mTime;

    //是否触发longClick方法
    private boolean mReady;

    public VoiceButton(Context context) {
        this(context, null);
    }

    public VoiceButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceButton(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDialogManger = new DialogManager(context);
        //TODO 判断SD卡是否存在
        if (!AppDevice.isSdcardReady()) {
            UIHelper.toast(context, "设备SD卡出错");
            return;
        }
        String dir;
        File filesDir = context.getExternalFilesDir("recorder");
        if (filesDir != null && filesDir.exists()) {
            dir = filesDir.getPath();
        } else {//程序母文件夹如果不存在，比如被用户强制删除
            dir = Environment.getExternalStorageDirectory().getPath()
                    + "Android" + File.separator + "data" + File.separator
                    + context.getPackageName() + File.separator
                    + "file" + File.separator + "recorder";
            File dirFile = new File(dir);

            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        mAudioManger = AudioManager.getInstance(dir);
        mAudioManger.setOnAudioStateListener(this);

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady = true;
                try {
                    mAudioManger.prepareAudio();
                } catch (Exception e) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED) {
                        UIHelper.toast(getContext(), "设备音频硬件模块出现故障!", Toast.LENGTH_LONG);
                    } else {
                        UIHelper.toast(getContext(), "请允许App使用设备的录音权限，这在语音聊天时需要!", Toast.LENGTH_LONG);
                    }
                }
                return false;
            }
        });
    }

    private AudioFinishRecorderListener mListener;

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取音量大小的Runnable
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    if (mTime < 60.0f) {
                        Thread.sleep(100);
                        mTime += 0.1f;
                        handler.sendEmptyMessage(MSG_VOICE_CHANGED);
                    } else {
                        handler.sendEmptyMessage(MSG_AUDIO_STOP);
                        isRecording = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    //显示应该在audio end prepared以后
                    mDialogManger.showRecordingDialog();
                    isRecording = true;

                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManger.updateVoiceLevel(mAudioManger.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManger.dimissDialog();
                    break;
                case MSG_AUDIO_STOP:
                    mDialogManger.showError(1);
                    mAudioManger.release();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                    if (mListener != null) {
                        mListener.onFinish(mTime, mAudioManger.getCurrentFilePath());
                    }
                    reset();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.setBackgroundResource(R.drawable.voice_button_onvoice);
                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new IPermissionListner() {
                    @Override
                    public void onUserAuthorize() {
                        changeState(STATE_RECORDING);
                        VoiceButton.this.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                    }

                    @Override
                    public void onRefusedAuthorize(List<String> deniedPermissions) {
                        UIHelper.toast(getContext(), "录音权限被拒绝,无法使用");
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((Activity) getContext()).finish();
                            }
                        }, 1000);
                    }
                });

                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    //根据x,y的坐标,判断是否取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                this.setBackgroundResource(R.drawable.voice_button_null);
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || mTime < 0.6f) {
                    mDialogManger.showError(0);
                    mAudioManger.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                } else if (mCurState == STATE_RECORDING) { //正常录制的时候结束
                    mDialogManger.dimissDialog();
                    //release
                    mAudioManger.release();
                    //callbackToAct
                    if (mListener != null) {
                        mListener.onFinish(mTime, mAudioManger.getCurrentFilePath());
                    }
                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    mDialogManger.dimissDialog();
                    //cancel
                    mAudioManger.cancel();
                }
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
        //超过按钮的宽度
        if (x < 0 || x > getWidth()) {
            return true;
        }
        //超过按钮的高度
        return y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL;
    }

    /**
     * 改变Button的文本以及弹窗
     */
    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL:
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManger.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManger.wantToCancel();
                    break;
            }
        }
    }
}
