package cn.gogoal.im.common.ggqrcode;

import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

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
        KLog.e(jsonBean);
        return urlHead + new String(Base64.encode(jsonBean.getBytes(), Base64.NO_WRAP));
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
//            Bean bean = new Bean();
//            bean.setQrType(Bean.QR_CODE_TYPE_PERSIONAL);
//            bean.setAccount_id(UserUtils.getMyAccountId());
//
//            bean.setConv_id("");

            Map<String,String> map=new HashMap<>();
            map.put("qrType",Bean.QR_CODE_TYPE_PERSIONAL+"");
            map.put("account_id",UserUtils.getMyAccountId());

            return urlHead + getUserQrCodeBase64String(JSONObject.toJSONString(map));
        }
        return null;
    }
}
