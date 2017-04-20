package cn.gogoal.im.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.socks.library.KLog;

import java.io.File;

import cn.gogoal.im.common.ImageUtils.ImageUtils;

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

    private static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            SparseArray<Object> map = (SparseArray<Object>) msg.obj;

            Bitmap bitmap = (Bitmap) map.get(10086);

            String name = (String) map.get(10010);

            if (bitmap == null) {
                if (callBack != null) {
                    callBack.error("bitmap is null");
                }
            } else {
                ImageUtils.saveBitmapFile(bitmap, new File(dirs), name + ".png");
                if (callBack != null) {
                    callBack.success();
                }
            }
            super.handleMessage(msg);
        }
    };

    private DownloadUtils(String dir) {
        if (TextUtils.isEmpty(dir)) {
            dirs = DEFAULT_DOWNLOAD_PATH;
        } else {
            dirs = dir;
        }
    }

    public static DownloadUtils getInstance(String dir) {
        return new DownloadUtils(dir);
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
        callBack = mCallBack;

        KLog.e(imageUrl);

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {
                try {
                    Bitmap myBitmap = Glide.with(context)
                            .load(imageUrl)
                            .asBitmap() //必须
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();

                    Message message = handler.obtainMessage();
                    SparseArray<Object> map = new SparseArray<>();
                    map.put(10086, myBitmap);
                    if (TextUtils.isEmpty(saveName)) {
                        map.put(10010, MD5Utils.getMD5EncryptyString16(imageUrl));
                    } else {
                        map.put(10010, saveName);
                    }
                    message.obj = map;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.error(e.getMessage());
                    }
                }
            }

            @Override
            public void onPostExecute() {

            }
        });

    }
}
