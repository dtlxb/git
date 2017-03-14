package cn.gogoal.im.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.RelaterVideoData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGAPI;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.PlayerUtils.CountDownTimerView;
import cn.gogoal.im.common.PlayerUtils.PlayerControl;
import cn.gogoal.im.common.PlayerUtils.StatusListener;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.widget.BottomSheetNormalDialog;
import cn.gogoal.im.ui.widget.EditTextDialog;

/**
 * Created by dave.
 * Date: 2017/2/16.
 * Desc: 播放器页面
 */
public class PlayerActivity extends BaseActivity {

    @BindView(R.id.GLViewContainer)
    FrameLayout frameContainer;

    @BindView(R.id.player_recyc)
    RecyclerView recyler_chat;
    @BindView(R.id.linearPlayerChat)
    LinearLayout linearPlayerChat;

    //详情相关控件
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.imgPalyer)
    ImageView imgPalyer;
    @BindView(R.id.textCompany)
    TextView textCompany;
    @BindView(R.id.textMarInter)
    TextView textMarInter;
    @BindView(R.id.textOnlineNumber)
    TextView textOnlineNumber;

    //直播预告展示
    @BindView(R.id.countDownTimer)
    CountDownTimerView countDownTimer;

    public static final int STATUS_START = 1;
    public static final int STATUS_STOP = 2;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_RESUME = 4;

    public static final int CMD_START = 1;
    public static final int CMD_STOP = 2;
    public static final int CMD_PAUSE = 3;
    public static final int CMD_RESUME = 4;
    public static final int CMD_VOLUME = 5;
    public static final int CMD_SEEK = 6;

    public static final int TEST = 0;

    private boolean isLastWifiConnected = false;

    //媒体播放器功能实现类
    private AliVcMediaPlayer mPlayer = null;
    private SurfaceHolder mSurfaceHolder = null;
    private SurfaceView mSurfaceView = null;
    private GestureDetector mGestureDetector;

    private StatusListener mStatusListener = null;

    private PlayerControl mPlayerControl = null;

    private PowerManager.WakeLock mWakeLock = null;

    // 标记播放器是否已经停止
    private boolean isStopPlayer = false;
    // 标记播放器是否已经暂停
    private boolean isPausePlayer = false;
    private boolean isPausedByUser = false;
    //用来控制应用前后台切换的逻辑
    private boolean isCurrentRunningForeground = true;

    //视频播放位置
    private int mPosition = 0;
    private int mVolumn = 50;
    private List<AVIMMessage> messageList = new ArrayList<>();
    private LiveChatAdapter mLiveChatAdapter;

    private String mURI = "rtmp://192.168.52.143:1935/hls/androidtest";

    //聊天对象
    private AVIMConversation imConversation;

    private String live_id;
    private String source;

    //直播介绍
    private JSONObject anchor;

    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CMD_PAUSE:
                    pause();
                    break;
                case CMD_RESUME:
                    start();
                    break;
                case CMD_SEEK:
                    mPlayer.seekTo(msg.arg1);
                    break;
                case CMD_START:
                    startToPlay();
                    break;
                case CMD_STOP:
                    stop();
                    break;
                case CMD_VOLUME:
                    mPlayer.setVolume(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_player;
    }

    @Override
    public void doBusiness(Context mContext) {
        setImmersive(true);

        live_id = getIntent().getStringExtra("live_id");
        source = getIntent().getStringExtra("source");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);

        if (TEST == 1) {
            mPlayerControl = new PlayerControl(this);
            mPlayerControl.setControllerListener(mController);
        }

        acquireWakeLock();

        initSurface();

        initRecycleView(recyler_chat, null);
        //String conversationID = this.getIntent().getExtras().getString("conversation_id");
        String conversationID = AppConst.LEAN_CLOUD_CONVERSATION_ID;

        getSquareConversation(conversationID);

        mLiveChatAdapter = new LiveChatAdapter(PlayerActivity.this, R.layout.item_live_chat, messageList);
        recyler_chat.setAdapter(mLiveChatAdapter);

        if (source.equals("live")) {
            linearPlayerChat.setVisibility(View.VISIBLE);
        } else if (source.equals("video")) {
            linearPlayerChat.setVisibility(View.GONE);
        }

        getPlayerInfo();

        getRelaterVideoInfo();
    }

    /*
    * 获取直播详情
    * */
    private void getPlayerInfo() {

        Map<String, String> param = new HashMap<>();

        if (source.equals("live")) {
            param.put("live_id", live_id);
        } else if (source.equals("video")) {
            param.put("video_id", live_id);
        }

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    //直播详情
                    textTitle.setText(data.getString("video_name")); //直播名称
                    ImageDisplay.loadCircleNetImage(getContext(), data.getString("introduction_img"), imgPalyer);
                    textCompany.setText(data.getString("朝阳永续"));
                    textMarInter.setText(data.getString("programme_name"));
                    textOnlineNumber.setText(data.getString("play_base") + "人在线");
                    //主播介绍
                    anchor = data.getJSONObject("anchor");
                    //倒计时
                    if (data.getLongValue("launch_time") > 0) {
                        countDownTimer.setVisibility(View.VISIBLE);
                        countDownTimer.addTime(data.getString("live_time_start"));
                        countDownTimer.start();
                    } else {
                        countDownTimer.setVisibility(View.GONE);
                    }

                    mURI = data.getString("url_rtmp");

                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        if (source.equals("live")) {
            new GGOKHTTP(param, GGOKHTTP.GET_STUDIO_LIST, ggHttpInterface).startGet();
        } else if (source.equals("video")) {
            new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
        }
    }

    private void getSquareConversation(String conversationId) {
        AVIMConversationQuery conversationQuery = AVImClientManager.getInstance().getClient().getQuery();
        // 根据room_id查找房间
        conversationQuery.whereEqualTo("objectId", conversationId);

        Log.e("LEAN_CLOUD", "查找聊天室" + conversationId);

        // 查找聊天
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {

                if (null == e) {
                    // 查询列表取第一个
                    if (null != list && list.size() > 0) {

                        imConversation = list.get(0);
                        joinSquare(imConversation);
                        Log.e("LEAN_CLOUD", "search chatroom success");
                    } else {
                        Log.e("LEAN_CLOUD", "search chatroom fail ");
                    }
                } else {
                    UIHelper.toastInCenter(PlayerActivity.this, "当前聊天房间不存在");
                    Log.e("LEAN_CLOUD", "查询条件没有查找到聊天对象" + e.toString());
                }
            }
        });

    }

    /**
     * 加入聊天室
     */
    private void joinSquare(AVIMConversation conversation) {
        conversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {

                }
            }
        });
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        if (null != imConversation && null != baseMessage) {
            Map<String, Object> map = baseMessage.getOthers();
            AVIMMessage message = (AVIMMessage) map.get("message");
            AVIMConversation conversation = (AVIMConversation) map.get("conversation");

            //判断房间一致然后做消息接收处理
            if (imConversation.getConversationId().equals(conversation.getConversationId())) {
                messageList.add(message);
                mLiveChatAdapter.notifyDataSetChanged();
                recyler_chat.smoothScrollToPosition(messageList.size());
            }
        }
    }

    /**
     * 重点:发生从wifi切换到4g时,提示用户是否需要继续播放,此处有两种做法:
     * 1.从历史位置从新播放
     * 2.暂停播放,因为存在网络切换,续播有时会不成功
     */
    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            KLog.json("mobile " + mobNetInfo.isConnected() + " wifi " + wifiNetInfo.isConnected());

            if (!isLastWifiConnected && wifiNetInfo.isConnected()) {
                isLastWifiConnected = true;
            }
            if (isLastWifiConnected && mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                isLastWifiConnected = false;
                if (mPlayer != null) {
                    mPosition = mPlayer.getCurrentPosition();
                    // 重点:新增接口,此处必须要将之前的surface释放掉
                    mPlayer.releaseVideoSurface();
                    mPlayer.stop();
                    mPlayer.destroy();
                    mPlayer = null;
                }
                setDialog();
            }

        }
    };

    protected void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
        builder.setMessage("确认继续播放吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                initSurface();

            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private PlayerControl.ControllerListener mController = new PlayerControl.ControllerListener() {

        @Override
        public void notifyController(int cmd, int extra) {
            Message msg = Message.obtain();
            switch (cmd) {
                case PlayerControl.CMD_PAUSE:
                    msg.what = CMD_PAUSE;
                    break;
                case PlayerControl.CMD_RESUME:
                    msg.what = CMD_RESUME;
                    break;
                case PlayerControl.CMD_SEEK:
                    msg.what = CMD_SEEK;
                    msg.arg1 = extra;
                    break;
                case PlayerControl.CMD_START:
                    msg.what = CMD_START;
                    break;
                case PlayerControl.CMD_STOP:
                    msg.what = CMD_STOP;
                    break;
                case PlayerControl.CMD_VOLUME:
                    msg.what = CMD_VOLUME;
                    msg.arg1 = extra;
                    break;
                default:
                    break;
            }

            if (TEST != 0) {
                mTimerHandler.sendMessage(msg);
            }
        }
    };

    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pMgr = (PowerManager) this.getSystemService(this.POWER_SERVICE);
            mWakeLock = pMgr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "SmsSyncService.sync() wakelock.");
        }
        mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        mWakeLock.release();
        mWakeLock = null;
    }

    /**
     * 重点:初始化播放器使用的SurfaceView,此处的SurfaceView采用动态添加
     *
     * @return 是否成功
     */
    private boolean initSurface() {
        frameContainer.setBackgroundColor(Color.rgb(0, 0, 0));
        mSurfaceView = new SurfaceView(this);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mSurfaceView.setLayoutParams(params);
        // 为避免重复添加,事先remove子view
        frameContainer.removeAllViews();
        frameContainer.addView(mSurfaceView);

        mSurfaceView.setZOrderOnTop(false);

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            private long mLastDownTimestamp = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastDownTimestamp = System.currentTimeMillis();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mPlayer != null && !mPlayer.isPlaying() && mPlayer.getDuration() > 0) {
                        start();
                        return false;
                    }

                    //just show the progress bar
                    if ((System.currentTimeMillis() - mLastDownTimestamp) > 200) {
                        //mTimerHandler.postDelayed(mUIRunnable, 3000);
                        return true;
                    } else {
                        if (mPlayer != null && mPlayer.getDuration() > 0)
                            pause();
                    }
                    return false;
                }
                return false;
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCB);

        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            final double FLING_MIN_DISTANCE = 0.5;
            final double FLING_MIN_VELOCITY = 0.5;

            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                onVolumeSlide(1);
            }
            if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE
                    && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                onVolumeSlide(-1);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void onVolumeSlide(int vol) {
        if (mPlayer != null) {
            mVolumn += vol;
            if (mVolumn > 100)
                mVolumn = 100;
            if (mVolumn < 0)
                mVolumn = 0;
            mPlayer.setVolume(mVolumn);
        }
    }

    private SurfaceHolder.Callback mSurfaceHolderCB = new SurfaceHolder.Callback() {
        @SuppressWarnings("deprecation")
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);
            KLog.json("AlivcPlayer onSurfaceCreated.");

            // 重点:
            if (mPlayer != null) {
                // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                mPlayer.setVideoSurface(mSurfaceView.getHolder().getSurface());
            } else {
                // 创建并启动播放器
                startToPlay();
            }

            if (mPlayerControl != null)
                mPlayerControl.start();
            KLog.json("AlivcPlayeron SurfaceCreated over.");
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            KLog.json("onSurfaceChanged is valid ? " + holder.getSurface().isValid());
            if (mPlayer != null)
                mPlayer.setSurfaceChanged();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.json("onSurfaceDestroy.");

            if (mPlayer != null) {
                mPlayer.releaseVideoSurface();
            }
        }
    };

    private boolean startToPlay() {
        KLog.json("start play.");

        if (mPlayer == null) {
            // 初始化播放器
            mPlayer = new AliVcMediaPlayer(this, mSurfaceView);
            mPlayer.setMediaType(MediaPlayer.MediaType.Live); //媒体类型 Live 表示直播；Vod 表示点播
            mPlayer.setPreparedListener(new VideoPreparedListener());
            mPlayer.setErrorListener(new VideoErrorListener());
            mPlayer.setInfoListener(new VideoInfolistener());
            mPlayer.setSeekCompleteListener(new VideoSeekCompletelistener());
            mPlayer.setCompletedListener(new VideoCompletelistener());
            mPlayer.setVideoSizeChangeListener(new VideoSizeChangelistener());
            mPlayer.setBufferingUpdateListener(new VideoBufferUpdatelistener());
            mPlayer.setStopedListener(new VideoStoppedListener());
            // 如果同时支持软解和硬解是有用
            mPlayer.setDefaultDecoder(1);

            // 重点: 在调试阶段可以使用以下方法打开native log
            mPlayer.enableNativeLog();

            if (mPosition != 0) {
                mPlayer.seekTo(mPosition);
            }
        }

        //添加播放器地址
        mPlayer.prepareAndPlay(mURI);

        if (mStatusListener != null)
            mStatusListener.notifyStatus(STATUS_START);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //mDecoderTypeView.setText(NDKCallback.getDecoderType() == 0 ? "HardDeCoder" : "SoftDecoder");
            }
        }, 5000);
        return true;

    }

    /**
     * 准备完成监听器:调度更新进度
     */
    private class VideoPreparedListener implements MediaPlayer.MediaPlayerPreparedListener {

        @Override
        public void onPrepared() {
            KLog.json("onPrepared");
            if (mPlayer != null) {
                mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mTimerHandler.postDelayed(mRunnable, 1000);
                mTimerHandler.postDelayed(mUIRunnable, 3000);
            }
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null && mPlayer.isPlaying())
                mTimerHandler.postDelayed(this, 1000);
        }
    };

    Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    /**
     * 错误处理监听器
     */
    private class VideoErrorListener implements MediaPlayer.MediaPlayerErrorListener {

        public void onError(int what, int extra) {

            if (mPlayer == null) {
                return;
            }

            switch (what) {
                case MediaPlayer.ALIVC_ERR_LOADING_TIMEOUT: //缓冲超时
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_INPUTFILE: //未设置视频源
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_VIEW: //无效的surface
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_INVALID_INPUTFILE: //无效的视频源
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_SUPPORT_CODEC: //无支持的解码器
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_FUNCTION_DENIED: //操作无权限
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_UNKNOWN: //未知错误
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_NETWORK: //网络不可用
                    mPlayer.reset();
                    break;
                case MediaPlayer.ALIVC_ERR_ILLEGALSTATUS: //非法状态
                    break;
                case MediaPlayer.ALIVC_ERR_NOTAUTH: //未鉴权
                    break;
                case MediaPlayer.ALIVC_ERR_READD: //视频源访问失败
                    mPlayer.reset();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 信息通知监听器:重点是缓存开始/结束
     */
    private class VideoInfolistener implements MediaPlayer.MediaPlayerInfoListener {

        public void onInfo(int what, int extra) {
            KLog.json("onInfo what = " + what + " extra = " + extra);

            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOW: //未知信息
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: //当开始缓冲时，收到该信息
                    //pause();
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: //缓冲结束时收到该信息
                    //start();
                    break;
                case MediaPlayer.MEDIA_INFO_TRACKING_LAGGING: //
                    break;
                case MediaPlayer.MEDIA_INFO_NETWORK_ERROR: //
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //首帧显示时间
                    if (mPlayer != null)
                        KLog.json("on Info first render start : "
                                + ((long) mPlayer.getPropertyDouble(AliVcMediaPlayer.FFP_PROP_DOUBLE_1st_VFRAME_SHOW_TIME, -1)
                                - (long) mPlayer.getPropertyDouble(AliVcMediaPlayer.FFP_PROP_DOUBLE_OPEN_STREAM_TIME, -1)));
                    break;
            }
        }
    }

    /**
     * 快进完成监听器
     */
    private class VideoSeekCompletelistener implements MediaPlayer.MediaPlayerSeekCompleteListener {

        public void onSeekCompleted() {
        }
    }

    /**
     * 视频播完监听器
     */
    private class VideoCompletelistener implements MediaPlayer.MediaPlayerCompletedListener {

        public void onCompleted() {
            KLog.json("onCompleted.");

            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
            builder.setMessage("播放结束");
            builder.setTitle("提示");
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        }
    }

    /**
     * 视频大小变化监听器
     */
    private class VideoSizeChangelistener implements MediaPlayer.MediaPlayerVideoSizeChangeListener {

        public void onVideoSizeChange(int width, int height) {
            KLog.json("onVideoSizeChange width = " + width + " height = " + height);
        }
    }

    /**
     * 视频缓存变化监听器: percent 为 0~100之间的数字】
     */
    private class VideoBufferUpdatelistener implements MediaPlayer.MediaPlayerBufferingUpdateListener {

        public void onBufferingUpdateListener(int percent) {

        }
    }

    private class VideoStoppedListener implements MediaPlayer.MediaPlayerStopedListener {
        @Override
        public void onStopped() {
            KLog.json("onVideoStopped.");
        }
    }

    //start the video
    private void start() {
        if (mPlayer != null) {
            isPausePlayer = false;
            isPausedByUser = false;
            isStopPlayer = false;
            mPlayer.play();
            if (mStatusListener != null) {
                mStatusListener.notifyStatus(STATUS_RESUME);
            }
        }
    }

    //pause the video
    private void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            isPausePlayer = true;
            isPausedByUser = true;
            if (mStatusListener != null) {
                mStatusListener.notifyStatus(STATUS_PAUSE);
            }
        }
    }

    //stop the video
    private void stop() {
        KLog.json("AudioRender: stop play");
        if (mPlayer != null) {
            mPlayer.stop();
            if (mStatusListener != null)
                mStatusListener.notifyStatus(STATUS_STOP);
            mPlayer.destroy();
            mPlayer = null;
        }
    }

    public void setStatusListener(StatusListener listener) {
        mStatusListener = listener;
    }

    @Override
    protected void onDestroy() {
        KLog.json("AudioRender: onDestroy.");
        if (mPlayer != null) {
            mTimerHandler.removeCallbacks(mRunnable);
        }

        releaseWakeLock();

        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }

        // 重点:在 activity destroy的时候,要停止播放器并释放播放器
        if (mPlayer != null) {
            mPosition = mPlayer.getCurrentPosition();
            stop();
            if (mPlayerControl != null) {
                mPlayerControl.stop();
            }
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        KLog.json("onResume");

        // 重点:如果播放器是从锁屏/后台切换到前台,那么调用player.stat
        if (mPlayer != null && !isStopPlayer && isPausePlayer) {
            if (!isPausedByUser) {
                isPausePlayer = false;
                mPlayer.play();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        KLog.json("onStart.");

        if (!isCurrentRunningForeground) {
            KLog.json(">>>>>>>>>>>>>>>>>>>切到前台 activity process");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        KLog.json("onPause." + isStopPlayer + " " + isPausePlayer + " " + (mPlayer == null));

        // 重点:播放器没有停止,也没有暂停的时候,在activity的pause的时候也需要pause
        if (!isStopPlayer && !isPausePlayer && mPlayer != null) {
            KLog.json("onPause mpayer.");
            mPlayer.pause();
            isPausePlayer = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        KLog.json("onStop.");

        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            KLog.json(">>>>>>>>>>>>>>>>>>>切到后台 activity process");
        }
    }

    // 重点:判定是否在前台工作
    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    KLog.json("EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        KLog.json("EntryActivity isRunningBackGround");
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isStopPlayer = true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.imgPlayerChat, R.id.imgPlayerProfiles, R.id.imgPlayerRelaterVideo, R.id.imgPlayerShare,
            R.id.imgPlayerShotCut, R.id.imgPlayerClose})
    public void setClickFunctionBar(View v) {
        switch (v.getId()) {
            case R.id.imgPlayerChat: //发消息
                showPlayerChat();
                break;
            case R.id.imgPlayerProfiles: //主播介绍
                showAnchorProfiles();
                break;
            case R.id.imgPlayerRelaterVideo: //相关视频
                showRelaterVideo();
                break;
            case R.id.imgPlayerShare: //分享
                DialogHelp.showShareDialog(getContext(), GGAPI.WEB_URL + "/live/share/" + live_id, "http://g1.dfcfw.com/g2/201702/20170216133526.png", "分享", "第一次分享");
                break;
            case R.id.imgPlayerShotCut:
                break;
            case R.id.imgPlayerClose:
                finish();
                break;
        }
    }

    private void showPlayerChat() {
        final EditTextDialog dialog = new EditTextDialog();
        dialog.show(getContext().getSupportFragmentManager());

        dialog.setOnSendButtonClick(new EditTextDialog.OnSendMessageListener() {
            @Override
            public void doSend(View view, final EditText player_edit) {

                if (null != imConversation) {
                    HashMap<String, Object> attrsMap = new HashMap<String, Object>();
                    attrsMap.put("username", AppConst.LEAN_CLOUD_TOKEN);
                    final AVIMTextMessage msg = new AVIMTextMessage();
                    msg.setText(player_edit.getText().toString());
                    msg.setAttrs(attrsMap);

                    messageList.add(msg);
                    mLiveChatAdapter.notifyDataSetChanged();
                    recyler_chat.smoothScrollToPosition(messageList.size());

                    imConversation.sendMessage(msg, new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            dialog.dismiss();
                            AppDevice.hideSoftKeyboard(player_edit);
                            player_edit.setText("");
                        }
                    });
                }
            }
        });
    }

    private void showAnchorProfiles() {

        DialogHelp.getBottomSheelNormalDialog(getContext(), R.layout.dialog_anchor_introduction, new BottomSheetNormalDialog.ViewListener() {
            @Override
            public void bindDialogView(BottomSheetNormalDialog dialog, View dialogView) {
                ImageView anchor_avatar = (ImageView) dialogView.findViewById(R.id.anchor_avatar);
                TextView anchor_name = (TextView) dialogView.findViewById(R.id.anchor_name);
                TextView anchor_position = (TextView) dialogView.findViewById(R.id.anchor_position);
                TextView anchor_achieve = (TextView) dialogView.findViewById(R.id.anchor_achieve);
                TextView anchor_intro = (TextView) dialogView.findViewById(R.id.anchor_intro);

                if (anchor != null) {
                    ImageDisplay.loadNetImage(getContext(), anchor.getString("face_url"), anchor_avatar);
                    anchor_name.setText(anchor.getString("anchor_name"));
                    anchor_position.setText(anchor.getString("organization"));
                    anchor_achieve.setText(anchor.getString("anchor_position"));
                    anchor_intro.setText(anchor.getString("anchor_introduction"));
                }
            }
        });

    }

    private void showRelaterVideo() {

        DialogHelp.getBottomSheelNormalDialog(getContext(), R.layout.dialog_relater_video, new BottomSheetNormalDialog.ViewListener() {
            @Override
            public void bindDialogView(BottomSheetNormalDialog dialog, View dialogView) {
                RecyclerView recy_relater = (RecyclerView) dialogView.findViewById(R.id.recy_relater);
                initRecycleView(recy_relater, null);

                String data = SPTools.getString("data", null);

                KLog.json(data);

                List<RelaterVideoData> videoDatas = JSONObject.parseArray(data, RelaterVideoData.class);

                recy_relater.setAdapter(new RelaterVideoAdapter(getContext(), videoDatas));
            }
        });
    }

    /*
    * 获取直播相关视频
    * */
    private void getRelaterVideoInfo() {

        Map<String, String> param = new HashMap<>();
        param.put("video_id", live_id);

        if (source.equals("live")) {
            param.put("video_type", "1");
        } else if (source.equals("video")) {
            param.put("video_type", "2");
        }

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    SPTools.saveString("data", String.valueOf(object.getJSONArray("data")));
                } else if (object.getIntValue("code") == 1001) {
                    UIHelper.toast(getContext(), R.string.nodata_hint);
                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RELATED_VIDEO, ggHttpInterface).startGet();
    }

    class RelaterVideoAdapter extends CommonAdapter<RelaterVideoData> {

        public RelaterVideoAdapter(Context context, List<RelaterVideoData> list) {
            super(context, R.layout.item_relater_video, list);
        }

        @Override
        protected void convert(ViewHolder holder, final RelaterVideoData data, int position) {

            holder.setAlpha(R.id.text_playback, (float) 0.5);
            if (data.getType() == 1) {
                holder.setVisible(R.id.relative_player, true);
            } else {
                holder.setVisible(R.id.relative_player, false);
            }
            ImageView relater_img = holder.getView(R.id.relater_img);
            ImageDisplay.loadNetImage(getContext(), data.getVideo_img_url(), relater_img);
            holder.setText(R.id.relater_tittle, data.getVideo_name());
            holder.setText(R.id.relater_play_count, data.getPlay_base() + "次");
            ImageView relater_avatar = holder.getView(R.id.relater_avatar);
            ImageDisplay.loadCircleNetImage(getContext(), data.getFace_url(), relater_avatar);
            holder.setText(R.id.relater_name, data.getAnchor_name());
            holder.setText(R.id.relater_content, data.getProgramme_name());
        }
    }

    class LiveChatAdapter extends CommonAdapter<AVIMMessage> {

        public LiveChatAdapter(Context context, int layoutId, List<AVIMMessage> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, AVIMMessage message, int position) {
            AVIMTextMessage msg = (AVIMTextMessage) message;
            String username = msg.getAttrs().get("username") + ": ";

            TextView textSend = holder.getView(R.id.text_you_send);
            textSend.setText(username + msg.getText());

            SpannableStringBuilder builder = new SpannableStringBuilder(textSend.getText().toString());
            ForegroundColorSpan Span1 = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.live_chat_level1));
            ForegroundColorSpan Span2 = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.textColor_333333));
            builder.setSpan(Span1, 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(Span2, username.length(), textSend.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textSend.setText(builder);
        }
    }
}
