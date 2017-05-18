package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AsyncTaskUtil;

/**
 * author wangjd on 2017/3/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :群主头像拼接.
 */
public class GroupFaceImage<T> {

    private static final int INNER_DIVIDER = 2;//间距单元

    private static final int BITMAP_CANVAS = INNER_DIVIDER * 37;//画布大小，没所谓

    private static int minItemH;//每个小头像宽高

    private List<T> imageUrls;//头像url集合

    private OnMatchingListener matchingListener;

    private Context context;

    private GroupFaceImage(Context context, List<T> imageUrls) {
        this.context = context.getApplicationContext();
        this.imageUrls = imageUrls;
    }

    public static <T> GroupFaceImage<T> getInstance(Context context, List<T> imageUrls) {
        List<T> imgUrls = new ArrayList<>();
        imageUrls = new ArrayList<>(new HashSet<>(imageUrls));

        if (imageUrls.size() > 9) {
            imgUrls = imageUrls.subList(0, 9);
        }

        GroupFaceImage<T> instance = new GroupFaceImage<>(context, imgUrls);

        switch (imgUrls.size()) {
            case 1:
            case 2:
            case 3:
            case 4:
                minItemH = 17 * INNER_DIVIDER;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                minItemH = 11 * INNER_DIVIDER;
                break;
            default:
                minItemH = 11 * INNER_DIVIDER;
                break;
        }
        return instance;
    }

    public void load(OnMatchingListener listener) {
        this.matchingListener = listener;
        final List<Bitmap> bitmaps = new ArrayList<>();
        if (null == imageUrls || imageUrls.isEmpty()) {
            listener.onSuccess(BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_placeholder));
            return;
        }
        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                for (int i = 0; i < imageUrls.size(); i++) {
                    try {
                        Bitmap myBitmap = Glide.with(context)
                                .load(imageUrls.get(i))
                                .asBitmap() //必须
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(minItemH, minItemH)
                                .get();
                        bitmaps.add(myBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        matchingListener.onError(e);
                    }
                }
                matchingListener.onSuccess(mathingBitmap(bitmaps));
            }

            public void onPostExecute() {
            }
        });
    }

    private Bitmap mathingBitmap(List<Bitmap> bitmapList) {
        if (null == bitmapList || bitmapList.isEmpty()) {
            return null;
        }
        Bitmap result = Bitmap.createBitmap(BITMAP_CANVAS, BITMAP_CANVAS, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawRGB(219, 223, 224);//绘制背景颜色
        switch (bitmapList.size()) {
            case 1:
                canvas.drawBitmap(bitmapList.get(0), 10 * INNER_DIVIDER, 10 * INNER_DIVIDER, null);
                return result;
            case 2:
                canvas.drawBitmap(bitmapList.get(0), INNER_DIVIDER, 10 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(1), 19 * INNER_DIVIDER, 10 * INNER_DIVIDER, null);
                return result;
            case 3:
                canvas.drawBitmap(bitmapList.get(0), 10 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(2), 19 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                return result;

            case 4:
                canvas.drawBitmap(bitmapList.get(0), INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), 19 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(2), INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(3), 19 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                return result;

            case 5:
                canvas.drawBitmap(bitmapList.get(0), 7 * INNER_DIVIDER, 7 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), 19 * INNER_DIVIDER, 7 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(2), INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(3), 13 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(4), 25 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                return result;

            case 6:
                canvas.drawBitmap(bitmapList.get(0), INNER_DIVIDER, 7 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), 13 * INNER_DIVIDER, 7 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(2), 25 * INNER_DIVIDER, 7 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(3), INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(4), 13 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(5), 25 * INNER_DIVIDER, 19 * INNER_DIVIDER, null);

                return result;

            case 7:
                canvas.drawBitmap(bitmapList.get(0), 13 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(2), 13 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(3), 25 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(4), INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(5), 13 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(6), 25 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);

                return result;

            case 8:
                canvas.drawBitmap(bitmapList.get(0), 7 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(1), 19 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(2), INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(3), 13 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(4), 25 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(5), INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(5), 13 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(7), 25 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                return result;

            case 9:
                canvas.drawBitmap(bitmapList.get(0), INNER_DIVIDER, INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(1), 13 * INNER_DIVIDER, INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(2), 25 * INNER_DIVIDER, INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(3), INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(4), 13 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(5), 25 * INNER_DIVIDER, 13 * INNER_DIVIDER, null);

                canvas.drawBitmap(bitmapList.get(6), INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(7), 13 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);
                canvas.drawBitmap(bitmapList.get(8), 25 * INNER_DIVIDER, 25 * INNER_DIVIDER, null);

                return result;

            default:
                return null;
        }
    }

    /**
     * 定义回调接口
     */
    public interface OnMatchingListener {
        void onSuccess(Bitmap mathingBitmap);

        void onError(Exception e);
    }

}
