package cn.gogoal.im.common.permission;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :检查是否具有直播权限
 */
public interface CheckLivePermissionListener {
    void hasPermission(String liveId,boolean hasPermission);
}
