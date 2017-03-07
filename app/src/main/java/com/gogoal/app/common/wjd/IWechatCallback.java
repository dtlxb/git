package com.gogoal.app.common.wjd;

/**
 * author wangjd on 2017/3/6 0006.
 * Staff_id 1375
 * phone 18930640263
 */
public interface IWechatCallback {

    void onFailed(String errorString);

    void onSuccess(String responceString);
}
