package cn.gogoal.im.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * author wangjd on 2016/12/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */

public class MD5Utils {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**32位md5加密*/
    public static String getMD5EncryptyString32(String data){
        return bytes2HexString(encryptMD5(data.getBytes())).toUpperCase(Locale.ENGLISH);
    }

    /**16位md5加密*/
    public static String getMD5EncryptyString16(String data) {
        return getMD5EncryptyString32(data).substring(8,24);
    }

    /**
     * MD5加密
     */
    private static String encryptMD5ToString(byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    //二进制流md5加密
    private static byte[] encryptMD5(byte[] data) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //二进制数组转字符串
    private static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
