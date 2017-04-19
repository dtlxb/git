package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import cn.gogoal.im.common.AppDevice;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BorderCircleTransformation implements Transformation<Bitmap> {

    private Context context;

    private int colorBorder = 0xfff0f0f0;

    private int borderWidth = 2;

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setColorBorder(@ColorInt int colorBorder) {
        this.colorBorder = colorBorder;
    }

    private BitmapPool mBitmapPool;

    public BorderCircleTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
        this.context = context;
    }

    public BorderCircleTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }

        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;

        Paint boardPaint = new Paint();
        boardPaint.setAntiAlias(true);

        boardPaint.setStrokeWidth(AppDevice.dp2px(context, borderWidth));
        boardPaint.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形
        boardPaint.setColor(colorBorder);

        canvas.drawCircle(r, r, r-boardPaint.getStrokeWidth(), boardPaint);//
        canvas.drawCircle(r, r, r-boardPaint.getStrokeWidth(), paint);

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "BorderCircleTransformation()";
    }
}
