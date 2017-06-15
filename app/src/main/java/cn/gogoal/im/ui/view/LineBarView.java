package cn.gogoal.im.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by huangxx on 2017/6/13.
 */

public class LineBarView extends View {
    //横、纵线数
    private int horizontalLines;
    private int verticalLines;
    //左右两边距离
    private int marginLeft;
    private int marginRight;
    //上下空出高度
    private int marginTop;
    private int marginBottom;
    //字体与柱体距离
    private int marginBar;
    private int viewWidth;
    private int viewHeight;
    //线绘制
    private Paint linePaint;
    //柱体绘制
    private Paint rectPaint;
    //绘制字符串
    private Paint titlePaint;
    //绘制字符串
    private Paint circlePaint;
    //线宽
    private int lineSize;
    //字大小
    private int textSize;

    //柱体数字
    private float biggest;
    private float positive_num;
    private float negative_num;
    //线数字
    private float lineMax;
    private float line_pos;
    private float line_neg;

    //柱体率
    private float barRate;
    //折线率
    private float lineRate;
    //柱体增长率
    private float upRate;
    //数据
    private List<ChartBean> beanList;

    public LineBarView(Context context) {
        super(context);
        init();
    }

    public LineBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        horizontalLines = 3;
        verticalLines = 2;
        marginLeft = 50;
        marginRight = 50;
        marginTop = 50;
        marginBottom = 50;
        marginBar = 5;
        lineSize = 2;
        textSize = 20;
        barRate = 0;
        lineRate = 0;
        lineMax = 0;
        biggest = 0;
        positive_num = 0;
        negative_num = 0;
        line_neg = 0;
        line_pos = 0;
        linePaint = new Paint();
        rectPaint = new Paint();
        titlePaint = new Paint();
        circlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHeight = getHeight() - marginBottom - marginTop;
        viewWidth = getWidth() - marginLeft - marginRight;
        barRate = (float) viewHeight / biggest;
        lineRate = (float) viewHeight / lineMax;

        drawLineBar(canvas);
    }

    private void drawLineBar(Canvas canvas) {
        //写字
        titlePaint.setTextSize(textSize);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        if (null != beanList && beanList.size() > 0) {
            //画网格
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(lineSize);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));

            float leftNum = biggest / (horizontalLines > 1 ? (horizontalLines - 1) : 1);
            float RightNum = lineMax / (horizontalLines > 1 ? (horizontalLines - 1) : 1);

            //画横线、标注
            for (int i = 0; i < horizontalLines; i++) {
                canvas.drawLine(marginLeft, marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), marginLeft + viewWidth,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), linePaint);

                //左边
                canvas.drawText(StringUtils.save2Significand(positive_num - leftNum * i),
                        marginLeft - titlePaint.measureText(StringUtils.save2Significand(positive_num - leftNum * i)) - 2 * lineSize,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1) + fontHeight / 3, titlePaint);

                //右边
                canvas.drawText(StringUtils.save2Significand(line_pos - RightNum * i), getWidth() - marginRight + 2 * lineSize,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1) + fontHeight / 3, titlePaint);
            }
            //画纵线
            for (int i = 0; i < verticalLines; i++) {
                canvas.drawLine(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop,
                        marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight, linePaint);
            }
            //画柱体
            rectPaint.setStrokeWidth(viewWidth / (beanList.size() * 2));
            //画折线
            linePaint.setStrokeWidth(2 * lineSize);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.tabSelected));
            linePaint.setStyle(Paint.Style.STROKE);
            //画内圆
            circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setAntiAlias(true);
            Path linePath = new Path();
            Path circlePath = new Path();
            for (int i = 0; i < beanList.size(); i++) {
                float barHeight;
                float lineHeight;
                float startY = getHeight() - marginBottom;
                float startX = marginLeft + viewWidth * i / beanList.size() + viewWidth / (beanList.size() * 2);
                String date = beanList.get(i).getDate();
                barHeight = startY - (beanList.get(i).getBarValue() + negative_num) * barRate * upRate;
                lineHeight = startY - (beanList.get(i).getLineValue() + line_neg) * lineRate * upRate;
                //画笔颜色
                rectPaint.setColor(ContextCompat.getColor(getContext(), R.color.decision_search));
                //画柱体
                canvas.drawLine(startX, startY, startX, barHeight, rectPaint);
                //画圈圈
                linePath.addCircle(startX, lineHeight, lineSize * 4, Path.Direction.CCW);
                circlePath.addCircle(startX, lineHeight, lineSize * 3, Path.Direction.CCW);
                //画折线
                if (i > 0) {
                    canvas.drawLine(startX - viewWidth / beanList.size(), startY -
                                    (beanList.get(i - 1).getLineValue() + line_neg) * lineRate * upRate,
                            startX, lineHeight, linePaint);
                }

                //画日期
                canvas.drawText(date, startX - titlePaint.measureText(date) / 2,
                        startY + fontHeight, titlePaint);
            }
            canvas.drawPath(linePath, linePaint);
            canvas.drawPath(circlePath, circlePaint);

            if (upRate < 1) {
                upRate += 0.02;
            } else {
                upRate = 1;
            }
            invalidate();
        } else {
            //无数据处理
            titlePaint.setTextSize(2 * textSize);
            String text = "暂无数据";
            canvas.drawText(text, getWidth() / 2 - titlePaint.measureText(text) / 2, getHeight() / 2, titlePaint);
        }
    }

    public void setChartData(List<ChartBean> beans) {
        if (null != beans && beans.size() > 0) {
            beanList = new ArrayList<>();
            beanList.addAll(beans);
            for (int i = 0; i < beans.size(); i++) {
                float line = beans.get(i).getLineValue();
                if (line > 0) {
                    line_pos = line_pos > line ? line_pos : line;
                } else {
                    line_neg = line_neg > Math.abs(line) ? line_neg : Math.abs(line);
                }

                float bar = beans.get(i).getBarValue();
                if (bar > 0) {
                    positive_num = positive_num > bar ? positive_num : bar;
                } else {
                    negative_num = negative_num > Math.abs(bar) ? negative_num : Math.abs(bar);
                }
            }
            biggest = positive_num + negative_num;
            lineMax = line_pos + line_neg;
            invalidate();
        }
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

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
