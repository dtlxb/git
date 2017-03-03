package com.gogoal.app.common.ImageUtils;

/**
 * author wangjd on 2017/3/2 0002.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * UFle图片url处理帮助类
 * http://hackfile.ufile.ucloud.cn/avatar_67_500%C3%97500.jpg?iopcmd=rotate&degree=180|iopcmd=thumbnail&type=1&scale=40|iopcmd=convert&dst=jpg&q=40
 */
public class UFileImageHelper {

    private static String IAMGE_COMPRESS="iopcmd=convert&dst=jpg&q=%s";//质量压缩

    private static String IMAGE_CROP="";//尺寸压缩

    private static String IMAGE_ROTATE="iopcmd=rotate&degree=%s";//旋转处理

    private static String IMAGE_SCALE="iopcmd=thumbnail&type=1&scale=%s";//长宽按照scale后面的值缩放

    /**
     * 获取UCloud的实际头像大图
     */
    public static String getUFileOriginalImage(String miniUrl) {
        return miniUrl.contains("?iopcmd") ? miniUrl.substring(0, miniUrl.indexOf("?iopcmd")) : miniUrl;
    }
}
