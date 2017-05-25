package cn.gogoal.im.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.RelaterVideoData;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.PlayerUtils.PlayerControl;
import cn.gogoal.im.common.PlayerUtils.StatusListener;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.linkUtils.PlayDataStatistics;

/**
 * Created by dave.
 * Date: 2017/2/16.
 * Desc: 播放器页面
 */
public class PlayerActivity extends BaseActivity {

    @BindView(R.id.GLViewContainer)
    FrameLayout frameContainer;

    //缓冲控件
    @BindView(R.id.LayoutTip)
    LinearLayout LayoutTip;
    @BindView(R.id.text_tip)
    ImageView text_tip;
    //暂停
    @BindView(R.id.imgPause)
    CheckBox imgPause;
    //进度条
    @BindView(R.id.LayoutProgress)
    LinearLayout LayoutProgress;
    @BindView(R.id.currentDuration)
    TextView currentDuration;
    @BindView(R.id.progress)
    SeekBar mSeekBar;
    @BindView(R.id.totalDuration)
    TextView totalDuration;

    //介绍
    @BindView(R.id.player_line_tab)
    LinearLayout player_line_tab;
    //主播
    @BindView(R.id.player_anchor_tv)
    TextView player_anchor_tv;
    @BindView(R.id.tv_player_anchor)
    TextView tv_player_anchor;

    @BindView(R.id.linearPlayerFun)
    LinearLayout linearPlayerFun;

    //相关视频
    @BindView(R.id.player_video_tv)
    TextView player_video_tv;
    @BindView(R.id.tv_player_video)
    TextView tv_player_video;

    @BindView(R.id.recy_relater)
    RecyclerView recy_relater;
    @BindView(R.id.linearShowError)
    LinearLayout linearShowError;
    //主播介绍
    @BindView(R.id.scrollIntroduce)
    ScrollView scrollIntroduce;
    @BindView(R.id.anchor_avatar)
    RoundedImageView anchor_avatar;
    @BindView(R.id.anchor_name)
    TextView anchor_name;
    @BindView(R.id.anchor_position)
    TextView anchor_position;
    @BindView(R.id.anchor_achieve)
    TextView anchor_achieve;
    @BindView(R.id.live_avatar)
    ImageView live_avatar;
    @BindView(R.id.live_achieve)
    TextView live_achieve;

    private RotateAnimation animation;

    private boolean mEnableUpdateProgress = true;

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

    private String mURI;

    private String live_id;

    private RelaterVideoAdapter adapter;

    private int screenWidth;

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
                    startToPlay(mURI);
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

        screenWidth = AppDevice.getWidth(this);

        live_id = getIntent().getStringExtra("live_id");

        animation = AnimationUtils.getInstance().setLoadingAnime(text_tip, R.mipmap.login_loading);
        animation.startNow();

        PlayDataStatistics.getStatisticalData(getContext(), "2", live_id, "2", "1");

