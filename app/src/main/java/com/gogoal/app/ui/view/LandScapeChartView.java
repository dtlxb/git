package com.gogoal.app.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gogoal.app.R;
import com.gogoal.app.bean.StockMinuteData;
import com.gogoal.app.common.ImageUtils.TimesFivesBitmap;
import com.gogoal.app.common.StockUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by huangxx on 2017/valentine's day.
 */
public class LandScapeChartView extends View {
    private Bitmap bitmap = null;
    private Paint paint;
    private Paint circlepaint;
    private Paint titlePaint;
    private boolean showDetail;
    private float touchX;
    private float margin;
    private float rightMargin;
    private float uperHalfHigh;
    private float dataSpacing;
    private double initialWeightedIndex;
    private float uperBottom;
    private int mAxisTitleSize;
    private double closePrice;
    private int lastPoint;
    private float uperRate;
    private HashMap<String, StockMinuteData> DetailMap = new HashMap<String, StockMinuteData>();
    private List<StockMinuteData> DetailList = new ArrayList<>();
    private TimesFivesBitmap timesFivesBitmap;
    private List<StockMinuteData> timesList = new ArrayList<>();
    private StockMinuteData fenshiData = new StockMinuteData();
    private int red = Color.rgb(0xf2, 0x49, 0x57);
    private int green = Color.rgb(0x1d, 0xbf, 0x60);
    private int gray = Color.rgb(0x54, 0x69, 0x80);
    private List<String> dateList = new ArrayList<String>();
    float out_r = 0;
    float in_r = 0;
    float alph = 0;
    private boolean isTimes;
    private boolean needDraw;
    private int mSize;
    private int mSpaceSize;

    public LandScapeChartView(Context context) {
        super(context);
        init();
    }

