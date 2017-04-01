package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.socks.library.KLog;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.UIHelper;

/*
* 推流直播页面
* */
public class LiveActivity extends BaseActivity {

    //推流预览的SurfaceView
    @BindView(R.id.surfaceLive)
    SurfaceView surfaceLive;
    //推流关闭按钮
    @BindView(R.id.imgClose)
    ImageView imgClose;
    //连麦显示
    @BindView(R.id.parter_view_container)
    LinearLayout mParterViewContainer;
    //连麦关闭按钮
    @BindView(R.id.imgParterClose)
    ImageView imgParterClose;

    //连麦播放的小窗SurfaceView
    private SurfaceView mPlaySurfaceView;

    //surface状态
    enum SurfaceStatus {
        UNINITED, CREATED, CHANGED, DESTROYED, RECREATED
    }

    private SurfaceStatus mPreviewSurfaceStatus = SurfaceStatus.UNINITED;
    private SurfaceStatus mPlayerSurfaceStatus = SurfaceStatus.UNINITED;

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

    private int mNoPermissionIndex = 0;


    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;


    @Override
    public int bindLayout() {
        return R.layout.activity_live;
    }

    @Override
    public void doBusiness(Context mContext) {

        if (permissionCheck()) {
            // 更新权限状态
            //mLivePresenter.updatePermissionStatus(true);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
                finish();
            }
        }

        surfaceLive.getHolder().addCallback(mPreviewCallback);


    }

    /**
     * 权限检查（适配6.0以上手机）
     */
    private boolean permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        String permission;
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

    /**
     * 没有权限的提醒
     *
     * @param tip
     */
    private void showNoPermissionTip(String tip) {
        UIHelper.toast(this, tip);
    }

    SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(final SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface created");
            //记录Surface的状态
            if (mPreviewSurfaceStatus == SurfaceStatus.UNINITED) {
                mPreviewSurfaceStatus = SurfaceStatus.CREATED;
                //mLivePresenter.startPreView(holder);
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
                //mLivePresenter.resumePublishStream(surfaceLive, mPlaySurfaceView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.d("LiveActivity-->Preview surface destroyed");
            mPreviewSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };
}
