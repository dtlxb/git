package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.gogoal.im.common.AppDevice;

/**
 * author wangjd on 2017/4/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Ring extends View {

    private int ringBoardWidth;

    private int ringColor = 0xffff0000;

    private Paint p;

    public Ring(Context context) {
        this(context, null, 0);
    }

    public Ring(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ring(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ringBoardWidth = AppDevice.dp2px(context, 4);

        p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(AppDevice.dp2px(context, 4));
        p.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形
        p.setColor(ringColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - ringBoardWidth, p);
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
    }

    public void setRingBoardWidth(int ringBoardWidth) {
        this.ringBoardWidth = ringBoardWidth;
    }
}
