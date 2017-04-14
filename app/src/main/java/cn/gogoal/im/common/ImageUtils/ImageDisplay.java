package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;

import cn.gogoal.im.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
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

    public static void loadResImage(Context context, int resId, ImageView view) {
        Glide.with(context).load(resId)
                .into(view);
    }

    public static void loadFileImage(Context context, File fileImage, ImageView view) {
        if (fileImage.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(fileImage))
                    .placeholder(R.mipmap.image_placeholder)
                    .into(view);
        }
    }

    public static void loadNetImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .dontTransform()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.image_placeholder)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    public static void loadNetImage(Context context, String url, ImageView imageView, int needPlaceholder) {
        if (!TextUtils.isEmpty(url)) {
            DrawableRequestBuilder<String> builder = Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .dontTransform()
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .thumbnail(0.1f);
            if (needPlaceholder == 0x00) {
                builder.into(imageView);
            } else {
                builder.placeholder(R.mipmap.image_placeholder);
                builder.into(imageView);
            }

        }
    }

    public static void loadChartImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            DrawableRequestBuilder<String> builder = Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .dontTransform()
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT);
            builder.into(imageView);

        }
    }

    public static void loadNetImage(Context context, String url, ImageView imageView, boolean skipMemoryCache) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .skipMemoryCache(skipMemoryCache)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.image_placeholder)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    /**
     * 加载回调
     */
    public static void loadNetImage(Context context, String url, ImageView imageView, LoadNetImageListener listener) {
        mListener = listener;
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.image_placeholder)
                    .thumbnail(0.1f)
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            mListener.onResourceReady(resource, animation);//资源加载完成
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            mListener.onStart();
                        }

                        @Override
                        public void onStop() {
                            super.onStop();
                            mListener.onStop();
                        }

                        @Override
                        protected void setResource(GlideDrawable resource) {
                            super.setResource(resource);
                            mListener.setResource(resource);
                        }
                    });
        }
    }

    //=====================================圆角矩形=======================================

    /**
     * @param context 上下文对象
     * @param radius  圆角矩形角度
     * @param res     图片资源res
     */
    public static void loadRoundedRectangleImage(Context context, ImageView imageView, int radius, @DrawableRes int res) {
        Glide.with(context)
                .load(res)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    /**
     * @param context  上下文对象
     * @param radius   圆角矩形角度
     * @param imageUrl 图片url地址
     */
    public static void loadRoundedRectangleImage(Context context, ImageView imageView, int radius, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    /**
     * @param context   上下文对象
     * @param radius    圆角矩形角度
     * @param imageFile 磁盘图片
     */
    public static void loadRoundedRectangleImage(Context context, ImageView imageView, int radius, File imageFile) {
        Glide.with(context)
                .load(Uri.fromFile(imageFile))
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    //=====================================加载GIF图=======================================
    public static void loadGifResImage(Context context, int resId, ImageView view) {
        Glide.with(context).load(resId)
                .asGif()
                .into(view);
    }

    public static void loadGifFileImage(Context context, File fileImage, ImageView view) {
        if (fileImage.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(fileImage))
                    .asGif()
                    .placeholder(R.mipmap.image_placeholder)
                    .into(view);
        }
    }

    public static void loadGifNetImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .asGif()
                    .placeholder(R.mipmap.image_placeholder)
                    .into(imageView);
        }
    }

    /**
     * 加载网络gif回调
     */
    public static void loadGifNetImage(Context context, String url, ImageView imageView, LoadNetImageListener listener) {
        mListener = listener;
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.image_placeholder)
                    .thumbnail(0.1f)
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            mListener.onResourceReady(resource, animation);//资源加载完成
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            mListener.onStart();
                        }

                        @Override
                        public void onStop() {
                            super.onStop();
                            mListener.onStop();
                        }

                        @Override
                        protected void setResource(GlideDrawable resource) {
                            super.setResource(resource);
                            mListener.setResource(resource);
                        }
                    });
        }
    }

    //=====================================加载圆形图=======================================
    public static void loadCircleResImage(Context context, int resId, ImageView view) {
        Glide.with(context).load(resId)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(view);
    }

    public static void loadCircleFileImage(Context context, File fileImage, ImageView view) {
        if (fileImage.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(fileImage))
                    .bitmapTransform(new CropCircleTransformation(context))
                    .placeholder(R.mipmap.image_placeholder)
                    .into(view);
        }
    }

    public static void loadCircleNetImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.image_placeholder)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    /**
     * 加载回调
     */
    public static void loadCircleNetImage(Context context, String url, ImageView imageView, LoadNetImageListener listener) {
        mListener = listener;
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.image_placeholder)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .thumbnail(0.1f)
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            mListener.onResourceReady(resource, animation);//资源加载完成
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            mListener.onStart();
                        }

                        @Override
                        public void onStop() {
                            super.onStop();
                            mListener.onStop();
                        }

                        @Override
                        protected void setResource(GlideDrawable resource) {
                            super.setResource(resource);
                            mListener.setResource(resource);
                        }
                    });
        }
    }

    //=========================================头像图片处理=======================================

    /**
     * 头像图片很小，禁用图片缓存，方便用户修改后实时刷新，防止残留
     */
    public static void loadResAvatar(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .skipMemoryCache(true)//禁止内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁止内存缓存
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void loadNetAvatar(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)//禁止内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁止内存缓存
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    /*加圆圈——资源图*/
//    public static void loadResAvatarWithBorder(Context context,int resId,ImageView imageView){
//        BorderView borderView=new BorderView(context,imageView);
//        borderView.setBackgroundResource(R.drawable.shape_circle_white);
//        Glide.with(context)
//                .load(resId)
//                .skipMemoryCache(true)//禁止内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁止内存缓存
//                .bitmapTransform(new CropCircleTransformation(context))
//                .into(imageView);
//    }
    /*加圆圈——网络图*/
    public static void loadNetAvatarWithBorder(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)//禁止内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁止内存缓存
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }


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


    /**图片下载*/
//    public static void DownloadImage(final Context context, String url, final File saveFile){
//        final WeakReferenceHandler handler = new WeakReferenceHandler(context,saveFile.getAbsolutePath());
//        /**
//         * 启动图片下载线程
//         */
//        DownLoadImageService service = new DownLoadImageService(context.getApplicationContext(),
//                url,
//                new DownLoadImageService.ImageDownLoadCallBack() {
//
//                    @Override
//                    public void onDownLoadSuccess(File file, Bitmap bitmap) {
//                        // 在这里执行图片保存方法
//                        Message message = handler.obtainMessage();
//                        message.what = 10086;
//                        message.obj = file.getAbsolutePath();
//                        handler.sendMessageDelayed(message, 1000);
//                    }
//
//                    @Override
//                    public void onDownLoadFailed() {
//                        // 图片保存失败
//                        Message message = handler.obtainMessage();
//                        message.what = 500;
//                        message.obj = "failed";
//                        handler.sendMessage(message);
//                    }
//                });
//        //启动图片下载线程
//        new Thread(service).start();
//    }
//    private static class WeakReferenceHandler extends Handler {
//        Context mContext;
//        String filePath;
//
//        public WeakReferenceHandler(Context context,String filePath) {
//            this.filePath = filePath;
//            mContext = context;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 10086:
//                    KLog.e(msg.obj.toString());
//                    Toast.makeText(mContext.getApplicationContext(), "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
//                    MediaScannerConnection.scanFile(mContext, new String[]{msg.obj.toString()}, null, null);
//                    break;
//                case 500:
//                    Toast.makeText(mContext.getApplicationContext(), "图片保存出错", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }

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
