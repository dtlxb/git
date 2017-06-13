package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import hply.com.niugu.StringUtils;


/**
 * Created by huangxx on 2017/6/9.
 */

public class BarView extends View {

    //左右两边距离
    private int marginLeft;
    private int marginRight;
    //上下空出高度
    private int marginTop;
    private int marginBottom;
    //字体与柱体距离
    private int marginBar;
    //控件宽高
    private int viewHeight;
    private int viewWidth;
    //水平线绘制
    private Paint linePaint;
    //柱体绘制
    private Paint rectPaint;
    //绘制字符串
    private Paint titlePaint;
    //线宽
    private int lineSize;
    //字大小
    private int textSize;

    //最大数
    private float barBiggest;
    private float positive_num;
    private float negative_nam;
    //柱体率
    private float barRate;
    //柱体增长率
    private float upRate;

    //数据
    private List<Float> values;
    private List<String> dates;

    public BarView(Context context) {
        super(context);
        init();
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        marginLeft = 50;
        marginRight = 50;
        marginTop = 50;
        marginBottom = 50;
        marginBar = 5;
        lineSize = 2;
        textSize = 20;
        barRate = 0;
        barBiggest = 0;
        positive_num = 0;
        negative_nam = 0;
        linePaint = new Paint();
        rectPaint = new Paint();
        titlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHeight = getHeight();
        viewWidth = getWidth() - marginLeft - marginRight;
        barRate = (float) (viewHeight - marginBottom - marginTop) / barBiggest;
        drawBars(canvas);
    }

    private void drawBars(Canvas canvas) {
        //绘制水平线
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineSize);
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
        float startY = marginTop + barRate * positive_num;
        canvas.drawLine(marginLeft, startY, getWidth() - marginRight, startY, linePaint);

        //绘制标注
        Rect r = new Rect();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        //绘制柱体
        if (null != values && values.size() > 0) {
            rectPaint.setStrokeWidth(viewWidth / (values.size() * 2));
            for (int i = 0; i < values.size(); i++) {
                float barHeight;
                String dataText = numberParse(Math.round(values.get(i)));
                float startX = marginLeft + viewWidth * i / values.size() + viewWidth / (values.size() * 2);
                barHeight = startY - barRate * values.get(i) * upRate;
                //画笔颜色
                titlePaint.setColor(values.get(i) < 0 ? ContextCompat.getColor(getContext(), R.color.stock_green_market)
                        : ContextCompat.getColor(getContext(), R.color.stock_red_market));
                rectPaint.setColor(values.get(i) < 0 ? ContextCompat.getColor(getContext(), R.color.stock_green_market)
                        : ContextCompat.getColor(getContext(), R.color.stock_red_market));
                //画柱体
                canvas.drawLine(startX, startY, startX, barHeight, rectPaint);
                //画标注
                titlePaint.getTextBounds(dataText, 0, dataText.length(), r);
                canvas.drawText(numberParse((int) (values.get(i) * upRate)),
                        startX - titlePaint.measureText(numberParse((int) (values.get(i) * upRate))) / 2,
                        values.get(i) < 0 ? barHeight + fontHeight * 3 / 5 + marginBar : barHeight - marginBar, titlePaint);
                //画日期
                titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
                canvas.drawText(dates.get(i),
                        startX - titlePaint.measureText(dates.get(i)) / 2,
                        values.get(i) > 0 ? startY + fontHeight * 3 / 5 + 3 * marginBar : startY - 3 * marginBar, titlePaint);
            }
            if (upRate < 1) {
                upRate += 0.02;
            } else {
                upRate = 1;
            }
            invalidate();
        }
    }

    //净利润一致预期数据处理
    private String numberParse(int number) {
        String text;
        if ((number < 100000 & number > 10000) || (number < -10000 & number > -100000)) {
            text = StringUtils.save2Significand(number / 10000.0f) + "亿";
        } else if (number > 100000 || number < -100000) {
            text = Math.round(number / 10000.0f) + "亿";
        } else {
            text = number + "万";
        }
        return text;
    }

    public void setChartData(Map<String, Object> data) {
        values = new ArrayList<>();
        dates = new ArrayList<>();
        values.addAll((Collection<? extends Float>) data.get("values"));
        dates.addAll((Collection<? extends String>) data.get("dates"));
        for (int i = 0; i < values.size(); i++) {
            float num = values.get(i);
            if (num > 0) {
                positive_num = positive_num > num ? positive_num : num;
            } else {
                negative_nam = negative_nam > Math.abs(num) ? negative_nam : Math.abs(num);
            }
        }
        barBiggest = positive_num + negative_nam;
        invalidate();
    }


    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginBar() {
        return marginBar;
    }

    public void setMarginBar(int marginBar) {
        this.marginBar = marginBar;
    }
}
