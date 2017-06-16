package cn.gogoal.im.ui.stockviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import hply.com.niugu.StringUtils;
import hply.com.niugu.stock.StockMinuteData;

/**
 * Created by huangxx on 2017/5/31.
 */

public class BitmapChartView extends View {
    private List<StockMinuteData> timesList;
    private float uperBottom;
    private Paint circlePaint;
    private float uperHalfHigh;
    private double initialWeightedIndex;
    private float uperRate;
    private float dataSpacing;
    private int lastPoint;
    private float margin;
    private float out_r = 0;
    private float in_r = 0;
    private float alpha = 0;
    private Bitmap bitmap = null;
    private boolean needDraw;
    private boolean isTimes;

    public BitmapChartView(Context context) {
        super(context);
        init();
    }

    public BitmapChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
            if (needDraw) {
                drawNow(canvas);
            }
        }
    }

    private void drawNow(Canvas canvas) {
        //circlePaint.setColor(this.getContext().getColor(R.color.decision_tab));
        circlePaint.setColor(Color.parseColor("#1a79ff"));
        circlePaint.setStrokeWidth(3);
        circlePaint.setAntiAlias(true);
        StockMinuteData fenshiData = timesList.get(timesList.size() - 1);
        float priceY = (float) (uperBottom - (StringUtils.getDouble(fenshiData.getPrice()) + uperHalfHigh - initialWeightedIndex)
                * uperRate);
        float priceX;
        if (isTimes) {
            priceX = dataSpacing * lastPoint;
        } else {
            priceX = dataSpacing * (timesList.size() - 1);
        }
        canvas.drawCircle(priceX + margin, priceY, (float) (0.9 * in_r), circlePaint);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.out_circle));
        if (out_r < 2 * in_r) {
            out_r += in_r * 0.008;
            alpha -= alpha * 0.008;
            circlePaint.setAlpha((int) alpha);
            canvas.drawCircle(priceX + margin, priceY, out_r, circlePaint);
        } else {
            alpha = 255;
            out_r = in_r;
        }
        invalidate();
    }

    /**
     * 绘制图形
     * needDraw  是否需要绘制交易点
     */
    public void setBitmap(Bitmap bitmap, boolean needDraw, TimesFivesBitmap timesFivesBitmap) {
        this.bitmap = bitmap;
        this.needDraw = false;
        timesList = new ArrayList<>();
        if (timesFivesBitmap == null) {
            return;
        }
        timesList = timesFivesBitmap.getTimeList();
        if (timesList.size() > 0 && timesList != null) {
            this.needDraw = needDraw;
        }
        in_r = (float) (0.8 * timesFivesBitmap.getmSpaceSize());
        out_r = (float) (0.8 * timesFivesBitmap.getmSpaceSize());
        alpha = 255;
        isTimes = timesFivesBitmap.isTimes();
        dataSpacing = timesFivesBitmap.getDataspcing();
        lastPoint = timesFivesBitmap.getLastPoint();
        margin = timesFivesBitmap.getLeftMargin();
        uperHalfHigh = timesFivesBitmap.getUperHalfHigh();
        uperBottom = timesFivesBitmap.getUperBottom();
        uperRate = timesFivesBitmap.getUperRate();
        initialWeightedIndex = timesFivesBitmap.getInitialWeightedIndex();
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        needDraw = false;
        invalidate();
    }
}
