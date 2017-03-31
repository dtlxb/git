package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.common.AsyncTaskUtil;

/**
 * author wangjd on 2017/3/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :群主头像拼接.
 */
public class GroupFaceImage {

    private static int minItemH;

    private List<String> imageUrls;

    private OnMatchingListener matchingListener;

    private Context context;

    private GroupFaceImage(Context context, List<String> imageUrls) {
        this.context = context.getApplicationContext();
        this.imageUrls = imageUrls;
    }

    public static GroupFaceImage getInstance(Context context, List<String> imageUrls) {
        GroupFaceImage instance = new GroupFaceImage(context, imageUrls);
        KLog.e("初始化成功");
        KLog.e(JSONObject.toJSONString(imageUrls));
        switch (imageUrls.size()) {
            case 1:
            case 2:
            case 3:
            case 4:
                minItemH = 144 / 2;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                minItemH = 144 / 3;
                break;
            default:
                break;
        }
        return instance;
    }

    public void load(OnMatchingListener listener) {
        this.matchingListener = listener;
        final List<Bitmap> bitmaps = new ArrayList<>();
        if (null == imageUrls || imageUrls.isEmpty()) {
            throw new NullPointerException("图像集合不能为空");
        }
        if (imageUrls.size() > 9) {
            imageUrls = imageUrls.subList(0, 9);
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
                                .into(minItemH, minItemH)
                                .get();
                        bitmaps.add(myBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        matchingListener.onError(e);
                    }
                }

                matchingListener.onSuccess(mathingBitmap(bitmaps));
//                switch (imageUrls.size()) {
//                    case 1:
//                        listener.onSuccess(bitmaps.get(0));
//                        break;
//                    case 2:
//                        listener.onSuccess(addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1)));
//                        break;
//                    case 3:
//                        listener.onSuccess(addVerticalBitmap(bitmaps.get(0), addHorizontalBitmap(bitmaps.get(1), bitmaps.get(2))));
//                        break;
//                    case 4:
//                        listener.onSuccess(addVerticalBitmap(
//                                addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1)),
//                                addHorizontalBitmap(bitmaps.get(2), bitmaps.get(3))
//                        ));
//                        break;
//                    case 5:
//                        listener.onSuccess(addVerticalBitmap(
//                                addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1)),
//                                addHorizontalBitmap(bitmaps.get(2), bitmaps.get(3), bitmaps.get(4))
//                        ));
//                        break;
//                    case 6:
//                        listener.onSuccess(addVerticalBitmap(
//                                addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1), bitmaps.get(2)),
//                                addHorizontalBitmap(bitmaps.get(3), bitmaps.get(4), bitmaps.get(5))
//
//                        ));
//                    case 7:
//                        listener.onSuccess(addVerticalBitmap(
//                                bitmaps.get(0),
//                                addHorizontalBitmap(bitmaps.get(1), bitmaps.get(2), bitmaps.get(3)),
//                                addHorizontalBitmap(bitmaps.get(4), bitmaps.get(5), bitmaps.get(6))
//                        ));
//                    case 8:
//                        listener.onSuccess(addVerticalBitmap(
//                                addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1)),
//                                addHorizontalBitmap(bitmaps.get(2), bitmaps.get(3), bitmaps.get(4)),
//                                addHorizontalBitmap(bitmaps.get(5), bitmaps.get(6), bitmaps.get(7))
//                        ));
//                        break;
//                    case 9:
//                        listener.onSuccess(addVerticalBitmap(
//                                addHorizontalBitmap(bitmaps.get(0), bitmaps.get(1), bitmaps.get(2)),
//                                addHorizontalBitmap(bitmaps.get(3), bitmaps.get(4), bitmaps.get(5)),
//                                addHorizontalBitmap(bitmaps.get(6), bitmaps.get(7), bitmaps.get(8))
//                        ));
//                        break;
//                }
            }

