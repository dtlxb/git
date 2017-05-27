package cn.gogoal.im.common.linkUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.LiveActivity;
import cn.gogoal.im.activity.WatchLiveActivity;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.MD5Utils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.permission.IPermissionListner;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: 直播录播播放数据统计
 */
public class PlayDataStatistics {

    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int[] noPermissionTip = {
            R.string.no_camera_permission,
            R.string.no_record_audio_permission,
            R.string.no_read_phone_state_permission,
            R.string.no_write_external_storage_permission,
            R.string.no_read_external_storage_permission
    };


    /**
     * 统计直播录播
     *
     * @param context
     * @param video_type
     * @param video_id
     * @param source
     * @param type
     */
    public static void getStatisticalData(final Context context, final String video_type, final String video_id, final String source, final String type) {

        // READ_PHONE_STATE

        BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.READ_PHONE_STATE},
                new IPermissionListner() {
                    @Override
                    public void onUserAuthorize() {
                        getData(context, video_type, video_id, source, type);
                    }

                    @Override
                    public void onRefusedAuthorize(List<String> deniedPermissions) {
                    }
                });
    }

    private static void getData(Context context, String video_type, String video_id, String source, String type) {

        String equipment_id = MD5Utils.getMD5EncryptyString16(AppDevice.getIMEI(context) + AppDevice.getIMSI(context));

        Map<String, String> param = new HashMap<>();
        param.put("video_id", video_id);
        param.put("source", source);
        param.put("token", UserUtils.getToken());
        param.put("equipment_id", equipment_id);
        param.put("type", type);
        param.put("video_type", video_type);
        param.put("product_line", "4");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ADD_PALY_DATE, ggHttpInterface).startGet();
    }

    /**
     * 进入直播授权
     *
     * @param isLive
     * @param mContext
     * @param live_id
     * @param isFinish
     */
    public static void enterLiveAuthorize(final boolean isLive, final Activity mContext, final String live_id, final boolean isFinish) {
        //申请授权
        BaseActivity.requestRuntimePermission(permissionManifest, new IPermissionListner() {
            @Override
            public void onUserAuthorize() {
                if (isFinish) {
                    mContext.finish();
                }

                Intent intent = null;
                if (isLive) {
                    intent = new Intent(mContext, LiveActivity.class);
                } else {
                    intent = new Intent(mContext, WatchLiveActivity.class);
                }
                intent.putExtra("live_id", live_id);
                mContext.startActivity(intent);
            }

            @Override
            public void onRefusedAuthorize(List<String> deniedPermissions) {
                UIHelper.toast(mContext, "在设置-应用-GoGoal-权限中开启相关权限，以正常使用App功能");
            }
        });
    }
}
