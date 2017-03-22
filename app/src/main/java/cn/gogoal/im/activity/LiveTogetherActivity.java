package cn.gogoal.im.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ToggleButton;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alibaba.livecloud.live.AlivcMediaRecorder;
import com.alibaba.livecloud.live.AlivcMediaRecorderFactory;
import com.alibaba.livecloud.live.AlivcRecordReporter;
import com.alibaba.livecloud.live.AlivcStatusCode;
import com.alibaba.livecloud.live.OnLiveRecordErrorListener;
import com.alibaba.livecloud.live.OnNetworkStatusListener;
import com.alibaba.livecloud.live.OnRecordStatusListener;
import com.alibaba.livecloud.model.AlivcWatermark;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.PlayerUtils.DataStatistics;
import cn.gogoal.im.common.UIHelper;

/*
* 直播页面
* */
public class LiveTogetherActivity extends BaseActivity {

    @BindView(R.id.camera_surface)
    SurfaceView cameraSurface;

    @BindView(R.id.toggle_live_push)
    ToggleButton btn_live_push;

    //推流地址
    private String pushUrl;
    //分辨率
    private int videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_480P;
    //横竖屏
    private boolean screenOrientation = false;
    //前后置摄像头
    private int cameraFrontFacing = AlivcMediaFormat.CAMERA_FACING_FRONT;
    //水印
    private AlivcWatermark mWatermark = new AlivcWatermark.Builder()
            .watermarkUrl("") //地址
            .paddingX(14)
            .paddingY(14)
            .site(1)
            .build();
    //帧率
    private int frameRate = 30;
    private int bestBitrate = 600;
    private int minBitrate = 500;
    private int maxBitrate = 800;
    private int initBitrate = 600;

    //权限方面
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final int PERMISSION_DELAY = 100;
    private boolean mHasPermission = false;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private AlivcMediaRecorder mMediaRecorder;
    private AlivcRecordReporter mRecordReporter; //看日志

    private Surface mPreviewSurface;
    private Map<String, Object> mConfigure = new HashMap<>();
    private boolean isRecording = false;
    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private DataStatistics mDataStatistics = new DataStatistics(1000);

    private Handler mHandler = new Handler();
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;

    private AlertDialog illegalArgumentDialog = null;

    @Override
    public int bindLayout() {
        return R.layout.activity_live_together;
    }

    @Override
    public void doBusiness(Context mContext) {
        setImmersive(true);

        pushUrl = getIntent().getStringExtra("pushUrl");

        KLog.json(pushUrl);

        //检查是否有权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            mHasPermission = true;
        }

        //设置横竖屏
        setRequestedOrientation(screenOrientation ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //设置摄像头信息
        setCameraInfo();
    }

