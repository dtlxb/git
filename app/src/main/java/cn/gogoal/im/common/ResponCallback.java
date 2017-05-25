package cn.gogoal.im.common;

/**
 * author wangjd on 2017/5/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public interface ResponCallback {

    /*成功*/
    void onSuccess(String jsonString);

    /*无数据*/
    void onEmpty();

    /*出错*/
    void onError(String errorMsg);
}
