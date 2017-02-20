package com.gogoal.app.common;

/**
 * /**
 * author wangjd on 2017/2/13 0013.
 * Staff_id 1375
 * phone 18930640263
 *
 * ufile上传的封装——半成品
 */

public class UFileUpload {
    public static final int UPLOAD_TYPE_IMAGE=100; //图片类型上传
    public static final int UPLOAD_TYPE_AUDIO=101; //音频文件上传
    public static final int UPLOAD_TYPE_TEXT=102; //文本文件上传
    public static final int UPLOAD_TYPE_VIDEO=103; //视频文件上传

    private UploadListener listener;

    public void setOnUploadListener(UploadListener listener) {
        this.listener = listener;
    }


    public interface UploadListener{
        void onSuccess();
        void onFailed();
    }

}
