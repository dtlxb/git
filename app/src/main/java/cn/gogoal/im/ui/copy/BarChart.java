package cn.gogoal.im.ui.copy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by huangxx on 2015/11/18.
 */
public class BarChart extends View {

    private static final int piechart_bule = 0xff419be9;
    private static final int piechart_red = 0xffed605a;
    private static final int barchart_gray = 0xffa6a6a6;
    private static final int guide_dot_color = 0xff5da3fb;
    private static final int color_red1 = 0xff5da3fb;

    //边线颜色
    protected int mBorderColor;
    public static final int DEFAULT_BORDER_COLOR = Color.GRAY;
    //水平线绘制
    private Paint linePaint;
    //绘制字符串
    private Paint titlePaint;
    //绘制字符串
    private Paint barsPaint;
    //控件宽高
    private int viewHight;
    private int viewWidth;
    //字体柱形距离边的宽度
    private int margin;
    //柱体率
    private float barRate;
    //柱体最大数值
    private float barBiggest;
    private float positive_num;
    private float negetive_num;
    //柱体之间的间距
    private float dataSpacing;
    //柱体位置
    private float dataPosition;
    //柱体高度
    private float perBarHeight;
    //柱体增加高度率
    private float upRate;
    //手机分辨率
    public boolean isSw480P;
    public boolean isSw720P;
    public boolean isSw1080P;
    //不同分辨率字体
    private int mTitleSize;
    private int mtsbSize;
    private int deviateSize;
    //标注的单位
    private String textType;
    //数据
    private List<Integer> profit_type = new ArrayList<>();
    private List<Float> data = new ArrayList<>();
    private List<String> time = new ArrayList<>();
    //是否净利润一致预期
    private boolean isFromCon;


    public BarChart(Context context) {
        super(context);
        init();
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        titlePaint = new Paint();
        barsPaint = new Paint();
        margin = 100;
        barBiggest = 0;
        mBorderColor = DEFAULT_BORDER_COLOR;
        mTitleSize = 30;
        deviateSize = 2;
        barRate = 0;
        barBiggest = 0;
        positive_num = 0;
        negetive_num = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHight = getHeight();
        viewWidth = getWidth();
        barRate = (float) (0.5 * viewHight / barBiggest);
        dataSpacing = (float) ((viewWidth - 2 * margin) / 9);

        //字体大小判断
        if (isSw480P) {
            mTitleSize = 17;
            mtsbSize = 7;
            margin = 50;
            deviateSize = 1;
        } else if (isSw720P) {
            mTitleSize = 25;
            mtsbSize = 10;
            margin = 80;
            deviateSize = 2;
        } else if (isSw1080P) {
            mTitleSize = 36;
            mtsbSize = 15;
            margin = 125;
            deviateSize = 2;
        } else {
            mTitleSize = 55;
            mtsbSize = 22;
            margin = 150;
            deviateSize = 3;
        }

        drawBars(canvas);

        drawTitles(canvas);
    }