            public void onPostExecute() {

            }
        });

    }

    private Bitmap addHorizontalBitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth() + second.getWidth();
        int height = Math.max(first.getHeight(), second.getHeight());
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;
    }

    private Bitmap addHorizontalBitmap(Bitmap first, Bitmap second, Bitmap third) {
        Bitmap bitmapNewFirst = addHorizontalBitmap(first, second);
        int width = bitmapNewFirst.getWidth() + third.getWidth();
        int height = Math.max(bitmapNewFirst.getHeight(), third.getHeight());
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmapNewFirst, 0, 0, null);
        canvas.drawBitmap(third, bitmapNewFirst.getWidth(), 0, null);
        return result;
    }

    private Bitmap addVerticalBitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }

    private Bitmap addVerticalBitmap(Bitmap first, Bitmap second, Bitmap third) {
        Bitmap bitmapNewFirst = addVerticalBitmap(first, second);
        int width = Math.max(bitmapNewFirst.getWidth(), third.getWidth());
        int height = bitmapNewFirst.getHeight() + third.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmapNewFirst, 0, 0, null);
        canvas.drawBitmap(third, 0, bitmapNewFirst.getHeight(), null);
        return result;
    }

    public Bitmap mathingBitmap(List<Bitmap> bitmapList) {
        if (null == bitmapList || bitmapList.isEmpty()) {
            return null;
        }
        int innerWidth = bitmapList.get(0).getWidth();
        int resultWidth = bitmapList.size() == 1 ?
                2 * innerWidth :
                (bitmapList.size() >= 5 ?
                        (3 * innerWidth + 4) :
                        (2 * innerWidth + 2));

        Bitmap result = Bitmap.createBitmap(resultWidth, resultWidth, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        canvas.drawRGB(240, 240, 240);
        switch (bitmapList.size()) {
            case 1:
                return bitmapList.get(0);
            case 2:
                return addHorizontalBitmap(bitmapList.get(0), bitmapList.get(1));
            case 3:

                canvas.drawBitmap(bitmapList.get(0), resultWidth / 4, 0, null);

                canvas.drawBitmap(bitmapList.get(1), 0, innerWidth, null);

                canvas.drawBitmap(bitmapList.get(2), innerWidth + 2,innerWidth, null);

                return result;

            case 4:
                canvas.drawBitmap(bitmapList.get(0), 0, 0, null);

                canvas.drawBitmap(bitmapList.get(1), innerWidth + 2, 0, null);

                canvas.drawBitmap(bitmapList.get(2), 0, innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(2), innerWidth + 2, innerWidth + 2, null);

                return result;

            case 5:
                canvas.drawBitmap(bitmapList.get(0), resultWidth / 6, resultWidth / 6, null);

                canvas.drawBitmap(bitmapList.get(1), resultWidth / 6 + 2 + innerWidth, resultWidth / 6, null);

                canvas.drawBitmap(bitmapList.get(2), 0, resultWidth / 6 + innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(3), innerWidth + 2, resultWidth / 6 + innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(4), 2 * innerWidth + 4, resultWidth / 6 + innerWidth + 2, null);

                return result;

            case 6:
                canvas.drawBitmap(bitmapList.get(0), 0, resultWidth / 6, null);

                canvas.drawBitmap(bitmapList.get(1), 2 + innerWidth, resultWidth / 6, null);

                canvas.drawBitmap(bitmapList.get(2), 4 + 2 * innerWidth, resultWidth / 6, null);


                canvas.drawBitmap(bitmapList.get(3), 0, resultWidth / 6 + innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(4), innerWidth + 2, resultWidth / 6 + innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(5), 2 * innerWidth + 4, resultWidth / 6 + innerWidth + 2, null);

                return result;

            case 7:
                canvas.drawBitmap(bitmapList.get(0), innerWidth, 0, null);

                canvas.drawBitmap(bitmapList.get(1), 0, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(2), innerWidth + 2, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(3), 2 * innerWidth + 4, innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(4), 0, innerWidth * 2 + 4, null);
                canvas.drawBitmap(bitmapList.get(5), innerWidth + 2, innerWidth * 2 + 4, null);
                canvas.drawBitmap(bitmapList.get(6), innerWidth * 2 + 4, innerWidth * 2 + 4, null);

                return result;
            case 8:
                canvas.drawBitmap(bitmapList.get(0), resultWidth / 6, 0, null);
                canvas.drawBitmap(bitmapList.get(1), resultWidth / 6 + innerWidth + 2, 0, null);

                canvas.drawBitmap(bitmapList.get(2), 0, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(3), innerWidth + 2, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(4), 2 * innerWidth + 4, innerWidth + 2, null);

                return result;

            case 9:

                canvas.drawBitmap(bitmapList.get(0), 0, 0, null);
                canvas.drawBitmap(bitmapList.get(1), innerWidth, 0, null);
                canvas.drawBitmap(bitmapList.get(2), 2 * innerWidth + 4, 0, null);

                canvas.drawBitmap(bitmapList.get(3), 0, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(4), innerWidth + 2, innerWidth + 2, null);
                canvas.drawBitmap(bitmapList.get(5), 2 * innerWidth + 4, innerWidth + 2, null);

                canvas.drawBitmap(bitmapList.get(6), 0, innerWidth * 2 + 4, null);
                canvas.drawBitmap(bitmapList.get(7), innerWidth + 2, innerWidth * 2 + 4, null);
                canvas.drawBitmap(bitmapList.get(8), innerWidth * 2 + 4, innerWidth * 2 + 4, null);

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
