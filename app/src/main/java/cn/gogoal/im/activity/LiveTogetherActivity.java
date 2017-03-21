package cn.gogoal.im.activity;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.view.SurfaceView;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;

/*
* 直播页面
* */
public class LiveTogetherActivity extends BaseActivity {

    @BindView(R.id.camera_surface)
    SurfaceView surfaceView;

    //权限方面
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final int PERMISSION_DELAY = 100;
    private boolean mHasPermission = false;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_live_together;
    }

    @Override
    public void doBusiness(Context mContext) {
        setImmersive(true);

        if (Build.VERSION.SDK_INT >= 23) {

        } else {
            mHasPermission = true;
        }
    }
}