    private void drawBars(Canvas canvas) {

        //绘制水平线
        linePaint.setStrokeWidth(2);
        linePaint.setColor(0xff999999);
        canvas.drawLine((float) 0.5 * margin, viewHight - negetive_num * barRate - 2 * margin, viewWidth - (float) (0.5 * margin), viewHight - negetive_num * barRate - 2 * margin, linePaint);
        //绘制标注
        Rect r = new Rect();
        String dataText = null;
        float perBarText = 0;
        //绘制柱体
        barsPaint.setStrokeWidth((viewWidth - 2 * margin) / 9);
        if (data.size() > 0 && data != null) {
            for (int i = 0; i < data.size(); i++) {
                dataPosition = (float) (margin + dataSpacing * 2 * i + 0.5 * dataSpacing);
                perBarHeight = data.get(i) * barRate;

                if (isFromCon) {
                    titlePaint.setTextSize((float) (0.8 * mTitleSize));
                    dataText = numberParse(Math.round(data.get(i))) + textType;
                } else {
                    titlePaint.setTextSize((float) (0.9 * mTitleSize));
                    dataText = saveSignificand(data.get(i), 2) + textType;
                }
                perBarText = data.get(i);
                //标注和柱体颜色设置
                if (profit_type.get(i) == 0) {
                    barsPaint.setColor(piechart_bule);
                    titlePaint.setColor(piechart_bule);
                } else if (profit_type.get(i) == 1) {
                    barsPaint.setColor(piechart_red);
                    titlePaint.setColor(piechart_red);
                } else {
                    barsPaint.setColor(barchart_gray);
                    titlePaint.setColor(barchart_gray);
                }

                titlePaint.getTextBounds(dataText, 0, dataText.length(), r);

                //柱体顶部标注
                if (data.get(i) >= 0) {
                    if (isFromCon) {
                        canvas.drawText(numberParse((int) (perBarText * upRate)) + textType, (float) (dataPosition - 0.5 * r.width()), viewHight - negetive_num * barRate - 2 * margin - deviateSize * 5 - perBarHeight * upRate, titlePaint);
                    } else {
                        canvas.drawText(saveSignificand(perBarText * upRate, 2) + textType, (float) (dataPosition - 0.5 * r.width()), viewHight - negetive_num * barRate - 2 * margin - deviateSize * 5 - perBarHeight * upRate, titlePaint);
                    }
                } else if (data.get(i) < 0) {
                    if (isFromCon) {
                        canvas.drawText(numberParse((int) (perBarText * upRate)) + textType, (float) (dataPosition - 0.5 * r.width()), viewHight - negetive_num * barRate - 2 * margin - mtsbSize, titlePaint);
                    } else {
                        canvas.drawText(saveSignificand(perBarText * upRate, 2) + textType, (float) (dataPosition - 0.5 * r.width()), viewHight - negetive_num * barRate - 2 * margin - mtsbSize, titlePaint);
                    }
                }

                //柱体增长过程
                canvas.drawLine(dataPosition, viewHight - negetive_num * barRate - 2 * margin, dataPosition, viewHight - negetive_num * barRate - 2 * margin - perBarHeight * upRate, barsPaint);
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
        String text = "";
        if ((number < 100000 & number > 10000) || (number < -10000 & number > -100000)) {
            text = saveSignificand(number / 10000.0f, 2) + "亿";
        } else if (number > 100000 || number < -100000) {
            text = Math.round(number / 10000.0f) + "亿";
        } else {
            text = number + "万";
        }
        return text;
    }

    private void drawTitles(Canvas canvas) {
        int fontHeight;
        Rect r = new Rect();

        titlePaint.setTextSize(mTitleSize);
        titlePaint.setColor(Color.GRAY);

        String tb1 = "真实财务数据";
        String tb2 = "超5家机构一致预测数据";
        String tb3 = "预测数据不足";

        //绘制底部年份
        for (int i = 0; i < time.size(); i++) {
            titlePaint.getTextBounds(time.get(i), 0, time.get(i).length(), r);
        }
        fontHeight = r.height();

        for (int i = 0; i < time.size(); i++) {
            canvas.drawText(time.get(i), (float) (margin + 2 * dataSpacing * i + 0.1 * r.width()), viewHight - fontHeight - margin, titlePaint);
        }

        //画图例
        titlePaint.setTextSize(2 * mtsbSize);
        canvas.drawText(tb1, (float) (margin + 0.5 * dataSpacing), (float) (viewHight - 2 * fontHeight), titlePaint);
        canvas.drawText(tb2, margin + 3 * dataSpacing + 2 * deviateSize, (float) (viewHight - 2 * fontHeight), titlePaint);
        canvas.drawText(tb3, margin + 7 * dataSpacing, (float) (viewHight - 2 * fontHeight), titlePaint);

        titlePaint.setAntiAlias(true);
        titlePaint.setColor(guide_dot_color);
        canvas.drawCircle((float) (margin + 0.5 * dataSpacing - 1.5 * mtsbSize), viewHight - 2 * fontHeight - (float) (mtsbSize * 0.5) - deviateSize, mtsbSize * 5 / 7, titlePaint);
        titlePaint.setColor(color_red1);
        canvas.drawCircle((float) (margin + 3 * dataSpacing - 1.5 * mtsbSize) + 2 * deviateSize, viewHight - 2 * fontHeight - (float) (mtsbSize * 0.5) - deviateSize, mtsbSize * 5 / 7, titlePaint);
        titlePaint.setColor(barchart_gray);
        canvas.drawCircle((float) (margin + 7 * dataSpacing - 1.5 * mtsbSize), viewHight - 2 * fontHeight - (float) (mtsbSize * 0.5) - deviateSize, mtsbSize * 5 / 7, titlePaint);
    }

    public void setData(ArrayList<Map<String, String>> list, boolean isFromCon) {
        data.clear();
        time.clear();
        positive_num = 0;
        negetive_num = 0;
        barBiggest = 0;
        upRate = 0;
        textType = null;
        profit_type.clear();
        this.isFromCon = isFromCon;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                profit_type.add(Integer.parseInt(list.get(i).get("profit_type")));
                if (list.get(i).get("data") == null) {
                    data.add(0f);
                } else {
                    data.add(Float.parseFloat(list.get(i).get("data")));
                }
                time.add(list.get(i).get("year"));
            }
            textType = list.get(0).get("type").toString();
            for (int i = 0; i < data.size(); i++) {
                float num = data.get(i);
                if (num > 0) {
                    positive_num = positive_num > num ? positive_num : num;
                } else {
                    negetive_num = negetive_num > Math.abs(num) ? negetive_num : Math.abs(num);
                }
            }
            barBiggest = positive_num + negetive_num;
        }
        invalidate();
    }

    public void setIsSw1080P(boolean isSw1080P) {
        this.isSw1080P = isSw1080P;
    }

    public void setIsSw480P(boolean isSw480P) {
        this.isSw480P = isSw480P;
    }

    public void setIsSw720P(boolean isSw720P) {
        this.isSw720P = isSw720P;
    }

    /**
     * 保留有效数字
     */
    public static String saveSignificand(double doubleData, int significand) {
        String result = String.format("%." + significand + "f", doubleData);
        return result;
    }

    public static String saveSignificand(String strDoubleData, int significand) {
        try {
            return String.format("%." + significand + "f", Double.parseDouble(strDoubleData));
        } catch (Exception e) {
            return "--";
        }
    }

}
