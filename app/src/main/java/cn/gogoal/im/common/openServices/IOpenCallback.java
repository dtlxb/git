package cn.gogoal.im.common.openServices;

/**
 * author wangjd on 2017/3/6 0006.
 * Staff_id 1375
 * phone 18930640263
 */
public interface IOpenCallback {

    void onFailed(String errorString);

    void onSuccess(String responceString);
}
