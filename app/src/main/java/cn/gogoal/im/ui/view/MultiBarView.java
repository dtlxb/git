package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by huangxx on 2017/6/14.
 */

public class MultiBarView extends View {

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
    private float negative_num;
    //柱体率
    private float barRate;
    //柱体增长率
    private float upRate;
    //数据
    private List<ChartBean> beanList;

    public MultiBarView(Context context) {
        super(context);
        init();
    }

    public MultiBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        horizontalLines = 6;
        verticalLines = 1;
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
        negative_num = 0;
        linePaint = new Paint();
        rectPaint = new Paint();
        titlePaint = new Paint();
        beanList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHeight = getHeight() - marginBottom - marginTop;
        viewWidth = getWidth() - marginLeft - marginRight;
        barRate = (float) viewHeight / barBiggest;
        drawBars(canvas);
    }

    private void drawBars(Canvas canvas) {
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
            //刻度
            float leftNum = barBiggest / (horizontalLines > 1 ? (horizontalLines - 1) : 1);
            //画横线、标注
            for (int i = 0; i < horizontalLines; i++) {
                canvas.drawLine(marginLeft, marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), marginLeft + viewWidth,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), linePaint);

                //左边
                canvas.drawText(StringUtils.save2Significand(positive_num - leftNum * i),
                        marginLeft - titlePaint.measureText(StringUtils.save2Significand(positive_num - leftNum * i)) - 2 * lineSize,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1) + fontHeight / 3, titlePaint);
            }
            //画纵线
            for (int i = 0; i < verticalLines; i++) {
                canvas.drawLine(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop,
                        marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight, linePaint);
            }
            //画柱体
            rectPaint.setStrokeWidth(viewWidth / (beanList.size() * 4));
            for (int i = 0; i < beanList.size(); i++) {
                float fBarHeight;
                float sBarHeight;
                float tBarHeight;
                float startY = getHeight() - marginBottom;
                float startX = marginLeft + viewWidth * i / beanList.size() + viewWidth / (beanList.size() * 2);
                String date = beanList.get(i).getDate();
                fBarHeight = startY - (beanList.get(i).getBarValue() + negative_num) * barRate * upRate;
                sBarHeight = startY - (beanList.get(i).getSecondBarValue() + negative_num) * barRate * upRate;
                tBarHeight = startY - (beanList.get(i).getThirdBarValue() + negative_num) * barRate * upRate;
                //画柱1
                rectPaint.setColor(ContextCompat.getColor(getContext(), R.color.piechart_red));
                canvas.drawLine(startX - viewWidth / (beanList.size() * 4) - lineSize, startY,
                        startX - viewWidth / (beanList.size() * 4) - lineSize, fBarHeight, rectPaint);
                //画柱2
                rectPaint.setColor(ContextCompat.getColor(getContext(), R.color.decision_search));
                canvas.drawLine(startX, startY, startX, sBarHeight, rectPaint);
                //画柱3
                rectPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
                canvas.drawLine(startX + viewWidth / (beanList.size() * 4) + lineSize, startY,
                        startX + viewWidth / (beanList.size() * 4) + lineSize, tBarHeight, rectPaint);

                //画日期
                canvas.drawText(date, startX - titlePaint.measureText(date) / 2,
                        startY + fontHeight, titlePaint);
            }
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
            beanList.clear();
            beanList.addAll(beans);
            positive_num = 0;
            negative_num = 0;
            barBiggest = 0;
            barRate = 0;
            upRate = 0;
            for (int i = 0; i < beans.size(); i++) {
                float bar1 = beans.get(i).getBarValue();
                float bar2 = beans.get(i).getSecondBarValue();
                float bar3 = beans.get(i).getThirdBarValue();
                float num = bar1 > bar2 ? (bar1 > bar3 ? bar1 : bar3) : (bar2 > bar3 ? bar2 : bar3);

                if (num > 0) {
                    positive_num = positive_num > num ? positive_num : num;
                } else {
                    negative_num = negative_num > Math.abs(num) ? negative_num : Math.abs(num);
                }
            }
            barBiggest = positive_num + negative_num;
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
