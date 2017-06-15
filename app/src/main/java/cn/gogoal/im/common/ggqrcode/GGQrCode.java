package cn.gogoal.im.common.ggqrcode;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.gogoal.im.common.Base64Decoder;
import cn.gogoal.im.common.Base64Encoder;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * author wangjd on 2017/6/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :二维码生成逻辑
 */
public class GGQrCode {

    private static final String urlHead = "http://www.baidu.com/page/profile?";

    public static final int QR_CODE_TYPE_PERSIONAL = 0x0;

    public static final int QR_CODE_TYPE_GROUP = 0x1;

    /**
     * 获取生成后的二维码链接
     */
    public static String getUserQrCodeBase64String(String jsonBean) {
        return urlHead + Base64Encoder.encode(jsonBean);
    }

    /**
     * 获取生成后的二维码链接中真实信息
     */
    public static String getUserQrCodeBody(String codeString) {
        if (codeString.startsWith(urlHead)) {
            return Base64Decoder.decode(codeString.replace(urlHead,""));
        }
        return null;
    }


    /**
     * 是否是我司的二维码
     */
    public static boolean isGGQrcodeString(String qrCodeString) {
        return qrCodeString.startsWith(urlHead);
    }

    /*
    * 获取我自己的二维码串
    * */
    public static String getQrcodeString(int type, String id) {
        if (!StringUtils.isActuallyEmpty(id)) {
            Map<String, String> map = new HashMap<>();
            map.put("qrType", String.valueOf(type));
            map.put(type == QR_CODE_TYPE_PERSIONAL ? "account_id" : "conv_id", UserUtils.getMyAccountId());
            return getUserQrCodeBase64String(JSONObject.toJSONString(map));
        }
        return null;
    }
}
