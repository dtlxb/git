package com.gogoal.app.common;

import android.util.Base64;

import com.gogoal.app.R;
import com.gogoal.app.base.MyApp;
import com.gogoal.app.common.ImageUtils.ImageUtils;
import com.socks.library.KLog;

import java.io.File;

import cn.ucloud.ufilesdk.Callback;
import cn.ucloud.ufilesdk.UFileRequest;
import cn.ucloud.ufilesdk.UFileSDK;
import cn.ucloud.ufilesdk.UFileUtils;


/**
 * /**
 * author wangjd on 2017/2/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * ufile上传的封装——半成品
 */

public class UFileUpload {

    private static final String bucket = "hackfile";

    private UFileUpload() {
    }

    private static UFileUpload instance = null;

    public static UFileUpload getInstance() {
        if (instance == null) {
            synchronized (UFileUpload.class) {
                if (instance == null) {
                    instance = new UFileUpload();
                    uFileSDK = new UFileSDK(bucket);//域名后缀
                }
            }
        }
        return instance;
    }

    private static UFileSDK uFileSDK;

    public interface UploadListener {

        void onUploading(int progress);

        void onSuccess(String onlineUri);

        void onFailed();
    }

    /**
     * 枚举四中类型
     */
    public enum Type {
        IMAGE(0), AUDIO(1), VIDEO(2), FILE(4);

        int type;

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public void upload(final File file, Type type, final UploadListener listener) {

        String http_method = "PUT";

        String content_md5 = UFileUtils.getFileMD5(file);

        String content_type = null;
        switch (type) {
            case IMAGE:
                content_type = ImageUtils.getImageType(file);
                break;
            case AUDIO:
                content_type = "audio/amr";
                break;
            case VIDEO:
                content_type = "video/mpeg4";
                break;
            case FILE:
                content_type = "application/octet-stream";//文件
                break;
        }

        String date = "";

        String key_name = MyApp.getContext().getString(R.string.app_name) + "_" +
                MD5Utils.getMD5EncryptyString(file.getPath()) +
                file.getPath().substring(file.getPath().lastIndexOf('.'));

        String authorization = getAuthorization(http_method, content_md5, content_type, date, bucket, key_name);
        final UFileRequest request = new UFileRequest();
        request.setHttpMethod(http_method);
        request.setAuthorization(authorization);
        request.setContentMD5(content_md5);
        request.setContentType(content_type);

        uFileSDK.putFile(request, file, key_name, new Callback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                listener.onSuccess(uFileSDK.getUrl());
            }

            @Override
            public void onProcess(long len) {
                listener.onUploading((int) (len * 100 / file.length()));
            }

            @Override
            public void onFail(org.json.JSONObject response) {
                KLog.i("onFail " + response);
                listener.onFailed();
            }
        });

    }

    /**
     * UCloud签名
     */
    private String getAuthorization(String http_method, String content_md5, String content_type, String date, String bucket, String key) {
        String signature = "";
        try {
            String strToSign = http_method + "\n" + content_md5 + "\n" + content_type + "\n" + date + "\n" + "/" + bucket + "/" + key;
            byte[] hmac = UFileUtils.hmacSha1(AppConst.privatekey, strToSign);
            signature = Base64.encodeToString(hmac, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UCloud" + " " + AppConst.publicKey + ":" + signature;
    }
}
