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

    /**
     * 获取生成后的二维码链接
     *
     * @see Bean 本来可以直接传Bean对象，考虑到后面要生成json,会使用fastjson，
     * 而日后可能不用fastjson或jackjson或者gson，所以外部先将Bean生成json传入
     */
    public static String getUserQrCodeBase64String(String jsonBean) {
        return urlHead + Base64Encoder.encode(jsonBean);
    }

    /**
     * 获取生成后的二维码链接中真实信息
     */
    public static String getUserQrCodeBody(String codeString) {
        if (codeString.startsWith(urlHead)) {
            return Base64Decoder.decode(codeString.substring(codeString.indexOf("?")+1));
        }
        return null;
    }


    /**
     * 是否是我司的二维码
     *
     * @see Bean 本来可以直接传Bean对象，考虑到后面要生成json,会使用fastjson，
     * 而日后可能不用fastjson或jackjson或者gson，所以外部先将Bean生成json传入
     */
    public static boolean isGGQrcodeString(String qrCodeString) {
        return qrCodeString.startsWith(urlHead);
    }

    /*
    * 获取我自己的二维码串
    * */
    public static String getMyQrcodeString() {
        String accountId = UserUtils.getMyAccountId();
        if (!StringUtils.isActuallyEmpty(accountId)) {
            Map<String, String> map = new HashMap<>();
            map.put("qrType", Bean.QR_CODE_TYPE_PERSIONAL + "");
            map.put("account_id", UserUtils.getMyAccountId());

            return getUserQrCodeBase64String(JSONObject.toJSONString(map));
        }
        return null;
    }
}
