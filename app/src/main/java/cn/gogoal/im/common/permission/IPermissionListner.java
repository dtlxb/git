package cn.gogoal.im.common.permission;

import java.util.List;

/**
 * author wangjd on 2017/3/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public interface IPermissionListner {

    void onUserAuthorize();//授权

    void onRefusedAuthorize(List<String> deniedPermissions);//拒绝(拒绝的权限集合)

}
