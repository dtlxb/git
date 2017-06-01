package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hply.roundimage.roundImage.RoundedImageView;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */

public class ImageDisplay {
    /*
     * 图片管理工具类
     * 1.加载项目资源图片
     * 2.加载手机存储图片
     * 3.加载网络图片
     *
     * 4.加载资源gif
     * 5.加载手机存储中gif
     * 6.加载网络gif
     *
     * 7.加载圆形本地
     * 8.加载圆形手机存储
     * 9.加载圆形网络
     *
     */

    //是否需要占位图
    public static <T> void loadImage(Context context, T image, ImageView imageView, boolean needPlaceholdeer) {
        if (image != null) {

            RequestOptions options = new RequestOptions();
            options.dontAnimate().dontTransform().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);

            if (needPlaceholdeer) {
                options.placeholder(R.mipmap.image_placeholder);
            }
            Glide.with(context).applyDefaultRequestOptions(options).load(image).into(imageView);
        }
    }

    public static <T> void loadImage(Context context, T image, final ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.dontAnimate()
                .dontTransform()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        try {
            Glide.with(context)
                    .load(image)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(imageView);

        } catch (Exception e) {
            KLog.e("出错了！！！！");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ((Bitmap) image).compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            if (context != null) {
                Glide.with(context)
                        .load(bytes)
                        .apply(options)
                        .into(imageView);
            }
        }
    }

//    /**
//     * 加载回调
//     */
//    public static void loadImage(Context context, String url, ImageView imageView, LoadNetImageListener listener) {
//        mListener = listener;
//        if (!TextUtils.isEmpty(url)) {
//            Glide.with(context)
//                    .load(url)
//                    .placeholder(R.mipmap.image_placeholder)
//                    .thumbnail(0.1f)
//                    .into(new GlideDrawableImageViewTarget(imageView) {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                            super.onResourceReady(resource, animation);
//                            mListener.onResourceReady(resource, animation);//资源加载完成
//                        }
//
//                        @Override
//                        public void onStart() {
//                            super.onStart();
//                            mListener.onStart();
//                        }
//
//                        @Override
//                        public void onStop() {
//                            super.onStop();
//                            mListener.onStop();
//                        }
//
//                        @Override
//                        protected void setResource(GlideDrawable resource) {
//                            super.setResource(resource);
//                            mListener.setResource(resource);
//                        }
//                    });
//        }
//    }

    //=====================================圆角矩形=======================================

    /**
     * @param context 上下文对象
     * @param radius  圆角矩形角度
     * @param image   图片资源
     */
    public static <T> void loadRoundedRectangleImage(Context context, T image, RoundedImageView imageView, int radius) {
        imageView.setCornerRadius(radius);
        Glide.with(context)
                .asBitmap()
                .load(image)
                .into(imageView);
    }

    /**
     * @param context 上下文对象
     * @param image   图片资源
     */
    public static <T> void loadRoundedRectangleImage(Context context, T image, RoundedImageView imageView) {
        imageView.setCornerRadius(AppDevice.dp2px(context, 4));
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .into(imageView);
        } catch (Exception e) {
        }
    }

    //=====================================加载圆形图=======================================

    public static <T> void loadCircleImage(Context context, T image, RoundedImageView imageView) {
        imageView.setOval(true);

        RequestOptions op = new RequestOptions();
        op.placeholder(R.mipmap.image_placeholder);
        op.dontAnimate();

        if (image != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .thumbnail(0.1f)
                    .apply(op)
                    .into(imageView);
        }
    }

    public static <T> void loadCircleImage(Context context, T image, RoundedImageView imageView, boolean needPlaveHolder) {
        imageView.setOval(true);

        RequestOptions op = new RequestOptions();
        if (needPlaveHolder) {
            op.placeholder(R.mipmap.image_placeholder);
        }
        op.dontAnimate();

        if (image != null) {
            Glide.with(context)
                    .asBitmap().load(image)
                    .apply(op)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    /**
     * 图片模糊处理
     */
    public static void BlurImage(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions.bitmapTransform(new BlurTransformation(context));
        options.dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 图片马赛克处理
     */
    public static void mosaicImage(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions.bitmapTransform(new PixelationFilterTransformation(context, 10));
        options.dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 图片灰度、黑白处理
     */
    public static void GrayImage(Context context, String url, ImageView imageView) {
        RequestOptions options = RequestOptions.bitmapTransform(new GrayscaleTransformation(context));
        options.dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

}
