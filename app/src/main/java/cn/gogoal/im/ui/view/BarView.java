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
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.StringUtils;


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

    private int barType;

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

        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        if (null != beanList && beanList.size() > 0) {
            //绘制水平线
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(lineSize);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
            float startY = marginTop + barRate * positive_num;
            canvas.drawLine(marginLeft, startY, getWidth() - marginRight, startY, linePaint);

            rectPaint.setStrokeWidth(viewWidth / (beanList.size() * 2));
            for (int i = 0; i < beanList.size(); i++) {
                float barHeight;
                String titleText;
                float startX = marginLeft + viewWidth * i / beanList.size() + viewWidth / (beanList.size() * 2);
                barHeight = startY - barRate * beanList.get(i).getBarValue() * upRate;
                if (barType == AppConst.TYPE_FRAGMENT_TURNOVER_RATE) {
                    titleText = StringUtils.save2Significand(beanList.get(i).getBarValue() * upRate) + "%";
                    rectPaint.setColor(ContextCompat.getColor(getContext(), R.color.company_change_bar));
                } else {
                    titleText = StringUtils.save2Significand(beanList.get(i).getBarValue() * upRate);
                    //画笔颜色
                    titlePaint.setColor(beanList.get(i).getBarValue() < 0 ? ContextCompat.getColor(getContext(), R.color.stock_green_market)
                            : ContextCompat.getColor(getContext(), R.color.stock_red_market));
                    rectPaint.setColor(beanList.get(i).getBarValue() < 0 ? ContextCompat.getColor(getContext(), R.color.stock_green_market)
                            : ContextCompat.getColor(getContext(), R.color.stock_red_market));
                }
                //画柱体
                canvas.drawLine(startX, startY, startX, barHeight, rectPaint);
                //画标注
                canvas.drawText(titleText, startX - titlePaint.measureText(titleText) / 2,
                        beanList.get(i).getBarValue() < 0 ? barHeight + fontHeight * 3 / 5 + marginBar : barHeight - marginBar, titlePaint);
                //画日期
                titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
                canvas.drawText(beanList.get(i).getDate(),
                        startX - titlePaint.measureText(beanList.get(i).getDate()) / 2,
                        beanList.get(i).getBarValue() > 0 ? startY + fontHeight * 3 / 5 + 3 * marginBar : startY - 3 * marginBar, titlePaint);
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
            titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
            String text = "暂无数据";
            canvas.drawText(text, getWidth() / 2 - titlePaint.measureText(text) / 2, getHeight() / 2, titlePaint);
        }
    }

    public void setChartData(List<ChartBean> beans) {
        if (null != beans && beans.size() > 0) {
            beanList.clear();
            beanList.addAll(beans);
            barBiggest = 0;
            positive_num = 0;
            negative_num = 0;
            upRate = 0;
            barRate = 0;
            for (int i = 0; i < beans.size(); i++) {
                float num = beans.get(i).getBarValue();
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


    public int getBarType() {
        return barType;
    }

    public void setBarType(int barType) {
        this.barType = barType;
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