        initRecycleView(recy_relater, null);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);

        if (TEST == 1) {
            mPlayerControl = new PlayerControl(this);
            mPlayerControl.setControllerListener(mController);
        }

        acquireWakeLock();

        initSurface();

        getRelaterVideoInfo();
    }

    /**
     * 获取直播详情
     */
    private void getPlayerInfo() {

        Map<String, String> param = new HashMap<>();
        param.put("video_id", live_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    //主播介绍
                    JSONObject anchor = data.getJSONObject("anchor");

                    //初始化
                    player_anchor_tv.setTextColor(getResColor(R.color.player_add_friend));
                    tv_player_anchor.setVisibility(View.VISIBLE);
                    player_video_tv.setTextColor(getResColor(R.color.textColor_333333));
                    tv_player_video.setVisibility(View.GONE);

                    if (anchor != null) {
                        linearShowError.setVisibility(View.GONE);
                        scrollIntroduce.setVisibility(View.VISIBLE);
                        recy_relater.setVisibility(View.GONE);

                        ImageDisplay.loadCircleImage(getContext(), anchor.getString("face_url"), anchor_avatar);
                        anchor_name.setText(anchor.getString("anchor_name"));
                        anchor_position.setText(anchor.getString("organization") + " | " + anchor.getString("anchor_position"));
                        anchor_achieve.setText(anchor.getString("anchor_introduction"));
                        ImageDisplay.loadImage(getContext(), data.getString("introduction_img"), live_avatar);
                        live_achieve.setText(data.getString("introduction"));
                    } else {
                        linearShowError.setVisibility(View.VISIBLE);
                        scrollIntroduce.setVisibility(View.GONE);
                    }

                    mURI = data.getString("video_file");

                    startToPlay(mURI);

                } else {
                    UIHelper.toast(getContext(), R.string.net_erro_hint);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };

        new GGOKHTTP(param, GGOKHTTP.GET_RECORD_LIST, ggHttpInterface).startGet();
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

    public void setStatusListener(StatusListener listener) {
        mStatusListener = listener;
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
                FrameLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth * 9.0f / 16));
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
                    KLog.e("位置1");

                    if (LayoutProgress.getVisibility() == View.GONE) {
                        show_progress_ui(true);
                        mTimerHandler.postDelayed(mUIRunnable, 5000);
                    } else {
                        show_progress_ui(false);
                        mTimerHandler.removeCallbacks(mUIRunnable);
                    }

                    //just show the progress bar
                    if ((System.currentTimeMillis() - mLastDownTimestamp) > 200) {
                        KLog.e("位置2");
                        return true;
                    } else {
                        KLog.e("位置3");
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
                getPlayerInfo();
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

    private boolean startToPlay(String mURI) {
        KLog.json("start play.");

        resetUI();

        if (mPlayer == null) {
            // 初始化播放器
            mPlayer = new AliVcMediaPlayer(this, mSurfaceView);
            //媒体类型 Live 表示直播；Vod 表示点播
            mPlayer.setMediaType(MediaPlayer.MediaType.Vod);
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
            //mPlayer.enableNativeLog();

            if (mPosition != 0) {
                mPlayer.seekTo(mPosition);
            }
        }

        //添加播放器地址
        mPlayer.prepareAndPlay(mURI);

        KLog.e(mURI);

        if (mStatusListener != null)
            mStatusListener.notifyStatus(STATUS_START);

        /*new Handler().postDelayed(new Runnable() {
            public void run() {
                mDecoderTypeView.setText(NDKCallback.getDecoderType() == 0 ? "HardDeCoder" : "SoftDecoder");
            }
        }, 5000);*/
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
                //VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING  |  VIDEO_SCALING_MODE_SCALE_TO_FIT
                mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                update_total_duration(mPlayer.getDuration());
                mTimerHandler.postDelayed(mRunnable, 1000);
                mTimerHandler.postDelayed(mUIRunnable, 3000);
            }
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null && mPlayer.isPlaying()) {
                update_progress(mPlayer.getCurrentPosition());
            }
            mTimerHandler.postDelayed(this, 1000);

        }
    };

    Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {
            show_progress_ui(false);
        }
    };

    /**
     * 错误处理监听器
     */
    private class VideoErrorListener implements MediaPlayer.MediaPlayerErrorListener {

        public void onError(int what, int extra) {

            KLog.json("Error what = " + what + " extra = " + extra);

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
                    show_buffering_ui(true);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: //缓冲结束时收到该信息
                    //start();
                    show_buffering_ui(false);
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

                    show_buffering_ui(false);
                    break;
            }
        }
    }

    /**
     * 快进完成监听器
     */
    private class VideoSeekCompletelistener implements MediaPlayer.MediaPlayerSeekCompleteListener {

        public void onSeekCompleted() {
            mEnableUpdateProgress = true;
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

    /*
    * 缓冲显示
    * */
    private void show_buffering_ui(boolean bShowTip) {
        LayoutTip.setVisibility(bShowTip ? View.VISIBLE : View.GONE);
        if (bShowTip) {
            animation.startNow();
        } else {
            if (animation != null) {
                animation.cancel();
            }
        }
    }

    /*
    * 进度条显示
    * */
    private void show_progress_ui(boolean bShowPause) {

        if (bShowPause) {
            LayoutProgress.setVisibility(View.VISIBLE);
        } else {
            LayoutProgress.setVisibility(View.GONE);
        }
    }

    private void update_total_duration(int ms) {
        int var = (int) (ms / 1000.0f + 0.5f);
        int min = var / 60;
        int sec = var % 60;
        totalDuration.setText((min >= 10 ? min : "0" + min) + ":" + (sec >= 10 ? sec : "0" + sec));

        SeekBar sb = (SeekBar) findViewById(R.id.progress);
        sb.setMax(ms);
        sb.setKeyProgressIncrement(10000); //5000ms = 5sec.
        sb.setProgress(0);
        sb.setSecondaryProgress(0); //reset progress now.

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int i, boolean fromuser) {
                int var = (int) (i / 1000.0f + 0.5f);
                int min = var / 60;
                int sec = var % 60;
                String strCur = String.format("%1$d:%2$d", min, sec);
                currentDuration.setText((min >= 10 ? min : "0" + min) + ":" + (sec >= 10 ? sec : "0" + sec));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mEnableUpdateProgress = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int ms = seekBar.getProgress();
                mPlayer.seekTo(ms);
            }
        });

        return;
    }

    private void update_progress(int ms) {
        if (mEnableUpdateProgress) {
            mSeekBar.setProgress(ms);
        }
        return;
    }

    /*
    * 重置UI
    * */
    private void resetUI() {
        mSeekBar.setProgress(0);
        show_progress_ui(false);
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
        return;
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

            if (AppDevice.isLandscape(getContext())) {
                setProtrait();
            } else if (AppDevice.isPortrait(getContext())) {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.imgPlayerFullScreen, R.id.imgPlayerShare, R.id.imgPlayerClose,
            R.id.player_anchor_layout, R.id.player_video_layout, R.id.textAgainLoad, R.id.imgPause})
    public void setClickFunctionBar(View v) {
        switch (v.getId()) {
            case R.id.imgPlayerFullScreen: //横竖屏切换
                if (AppDevice.isLandscape(getContext())) {
                    setProtrait();
                } else if (AppDevice.isPortrait(getContext())) {
                    setLandscape();
                }
                break;
            case R.id.imgPlayerShare: //分享
                PlayDataStatistics.getStatisticalData(getContext(), "2", live_id, "2", "2");
                DialogHelp.showShareDialog(getContext(), AppConst.GG_LIVE_SHARE + live_id + "?video", "http://g1.dfcfw.com/g2/201702/20170216133526.png", "分享", "第一次分享");
                break;
            case R.id.imgPlayerClose: //退出
                if (AppDevice.isLandscape(getContext())) {
                    setProtrait();
                } else if (AppDevice.isPortrait(getContext())) {
                    finish();
                }
                break;
            case R.id.player_anchor_layout: //主播
                if (tv_player_anchor.getVisibility() == View.GONE) {
                    player_anchor_tv.setTextColor(getResColor(R.color.player_add_friend));
                    tv_player_anchor.setVisibility(View.VISIBLE);
                    player_video_tv.setTextColor(getResColor(R.color.textColor_333333));
                    tv_player_video.setVisibility(View.GONE);

                    scrollIntroduce.setVisibility(View.VISIBLE);
                    recy_relater.setVisibility(View.GONE);
                }
                break;
            case R.id.player_video_layout: //相关视频
                if (tv_player_video.getVisibility() == View.GONE) {
                    player_anchor_tv.setTextColor(getResColor(R.color.textColor_333333));
                    tv_player_anchor.setVisibility(View.GONE);
                    player_video_tv.setTextColor(getResColor(R.color.player_add_friend));
                    tv_player_video.setVisibility(View.VISIBLE);

                    scrollIntroduce.setVisibility(View.GONE);
                    recy_relater.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.textAgainLoad: //重新加载
                getRelaterVideoInfo();
                break;
            case R.id.imgPause: //播放暂停/开始
                if (imgPause.isChecked()) {
                    if (mPlayer != null && mPlayer.getDuration() > 0) pause();
                } else {
                    if (mPlayer != null && !mPlayer.isPlaying() && mPlayer.getDuration() > 0)
                        start();
                }
                break;
        }
    }

    //设置横屏
    private void setLandscape() {

        linearPlayerFun.setVisibility(View.GONE);
        player_line_tab.setVisibility(View.GONE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (mSurfaceView != null) {
            //隐藏状态栏
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            mSurfaceView.setLayoutParams(params);
        }
    }

    //设置竖屏
    private void setProtrait() {

        linearPlayerFun.setVisibility(View.VISIBLE);
        player_line_tab.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (mSurfaceView != null) {
            //显示状态栏
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth * 9.0f / 16));
            params.gravity = Gravity.CENTER;
            mSurfaceView.setLayoutParams(params);
        }
    }

    /*
    * 获取直播相关视频
    * */
    private void getRelaterVideoInfo() {

        Map<String, String> param = new HashMap<>();
        param.put("video_id", live_id);
        param.put("video_type", "2");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    recy_relater.setVisibility(View.VISIBLE);
                    linearShowError.setVisibility(View.GONE);

                    List<RelaterVideoData> videoDatas = JSONObject.parseArray(String.valueOf(object.getJSONArray("data")), RelaterVideoData.class);
                    adapter = new RelaterVideoAdapter(getContext(), videoDatas);
                    recy_relater.setAdapter(adapter);
                } else {
                    recy_relater.setVisibility(View.GONE);
                    linearShowError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RELATED_VIDEO, ggHttpInterface).startGet();
    }

    class RelaterVideoAdapter extends CommonAdapter<RelaterVideoData, BaseViewHolder> {

        public RelaterVideoAdapter(Context context, List<RelaterVideoData> list) {
            super(R.layout.item_relater_video, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, final RelaterVideoData data, int position) {

            holder.setAlpha(R.id.text_playback, (float) 0.5);
            if (data.getType() == 1) {
                holder.setVisible(R.id.relative_player, true);
            } else {
                holder.setVisible(R.id.relative_player, false);
            }
            ImageView relater_img = holder.getView(R.id.relater_img);
            ImageDisplay.loadImage(getContext(), data.getVideo_img_url(), relater_img);
            holder.setText(R.id.relater_tittle, data.getVideo_name());
            holder.setText(R.id.relater_play_count, data.getPlay_base() + "次");
            RoundedImageView relater_avatar = holder.getView(R.id.relater_avatar);
            ImageDisplay.loadCircleImage(getContext(), data.getFace_url(), relater_avatar);
            holder.setText(R.id.relater_name, data.getAnchor_name());
            holder.setText(R.id.relater_content, data.getProgramme_name());

            holder.setOnClickListener(R.id.linearRelaterVideo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    live_id = data.getVideo_id();
                    getPlayerInfo();
                }
            });
        }
    }

    private PlayerActivity getContext() {
        return PlayerActivity.this;
    }
}
