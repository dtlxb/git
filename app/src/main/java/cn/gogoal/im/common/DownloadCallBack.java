package cn.gogoal.im.common;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :下载回调.
 */
public interface DownloadCallBack {
    void success();

    void error(String errorMsg);

//        void downloading(int progress);
}
