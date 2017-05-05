package hply.com.niugu.stock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hply.com.niugu.StringUtils;

/**
 * Created by huangxx on 2015/10/14.
 */
public class BitmapChartView extends View {
    private List<StockMinuteData> timesList;
    private StockMinuteData fenshiData = new StockMinuteData();
    private float uperBottom;
    private Paint circlepaint;
    private float uperHalfHigh;
    private double initialWeightedIndex;
    private float uperRate;
    private float dataSpacing;
    private int lastPoint;
    private float margin;
    private float out_r = 0;
    private float in_r = 0;
    private float alph = 0;
    private Bitmap bitmap = null;
    private boolean needDraw;
    private boolean isTimes;
    ;

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
        circlepaint = new Paint();
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
        circlepaint.setColor(0x1a79ff);
        circlepaint.setStrokeWidth(3);
        circlepaint.setAntiAlias(true);
        fenshiData = timesList.get(timesList.size() - 1);
        float priceY = (float) (uperBottom - (StringUtils.getDouble(fenshiData.getPrice()) + uperHalfHigh - initialWeightedIndex)
                * uperRate);
        float priceX;
        if (isTimes) {
            priceX = dataSpacing * lastPoint;
        } else {
            priceX = dataSpacing * (timesList.size() - 1);
        }
        canvas.drawCircle(priceX + margin, priceY, (float) (0.9 * in_r), circlepaint);
        circlepaint.setColor(0xff4b94fa);
        if (out_r < 2 * in_r) {
            out_r += in_r * 0.008;
            alph -= alph * 0.008;
            circlepaint.setAlpha((int) alph);
            canvas.drawCircle(priceX + margin, priceY, out_r, circlepaint);
        } else {
            alph = 255;
            out_r = in_r;
        }
        invalidate();
    }

    public void setBitmap(Bitmap bitmap, boolean needDraw, TimesFivesBitmap timesFivesBitmap) {
        this.bitmap = bitmap;
        this.needDraw = false;
        timesList = new ArrayList<StockMinuteData>();
        if (timesFivesBitmap == null) {
            return;
        }
        timesList = timesFivesBitmap.getTimeList();
        if (timesList.size() > 0 && timesList != null) {
            this.needDraw = needDraw;
        }
        in_r = (float) (0.8 * timesFivesBitmap.getCircleSize());
        out_r = (float) (0.8 * timesFivesBitmap.getCircleSize());
        alph = 255;
        isTimes = timesFivesBitmap.isTimes();
        dataSpacing = timesFivesBitmap.getDataspcing();
        lastPoint = timesFivesBitmap.getLastPoint();
        margin = timesFivesBitmap.getMargin();
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
