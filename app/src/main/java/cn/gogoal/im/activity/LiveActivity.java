package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alivc.player.MediaPlayer;
import com.alivc.publisher.IMediaPublisher;
import com.alivc.publisher.MediaConstants;
import com.alivc.publisher.MediaError;
import com.alivc.videochat.AlivcVideoChatHost;
import com.alivc.videochat.IVideoChatHost;
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

/*
* 推流直播页面
* */
public class LiveActivity extends BaseActivity {

    //推流预览的SurfaceView
    @BindView(R.id.surfaceLive)
    SurfaceView mPreviewSurfaceView;
    //推流关闭按钮
    @BindView(R.id.imgPreviewClose)
    ImageView imgPreviewClose;
    //连麦显示
    @BindView(R.id.parter_view_container)
    LinearLayout mParterViewContainer;
    //连麦关闭按钮
    @BindView(R.id.imgPlayClose)
    ImageView imgPlayClose;

    /*
    * 权限所需定义参数
    * */
    private final int PERMISSION_REQUEST_CODE = 1;
    private final int PERMISSION_DELAY = 100;
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
    private int mNoPermissionIndex = 0;

    private Runnable mPermissionRun = null;

    /**
     * 如果正在连麦的时候有新的连麦邀请过来，则在同意连麦的情况下会主动关闭之前的连麦
     */
    private boolean mIsLive = false;                //直播是否处在正在推流的状态
    private boolean isBeautyOn = true;              //美颜是否开启
    private boolean isFlashOn = false;              //闪光灯是否开启
    private boolean mIsPublishPaused = false;       //推流是否处于暂停的状态
    private boolean mHasPermission = false;         //是否有权限
    private boolean mIsRequestCloseChat = false;    //是否主动请求关闭连麦

    /*
    * 直播所需定义参数
    * */
    enum SurfaceStatus {
        UNINITED, CREATED, CHANGED, DESTROYED, RECREATED  //surface状态
    }

    private SurfaceStatus mPreviewSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceStatus mPlayerSurfaceStatus = SurfaceStatus.UNINITED;

    private AlivcVideoChatHost mChatHost;
    private Map<String, String> mMediaParam = new HashMap<>(); //推流器参数
    private int mCameraFacing = AlivcMediaFormat.CAMERA_FACING_FRONT;
    private Map mFilterMap = new HashMap<>(); //滤镜参数（目前只有美颜一个滤镜）

    private GestureDetector mDetector; //对焦
    private ScaleGestureDetector mScaleDetector; //缩放

    private ConnectivityMonitor mConnectivityMonitor = new ConnectivityMonitor();
    private HeadsetMonitor mHeadsetMonitor = new HeadsetMonitor();

    private LiveBottomFragment mLiveBottomFragment;

    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;

    private SurfaceView mPlaySurfaceView; //连麦播放的小窗SurfaceView
    private VideoChatStatus mVideoChatStatus = VideoChatStatus.UNCHAT; //未连麦的状态

