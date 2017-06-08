package com.hply.qrcode_lib.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hply.qrcode_lib.camera.BitmapLuminanceSource;
import com.hply.qrcode_lib.camera.CameraManager;
import com.hply.qrcode_lib.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 二维码扫描工具类
 */
public class CodeUtils {

    public static final String RESULT_TYPE = "result_type";
    public static final String RESULT_STRING = "result_string";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILED = 2;

    public static final String LAYOUT_ID = "layout_id";

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片二维码 Request Code
     */
    public static final int REQUEST_IMAGE = 112;

    /**
     * 解析二维码图片工具类
     *
     * @param analyzeCallback
     */
    public static void analyzeBitmap(String path, AnalyzeCallback analyzeCallback) {

        /**
         * 首先判断图片的大小,若图片过大,则执行图片的裁剪操作,防止OOM
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 400);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        mBitmap = BitmapFactory.decodeFile(path, options);

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(mBitmap, rawResult.getText());
            }
        } else {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        }
    }

    /**
     * 生成二维码图片
     */
    public static Bitmap createImage(Context context,String text, int size, Bitmap logo) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        try {
            Bitmap scaleLogo = getScaleLogo(context,logo, size);
//            Bitmap scaleLogo = getLogoBitmap(context, oriBitmap,oriBitmap.getWidth());

            int offsetX = size / 2;
            int offsetY = size / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (size - scaleWidth) / 2;
                offsetY = (size - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        int pixel = scaleLogo.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (bitMatrix.get(x, y)) {
                                pixel = 0xff000000;
                            } else {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * size + x] = pixel;
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param logo logo原图的bitmap对象
     */
    private static Bitmap getScaleLogo(Context context,Bitmap logo,int size) {
        if (logo == null) return null;

        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(size * 1.0f / 4.5f / logo.getWidth(), size * 1.0f / 4.5f / logo.getHeight());
        matrix.postScale(scaleFactor, scaleFactor);

        Bitmap bitmap = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        RoundedBitmapDrawable drawable=RoundedBitmapDrawableFactory.create(context.getResources(),bitmap);
        drawable.setAntiAlias(true);
        drawable.setCornerRadius(10);
        return drawableToBitmap(drawable);
    }

    /**
     * 解析二维码结果
     */
    public interface AnalyzeCallback {

        void onAnalyzeSuccess(Bitmap mBitmap, String result);

        void onAnalyzeFailed();
    }


    /**
     * 为CaptureFragment设置layout参数
     *
     * @param captureFragment;
     * @param layoutId;
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        if (captureFragment == null || layoutId == -1) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        captureFragment.setArguments(bundle);
    }

    public static void isLightEnable(boolean isEnable) {
        if (isEnable) {

            Camera camera = CameraManager.get().getCamera();
            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameter);
            }
        } else {
            Camera camera = CameraManager.get().getCamera();
            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameter);
            }
        }
    }

    //================================bitmap处理=========================================

    /**
     * @param context    上下文
     * @param logoBitmap 二维码logo源文件bitmap对象
     * @param size       logo部分的大小
     */
    private static Bitmap getLogoBitmap(Context context, Bitmap logoBitmap, int size) {
        int radiu=10;
        //1.
        Bitmap bitmapBg = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapBg);
        canvas.drawARGB(255, 228, 228, 228);
        RoundedBitmapDrawable drawableBg = RoundedBitmapDrawableFactory.create(context.getResources(), bitmapBg);
        drawableBg.setMipMap(false);
        drawableBg.setCornerRadius(radiu);

        //2.
        int width = logoBitmap.getWidth();
        int height = logoBitmap.getHeight();
        int newWidth = size - 52;//白边
        int newHeight = size - 52;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(logoBitmap, 0, 0, newWidth,//缩放logobitmap
                newHeight, matrix, true);

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), resizedBitmap);
        drawable.setMipMap(false);
        drawable.setCornerRadius(radiu);

        return drawableToBitmap(new LayerDrawable(new Drawable[]{drawableBg, drawable}));
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
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
}
