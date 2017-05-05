package hply.com.niugu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hply.com.niugu.stock.StockMinuteData;
import hply.com.niugu.stock.StockUtils;
import hply.com.niugu.stock.TimesFivesBitmap;

import static hply.com.niugu.ColorUtils.outCircleColor;


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
        circlepaint.setColor(ColorUtils.decisionTabColor);
        circlepaint.setStrokeWidth(3);
        circlepaint.setAntiAlias(true);
        fenshiData = timesList.get(timesList.size() - 1);
        float priceY = (float) (uperBottom - (StringUtils.getDouble(fenshiData.getPrice()) + uperHalfHigh - initialWeightedIndex)
                * uperRate);
        float priceX;
        if (isTimes){
            priceX = dataSpacing * lastPoint;
        }else {
            priceX=dataSpacing*(timesList.size() - 1);
        }
        canvas.drawCircle(priceX + margin, priceY, in_r, circlepaint);
        circlepaint.setColor(outCircleColor);
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
                paint.setStrokeWidth(3);
                float x = 0;
                Rect r = new Rect();
                Map<String, Integer> rValues = new HashMap<>();
                x = touchX;
                if (DetailMap.containsKey(StringUtils.getOnePointData((int) ((touchX - margin) / dataSpacing)))) {
                    fenshiData = DetailMap.get(StringUtils.getOnePointData((int) ((touchX - margin) / dataSpacing)));
                } else {
                    if (touchX <= margin) {
                        fenshiData = timesList.get(0);
                        x = margin;
                    }
                    if (isTimes){
                        if (touchX >= lastPoint * dataSpacing) {
                            fenshiData = timesList.get(timesList.size() - 1);
                            x = lastPoint * dataSpacing + margin;
                        }
                    }else {
                        if (touchX >= dataSpacing*(timesList.size() - 1)) {
                            fenshiData = timesList.get(timesList.size() - 1);
                            x = dataSpacing*(timesList.size() - 1) + margin;
                        }
                    }
                }
//                MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISPLAY_XCHART_TIME_DATA, fenshiData, 0);
                float priceY = (float) (uperBottom - (StringUtils.getDouble(fenshiData.getPrice()) + uperHalfHigh - initialWeightedIndex)
                        * uperRate);
                //绘制十字线
                canvas.drawLine(margin, priceY, bitmap.getWidth() - rightMargin, priceY, paint);
                canvas.drawLine(x, timesFivesBitmap.getCircleSize(), x, bitmap.getHeight(), paint);
                canvas.drawCircle(x, priceY, timesFivesBitmap.getCircleSize(), paint);
                //绘制左右两边价格
                double price = StringUtils.getDouble(fenshiData.getPrice());
                double tRate = StringUtils.getDouble(fenshiData.getPrice_change_rate());
                double priceSpread = price - closePrice;
                titlePaint.setTextSize(mAxisTitleSize);

                String sPriceY = StringUtils.save2Significand(price);
                titlePaint.getTextBounds(sPriceY, 0, sPriceY.length(), r);
                rValues.put("sPriceYW", r.width());
                rValues.put("sPriceYH", r.height());

                String sRate = StringUtils.save2Significand(tRate) + "%";
                titlePaint.getTextBounds(sRate, 0, sRate.length(), r);
                rValues.put("sRateW", r.width());
                rValues.put("sRateH", r.height());

                if (priceSpread > 0) {
                    titlePaint.setColor(red);
                } else if (priceSpread < 0) {
                    titlePaint.setColor(green);
                } else {
                    titlePaint.setColor(gray);
                }
                titlePaint.setAlpha(200);
                titlePaint.setAntiAlias(true);

                float w, h;
                h = rValues.get("sPriceYH");

                if (priceY >= (h / 2) + 5) {
                    w = rValues.get("sPriceYW");
                    h = rValues.get("sPriceYH");
                    canvas.drawRoundRect(
                            new RectF(
                                    margin - w,
                                    priceY - (h / 2) - 5,
                                    margin,
                                    priceY + (h / 2) + 5),
                            h / 3,
                            h / 3,
                            titlePaint);

                    w = rValues.get("sRateW");
                    h = rValues.get("sRateH");
                    canvas.drawRoundRect(
                            new RectF(
                                    bitmap.getWidth() - rightMargin,
                                    priceY - (h / 2) - 5,
                                    bitmap.getWidth() - rightMargin + w,
                                    priceY + (h / 2) + 5),
                            h / 3,
                            h / 3,
                            titlePaint);

                    titlePaint.setColor(Color.WHITE);
                    titlePaint.setAlpha(255);

                    w = rValues.get("sPriceYW");
                    h = rValues.get("sPriceYH");
                    canvas.drawText(
                            sPriceY,
                            margin - w - 1,
                            priceY + (h / 2),
                            titlePaint);

                    w = rValues.get("sRateW");
                    h = rValues.get("sRateH");
                    canvas.drawText(
                            sRate,
                            bitmap.getWidth() - rightMargin,
                            priceY + (h / 2),
                            titlePaint);
                } else {
                    w = rValues.get("sPriceYW");
                    h = rValues.get("sPriceYH");
                    canvas.drawRoundRect(
                            new RectF(
                                    margin - w,
                                    0,
                                    margin,
                                    h + 10),
                            h / 3,
                            h / 3,
                            titlePaint);

                    w = rValues.get("sRateW");
                    h = rValues.get("sRateH");
                    canvas.drawRoundRect(
                            new RectF(
                                    bitmap.getWidth() - rightMargin,
                                    0,
                                    bitmap.getWidth() - rightMargin + w,
                                    h + 10),
                            h / 3,
                            h / 3,
                            titlePaint);

                    titlePaint.setColor(Color.WHITE);
                    titlePaint.setAlpha(255);
                    w = rValues.get("sPriceYW");
                    h = rValues.get("sPriceYH");
                    canvas.drawText(
                            sPriceY,
                            margin - w - 1,
                            h + 5,
                            titlePaint);

                    w = rValues.get("sRateW");
                    h = rValues.get("sRateH");
                    canvas.drawText(
                            sRate,
                            bitmap.getWidth() - rightMargin,
                            h + 5,
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
//                MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                showDetail = false;
                postInvalidate();
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                showDetail = false;
//                MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
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
        in_r = timesFivesBitmap.getCircleSize();
        out_r = timesFivesBitmap.getCircleSize();
        alph = 255;
        isTimes = timesFivesBitmap.isTimes();
        DetailMap = timesFivesBitmap.getMap();
        lastPoint = timesFivesBitmap.getLastPoint();
        closePrice = timesFivesBitmap.getClosePrice();
        dataSpacing = timesFivesBitmap.getDataspcing();
        margin = timesFivesBitmap.getMargin();
        rightMargin = timesFivesBitmap.getRightMargin();
        uperHalfHigh = timesFivesBitmap.getUperHalfHigh();
        uperBottom = timesFivesBitmap.getUperBottom();
        uperRate = timesFivesBitmap.getUperRate();
        mAxisTitleSize = timesFivesBitmap.getMaxisTitleSize();
        initialWeightedIndex = timesFivesBitmap.getInitialWeightedIndex();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
}

