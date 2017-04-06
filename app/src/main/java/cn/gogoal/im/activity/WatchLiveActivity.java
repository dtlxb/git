package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alivc.player.MediaPlayer;
import com.alivc.publisher.IMediaPublisher;
import com.alivc.publisher.MediaConstants;
import com.alivc.publisher.MediaError;
import com.alivc.videochat.AlivcVideoChatParter;
import com.alivc.videochat.IVideoChatParter;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.linkUtils.ConnectivityMonitor;
import cn.gogoal.im.common.linkUtils.HeadsetMonitor;
import cn.gogoal.im.common.linkUtils.LinkConst;
import cn.gogoal.im.common.linkUtils.VideoChatStatus;
import cn.gogoal.im.fragment.LiveBottomFragment;
import cn.gogoal.im.fragment.WatchBottomFragment;

/*
* 观看直播页面
* */
public class WatchLiveActivity extends BaseActivity {

    @BindView(R.id.root_container)
    FrameLayout mRootContainer;
    //推流预览的SurfaceView
    @BindView(R.id.preview_surface_view)
    SurfaceView mPreviewSurfaceView;
    //播放的SurfaceView
    @BindView(R.id.play_surface_view)
    SurfaceView mPlaySurfaceView;
    //播放关闭按钮
    @BindView(R.id.iv_abort_chat)
    ImageView mIvChatClose;
    //缓冲控件
    @BindView(R.id.LayoutTip)
    LinearLayout LayoutTip;

    /*
    * 权限所需定义参数
    * */
    private final int PERMISSION_REQUEST_CODE = 1;
    private final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final int[] noPermissionTip = {
            R.string.no_camera_permission,
            R.string.no_record_audio_permission,
            R.string.no_read_phone_state_permission,
            R.string.no_write_external_storage_permission,
            R.string.no_read_external_storage_permission
    };

    /*
    * 直播所需定义参数
    * */
    enum SurfaceStatus {
        UNINITED, CREATED, CHANGED, DESTROYED, RECREATED; //surface状态
    }

    private SurfaceStatus mPreviewSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceStatus mPlaySurfaceStatus = SurfaceStatus.UNINITED;

    private VideoChatStatus mChatStatus = VideoChatStatus.UNCHAT; //当前的连麦状态，初始为未连麦状态

    AlivcVideoChatParter mChatParter;
    Map<String, String> mMediaParam = new HashMap<>();
    Map mFilterMap = new HashMap<>();
    private long mStartTime = 0;
    private boolean isBeautyOn = false;
    private boolean isFlashOn = false;
    private Boolean isCaching = false;

    public boolean isPlaying = false;
    private boolean mIsPublishPaused = false;
    private boolean isDestoyed = false; //activity是否销毁了，如果销毁了，则关闭连麦时就不需要做UI提醒了

    private WatchBottomFragment mBottomFragment;

    private String mIMFailedMessage;
    private ConnectivityMonitor mConnectivityMonitor = new ConnectivityMonitor();
    private HeadsetMonitor mHeadsetMonitor = new HeadsetMonitor();
    private boolean shouldOffLine = false; //surfaceChange时是否应该结束连麦

