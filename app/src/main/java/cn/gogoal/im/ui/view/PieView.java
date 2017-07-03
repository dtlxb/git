package cn.gogoal.im.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.PieBean;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by huangxx on 2017/6/13.
 */

public class PieView extends View {
    //画普通饼图
    private final int NORMAL_PIE = 0;
    //带外环线的
    private final int CIRCLE_PIE = 1;
    //画圆环的
    private final int ANNULAR_PIE = 2;
    //饼图类型
    private int pieType;
    //左右两边距离
    private int marginLeft, marginRight;
    //上下空出高度
    private int marginTop, marginBottom;
    //画笔
    private Paint linePaint, piePaint, titlePaint;
    //线宽,字大小
    private int lineSize, textSize;
    //主圆X,Y,R
    private int pieCenterX, pieCenterY, pieRadius;
    //扇形增长率
    private float pieRate;
    //数据
    private List<PieBean> beanList;
    private float totalValue;
    //是否需要画内白圆
    private boolean needInnerCircle;

    //是否需要弧形内标注
    private boolean needInnerTitle;

    public PieView(Context context) {
        super(context);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        marginLeft = 50;
        marginRight = 50;
        marginTop = 50;
        marginBottom = 50;
        lineSize = 2;
        textSize = 20;
        linePaint = new Paint();
        piePaint = new Paint();
        titlePaint = new Paint();
        beanList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewHeight = getHeight() - marginBottom - marginTop;
        int viewWidth = getWidth() - marginLeft - marginRight;

        pieRadius = viewHeight > viewWidth ? viewHeight / 2 : viewWidth / 2;
        pieCenterX = marginLeft + viewWidth / 2;
        pieCenterY = marginTop + viewHeight / 2;
        drawPie(canvas);
    }

    private void drawPie(Canvas canvas) {
        //写字
        titlePaint.setTextSize(textSize);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        if (null != beanList && beanList.size() > 0) {
            //线画笔
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(lineSize);
            //画主圆
            piePaint.setAntiAlias(true);
            piePaint.setStrokeWidth(lineSize);
            piePaint.setStyle(Paint.Style.STROKE);
            if (CIRCLE_PIE == pieType) {
                piePaint.setColor(ContextCompat.getColor(getContext(), R.color.actionsheet_gray));
                canvas.drawCircle(pieCenterX, pieCenterY, 1.05f * pieRadius, piePaint);
            }

            //扇形方框初始化
            RectF pieOval = new RectF();
            pieOval.left = pieCenterX - pieRadius;
            pieOval.top = pieCenterY - pieRadius;
            pieOval.right = pieCenterX + pieRadius;
            pieOval.bottom = pieCenterY + pieRadius;
            float start = 270.0f;
            piePaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < beanList.size(); i++) {
                //画扇形
                if (beanList.get(i).getColorValue() instanceof Integer) {
                    linePaint.setColor(ContextCompat.getColor(getContext(), (Integer) beanList.get(i).getColorValue()));
                    piePaint.setColor(ContextCompat.getColor(getContext(), (Integer) beanList.get(i).getColorValue()));
                } else if (beanList.get(i).getColorValue() instanceof String) {
                    linePaint.setColor(Color.parseColor((String) beanList.get(i).getColorValue()));
                    piePaint.setColor(Color.parseColor((String) beanList.get(i).getColorValue()));
                }
                float sweep = beanList.get(i).getPieValue() / totalValue * 360;
                canvas.drawArc(pieOval, start, sweep * pieRate, true, piePaint);

                float rate;
                String title;
                if (CIRCLE_PIE == pieType || ANNULAR_PIE == pieType) {
                    rate = 1.1f;
                    title = StringUtils.getIntegerData(String.valueOf(beanList.get(i).getPieValue() * 100 * pieRate / totalValue)) + "%";
                } else {
                    rate = 1.0f;
                    title = beanList.get(i).getTitle();
                }
                //画标注连线
                float radians = (float) ((start + sweep / 2) / 180 * Math.PI);
                float lineStartX = pieCenterX + pieRadius * rate * (float) (Math.cos(radians));
                float lineStartY = pieCenterY + pieRadius * rate * (float) (Math.sin(radians));

                if (ANNULAR_PIE != pieType) {
                    rate = 1.3f;
                    float lineStopX = pieCenterX + pieRadius * rate * (float) (Math.cos(radians));
                    float lineStopY = pieCenterY + pieRadius * rate * (float) (Math.sin(radians));
                    canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);
                    //画标注折线和点
                    float textLineStopX = lineStopX;
                    if (lineStartX > pieCenterX) {
                        textLineStopX += lineSize * 10;
                    } else {
                        textLineStopX -= lineSize * 10;
                    }
                    canvas.drawLine(lineStopX, lineStopY, textLineStopX, lineStopY, linePaint);
                    canvas.drawCircle(textLineStopX, lineStopY, lineSize * 3 * pieRate, linePaint);

                    //写标注
                    float fontWidth = titlePaint.measureText(title);
                    canvas.drawText(title, textLineStopX - (lineStartX > pieCenterX ? -5 * lineSize : fontWidth + 5 * lineSize),
                            lineStopY + fontHeight / 4, titlePaint);
                } else {
                    //圈内标注
                    if (needInnerTitle && !TextUtils.isEmpty(title)) {
                        titlePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
                        canvas.drawText(title, pieCenterX + (lineStartX - pieCenterX - titlePaint.measureText(title)) / 2,
                                pieCenterY + (lineStartY - pieCenterY + fontHeight) / 2, titlePaint);
                    }
                }
                start += sweep;
            }
            //画内圆
            if (CIRCLE_PIE == pieType) {
                piePaint.setColor(ContextCompat.getColor(getContext(), R.color.half_alpha_white));
                canvas.drawCircle(pieCenterX, pieCenterY, pieRadius / 2, piePaint);
            } else if (ANNULAR_PIE == pieType) {
                if (needInnerCircle) {
                    piePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
                    canvas.drawCircle(pieCenterX, pieCenterY, pieRadius / 3, piePaint);
                }
            }

            if (pieRate < 1) {
                pieRate += 0.02;
            } else {
                pieRate = 1;
            }
            invalidate();
        } else {
            //无数据处理
            titlePaint.setTextSize(2 * textSize);
            String text = "暂无数据";
            canvas.drawText(text, getWidth() / 2 - titlePaint.measureText(text) / 2, getHeight() / 2 + fontHeight / 2, titlePaint);
        }
    }

    public void setPieData(List<PieBean> beans) {
        if (null != beans && beans.size() > 0) {
            beanList.clear();
            beanList.addAll(beans);
            totalValue = 0;
            pieRate = 0;
            for (int i = 0; i < beans.size(); i++) {
                float num = beans.get(i).getPieValue();
                totalValue += num;
            }
            invalidate();
        }
    }

    public boolean isNeedInnerTitle() {
        return needInnerTitle;
    }

    public void setNeedInnerTitle(boolean needInnerTitle) {
        this.needInnerTitle = needInnerTitle;
    }

    public boolean isNeedInnerCircle() {
        return needInnerCircle;
    }

    public void setNeedInnerCircle(boolean needInnerCircle) {
        this.needInnerCircle = needInnerCircle;
    }

    public int getPieType() {
        return pieType;
    }

    public void setPieType(int pieType) {
        this.pieType = pieType;
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
