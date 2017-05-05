package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.view.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
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

    private static LoadNetImageListener mListener;

    //是否需要占位图
    public static <T> void loadImage(Context context, T image, ImageView imageView, boolean needPlaceholdeer) {
        if (image != null) {
            DrawableRequestBuilder<T> requestBuilder = Glide.with(context)
                    .load(image)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT);

            if (!needPlaceholdeer) {
                requestBuilder.into(imageView);
            } else {
                requestBuilder.placeholder(R.mipmap.image_placeholder).into(imageView);
            }
        }
    }

    public static <T> void loadImage(Context context, T image, ImageView imageView) {
        Glide.with(context)
                .load(image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.image_placeholder)
                .thumbnail(0.1f)
                .into(imageView);
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
    public static <T> void loadRoundedRectangleImage(Context context, ImageView imageView, int radius, T image) {
        Glide.with(context)
                .load(image)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    /**
     * @param context 上下文对象
     * @param image   图片资源
     */
    public static <T> void loadRoundedRectangleImage(Context context, ImageView imageView, T image) {
        Glide.with(context)
                .load(image)
                .bitmapTransform(new RoundedCornersTransformation(context, AppDevice.dp2px(context,4), 0))
                .into(imageView);
    }

    //=====================================加载GIF图=======================================
    public static <T> void loadGifImage(Context context, T resId, ImageView view) {
        Glide.with(context).load(resId)
                .asGif()
                .into(view);
    }

    //=====================================加载圆形图=======================================

    public static <T> void loadCircleImage(Context context, T image, CircleImageView imageView) {
        if (image != null) {
            Glide.with(context).load(image)
                    .asBitmap()
                    .thumbnail(0.1f)
                    .placeholder(R.mipmap.image_placeholder)
                    .into(imageView);
        }
    }

    public static <T> void loadCircleImage(Context context, T image, CircleImageView imageView, boolean needPlaveHolder) {
        if (image != null) {
            BitmapRequestBuilder<T, Bitmap> builder = Glide.with(context).load(image)
                    .asBitmap()
                    .thumbnail(0.1f);
            if (needPlaveHolder) {
                builder.placeholder(R.mipmap.image_placeholder).into(imageView);
            } else {
                builder.into(imageView);
            }
        }
    }

//    /**
//     * 加载回调
//     */
//    public static void loadCircleNetImage(Context context, String url, ImageView imageView, LoadNetImageListener listener) {
//        mListener = listener;
//        if (!TextUtils.isEmpty(url)) {
//            Glide.with(context)
//                    .load(url)
//                    .placeholder(R.mipmap.image_placeholder)
//                    .bitmapTransform(new CropCircleTransformation(context))
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

    /**
     * 图片模糊处理
     */
    public static void BlurImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new BlurTransformation(context))
                .into(imageView);
    }

    /**
     * 图片马赛克处理
     */
    public static void mosaicImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new BlurTransformation(context))
                .bitmapTransform(new PixelationFilterTransformation(context, 10))
                .into(imageView);
    }

    /**
     * 图片灰度、黑白处理
     */
    public static void GrayImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new GrayscaleTransformation(context))
                .into(imageView);
    }


    /**
     * 网络图片加载的监听接口
     */
    private interface LoadNetImageListener {
        void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation);

        void onStart();

        void onStop();

        void setResource(GlideDrawable resource);
    }
}
