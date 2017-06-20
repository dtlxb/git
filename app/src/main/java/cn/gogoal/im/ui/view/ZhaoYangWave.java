package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :登录页波纹
 */
public class ZhaoYangWave extends View {

    /**
     * y=Asin(ωx+φ)+k
     */

    private Paint paintA;
    private Paint paintB;
    private Paint paintC;
//    private Paint paintD;

    private Path pathA;
    private Path pathB;
    private Path pathC;
//    private Path pathD;

    private DrawFilter mDrawFilter;
//    private float φ1;
    private float φ2;
    private float φ3;
    private float φ4;

    private int A;
    public ZhaoYangWave(Context context) {
        this(context, null);
    }

    public ZhaoYangWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        A=getScreenHeight(context)/60;

        pathA = new Path();
        pathB = new Path();
        pathC = new Path();
//        pathD = new Path();

        paintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaint(paintA, 255);
        paintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaint(paintB, 200);
        paintC = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaint(paintC, 115);
//        paintD = new Paint(Paint.ANTI_ALIAS_FLAG);
//        initPaint(paintD, 45);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    private void initPaint(Paint paint, int alpha) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor("#FF4081"));
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setAlpha(alpha);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);

        pathA.reset();
        pathB.reset();
        pathC.reset();
//        pathD.reset();

//        φ1 += 0.02F;
        φ2 += 0.03F;
        φ3 += 0.04F;
        φ4 += 0.05F;

        float y1, y2, y3/*, y4*/;

        double ω = 2.3*Math.PI /getWidth();


        pathA.moveTo(getLeft(), getBottom());
        pathB.moveTo(getLeft(), getBottom());
        pathC.moveTo(getLeft(), getBottom());
//        pathD.moveTo(getLeft(), getBottom());

        for (float x = 0; x < getWidth(); x++) {
            y1 = (float) (A*Math.cos(ω * x + φ4) + 80);
            y2 = (float) (A*Math.cos(ω * x + φ3) + 80);
            y3 = (float) (A*Math.sin(ω * x + φ2) + 80);
//            y4 = (float) (A*Math.sin(ω * x + φ1) + 80);

            pathA.lineTo(x, y1);
            pathB.lineTo(x, y2);
            pathC.lineTo(x, y3);
//            pathD.lineTo(x, y4);
        }
        pathA.lineTo(getRight(), getBottom());
        pathB.lineTo(getRight(), getBottom());
        pathC.lineTo(getRight(), getBottom());
//        pathD.lineTo(getRight(), getBottom());

        canvas.drawPath(pathA, paintA);
        canvas.drawPath(pathB, paintB);
        canvas.drawPath(pathC, paintC);
//        canvas.drawPath(pathD, paintD);

        invalidate();
    }

    private int getScreenHeight(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
}