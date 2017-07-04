package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.companytags.StockHolderBean;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by huangxx on 2017/6/30.
 */

public class LineView extends View {

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
    //水平线绘制
    private Paint dashLine;
    //绘制字符串
    private Paint titlePaint;
    //绘制小圆
    private Paint circlePaint;
    //线宽
    private int lineSize;
    //字大小
    private int textSize;
    //虚线效果
    private PathEffect mDashEffect;

    //最大数
    private float lineBiggest;
    //最大数
    private float lineMini;
    //线率
    private float lineRate;

    //数据
    private List<StockHolderBean> beanList;

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        horizontalLines = 5;
        verticalLines = 13;
        marginLeft = 50;
        marginRight = 50;
        marginTop = 50;
        marginBottom = 50;
        marginBar = 5;
        lineSize = 2;
        textSize = 20;
        lineBiggest = 0;
        lineRate = 0;
        linePaint = new Paint();
        titlePaint = new Paint();
        circlePaint = new Paint();
        dashLine = new Paint();
        beanList = new ArrayList<>();
        mDashEffect = new DashPathEffect(new float[]{6, 8, 6, 8}, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHeight = getHeight() - marginBottom - marginTop;
        viewWidth = getWidth() - marginLeft - marginRight;
        lineRate = (float) viewHeight / (lineBiggest - lineMini);
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        //写字
        titlePaint.setTextSize(textSize);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        //画网格
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineSize);
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
        //画网格
        dashLine.setStrokeWidth(lineSize);
        dashLine.setStyle(Paint.Style.STROKE);
        dashLine.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
        dashLine.setPathEffect(mDashEffect);
        //刻度
        float leftNum = (lineBiggest - lineMini) / (horizontalLines > 1 ? (horizontalLines - 1) : 1);
        float eachWidth = viewWidth / (verticalLines > 1 ? (verticalLines - 1) : 1);

        if (null != beanList && beanList.size() > 0) {
            //画横线、标注
            for (int i = 0; i < horizontalLines; i++) {
                if (i == horizontalLines - 1) {
                    canvas.drawLine(marginLeft - marginBar, marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), marginLeft + viewWidth,
                            marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1), linePaint);
                } else {
                    Path dashPath = new Path();
                    dashPath.moveTo(marginLeft, marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1));
                    dashPath.lineTo(marginLeft + viewWidth, marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1));
                    canvas.drawPath(dashPath, dashLine);
                }
                //左边
                canvas.drawText(StringUtils.save2Significand(lineBiggest - leftNum * i) + "%",
                        marginLeft - titlePaint.measureText(StringUtils.save2Significand(lineBiggest - leftNum * i)) - 3 * marginBar,
                        marginTop + viewHeight * i / (horizontalLines > 1 ? (horizontalLines - 1) : 1) + fontHeight / 3, titlePaint);
            }
            //画纵线
            for (int i = 0; i < verticalLines; i++) {
                if (i == 0) {
                    canvas.drawLine(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop,
                            marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight + marginBar, linePaint);
                } else if (i == verticalLines - 1) {
                    Path dashPath = new Path();
                    dashPath.moveTo(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop);
                    dashPath.lineTo(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight + marginBar);
                    canvas.drawPath(dashPath, dashLine);

                } else {
                    canvas.drawLine(marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight,
                            marginLeft + viewWidth * i / (verticalLines > 1 ? (verticalLines - 1) : 1), marginTop + viewHeight + marginBar, linePaint);
                }
                titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.textColor_333333));
                //标注日期
                if (i < verticalLines - 1) {
                    String date = beanList.get(i).getDate().substring(2, 7);
                    canvas.drawText(date, marginLeft + eachWidth * i + eachWidth / 2 - titlePaint.measureText(date) / 2,
                            marginTop + viewHeight + fontHeight + marginBar, titlePaint);
                }
            }

            //绘制折线
            Path linePath = new Path();
            Path circlePath = new Path();
            //画内圆
            circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setAntiAlias(true);
            //画网格
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.card_color_red));
            linePaint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < beanList.size(); i++) {
                float startX = marginLeft + eachWidth * i + eachWidth / 2;
                float pointY = viewHeight - (beanList.get(i).getStock_total_ratio() - lineMini) * lineRate + marginTop;
                //画圈圈
                linePath.addCircle(startX, pointY, lineSize * 2, Path.Direction.CCW);
                circlePath.addCircle(startX, pointY, lineSize * 2, Path.Direction.CCW);
                //画折线
                if (i > 0) {
                    canvas.drawLine(startX - eachWidth, viewHeight - (beanList.get(i - 1).getStock_total_ratio() - lineMini) * lineRate + marginTop,
                            startX, pointY, linePaint);
                }
                //画字
                String titleText = StringUtils.save2Significand(beanList.get(i).getStock_total_ratio()) + "%";
                canvas.drawText(titleText, startX - titlePaint.measureText(titleText) / 2,
                        pointY - fontHeight / 2, titlePaint);
            }
            canvas.drawPath(linePath, linePaint);
            canvas.drawPath(circlePath, circlePaint);
        }


    }

    public void setChartData(List<StockHolderBean> beans) {
        if (null != beans && beans.size() > 0) {
            beanList.clear();
            beanList.addAll(beans);
            lineBiggest = 0;
            lineRate = 0;
            lineMini = beans.get(0).getStock_total_ratio();
            for (int i = 0; i < beans.size(); i++) {
                float num = beans.get(i).getStock_total_ratio();
                lineMini = lineMini < num ? lineMini : num;
                lineBiggest = lineBiggest > num ? lineBiggest : num;
            }
            invalidate();
        }
    }

    public int getHorizontalLines() {
        return horizontalLines;
    }

    public void setHorizontalLines(int horizontalLines) {
        this.horizontalLines = horizontalLines;
    }

    public int getVerticalLines() {
        return verticalLines;
    }

    public void setVerticalLines(int verticalLines) {
        this.verticalLines = verticalLines;
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

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public Paint getTitlePaint() {
        return titlePaint;
    }

    public void setTitlePaint(Paint titlePaint) {
        this.titlePaint = titlePaint;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
