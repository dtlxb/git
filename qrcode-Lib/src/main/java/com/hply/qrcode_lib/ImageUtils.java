package com.hply.qrcode_lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * author wangjd on 2017/6/8 0008.
 * Staff_id 1375
 * phone 18930640263
 * description :二维码图片的一些操作
 */
public class ImageUtils {

    /**
     * drawable转bitmap对象
     */

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);//取 drawable 的颜色格式);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /*拉伸bitmap*/
    public static Bitmap strechingBitmap(Bitmap bmp, int mScreenWidth, int mScreenHeight) {
        return Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
    }

    /**
     * ☆给bitmap添加白边，圆角
     *
     * @param context 上下文
     * @param bitmap  原始图像
     * @param radius  圆弧半径
     * @param border  边距宽度
     */
    public static Bitmap makeLogoQrcode(Context context, Bitmap bitmap, int radius, int border) {

        int innerWidth = bitmap.getWidth();

        int innerHeight = bitmap.getHeight();

        Bitmap bitmapResult = Bitmap.createBitmap(innerWidth + 2 * border,
                innerHeight + 2 * border, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawARGB(255, 228, 228, 228);

        canvas.drawBitmap(getRoundBitmap(context,bitmap,radius),border, border, null);

        return getRoundBitmap(context,bitmapResult,radius);
    }

    private static Bitmap getRoundBitmap(Context ctx,Bitmap bmp,int radius){
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), bmp);
        drawable.setCornerRadius(radius);
        drawable.setMipMap(false);
        drawable.setAntiAlias(true);
        drawable.setDither(true);
        drawable.setFilterBitmap(true);
        return drawable2Bitmap(drawable);
    }
}
