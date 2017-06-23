package cn.gogoal.im.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.ChartBean;

/**
 * Created by huangxx on 2017/6/22.
 */

public class ProgressBarView extends View {

    //左右两边距离
    private int marginLeft;
    private int marginRight;
    //字体与柱体距离
    private int marginBar;
    private int viewWidth;
    private int viewHeight;
    //水平线绘制
    private Paint linePaint;
    //绘制字符串
    private Paint titlePaint;
    //字大小
    private int textSize;
    //柱体率
    private float barRate;
    //柱体增长率
    private float upRate;
    //数据
    private float total;
    private List<ChartBean> beanList;
    //需要动画
    private boolean needAnime;

    public ProgressBarView(Context context) {
        super(context);
        init();
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        marginLeft = 0;
        marginRight = 0;
        marginBar = 5;
        textSize = 20;
        barRate = 0;
        total = 0;
        beanList = new ArrayList<>();
        linePaint = new Paint();
        titlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewWidth = getWidth() - marginLeft - marginRight;
        viewHeight = getHeight();
        barRate = (float) viewWidth / total;

        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        linePaint.setStrokeWidth(viewHeight);
        linePaint.setAntiAlias(true);

        titlePaint.setTextSize(textSize);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        if (null != beanList && beanList.size() > 0) {
            //画柱体
            float stopX = beanList.get(0).getBarValue() * barRate;
            float stopX2 = beanList.get(1).getBarValue() * barRate;
            linePaint.setColor(Color.parseColor(beanList.get(0).getDate()));
            canvas.drawLine(0, viewHeight / 2, stopX * upRate, viewHeight / 2, linePaint);
            if (titlePaint.measureText(numParse(beanList.get(0).getBarValue())) < stopX) {
                canvas.drawText(numParse(beanList.get(0).getBarValue()), marginBar, viewHeight / 2 + fontHeight * 2 / 5, titlePaint);
            }

            linePaint.setColor(Color.parseColor(beanList.get(1).getDate()));
            canvas.drawLine(viewWidth, viewHeight / 2, viewWidth - stopX2 * upRate, viewHeight / 2, linePaint);
            if (titlePaint.measureText(numParse(beanList.get(1).getBarValue())) < stopX2) {
                canvas.drawText(numParse(beanList.get(1).getBarValue()),
                        viewWidth - titlePaint.measureText(numParse(beanList.get(1).getBarValue())) - marginBar, viewHeight / 2 + fontHeight * 2 / 5, titlePaint);
            }

            if (upRate < 1) {
                upRate += 0.02;
            } else {
                upRate = 1;
            }
            invalidate();
        }
    }

    private String numParse(double number) {
        return (int) Math.rint(number * 100 / total) + "%";
    }

    public void setChartData(List<ChartBean> beans, boolean anim) {
        if (null != beans && beans.size() > 0) {
            beanList.clear();
            beanList.addAll(beans);
            total = 0;
            if (anim) {
                upRate = 0;
            } else {
                upRate = 1;
            }
            barRate = 0;
            for (int i = 0; i < beans.size(); i++) {
                total += beans.get(i).getBarValue();
            }
            KLog.e(beans);
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
