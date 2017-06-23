package cn.gogoal.im.common;

import android.util.Base64;

import com.socks.library.KLog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.gogoal.im.common.ImageUtils.ImageUtils;
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

    private UFileUpload(UFileSDK uFileSDK) {
        this.uFileSDK=uFileSDK;
    }
    
    public static UFileUpload getInstance() {
        return new UFileUpload(new UFileSDK(bucket));
    }

    private UFileSDK uFileSDK;

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

        String key_name = "gogoal"+File.separator+"avatar"+File.separator+"ucloud_"+
                MD5Utils.getMD5EncryptyString16(file.getPath()) +
                file.getPath().substring(file.getPath().lastIndexOf('.'));

        KLog.e(key_name);

        String authorization = getAuthorization(http_method, content_md5, content_type, date, bucket, key_name);
        final UFileRequest request = new UFileRequest();
        request.setHttpMethod(http_method);
        request.setAuthorization(authorization);
        request.setContentMD5(content_md5);
        request.setContentType(content_type);

//        http://hackfile.ufile.ucloud.com.cn
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
                KLog.e("onFail " + response);
                listener.onFailed();
            }
        });

    }

    private String decode(String separator) {
        try {
            return URLDecoder.decode(separator,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "/";
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
