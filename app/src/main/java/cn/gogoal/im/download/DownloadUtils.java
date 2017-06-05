package cn.gogoal.im.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.ImageUtils.ImageUtils;
import okhttp3.Call;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :下载工具类封装.
 */
public class DownloadUtils {

    private static DownloadCallBack callBack;

    public static final String DEFAULT_DOWNLOAD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .getAbsolutePath() + File.separator + "GoGoal";
    private static String dirs;

    private DownloadUtils(String dir) {
        if (TextUtils.isEmpty(dir)) {
            dirs = DEFAULT_DOWNLOAD_PATH;
        } else {
            dirs = dir;
        }
    }

    public static DownloadUtils getInstance(String parentDir) {
        return new DownloadUtils(parentDir);
    }

    /**
     * 下载图片
     *
     * @param context   上下文对象;
     * @param imageUrl  下载图片地址;
     * @param saveName  下载保存的名字，如果传null或者"",就以图片地址生成16位md5码作为图片名
     * @param mCallBack 下载回调，成功与否
     */
    public void downloadPicture(final Context context, final String imageUrl, final String saveName, final DownloadCallBack mCallBack) {
        if (mCallBack != null) {
            callBack = mCallBack;
        }
        ImageUtils.getUrlBitmap(context, imageUrl, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                try {
                    savaFileToSD(saveName, ImageUtils.getImageSuffix(imageUrl), resource);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.error(e.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 下载图片
     *
     * @param context   上下文对象;
     * @param imageUrl  下载图片地址;
     * @param saveName  下载保存的名字，如果传null或者"",就以图片地址生成16位md5码作为图片名
     * @param mCallBack 下载回调，成功与否
     */
    public void ggDownloadImage(final Context context, final String imageUrl, final String saveName, final DownloadCallBack mCallBack) {
        if (mCallBack != null) {
            callBack = mCallBack;
        }

        OkHttpUtils.get().url(imageUrl).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.error(e.getMessage());
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                ImageUtils.saveImageToSD(context,
                        dirs+ File.separator + saveName + ImageUtils.getImageSuffix(imageUrl),
                        response, 100);
            }
        });
    }

    //往SD卡写入文件的方法
    private void savaFileToSD(String filename, String suffer, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir1 = new File(dirs);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            File iamgeFile = new File(dirs, filename + suffer);
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = null;
            BufferedOutputStream bos = null;
            try {
                output = new FileOutputStream(iamgeFile);

                output = new FileOutputStream(iamgeFile);
                bos = new BufferedOutputStream(output);
                bos.write(baos.toByteArray());
                //将bytes写入到输出流中
//                Toast.makeText(context, "图片已成功保存到" + filePath, Toast.LENGTH_SHORT).show();
                if (callBack != null) {
                    callBack.success();
                }
            } catch (Exception e) {
                if (callBack != null) {
                    callBack.error(e.getMessage());
                }
                e.printStackTrace();
            } finally {
                FileUtil.closeIO(output);
                FileUtil.closeIO(bos);
            }
        } else {
            if (callBack != null) {
                callBack.error("SD卡不存在或者不可读写");
            }
        }
    }

}
