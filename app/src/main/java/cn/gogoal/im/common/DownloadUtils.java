package cn.gogoal.im.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

    private static String dirs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .getAbsolutePath() + File.separator + "GoGoal";

    private static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            SparseArray<Object> map= (SparseArray<Object>) msg.obj;

            Bitmap bitmap= (Bitmap) map.get(10086);

            String name= (String) map.get(10010);

            if (bitmap == null) {
                if (callBack != null) {
                    callBack.error("bitmap is null");
                }
            } else {
                ImageUtils.saveBitmapFile(bitmap, dirs,name + ".png");
                if (callBack != null) {
                    callBack.success();
                }
            }
            super.handleMessage(msg);
        }
    };

    private DownloadUtils(String dir) {
        dirs = dir;
    }

    /**
     * 下载图片
     */
    public static void downloadPicture(final Activity context, final String imageUrl, final DownloadCallBack mCallBack) {
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
                    SparseArray<Object> map=new SparseArray<>();
                    map.put(10086,myBitmap);
                    map.put(10010,MD5Utils.getMD5EncryptyString32(imageUrl));
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
