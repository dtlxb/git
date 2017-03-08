package cn.gogoal.im.common.ImageUtils;

/**
 * author wangjd on 2017/3/7 0007.
 * Staff_id 1375
 * phone 18930640263
 */
public class UFileImageHelper {

    private String originalImageUrl;

    private static class UFileImageHelper0Holder {
        private static final UFileImageHelper INSTANCE = new UFileImageHelper();
    }

    public static UFileImageHelper load(String originalImageUrl) {
        UFileImageHelper0Holder.INSTANCE.originalImageUrl=originalImageUrl;
        return UFileImageHelper0Holder.INSTANCE;
    }

    private UFileImageHelper() {
    }

    private static String IAMGE_COMPRESS="iopcmd=convert&dst=jpg&q=";//质量压缩

    private static String IMAGE_ROTATE="iopcmd=rotate&degree=";//旋转处理

    private static String IMAGE_SCALE="iopcmd=thumbnail&type=1&scale=";//长宽按照scale后面的值缩放

    /**
     * 获取UCloud的实际图片URL
     */
    public String getUFileOriginalImage(String miniUrl) {
        return miniUrl.contains("?iopcmd") ? miniUrl.substring(0, miniUrl.indexOf("?iopcmd")) : miniUrl;
    }

    /**质量压缩*/
    public UFileImageHelper compress(int rate){
        originalImageUrl=
                originalImageUrl+
                        (originalImageUrl.contains("?")?"|":"?")
                        +IAMGE_COMPRESS+rate;
        return load(originalImageUrl);
    }

    /**尺寸比例压缩*/
    public UFileImageHelper scale(int scale){
        originalImageUrl=originalImageUrl+(originalImageUrl.contains("?")?"|":"?")+IMAGE_SCALE+scale;
        return load(originalImageUrl);
    }

    /**旋转处理*/
    public UFileImageHelper rotate(int angle){
        originalImageUrl = originalImageUrl+(originalImageUrl.contains("?")?"|":"?")+IMAGE_ROTATE+angle;
        return load(originalImageUrl);
    }

    /**获取图片处理后的地址*/
    public String get(){
        return originalImageUrl;
    }
}