    //播放地址
    private String mPlayUrl = "http://ggliveo.oss-cn-hangzhou.aliyuncs.com/ggVod/Act-ss-mp4-hd/xiaoceshi.mp4";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /**
                 * 自己发送邀请，对方超时未响应，则自己更新本地的连麦状态为未连麦
                 */
                case LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT://连麦响应超时
                    mChatStatus = VideoChatStatus.UNCHAT;
                    UIHelper.toast(WatchLiveActivity.this, R.string.invite_timeout_tip); //提醒：对方长时间未响应，已取消连麦邀请
                    break;
                case LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT:
                    //feedbackInvite(FeedbackForm.STATUS_NOT_AGREE); //自动反馈不同意连麦
                    //UIHelper.toast(WatchLiveActivity.this, R.string.inviting_process_timeout); //提醒超时未处理，已经自动拒绝对方的连麦邀请
                    break;
                case LinkConst.MSG_WHAT_MIX_STREAM_ERROR:
                    if (isChatting()) {
                        closeVideoCall();
                    }
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_watch_live;
    }

    @Override
    public void doBusiness(Context mContext) {

        if (Build.VERSION.SDK_INT >= 23) {
            permissionCheck();
        }

        mBottomFragment = WatchBottomFragment.newInstance();
        mBottomFragment.setRecordUIClickListener(mUIClickListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_container, mBottomFragment).commit();

        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_ORIGINAL_BITRATE, "" + 800000);
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MIN_BITRATE, "" + 600000);
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MAX_BITRATE, "" + 1000000);

        initPlayer();

        //一进入界面就播放主播直播
        mPlaySurfaceView.setZOrderMediaOverlay(true);
        mPlaySurfaceView.getHolder().addCallback(mPlaySurfaceCB);
        mPreviewSurfaceView.setZOrderOnTop(false);
        mPreviewSurfaceView.getHolder().addCallback(mPublishSurfaceCB);

        mHeadsetMonitor.setHeadsetStatusChangedListener(new WatchLivePresenter());
    }

    /**
     * 权限检查（适配6.0以上手机）
     */
    private void permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        int toastTip = noPermissionTip[i];
                        if (toastTip != 0) {
                            UIHelper.toast(WatchLiveActivity.this, toastTip);
                        }
                    }
                }
                break;
        }
    }

    private LiveBottomFragment.RecorderUIClickListener mUIClickListener = new LiveBottomFragment.RecorderUIClickListener() {
        @Override
        public int onSwitchCamera() {
            //切换摄像头
            if (mChatParter != null) {
                mChatParter.switchCamera();
            }
            return -1;
        }

        @Override
        public boolean onBeautySwitch() {
            //开启/关闭美颜
            if (mChatParter != null) {
                mFilterMap.put(AlivcVideoChatParter.ALIVC_FILTER_PARAM_BEAUTY_ON, Boolean.toString(!isBeautyOn));
                mChatParter.setFilterParam(mFilterMap);
                isBeautyOn = !isBeautyOn;
            }
            return isBeautyOn;
        }

        @Override
        public boolean onFlashSwitch() {
            //开启／关闭闪光灯
            if (mChatParter != null) {
                mChatParter.setFlashOn(!isFlashOn);
                isFlashOn = !isFlashOn;
            }
            return isFlashOn;
        }
    };

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        mChatParter = new AlivcVideoChatParter();
        mChatParter.setErrorListener(mPlayerErrorListener);
        mChatParter.init(this);
        mChatParter.setInfoListener(mPlayerInfoListener);
        mChatParter.setParterViewScalingMode(IMediaPublisher.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        //设置连麦预览/推流时开启美颜
        mFilterMap.put(AlivcVideoChatParter.ALIVC_FILTER_PARAM_BEAUTY_ON, Boolean.toString(true));
        mChatParter.setFilterParam(mFilterMap);
    }

    /**
     * 播放错误回调
     */
    AlivcVideoChatParter.OnErrorListener mPlayerErrorListener = new AlivcVideoChatParter.OnErrorListener() {

        @Override
        public boolean onError(IVideoChatParter iVideoChatParter, int what, int extra) {
            KLog.e("WatchLiveActivity-->what = " + what + ", extra = " + extra);
            if (what == 0) {
                return false;
            }
            if (mChatParter == null) {
                return false;
            }

            switch (what) {
                case MediaError.ALIVC_ERR_PLAYER_INVALID_INPUTFILE:
                case MediaError.ALIVC_ERR_PLAYER_OPEN_FAILED:
                case MediaError.ALIVC_ERR_PLAYER_NO_MEMORY:
                case MediaError.ALIVC_ERR_PLAYER_INVALID_CODEC:
                case MediaError.ALIVC_ERR_PLAYER_NO_SURFACEVIEW:
                case MediaError.ALIVC_ERR_PLAYER_UNSUPPORTED:
                case MediaError.ALIVC_ERR_PLAYER_READ_PACKET_TIMEOUT:
                case MediaError.ALIVC_ERR_PLAYER_NO_NETWORK:
                case MediaError.ALIVC_ERR_PLAYER_UNKNOW:
                    UIHelper.toast(WatchLiveActivity.this, R.string.error_stop_playing);
                    //如果正在连麦则结束连麦
                    if (isChatting()) {
                        closeVideoCall();
                    }
                    stopPlaying();
                    break;
                case MediaError.ALIVC_ERR_PLAYER_TIMEOUT:
                    KLog.e("encounter player timeout, so call restartToPlayer");
                    mChatParter.reconnect();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_DISABLED://音频采集失败
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_NO_DATA:
                    if (isChatting()) {
                        KLog.e("音频采集失败，结束连麦");
                        closeVideoCall();
                    } else {
                        KLog.e("音频采集失败，但是当前没有处于连麦状态");
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_CAPTURE_DISABLED:
                    if (isChatting()) {
                        UIHelper.toast(WatchLiveActivity.this, R.string.camera_open_failure_for_chat);
                        closeVideoCall();
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_AUDIO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_MALLOC_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_UNCONNECTED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_POOR:
                case MediaError.ALIVC_ERR_PUBLISHER_SEND_DATA_TIMEOUT:
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_VIDEO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_ILLEGAL_ARGUMENT:
                    UIHelper.toast(WatchLiveActivity.this, R.string.network_busy);
                    break;
                default:
                    UIHelper.toast(WatchLiveActivity.this, R.string.error_unknown);
            }
            return true;
        }
    };

    /**
     * 播放状态回调
     */
    AlivcVideoChatParter.OnInfoListener mPlayerInfoListener = new AlivcVideoChatParter.OnInfoListener() {

        @Override
        public boolean onInfo(IVideoChatParter iVideoChatParter, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOW:
                    //未知
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    //开始缓冲
                    synchronized (isCaching) {
                        if (!isCaching) {
                            mHandler.postDelayed(mShowInterruptRun, LinkConst.INTERRUPT_DELAY);
                            isCaching = true;
                        }
                    }
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    synchronized (isCaching) {
                        if (isCaching) {
                            mHandler.removeCallbacks(mShowInterruptRun);
                            // 结束缓冲
                            isCaching = false;
                        }
                    }
                    break;
                case MediaError.ALIVC_INFO_PLAYER_FIRST_FRAME_RENDERED:
                    // 首帧显示时间
                    UIHelper.toast(WatchLiveActivity.this, R.string.show_first_frame);
                    if (!isChatting()) {
                        //计算首帧耗时
                        KLog.e("首帧耗时: " + (System.currentTimeMillis() - mStartTime) + "ms");
                    }
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_DISPLAY_FIRST_FRAME:
                    //预览首帧渲染完成
                    if (isChatting()) {//如果是正在连麦的状态，则首帧渲染完后需要通知UI层隐藏【正在连麦】的view
                        //mView.hideChattingView();
                    }
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_NETWORK_GOOD:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_START:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_SUCCESS:
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_FAILURE:
                case MediaError.ALIVC_INFO_PLAYER_PREPARED_PROCESS_FINISHED:
                case MediaError.ALIVC_INFO_PLAYER_INTERRUPT_PLAYING:
                case MediaError.ALIVC_INFO_PLAYER_STOP_PROCESS_FINISHED:
            }
            KLog.e("MediaPlayer onInfo, what =" + what + ", extra = " + extra);
            return false;
        }
    };

    /**
     * 播放surface
     */
    SurfaceHolder.Callback mPlaySurfaceCB = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("parter player surface create.");
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);

            if (mPlaySurfaceStatus == SurfaceStatus.UNINITED) {
                mPlaySurfaceStatus = SurfaceStatus.CREATED;
                startToPlay(mPlaySurfaceView);
                KLog.e("Player surface status is created");
            } else if (mPlaySurfaceStatus == SurfaceStatus.DESTROYED) {
                mPlaySurfaceStatus = SurfaceStatus.RECREATED;
                KLog.e("Player surface status is recreated");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("parter player surface change.");
            mPlaySurfaceStatus = SurfaceStatus.CHANGED;

            if (mPreviewSurfaceStatus == null
                    || mPreviewSurfaceStatus == SurfaceStatus.UNINITED
                    || mPreviewSurfaceStatus == SurfaceStatus.CHANGED) {
                KLog.e("WatchLivePresenter", "SurfaceChanged resume");
                mediaResume(mPlaySurfaceView, mPreviewSurfaceView);

                //TODO：对于需要结束连麦的情况，因为surface很可能重建了，所以需要sleep一秒，不然可能出现offlineChat后不能恢复播放的情况
                if (shouldOffLine) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (shouldOffLine) {
                //在这里调用真正的offlineChat，保证渲染出得最后一帧数据是正常播放的尺寸，而不是小窗播放的尺寸
                sdkOfflineChat();
                shouldOffLine = false;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPlaySurfaceStatus = SurfaceStatus.DESTROYED;
            KLog.e("parter player surface destroy.");
        }
    };

    /**
     * 推流surface
     */
    SurfaceHolder.Callback mPublishSurfaceCB = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("WatchLiveActivity-->preview surface created");
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);

            if (mPreviewSurfaceStatus == SurfaceStatus.UNINITED) {
                mPreviewSurfaceStatus = SurfaceStatus.CREATED;
            } else if (mPreviewSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPreviewSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("WatchLiveActivity-->preview surface changed, width=" + width + ",height=" + height);
            mPreviewSurfaceStatus = SurfaceStatus.CHANGED;
            if (mPreviewSurfaceStatus == SurfaceStatus.CHANGED && mPlaySurfaceStatus == SurfaceStatus.CHANGED) {
                mediaResume(mPlaySurfaceView, mPreviewSurfaceView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.e("WatchLiveActivity-->preview surface destroyed");
            mPreviewSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 开始直播播放（大窗）
     *
     * @param surfaceView
     */
    public void startToPlay(final SurfaceView surfaceView) {
        if (mChatParter == null) {
            initPlayer();
        }
        mStartTime = System.currentTimeMillis();
        KLog.e("WatchActivity --> mChatParter.startToPlay");
        mChatParter.startToPlay(mPlayUrl, surfaceView); //开始直播
        isPlaying = true;
        //TODO:显示loading
    }

    /**
     * 恢复播放/连麦
     *
     * @param previewSurf
     * @param playSurf
     */
    public void mediaResume(SurfaceView previewSurf, SurfaceView playSurf) {
        if (mChatParter != null && mIsPublishPaused) {
            KLog.e("WatchLiveActivity-->mChatParter.resume(), previewSurf = " + previewSurf + ", playSurf = " + playSurf);
            mChatParter.resume(previewSurf, playSurf);
            mIsPublishPaused = false;
        }
    }

    /**
     * 暂停播放 or 连麦
     */
    public void mediaPause() {
        if (mChatParter != null && !mIsPublishPaused) {
            mChatParter.pause();
            KLog.e("WatchLiveActivity--> mChatParter.pause()");
            mIsPublishPaused = true;
        }
    }

    /**
     * sdk调用offlineChat，结束连麦，这里之所以要暴露出接口，是为了playSurfaceView的surfaceChanged中可以调用，
     * 需要在surfaceChange发生后再调用真正的offlineChat，否则会出现左上角显示小窗的问题
     */
    public void sdkOfflineChat() {
        if (mChatParter != null && isChatting()) {
            KLog.e("WatchActivity --> offlineChat, Thread id: " + Thread.currentThread().getId());
            mChatParter.offlineChat();      //客户端SDK结束连麦
            mChatStatus = VideoChatStatus.UNCHAT; //更新当前连麦状态为未连麦状态
        } else {
            KLog.e("WatchActivity --> offlineChat, but mChatParter is null");
        }
    }

    /**
     * 设置对耳机状态的监听，并且通知给SDK
     */
    class WatchLivePresenter implements HeadsetMonitor.HeadSetStatusChangedListener {

        @Override
        public void onHeadsetStatusChanged(boolean on) {
            mChatParter.changeEarPhoneWhenChat(on);
        }
    }

    /**
     * 当前是否正在连麦
     *
     * @return
     */
    private boolean isChatting() {
        return mChatStatus == VideoChatStatus.MIX_SUCC || mChatStatus == VideoChatStatus.TRY_MIX; //混流成功和正在混流都认为是正在连麦的状态
    }

    /**
     * 结束连麦
     */
    public void closeVideoCall() {

//        KLog.e("Close video chat failed");
//        UIHelper.toast(WatchLiveActivity.this, R.string.close_video_chatting_failed);

        if (isChatting()) {
            abortChat(true);
        }
    }

    /**
     * 结束连麦成功处理
     *
     * @param isShowUI 是否需要显示相应的UI
     */
    private void abortChat(boolean isShowUI) {
        KLog.e("call abortChat(" + isShowUI + "), isDestroyed = " + isDestoyed);
        if (isShowUI && !isDestoyed) {
            changePlayViewToNormalMode();
            closeVideoChatSmallView();
        }
    }

    /**
     * 播放的View切换到正常的播放模式（小窗变大窗）
     */
    public void changePlayViewToNormalMode() {
        if (mPlaySurfaceView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPlaySurfaceView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            layoutParams.bottomMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.gravity = Gravity.NO_GRAVITY;
            mPlaySurfaceView.setLayoutParams(layoutParams);
        }
        mIvChatClose.setVisibility(View.GONE);
        KLog.e("mIvChatClose setVisibility gone");
        mBottomFragment.hideRecordView();
        shouldOffLine = true; //表示在surfaceChanged时需要调用真正的offlineChat，来结束连麦
    }

    /**
     * 关闭小窗播放的UI更新
     */
    public void closeVideoChatSmallView() {
        mIvChatClose.setVisibility(View.GONE);
        mBottomFragment.hideRecordView();
    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        if (mChatParter != null) {
            KLog.e("WatchActivity --> mChatPartter.stopPlaying(), Thread id: " + Thread.currentThread().getId());
            mChatParter.stopPlaying();
        }
    }

    private Runnable mShowInterruptRun = new Runnable() {
        @Override
        public void run() {
            LayoutTip.setVisibility(View.VISIBLE);
        }
    };

    @OnClick({R.id.img_close, R.id.iv_abort_chat})
    public void closeOnClick(View v) {
        switch (v.getId()) {
            case R.id.img_close: //关闭推流
                isPlaying = false;
                onBackPressed();
                break;
            case R.id.iv_abort_chat: //关闭播放

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityMonitor.register(this); //注册对网络状态的监听
        mHeadsetMonitor.register(this); //注册对耳机状态的监听

        if (mPlaySurfaceStatus != null && mPlaySurfaceStatus != SurfaceStatus.DESTROYED) {
            mediaResume(mPlaySurfaceView, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectivityMonitor.unRegister(this);
        mHeadsetMonitor.unRegister(this);

        mediaPause();
        isCaching = false;
        mHandler.removeCallbacks(mShowInterruptRun);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.e("WatchLiveActivity--> onDestroy");
        isDestoyed = true; //标记当前activity销毁了，则不需要再做UI提醒了
        if (mChatParter != null) {
            mChatParter.setErrorListener(null);
            //如果当前正在连麦，则要结束连麦
            if (isChatting()) {
                closeVideoCall();
            }
            mChatParter.stopPlaying(); //结束播放
            mChatParter.release();
        }

        //TODO:通知服务端，退出观看
    }
}