    public LandScapeChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LandScapeChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        timesFivesBitmap = null;
        paint = new Paint();
        titlePaint = new Paint();
        circlepaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (timesFivesBitmap != null) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, null);
                drawDetails(canvas);
                if (needDraw) {
                    drawNow(canvas);
                }
            }
        }
    }

    private void drawNow(Canvas canvas) {
        circlepaint.setColor(ContextCompat.getColor(getContext(), R.color.decision_tab));
        circlepaint.setStrokeWidth(3);
        circlepaint.setAntiAlias(true);
        fenshiData = timesList.get(timesList.size() - 1);
        float priceY = (float) (uperBottom - (fenshiData.getPrice() + uperHalfHigh - initialWeightedIndex)
                * uperRate);
        float priceX;
        if (isTimes) {
            priceX = dataSpacing * lastPoint;
        } else {
            priceX = dataSpacing * (timesList.size() - 1);
        }
        canvas.drawCircle(priceX + margin, priceY, in_r, circlepaint);
        circlepaint.setColor(ContextCompat.getColor(getContext(), R.color.out_circle));
        if (out_r < 2.5 * in_r) {
            out_r += in_r * 0.01;
            alph -= alph * 0.01;
            circlepaint.setAlpha((int) alph);
            canvas.drawCircle(priceX + margin, priceY, out_r, circlepaint);
        } else {
            alph = 255;
            out_r = in_r;
        }
        invalidate();
    }

    private void drawDetails(Canvas canvas) {
        if (showDetail) {
            try {
                paint.setColor(Color.BLACK);
                paint.setAlpha(150);
                paint.setStrokeWidth(mSize);
                float x = 0;

                for (Iterator iter = DetailMap.entrySet().iterator(); iter.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    if (touchX > Float.parseFloat(key) - dataSpacing / 2 &&
                            touchX < Float.parseFloat(key) + dataSpacing / 2) {
                        fenshiData = DetailMap.get(key);
                        x = Float.parseFloat(key) - mSize / 2;
                    } else {
                        if (touchX <= margin) {
                            fenshiData = timesList.get(0);
                            x = margin;
                        }
                        if (isTimes) {
                            if (touchX >= lastPoint * dataSpacing + margin) {
                                fenshiData = timesList.get(timesList.size() - 1);
                                x = lastPoint * dataSpacing + margin;
                            }
                        } else {
                            if (touchX >= dataSpacing * (timesList.size() - 1) + margin) {
                                fenshiData = timesList.get(timesList.size() - 1);
                                x = dataSpacing * (timesList.size() - 1) + margin;
                            }
                        }
                    }
                }

                //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, 0x02, fenshiData, 0);
                float priceY = (float) (uperBottom - (fenshiData.getPrice() + uperHalfHigh - initialWeightedIndex)
                        * uperRate);
                //绘制十字线
                canvas.drawLine(margin + 1, priceY, bitmap.getWidth() - rightMargin, priceY, paint);
                canvas.drawLine(x, timesFivesBitmap.getmSpaceSize() + 1, x, bitmap.getHeight(), paint);
                canvas.drawCircle(x, priceY, timesFivesBitmap.getmSpaceSize(), paint);
                //绘制左右两边价格
                titlePaint.setTextSize(mAxisTitleSize);
                Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
                float fontHeight = fontMetrics.descent - fontMetrics.ascent;

                float price = fenshiData.getPrice();
                float tRate = fenshiData.getPrice_change_rate();
                double priceSpread = price - closePrice;

                String sPriceY = String.format("%.2f", price);
                String sRate = String.format("%.2f", tRate) + "%";

                if (priceSpread > 0) {
                    titlePaint.setColor(red);
                } else if (priceSpread < 0) {
                    titlePaint.setColor(green);
                } else {
                    titlePaint.setColor(gray);
                }
                titlePaint.setAlpha(200);
                titlePaint.setAntiAlias(true);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                if (priceY >= fontHeight) {
                    canvas.drawRoundRect(
                            new RectF(
                                    0,
                                    priceY - (fontHeight / 2),
                                    margin,
                                    priceY + (fontHeight / 2)),
                            fontHeight / 3,
                            fontHeight / 3,
                            titlePaint);

                    canvas.drawRoundRect(
                            new RectF(
                                    bitmap.getWidth() - rightMargin,
                                    priceY - (fontHeight / 2),
                                    bitmap.getWidth(),
                                    priceY + (fontHeight / 2)),
                            fontHeight / 3,
                            fontHeight / 3,
                            titlePaint);

                    titlePaint.setColor(Color.WHITE);
                    titlePaint.setAlpha(255);

                    canvas.drawText(
                            sPriceY,
                            margin / 2,
                            priceY + fontHeight / 3,
                            titlePaint);

                    canvas.drawText(
                            sRate,
                            bitmap.getWidth() - rightMargin + margin / 2,
                            priceY + fontHeight / 3,
                            titlePaint);
                } else {
                    canvas.drawRoundRect(
                            new RectF(
                                    0,
                                    mSpaceSize,
                                    margin,
                                    fontHeight + mSpaceSize),
                            fontHeight / 3,
                            fontHeight / 3,
                            titlePaint);

                    canvas.drawRoundRect(
                            new RectF(
                                    bitmap.getWidth() - rightMargin,
                                    mSpaceSize,
                                    bitmap.getWidth(),
                                    fontHeight + mSpaceSize),
                            fontHeight / 3,
                            fontHeight / 3,
                            titlePaint);

                    titlePaint.setColor(Color.WHITE);
                    titlePaint.setAlpha(255);

                    canvas.drawText(
                            sPriceY,
                            margin / 2,
                            fontHeight / 2 + mSpaceSize + fontHeight / 3,
                            titlePaint);

                    canvas.drawText(
                            sRate,
                            bitmap.getWidth() - rightMargin + margin / 2,
                            fontHeight * 6 / 7 + mSpaceSize,
                            titlePaint);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (timesList == null) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                showDetail = true;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, 0x01, 0);
                showDetail = false;
                postInvalidate();
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                showDetail = false;
                //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, 0x01, 0);
                break;
            default:
                break;
        }
        return true;
    }

    public void setData(TimesFivesBitmap timesFivesBitmap, int stock_charge_type) {
        this.timesFivesBitmap = timesFivesBitmap;
        timesList.clear();
        timesList.addAll(timesFivesBitmap.getTimeList());
        if (timesList.size() > 0 && timesList != null && stock_charge_type == 1 && StockUtils.isTradeTime()) {
            needDraw = true;
        }
        dateList = timesFivesBitmap.getDateList();
        in_r = timesFivesBitmap.getmSpaceSize();
        out_r = timesFivesBitmap.getmSpaceSize();
        alph = 255;
        isTimes = timesFivesBitmap.isTimes();
        DetailMap = timesFivesBitmap.getMap();
        lastPoint = timesFivesBitmap.getLastPoint();
        closePrice = timesFivesBitmap.getClosePrice();
        dataSpacing = timesFivesBitmap.getDataspcing();
        margin = timesFivesBitmap.getLeftMargin();
        rightMargin = timesFivesBitmap.getRightMargin();
        uperHalfHigh = timesFivesBitmap.getUperHalfHigh();
        uperBottom = timesFivesBitmap.getUperBottom();
        uperRate = timesFivesBitmap.getUperRate();
        mAxisTitleSize = timesFivesBitmap.getmAxisTitleSize();
        initialWeightedIndex = timesFivesBitmap.getInitialWeightedIndex();
        mSize = timesFivesBitmap.getmSize();
        mSpaceSize = timesFivesBitmap.getmSpaceSize();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
}