    /*
    * 权限请求
    * */
    private void checkPermission() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
        } else {
            mHasPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean hasPermission = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        int toastTip = 0;
                        if (Manifest.permission.CAMERA.equals(permissions[i])) {
                            toastTip = R.string.no_camera_permission;
                        } else if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                            toastTip = R.string.no_record_audio_permission;
                        }
                        if (toastTip != 0) {
                            UIHelper.toast(this, toastTip);
                            hasPermission = false;
                        }
                    }
                }
                mHasPermission = hasPermission;
                break;
        }
    }

    /*
    * 初始化摄像头信息
    * */
    private void setCameraInfo() {
        //采集
        cameraSurface.getHolder().addCallback(CameraSurfaceCallback);
        cameraSurface.setOnTouchListener(mOnTouchListener);

        //对焦，缩放
        mDetector = new GestureDetector(cameraSurface.getContext(), mGestureDetector);
        mScaleDetector = new ScaleGestureDetector(cameraSurface.getContext(), mScaleGestureListener);

        //创建实例初始化
        mMediaRecorder = AlivcMediaRecorderFactory.createMediaRecorder();
        mMediaRecorder.init(this);
        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON); //开启美颜

        //mDataStatistics.setReportListener(mReportListener); //推流数据监听

        /*
        * this method only can be called after mMediaRecorder.init(),
        * else will return null;
        * */
        mRecordReporter = mMediaRecorder.getRecordReporter();

        mDataStatistics.start();
        mMediaRecorder.setOnRecordStatusListener(mRecordStatusListener);
        mMediaRecorder.setOnNetworkStatusListener(mOnNetworkStatusListener);
        mMediaRecorder.setOnRecordErrorListener(mOnErrorListener);

        //设置推流参数
        mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, cameraFrontFacing);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_ZOOM_LEVEL, 3);
        mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, videoResolution);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_VIDEO_BITRATE, maxBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_BEST_VIDEO_BITRATE, bestBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_MIN_VIDEO_BITRATE, minBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_INITIAL_VIDEO_BITRATE, initBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_DISPLAY_ROTATION, screenOrientation ? AlivcMediaFormat.DISPLAY_ROTATION_90 : AlivcMediaFormat.DISPLAY_ROTATION_0);
        mConfigure.put(AlivcMediaFormat.KEY_EXPOSURE_COMPENSATION, -1);//曝光度
        mConfigure.put(AlivcMediaFormat.KEY_WATERMARK, mWatermark);
        mConfigure.put(AlivcMediaFormat.KEY_FRAME_RATE, frameRate);
    }

    /*
    * 创建SurfaceView的Callback
    * */
    private final SurfaceHolder.Callback CameraSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setKeepScreenOn(true);
            mPreviewSurface = holder.getSurface();
            startPreview(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaRecorder.setPreviewSize(width, height);
            mPreviewWidth = width;
            mPreviewHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPreviewSurface = null;
            mMediaRecorder.stopRecord();
            mMediaRecorder.reset();
        }
    };

    /*
    * 开始预览
    * */
    private void startPreview(final SurfaceHolder holder) {
        if (!mHasPermission) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPreview(holder);
                }
            }, PERMISSION_DELAY);
            return;
        }

        mMediaRecorder.prepare(mConfigure, mPreviewSurface);
        mMediaRecorder.setPreviewSize(cameraSurface.getMeasuredWidth(), cameraSurface.getMeasuredHeight());
        if ((int) mConfigure.get(AlivcMediaFormat.KEY_CAMERA_FACING) == AlivcMediaFormat.CAMERA_FACING_FRONT) {
            mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        }
    }

    /*
    * 监听事件
    * */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    /*
    * 对焦
    * */
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
                mMediaRecorder.focusing(x, y);
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

    /*
    * 缩放
    * */
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mMediaRecorder.setZoom(scaleGestureDetector.getScaleFactor());
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

    /*
    * 设置推流的状态回调监听
    * */
    private OnRecordStatusListener mRecordStatusListener = new OnRecordStatusListener() {
        @Override
        public void onDeviceAttach() {
            //摄像头打开成功
        }

        @Override
        public void onDeviceAttachFailed(int i) {
            //摄像头打开失败
        }

        @Override
        public void onSessionAttach() {
            //开启预览成功
            if (isRecording && !TextUtils.isEmpty(pushUrl)) {
                mMediaRecorder.startRecord(pushUrl);
            }
            mMediaRecorder.focusing(0.5f, 0.5f);
        }

        @Override
        public void onSessionDetach() {
            //停止预览
        }

        @Override
        public void onDeviceDetach() {
            //关闭摄像头
        }

        @Override
        public void onIllegalOutputResolution() {
            //分辨率选择错误
            UIHelper.toast(getContext(), "设置分辨率过大");
        }
    };

    /*
    * 设置网络状态的回调监听
    * */
    private OnNetworkStatusListener mOnNetworkStatusListener = new OnNetworkStatusListener() {
        @Override
        public void onNetworkBusy() {
            //网络较差时的回调，此时推流buffer为满的状态，会执行丢包，此时数据流不能正常推送
            UIHelper.toast(getContext(), "当前网络状态极差，已无法正常流畅直播，确认要继续直播吗？");
        }

        @Override
        public void onNetworkFree() {
            //网络空闲状态，此时本地推流buffer不满，数据流可正常发送

        }

        @Override
        public void onConnectionStatusChange(int status) {
            //连接状态
            switch (status) {
                case AlivcStatusCode.STATUS_CONNECTION_START:
                    UIHelper.toast(getContext(), "Start live stream connection!");
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_ESTABLISHED:
                    UIHelper.toast(getContext(), "Live stream connection is established!");
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_CLOSED:
                    UIHelper.toast(getContext(), "Live stream connection is closed!");
                    break;
            }
        }

        @Override
        public boolean onNetworkReconnectFailed() {
            //重连失败
            UIHelper.toast(getContext(), "长时间重连失败，已不适合直播，请退出");
            mMediaRecorder.stopRecord();
            showIllegalArgumentDialog("网络重连失败");
            return false;
        }
    };

    /*
    * 弹窗提示
    * */
    public void showIllegalArgumentDialog(String message) {
        if (illegalArgumentDialog == null) {
            illegalArgumentDialog = new AlertDialog.Builder(this)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            illegalArgumentDialog.dismiss();
                        }
                    })
                    .setTitle("提示")
                    .create();
        }
        illegalArgumentDialog.dismiss();
        illegalArgumentDialog.setMessage(message);
        illegalArgumentDialog.show();
    }

    /*
    * 设置推流错误回调
    * */
    private OnLiveRecordErrorListener mOnErrorListener = new OnLiveRecordErrorListener() {
        @Override
        public void onError(int errorCode) {
            switch (errorCode) {
                case AlivcStatusCode.ERROR_BROKEN_PIPE: //管道中断
                case AlivcStatusCode.ERORR_OUT_OF_MEMORY: //内存不足
                case AlivcStatusCode.ERROR_IO: //I/O错误
                case AlivcStatusCode.ERROR_ILLEGAL_ARGUMENT: //参数非法
                case AlivcStatusCode.ERROR_NETWORK_UNREACHABLE: //网络不可达
                case AlivcStatusCode.ERROR_SERVER_CLOSED_CONNECTION: //服务器关闭链接
                case AlivcStatusCode.ERROR_CONNECTION_TIMEOUT: //网络链接超时
                case AlivcStatusCode.ERROR_AUTH_FAILED: //鉴权失败
                case AlivcStatusCode.ERROR_OPERATION_NOT_PERMITTED: //操作不允许
                case AlivcStatusCode.ERROR_CONNECTION_REFUSED: //服务器拒绝链接
                    UIHelper.toast(getContext(), "Live stream connection error-->" + errorCode);
                    break;
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreviewSurface != null) {
            mMediaRecorder.prepare(mConfigure, mPreviewSurface);
            KLog.json(" onResume==== isRecording =" + isRecording);
        }

    }

    @Override
    protected void onPause() {
        if (isRecording) {
            mMediaRecorder.stopRecord();
        }

        /**
         * 如果要调用stopRecord和reset()方法，则stopRecord（）必须在reset之前调用
         * 否则将会抛出IllegalStateException
         */
        mMediaRecorder.reset();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataStatistics.stop();
        mMediaRecorder.release();
    }

    @OnClick({R.id.toggle_live_push})
    public void setToggleBtnClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_live_push:
                if (btn_live_push.isChecked()) {
                    try {
                        mMediaRecorder.startRecord(pushUrl);
                    } catch (Exception e) {
                    }
                    isRecording = true;
                } else {
                    mMediaRecorder.stopRecord();
                    isRecording = false;
                }
                break;
        }
    }

    private LiveTogetherActivity getContext() {
        return LiveTogetherActivity.this;
    }
}
