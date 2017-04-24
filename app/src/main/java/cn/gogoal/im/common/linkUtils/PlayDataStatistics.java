package cn.gogoal.im.common.linkUtils;

import android.Manifest;
import android.content.Context;

import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.MD5Utils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.permission.IPermissionListner;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: 直播录播播放数据统计
 */
public class PlayDataStatistics {

    public static void getStatisticalData(final Context context, final String video_id, final String source, final String type) {

        // READ_PHONE_STATE

        BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.READ_PHONE_STATE},
                new IPermissionListner() {
                    @Override
                    public void onUserAuthorize() {
                        getData(context, video_id, source, type);
                    }

                    @Override
                    public void onRefusedAuthorize(List<String> deniedPermissions) {
                    }
                });
    }

    private static void getData(Context context, String video_id, String source, String type) {

        String equipment_id = MD5Utils.getMD5EncryptyString16(AppDevice.getIMEI(context) + AppDevice.getIMSI(context));

        Map<String, String> param = new HashMap<>();
        param.put("video_id", video_id);
        param.put("source", source);
        param.put("token", UserUtils.getToken());
        param.put("equipment_id", equipment_id);
        param.put("type", type);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ADD_PALY_DATE, ggHttpInterface).startGet();
    }
}