    private String mSmallDelayPlayUrl; //连麦延迟播放网址

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LinkConst.MSG_WHAT_INVITE_CHAT_TIMEOUT://连麦响应超时
                    if (mVideoChatStatus == VideoChatStatus.INVITE_FOR_RES) {
                        mVideoChatStatus = VideoChatStatus.UNCHAT;
                        UIHelper.toast(LiveActivity.this, R.string.invite_timeout_tip); //提醒：对方长时间未响应，已取消连麦邀请
                    }
                    break;
                case LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT:
                    feedbackInviting(false);  //自动反馈不同意连麦
                    UIHelper.toast(LiveActivity.this, R.string.inviting_process_timeout); //提醒超时未处理，已经自动拒绝对方的连麦邀请
                    break;
                case LinkConst.MSG_WHAT_MIX_STREAM_TIMEOUT:
                case LinkConst.MSG_WHAT_MIX_STREAM_ERROR:
                    //直接结束连麦
                    closeLiveChat();
                    UIHelper.toast(LiveActivity.this, R.string.mix_stream_timeout_tip); //提示等待混流超时

            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_live;
    }

    @Override
    public void doBusiness(Context mContext) {

        if (permissionCheck()) {
            // 更新权限状态
            mHasPermission = true;
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                UIHelper.toast(LiveActivity.this, noPermissionTip[mNoPermissionIndex]);
                finish();
            }
        }

        mPreviewSurfaceView.getHolder().addCallback(mPreviewCallback);
        mPreviewSurfaceView.setOnTouchListener(mOnTouchListener);

        //TODO:开始直播按钮
        /*CreateLiveFragment createLiveFragment = new CreateLiveFragment();
        createLiveFragment.setPendingPublishListener(mPendingPublishListener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root_container, createLiveFragment)
                .commit();*/

        //对焦，缩放
        mDetector = new GestureDetector(this, mGestureDetector);
        mScaleDetector = new ScaleGestureDetector(this, mScaleGestureListener);

        initRecorder();

        mHeadsetMonitor.setHeadsetStatusChangedListener(new LivePresenter());
    }

    /**
     * 权限检查（适配6.0以上手机）
     */
    private boolean permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        String permission = null;
        for (int i = 0; i < permissionManifest.length; i++) {
            permission = permissionManifest[i];
            mNoPermissionIndex = i;
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean hasPermission = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        int toastTip = noPermissionTip[i];
                        mNoPermissionIndex = i;
                        if (toastTip != 0) {
                            UIHelper.toast(LiveActivity.this, toastTip);
                            hasPermission = false;
                            finish();
                        }
                    }
                }
                mHasPermission = hasPermission;
                break;
        }
    }

    SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(final SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface created");
            //记录Surface的状态
            if (mPreviewSurfaceStatus == SurfaceStatus.UNINITED) {
                mPreviewSurfaceStatus = SurfaceStatus.CREATED;
                startPreView(holder);
            } else if (mPreviewSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPreviewSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d("LiveActivity-->Preview surface changed");
            mPreviewSurfaceStatus = SurfaceStatus.CHANGED;
            mPreviewWidth = width;
            mPreviewHeight = height;

            if ((mPlayerSurfaceStatus == SurfaceStatus.CHANGED || mPlaySurfaceView == null) && mPreviewSurfaceStatus == SurfaceStatus.CHANGED) {
                resumePublishStream(mPreviewSurfaceView, mPlaySurfaceView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface destroyed");
            mPreviewSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 开启预览
     */
    public void startPreView(final SurfaceHolder holder) {
        //需要先检查是否已经授权（6.0的动态权限请求是异步行为）
        if (mHasPermission) {
            //开启预览
            KLog.d("LiveActivity-->mChatHost.prepareToPublish()");
            mChatHost.prepareToPublish(holder.getSurface(), 360, 640, mMediaParam);
            if (mCameraFacing == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                mChatHost.setFilterParam(mFilterMap);
                mLiveBottomFragment.setBeautyUI(true); //更新美颜开关UI
            }
        } else {
            /**
             * 如果没有授权，需要判断当前系统版本是否是6.0以上，如果是6.0以上，因为动态请求权限属于异步行为，所以需要等待授权结果，
             * 采用postDelay的方式，一秒后再重新请求一次，如果是低于6.0则直接给出没有权限的提醒，并且finish掉
             */
            if (Build.VERSION.SDK_INT < 23) {
                if (mNoPermissionIndex >= 0 && mNoPermissionIndex < noPermissionTip.length) {
                    UIHelper.toast(LiveActivity.this, noPermissionTip[mNoPermissionIndex]);
                }
                finish();
            } else {
                mPermissionRun = new Runnable() {
                    @Override
                    public void run() {
                        mPermissionRun = null;
                        startPreView(holder);
                    }
                };
                mHandler.postDelayed(mPermissionRun, PERMISSION_DELAY);
            }
        }
    }

    /**
     * 反馈连麦邀请
     *
     * @param isAgree
     */
    public void feedbackInviting(boolean isAgree) {
        if (mVideoChatStatus == VideoChatStatus.RECEIVED_INVITE) {
            if (isAgree) {
                //TODO:同意连麦

            } else {
                //TODO:不同意连麦

                mVideoChatStatus = VideoChatStatus.UNCHAT; //更新连麦状态为未连麦状态
            }
            mHandler.removeMessages(LinkConst.MSG_WHAT_PROCESS_INVITING_TIMEOUT); //移除倒计时的消息
        } else {
            UIHelper.toast(LiveActivity.this, R.string.no_inviting_for_response); //当前没有连麦邀请需要反馈
        }
    }

    /**
     * 调用结束连麦的REST API
     */
    public void closeLiveChat() {
        //TODO:结束回调

        /*//显示结束连麦失败的
        KLog.e("Close chatting failed");
        UIHelper.toast(LiveActivity.this, R.string.close_chat_failed_for_new_chat);*/

        //调用SDK结束连麦成功
        abortChat(true);

    }

    /**
     * 终止连麦
     */
    public void abortChat(boolean isShowUI) {

        if (mChatHost != null && isChatting()) {
            KLog.d("LiveActivity-->mChatHost.abortChat()");
            mChatHost.abortChat();
            mVideoChatStatus = VideoChatStatus.UNCHAT;
            if (isShowUI) {
                showAbortChatUI();
            }
        }
    }

    /**
     * 是否正在进行连麦
     */
    public boolean isChatting() {
        return mVideoChatStatus == VideoChatStatus.MIX_SUCC || mVideoChatStatus == VideoChatStatus.TRY_MIX;
    }

    /**
     * 结束连麦播放,并且隐藏小窗的UI
     * 注意：因为下一次再连麦播放时需要一个新的SurfaceView，因此，这里需要将当前的playSurfaceView与mPlayCallback解绑，
     * 并且将当前的playSurfaceView从mParterViewContainer移除
     */
    public void showAbortChatUI() {
        if (mPlaySurfaceView != null) {
            mParterViewContainer.removeAllViews();
            mPlaySurfaceView.getHolder().removeCallback(mPlayCallback);
            mPlaySurfaceView = null;
        }
        imgPlayClose.setVisibility(View.GONE);
        mParterViewContainer.setVisibility(View.GONE);
    }

    private SurfaceHolder.Callback mPlayCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.e("host player surface create.");
            if (mPlayerSurfaceStatus == SurfaceStatus.UNINITED) {
                mPlayerSurfaceStatus = SurfaceStatus.CREATED;
                launchChat(mPlaySurfaceView);
            } else if (mPlayerSurfaceStatus == SurfaceStatus.DESTROYED) {
                mPlayerSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.e("host player surface changed.");
            mPlayerSurfaceStatus = SurfaceStatus.CHANGED;
            // 此处有两种情况:
            // 1.首次创建此surface,那么launchChat
            // 2.非首次,那么resume
            if (mPlayerSurfaceStatus == SurfaceStatus.CHANGED && mPreviewSurfaceStatus == SurfaceStatus.CHANGED)
                resumePublishStream(mPreviewSurfaceView, mPlaySurfaceView);

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.e("host player surface destroy.");
            mPlayerSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };

    /**
     * 进行连麦播放
     */
    public void launchChat(SurfaceView playSurfaceView) {
        if (mSmallDelayPlayUrl != null && isChatting()) {
            KLog.d("LiveActivity-->mChatHost.launchChat()");
            mChatHost.launchChat(mSmallDelayPlayUrl, playSurfaceView);
        }
    }

    /**
     * 恢复推流/连麦
     */
    public void resumePublishStream(SurfaceView mPreviewSurf, SurfaceView mPlaySurf) {
        if (mChatHost != null && mIsPublishPaused) {
            KLog.d("LiveActivity --> mChatHost.resume()");
            mChatHost.resume(mPreviewSurf, mPlaySurf);
            mIsPublishPaused = false;
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    /**
     * 自动对焦
     */
    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (mPreviewWidth > 0 && mPreviewHeight > 0) {
                float x = motionEvent.getX() / mPreviewWidth;
                float y = motionEvent.getY() / mPreviewHeight;
                if (mChatHost != null) {
                    mChatHost.focusCameraAtAdjustedPoint(x, y);
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    /**
     * 摄像头缩放
     */
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (mChatHost != null) {
                mChatHost.zoomCamera(scaleGestureDetector.getScaleFactor());
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    };

    /**
     * 初始化推流器
     */
    private void initRecorder() {
        //设置推流器推流相关参数
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_ORIGINAL_BITRATE, "" + 800000);        //初始码率
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MIN_BITRATE, "" + 600000);        //最小码率
        mMediaParam.put(MediaConstants.PUBLISHER_PARAM_MAX_BITRATE, "" + 1000000);        //最大码率

        mChatHost = new AlivcVideoChatHost();
        KLog.e("LiveActivity--> mChatHost.init()");
        mChatHost.init(this);
        mChatHost.setHostViewScalingMode(IMediaPublisher.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mChatHost.setParterViewScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        //设置错误信息回调
        mChatHost.setErrorListener(mOnErrorListener);

        //设置状态信息回调
        mChatHost.setInfoListener(mInfoListener);

        //设置美颜开启
        mFilterMap.put(AlivcVideoChatHost.ALIVC_FILTER_PARAM_BEAUTY_ON, Boolean.toString(true));
        mChatHost.setFilterParam(mFilterMap);
    }

    /**
     * 推流器错误回调
     */
    AlivcVideoChatHost.OnErrorListener mOnErrorListener = new IVideoChatHost.OnErrorListener() {
        @Override
        public boolean onError(IVideoChatHost iVideoChatHost, int what, int extra) {
            if (what == 0) {
                return false;
            }
            KLog.d("Live stream connection error-->" + what);
            switch (what) {
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_POOR:
                    UIHelper.toast(LiveActivity.this, R.string.network_busy);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_NO_NETWORK:
                    UIHelper.toast(LiveActivity.this, R.string.no_network);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_READ_PACKET_TIMEOUT:
                    UIHelper.toast(LiveActivity.this, R.string.network_busy);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_INVALID_INPUTFILE:
                case MediaError.ALIVC_ERR_PLAYER_NO_MEMORY:
                case MediaError.ALIVC_ERR_PLAYER_INVALID_CODEC:
                case MediaError.ALIVC_ERR_PLAYER_NO_SURFACEVIEW:
                    //超时等状态需要提示连麦结束
                    UIHelper.toast(LiveActivity.this, R.string.video_chatting_finished);
                    abortChat(true);
                    break;
                case MediaError.ALIVC_ERR_PLAYER_TIMEOUT:
                    KLog.d("encounter player timeout, so call restartToPlayer");
                    mChatHost.reconnectChat();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_DISABLED://音频采集关闭
                case MediaError.ALIVC_ERR_PUBLISHER_AUDIO_CAPTURE_NO_DATA://音频采集失败
                    if (isChatting()) {
                        closeLiveChat();
                    }
                    if (mIsLive) {
                        closeLive();
                    }
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_CAPTURE_DISABLED: //摄像头开启失败
                    UIHelper.toast(LiveActivity.this, R.string.camera_open_failure_for_live);
                    finish();
                    break;
                case MediaError.ALIVC_ERR_PUBLISHER_ENCODE_AUDIO_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_VIDEO_ENCODER_INIT_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_MALLOC_FAILED:
                case MediaError.ALIVC_ERR_PUBLISHER_NETWORK_UNCONNECTED:
                case MediaError.ALIVC_ERR_PUBLISHER_ILLEGAL_ARGUMENT:
                default:
                    KLog.e("Live stream connection error-->" + what);
                    break;
            }
            return false;
        }
    };

    /**
     * 调用关闭直播的Rest API
     */
    public void closeLive() {
        //TODO:结束回调

        //显示关闭直播失败
        /*UIHelper.toast(LiveActivity.this, R.string.close_live_failed);
        onLiveClose();*/

        //显示关闭直播成功
        finish();
    }

    /**
     * 推流器状态回调
     */
    AlivcVideoChatHost.OnInfoListener mInfoListener = new AlivcVideoChatHost.OnInfoListener() {

        @Override
        public boolean onInfo(IVideoChatHost iVideoChatHost, int what, int extra) {
            KLog.e("LiveActivity --> what = " + what + ", extra = " + extra);
            switch (what) {
                case MediaError.ALIVC_INFO_PUBLISH_NETWORK_GOOD:

                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_FAILURE:
                    UIHelper.toast(LiveActivity.this, R.string.network_busy);
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_START:
                    break;
                case MediaError.ALIVC_INFO_PUBLISH_RECONNECT_SUCCESS:
                    break;
            }
            return false;
        }
    };

    /**
     * 设置对耳机状态的监听，并且通知给SDK
     */
    class LivePresenter implements HeadsetMonitor.HeadSetStatusChangedListener {

        @Override
        public void onHeadsetStatusChanged(boolean on) {
            mChatHost.changeEarPhoneWhenChat(on);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityMonitor.register(this); //注册对网络状态的监听
        mHeadsetMonitor.register(this); //注册对耳机状态的监听

        if (mPreviewSurfaceStatus != SurfaceStatus.DESTROYED) {
            KLog.e("LiveActivity-->previewSurface and playSurface all is null");
            resumePublishStream(null, mPlaySurfaceView);
        } else {
            //恢复推流
            KLog.e("LiveActivity-->previewSurface is null, playSurface isn't null");
            resumePublishStream(null, mPlaySurfaceView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectivityMonitor.unRegister(this); //取消对网络状态的监听
        mHeadsetMonitor.unRegister(this); //取消对耳机状态的监听

        //TODO:如果当前有连麦请求，则取消该邀请连麦请求
        //TODO:如果当前有连麦反馈请求，则取消该连麦反馈请求

        pausePublishStream();

    }

    /**
     * 暂停推流 or 连麦
     */
    public void pausePublishStream() {
        if (mChatHost != null && !mIsPublishPaused) {
            KLog.e("LiveActivity-->mChatHost.pause()");
            //暂停推流
            mChatHost.pause();
            mIsPublishPaused = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        onLiveDestroy();
    }

    public void onLiveDestroy() {
        releaseRecorder();      //释放推流器资源
        if (mPermissionRun != null) {
            mHandler.removeCallbacks(mPermissionRun);
        }
    }

    /**
     * 释放推流器资源
     */
    public void releaseRecorder() {
        stopPublish();
        if (isChatting()) {
            abortChat(false);
            closeLiveChat();
        }
        if (mChatHost != null) {
            KLog.e("LiveActivity -->mChatHost.release()");
            mChatHost.release();
            mChatHost = null;
        }
    }

    /**
     * 停止推流
     */
    public void stopPublish() {
        if (null != mChatHost) {
            KLog.e("LiveActivity-->mChatHost.stopPublishing()");
            mChatHost.stopPublishing();
            KLog.e("LiveActivity-->mChatHost.finishPublishing()");
            mChatHost.finishPublishing();
            mIsLive = false;
        }
    }

    @OnClick({R.id.imgPreviewClose, R.id.imgPlayClose})
    public void closeOnClick(View v) {
        switch (v.getId()) {
            case R.id.imgPreviewClose:
                onBackPressed();
                break;
            case R.id.imgPlayClose:
                break;
        }
    }

    public void onBackPressed() {
        if (isChatting()) {
            closeLiveChat();
            abortChat(false);
        }

        if (mIsLive) {
            stopPublish();
            closeLive();
        }

        finish();
    }
}
