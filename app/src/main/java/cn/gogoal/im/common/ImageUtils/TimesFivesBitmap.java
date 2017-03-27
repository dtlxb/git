package cn.gogoal.im.common.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.gogoal.im.R;
import cn.gogoal.im.base.MyApp;
import cn.gogoal.im.bean.StockMinuteBean;
import cn.gogoal.im.bean.StockMinuteData;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by huangxx on 2017/valentine's day.
 */
public class TimesFivesBitmap {
    // /////////////默认值////////////////
    public static final int DEFAULT_BACKGROUD = Color.WHITE;        //默认背景色
    public static final int DEFAULT_TITLE_SIZE = 30;                //默认字体大小
    public static final int DEFAULT_SIZE = 4;                       //默认尺寸大小
    public static final int DEFAULT_LONGTITUDE_COLOR = Color.GRAY;//默认经纬线颜色
    public static final int DEFAULT_LAITUDE_COLOR = Color.RED;//默认经纬线颜色
    public static int DEFAULT_UPER_LATITUDE_NUM = 1;                 //默认上表纬线数
    public static int DEFAULT_LOGITUDE_NUM = 1;                      //默认经线数
    public static final int DEFAULT_BORDER_COLOR = Color.GRAY;       //默认边框的颜色
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{3, 3, 3,
            3}, 2);                                                  //默认虚线效果

    // /////////////属性////////////////
    private int mBackGround;                                   //背景色
    private int mLatitudeColor;                         //经纬线颜色
    private int mLongtitudeColor;                         //经纬线颜色
    private PathEffect mDashEffect;                          //虚线效果
    private int mBorderColor;                                //边线色
    private float mUperChartHeight;                          //上表高度
    private float mLowerChartHeight;                         //下表高度
    private int leftMargin;                                  //左边坐标宽度
    private int rightMargin;                                 //右边坐标宽度
    private int longitudeNum;                               //上表纬线数
    private int uperLatitudeNum;                            //经线数
    private float longitudeSpacing;                            //经度的间隔
    private float latitudeSpacing;                             //纬度的间隔
    private DashPathEffect mRedDashPathEffect;                 //中间红色虚线
    private boolean isFromStockDetail;                       //显示股票详情
    private int mAxisTitleSize;                              //字体尺寸
    private int mSpaceSize;                                  //第一条线到顶部间距
    private int mSize;                                       //图形尺寸

    //view的宽高
    private int viewHeight;
    private int viewWidth;
    //线与字体的颜色
    private int red = Color.rgb(0xf2, 0x49, 0x57);
    private int green = Color.rgb(0x1d, 0xbf, 0x60);
    private int gray = Color.rgb(0x54, 0x69, 0x80);

    // /////////////分时图////////////////
    //数据处理
    private int DATA_MAX_COUNT;
    private List<StockMinuteData> timesList = new ArrayList<StockMinuteData>();
    private List<Float> list = new ArrayList<Float>();

    public HashMap<String, StockMinuteData> DetailMap = new HashMap<String, StockMinuteData>();
    private StockMinuteData fenshiData;

    //分时图绘图尺寸
    private double closePrice;
    private float uperBottom;
    private float uperHeight;
    private float lowerBottom;
    private float lowerHeight;
    private float dataSpacing;

    //尺率
    private double initialWeightedIndex;
    private float uperHalfHigh;
    private float lowerHigh;
    private float uperRate;
    private float lowerRate;
    private List<String> dateList = new ArrayList<String>();

    //横竖屏判断
    private boolean isFromDetail;
    //区分分时图和五日图
    private boolean isTimes;
    //停牌处理
    private boolean isSuspension;
    private SimpleDateFormat dateFormat;
    private Calendar c;
    private int lastPoint;

    //绘图工具
    private Paint tilte_paint;
    private Paint paintTop;
    private Paint paintShadow;
    private Rect r;
    private float uperBlueX;
    private Paint paintBottom;


    public TimesFivesBitmap(Integer width, Integer height) {
        this.viewWidth = width;
        this.viewHeight = height;
        init();
    }

    private void init() {
        mAxisTitleSize = DEFAULT_TITLE_SIZE;
        mSize = DEFAULT_SIZE;
        leftMargin = 0;
        rightMargin = 0;
        mBackGround = DEFAULT_BACKGROUD;
        mLongtitudeColor = DEFAULT_LONGTITUDE_COLOR;
        mLatitudeColor = DEFAULT_LAITUDE_COLOR;
        mDashEffect = DEFAULT_DASH_EFFECT;
        mBorderColor = DEFAULT_BORDER_COLOR;
        longitudeNum = DEFAULT_LOGITUDE_NUM;
        uperLatitudeNum = DEFAULT_UPER_LATITUDE_NUM;
        mRedDashPathEffect = new DashPathEffect(new float[]{3, 3, 3, 3}, 2);

        uperBlueX = 0;
        isFromDetail = false;
        initialWeightedIndex = 0;
        tilte_paint = new Paint();
        paintTop = new Paint();
        paintShadow = new Paint();
        paintBottom = new Paint();
        r = new Rect();
    }

    protected void drawBitMap(Canvas canvas) {

        setUperChartHeight((viewHeight * 7) / 10f);
        setLowerChartHeight((float) ((viewHeight * 2.1) / 10f));

        uperBottom = getUperChartHeight() - 1;
        uperHeight = getUperChartHeight() - mSpaceSize - 2;
        lowerHeight = getLowerChartHeight() - 2;
        lowerBottom = viewHeight - 1;
        uperRate = uperHeight / uperHalfHigh / 2.0f;
        lowerRate = lowerHeight / lowerHigh;
        dataSpacing = (viewWidth - leftMargin - rightMargin) * 1.00f / (DATA_MAX_COUNT - 1);

        // 绘制边框
        drawBorders(canvas);

        // 绘制经线
        drawLongitudes(canvas);

        // 绘制纬线
        drawLatitudes(canvas);

        //绘制蓝线、黄线、量能线
        drawLines(canvas);

        //绘制Y轴下的坐标值
        drawXtitle(canvas);

        //绘制xy轴数据
        drawXYPostion(canvas);

        //停牌状态图表处理
        drawStopLines(canvas);
    }

    /**
     * 绘制边框
     */
    private void drawBorders(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(mBorderColor);
        paint.setStrokeWidth(mSize / 2);

        // 画上表
        //up
        canvas.drawLine(leftMargin + mSize / 2, mSize / 2 + mSpaceSize, viewWidth - rightMargin - mSize / 2, mSize / 2 + mSpaceSize, paint);
        if (isFromDetail) {
            //left
            canvas.drawLine(leftMargin + mSize / 2, mSize / 2 + mSpaceSize, leftMargin + mSize / 2, mUperChartHeight - mSize / 2, paint);
            //right
            canvas.drawLine(viewWidth - rightMargin - mSize / 2, mSize / 2 + mSpaceSize, viewWidth - rightMargin - mSize / 2, mUperChartHeight - mSize / 2, paint);
        }
        //bottom
        canvas.drawLine(leftMargin + mSize / 2, mUperChartHeight - mSize / 2, viewWidth - rightMargin - mSize / 2, mUperChartHeight - mSize / 2, paint);

        // 画下表
        if (isFromDetail) {
            //up
            canvas.drawLine(leftMargin + mSize / 2, viewHeight - mLowerChartHeight + mSize / 2, viewWidth - rightMargin - mSize / 2, viewHeight - mLowerChartHeight + mSize / 2, paint);
            //left
            canvas.drawLine(leftMargin + mSize / 2, viewHeight - mLowerChartHeight + mSize / 2, leftMargin + mSize / 2, viewHeight - mSize / 2, paint);
            //right
            canvas.drawLine(viewWidth - rightMargin - mSize / 2, viewHeight - mLowerChartHeight + mSize / 2, viewWidth - rightMargin - mSize / 2, viewHeight - mSize / 2, paint);
        }
        //bottom
        canvas.drawLine(leftMargin + mSize / 2, viewHeight - mSize / 2, viewWidth - rightMargin - mSize / 2, viewHeight - mSize / 2, paint);

    }

    /**
     * 绘制经线
     */
    private void drawLongitudes(Canvas canvas) {

        longitudeSpacing = (viewWidth - leftMargin - rightMargin) / (longitudeNum + 1);

        Paint paint = new Paint();
        paint.setColor(mLongtitudeColor);
        paint.setPathEffect(mDashEffect);
        paint.setStrokeWidth(mSize / 2);

        for (int i = 1; i <= longitudeNum; i++) {
            canvas.drawLine(longitudeSpacing * i + leftMargin + mSize / 2, mSpaceSize + mSize / 2, longitudeSpacing * i + leftMargin + mSize / 2,
                    mUperChartHeight - mSize / 2, paint);
            canvas.drawLine(mSize / 2 + longitudeSpacing * i + leftMargin, viewHeight - getLowerChartHeight() + mSize / 2, mSize / 2 + longitudeSpacing * i + leftMargin,
                    viewHeight - mSize / 2, paint);
        }

    }

    /**
     * 绘制纬线（只包含上表）
     */
    private void drawLatitudes(Canvas canvas) {

        latitudeSpacing = (mUperChartHeight - mSize + mSpaceSize) / (uperLatitudeNum + 1);

        Paint paint = new Paint();
        paint.setColor(mLatitudeColor);
        paint.setStrokeWidth(mSize / 2);
        paint.setPathEffect(mRedDashPathEffect);

        for (int i = 1; i <= uperLatitudeNum; i++) {
            canvas.drawLine(leftMargin + mSize / 2, mSize / 2 + latitudeSpacing * i, viewWidth - rightMargin - mSize / 2, mSize / 2 + latitudeSpacing * i, paint);
        }
    }

    /**
     * 绘制xy轴数据
     * */
    public void drawXYPostion(Canvas canvas) {
        String unit = "";
        String volume = "";

        tilte_paint.setTextSize(mAxisTitleSize);
        tilte_paint.setColor(gray);
        tilte_paint.setAntiAlias(true);

        Paint.FontMetrics fontMetrics = tilte_paint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        //绘制左边价格
        String pos_max_price = "";
        if (isTimes && isSuspension) {
            pos_max_price = StringUtils.getAnyPointDouble("%.2f",1.01 * closePrice);
        } else {
            pos_max_price = StringUtils.getAnyPointDouble("%.2f",initialWeightedIndex + uperHalfHigh);
        }

        String close_price = String.format("%.2f", closePrice);

        String pos_min_price = "";
        if (isTimes && isSuspension) {
            pos_min_price = StringUtils.getAnyPointDouble("%.2f",0.99 * closePrice);
        } else {
            pos_min_price = StringUtils.getAnyPointDouble("%.2f",initialWeightedIndex - uperHalfHigh);
        }

        if (isFromDetail) {
            if ((lowerHigh / 10000 / 100) < 1) {
                unit = "手";
                volume = String.valueOf((int) (lowerHigh / 100));
            } else {
                unit = "万手";
                volume = String.format("%.2f", lowerHigh / 10000 / 100);
            }
        }

        //绘制右边率
        String pos_max_rate = "";
        if (isTimes && isSuspension) {
            pos_max_rate = StringUtils.getAnyPointDouble("%.2f",1.00) + "%";
        } else {
            pos_max_rate = StringUtils.getAnyPointDouble("%.2f",uperHalfHigh / initialWeightedIndex * 100) + "%";
        }

        String close_rate = String.format("%.2f", 0.00) + "%";

        String pos_min_rate = "";
        if (isTimes && isSuspension) {
            pos_min_rate = StringUtils.getAnyPointDouble("%.2f",-1.00) + "%";
        } else {
            pos_min_rate = StringUtils.getAnyPointDouble("%.2f",-uperHalfHigh / initialWeightedIndex * 100) + "%";
        }

        fontHeight = r.height();
        //margin+5，使左右游标显示完全
        if (isFromDetail) {
            canvas.drawText(pos_max_price, leftMargin - tilte_paint.measureText(pos_max_price) - mSize, fontHeight + mSpaceSize, tilte_paint);
            canvas.drawText(close_price, leftMargin - tilte_paint.measureText(close_price) - mSize, (mUperChartHeight + mSpaceSize) / 2 + fontHeight / 3, tilte_paint);
            canvas.drawText(pos_min_price, leftMargin - tilte_paint.measureText(pos_min_price) - mSize, mUperChartHeight, tilte_paint);
            canvas.drawText(volume, leftMargin - tilte_paint.measureText(volume) - mSize, viewHeight - mLowerChartHeight + fontHeight, tilte_paint);
            canvas.drawText(unit, leftMargin - tilte_paint.measureText(unit) - mSize, viewHeight - mSize, tilte_paint);
            canvas.drawText(pos_max_rate, viewWidth - rightMargin + mSize, fontHeight + mSpaceSize, tilte_paint);
            canvas.drawText(close_rate, viewWidth - rightMargin + mSize, (mUperChartHeight + mSpaceSize) / 2 + fontHeight / 3, tilte_paint);
            canvas.drawText(pos_min_rate, viewWidth - rightMargin + mSize, mUperChartHeight, tilte_paint);
        } else {
            // 绘制左边Y轴的坐标
            canvas.drawText(pos_max_price, leftMargin, fontHeight + 3 * mSize + mSpaceSize, tilte_paint);
            canvas.drawText(pos_min_price, leftMargin, mUperChartHeight - 3 * mSize, tilte_paint);
            // 绘制右边Y轴的坐标
            canvas.drawText(pos_max_rate, viewWidth - rightMargin - tilte_paint.measureText(pos_max_rate) - mSize / 2, fontHeight + 3 * mSize + mSpaceSize, tilte_paint);
            canvas.drawText(pos_min_rate, viewWidth - rightMargin - tilte_paint.measureText(pos_min_rate) - mSize / 2, mUperChartHeight - 3 * mSize, tilte_paint);
        }
    }

    /**
     * 绘制Y轴下的坐标值
     * */
    private void drawXtitle(Canvas canvas) {

        tilte_paint.setTextSize(mAxisTitleSize);
        tilte_paint.setColor(gray);
        tilte_paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = tilte_paint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        if (isTimes) {
            // 底部坐标
            String beginTime = "9:30";
            tilte_paint.getTextBounds(beginTime, 0, beginTime.length(), r);
            int beginTimeSize = r.width();

            String middleTime = "11:30/13:00";
            tilte_paint.getTextBounds(middleTime, 0, middleTime.length(), r);
            int middleTimeSize = r.width();

            String overTime = "15:00";
            tilte_paint.getTextBounds(overTime, 0, overTime.length(), r);
            int overTimeSize = r.width();

            // 绘制上表的X轴坐标
            canvas.drawText(beginTime, leftMargin, (viewHeight - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
            canvas.drawText(middleTime, (viewWidth - leftMargin - rightMargin - middleTimeSize) / 2 + leftMargin, (viewHeight - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
            canvas.drawText(overTime, viewWidth - rightMargin - overTimeSize - 2, (viewHeight - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
        } else {
            for (int i = 0; i < dateList.size(); i++) {
                String beginTime = dateList.get(i);
                tilte_paint.getTextBounds(beginTime, 0, beginTime.length(), r);
                int beginTimeSize = r.width();
                canvas.drawText(beginTime, leftMargin + getLongitudeSpacing() / 2 + getLongitudeSpacing() * i - beginTimeSize / 2,
                        (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3, tilte_paint);
            }
        }
    }

    /**
     * 绘制蓝线、黄线、量能线
     * */
    private void drawLines(Canvas canvas) {

        DetailMap.clear();
        paintTop.setStyle(Paint.Style.STROKE);
        paintTop.setStrokeWidth(mSize);

        paintShadow.setStyle(Paint.Style.FILL);//实心矩形框
        paintShadow.setColor(ContextCompat.getColor(MyApp.getAppContext(), R.color.time_shadow_color));
        Path time_line_path = new Path();
        Path avg_line_path = new Path();
        Path time_shadow_path = new Path();
        float endBlueFirstX = 0;
        float endBlueLastX = 0;
        float endBlueMidX = 0;
        float endBlueMidStartX = 0;
        int cut = -1;

        //找到第一点
        if (timesList.size() > 0 && timesList != null) {
            StockMinuteData MinuteDataFirst = timesList.get(0);
            int stock_hour = Integer.parseInt(MinuteDataFirst.getDate().substring(11, 13));
            int stock_minute = Integer.parseInt(MinuteDataFirst.getDate().substring(14, 16));
            int position = getMinute(stock_hour, stock_minute);
            endBlueFirstX = dataSpacing * position + leftMargin;
            float endFirstBlueY = (float) (uperBottom - (MinuteDataFirst.getPrice() + uperHalfHigh - initialWeightedIndex) * uperRate);
            float endFirstYellowY = (float) (uperBottom - (MinuteDataFirst.getAvg_price() + uperHalfHigh - initialWeightedIndex) * uperRate);
            time_line_path.moveTo(endBlueFirstX, endFirstBlueY);
            avg_line_path.moveTo(endBlueFirstX, endFirstYellowY);
            time_shadow_path.moveTo(endBlueFirstX, endFirstBlueY);
            endBlueMidStartX = endBlueFirstX;
        }

        //找到其它点绘制图形
        for (int i = 0; i < timesList.size(); i++) {
            cut++;
            paintTop.setStrokeWidth(mSize);
            fenshiData = timesList.get(i);
            int stock_hour = Integer.parseInt(fenshiData.getDate().substring(11, 13));
            int stock_minute = Integer.parseInt(fenshiData.getDate().substring(14, 16));
            int position = getMinute(stock_hour, stock_minute);
            if (isTimes) {
                uperBlueX = dataSpacing * position + leftMargin;
            } else {
                uperBlueX = dataSpacing * i + leftMargin;
            }
            DetailMap.put(StringUtils.getAnyPointDouble("%.2f",uperBlueX), fenshiData);
            float endBlueY = (float) (uperBottom - (DetailMap.get(StringUtils.getAnyPointDouble("%.2f",uperBlueX)).getPrice()
                    + uperHalfHigh - initialWeightedIndex)
                    * uperRate);
            if (i == timesList.size() - 1) {
                endBlueLastX = uperBlueX;
            }
            float endYelloY = (float) (uperBottom - (DetailMap.get(StringUtils.getAnyPointDouble("%.2f",uperBlueX)).getAvg_price() + uperHalfHigh - initialWeightedIndex)
                    * uperRate);
            //五日图绘制
            if (!isTimes) {
                time_line_path.lineTo(uperBlueX, endBlueY);
                avg_line_path.lineTo(uperBlueX, endYelloY);
                time_shadow_path.lineTo(uperBlueX, endBlueY);

                paintTop.setColor(ContextCompat.getColor(MyApp.getAppContext(), R.color.time_prive));
                canvas.drawPath(time_line_path, paintTop);
                paintTop.setColor(ContextCompat.getColor(MyApp.getAppContext(),R.color.time_avg_prive));
                canvas.drawPath(avg_line_path, paintTop);
            }

            //分时图绘制
            if (isTimes) {
                if (cut == position) {
                    endBlueMidX = uperBlueX;
                    time_line_path.lineTo(uperBlueX, endBlueY);
                    avg_line_path.lineTo(uperBlueX, endYelloY);
                    time_shadow_path.lineTo(uperBlueX, endBlueY);
                } else {
                    if (cut == 0) {
                        time_shadow_path.lineTo(endBlueMidStartX, mUperChartHeight);
                    } else {
                        time_shadow_path.lineTo(endBlueMidX, mUperChartHeight);
                    }
                    time_shadow_path.lineTo(endBlueMidStartX, mUperChartHeight);
                    time_shadow_path.close();

                    paintTop.setColor(ContextCompat.getColor(MyApp.getAppContext(), R.color.time_prive));
                    canvas.drawPath(time_line_path, paintTop);
                    paintTop.setColor(ContextCompat.getColor(MyApp.getAppContext(), R.color.time_avg_prive));
                    canvas.drawPath(avg_line_path, paintTop);
                    canvas.drawPath(time_shadow_path, paintShadow);

                    time_line_path = new Path();
                    avg_line_path = new Path();
                    time_shadow_path = new Path();

                    endBlueMidStartX = uperBlueX;
                    time_line_path.moveTo(uperBlueX, endBlueY);
                    avg_line_path.moveTo(uperBlueX, endYelloY);
                    time_shadow_path.moveTo(uperBlueX, endBlueY);

                    cut = position;
                }
                paintTop.setColor(Color.rgb(0x47, 0x98, 0xfc));
                canvas.drawPath(time_line_path, paintTop);
                paintTop.setColor(Color.rgb(0xfd, 0xc1, 0x01));
                canvas.drawPath(avg_line_path, paintTop);
            }


            if (isFromDetail) {
                paintTop.setStrokeWidth((float) (mSize * 1.5));
            } else {
                paintTop.setStrokeWidth(mSize);
            }
            if (isFromDetail) {
                paintBottom.setStrokeWidth(mSize);
            } else {
                paintBottom.setStrokeWidth(mSize / 2);
            }
            long buy = fenshiData.getVolume();
            if (i == 0) {
                paintBottom.setColor(Color.rgb(0xf1, 0x4a, 0x56));
            } else if (fenshiData.getPrice() >= timesList.get(i - 1).getPrice()) {
                paintBottom.setColor(Color.rgb(0xf1, 0x4a, 0x56));
            } else if (fenshiData.getPrice() == timesList.get(i - 1).getPrice()) {
                paintBottom.setColor(Color.rgb(0x1d, 0xbf, 0x60));
            } else {
                paintBottom.setColor(Color.rgb(0x01, 0xb7, 0x0a));
            }
            //量能线绘制
            canvas.drawLine(uperBlueX - mSize / 2, lowerBottom, uperBlueX - mSize / 2, lowerBottom - buy * lowerRate, paintBottom);
        }
        time_shadow_path.lineTo(endBlueLastX, mUperChartHeight);
        time_shadow_path.lineTo(endBlueMidStartX, mUperChartHeight);
        time_shadow_path.close();
        canvas.drawPath(time_shadow_path, paintShadow);
    }

    /**
     * 停牌状态图表处理
     * */
    private void drawStopLines(Canvas canvas) {
        if (isSuspension && StockUtils.isTradeTime()) {
            //绘制停牌阴影部分
            Path stop_shadow_path = new Path();
            //绘制停牌分时线
            float endStopX1;
            float endStopX2;
            float endStopY;
            paintTop.setStyle(Paint.Style.STROKE);
            paintTop.setColor(Color.rgb(0xfd, 0xc1, 0x01));
            paintTop.setStrokeWidth(mSize);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            if (isTimes) {
                endStopX1 = leftMargin;
                endStopY = getLatitudeSpacing();
                if (StockUtils.isTradeTime()) {
                    endStopX2 = getMinute(hour, minute) * dataSpacing + leftMargin;
                } else {
                    if (hour == 11 || hour == 12) {
                        endStopX2 = 121 * dataSpacing + leftMargin;
                    } else {
                        endStopX2 = 241 * dataSpacing + leftMargin;
                    }
                }
            } else {
                fenshiData = timesList.get(timesList.size() - 1);
                endStopX1 = (dateList.size() - 1) * 60 * dataSpacing + leftMargin;
                endStopY = (float) (uperBottom - (fenshiData.getPrice() + uperHalfHigh - initialWeightedIndex) * uperRate);
                if (StockUtils.isTradeTime()) {
                    endStopX2 = ((dateList.size() - 1) * 60 + getMinute(hour, minute) / 4 + 1) * dataSpacing + leftMargin;
                } else {
                    if (hour == 11 || hour == 12) {
                        endStopX2 = (dateList.size() * 60 - 29) * dataSpacing + leftMargin;
                    } else {
                        endStopX2 = (dateList.size() * 60 + 1) * dataSpacing + leftMargin;
                    }
                }
            }
            stop_shadow_path.moveTo(endStopX1, endStopY);
            stop_shadow_path.lineTo(endStopX2, endStopY);
            stop_shadow_path.lineTo(endStopX2, mUperChartHeight);
            stop_shadow_path.lineTo(endStopX1, mUperChartHeight);
            canvas.drawPath(stop_shadow_path, paintShadow);
            canvas.drawLine(endStopX1, endStopY, endStopX2, endStopY, paintTop);
        }
    }

    /**
     * 带入数据
     * isTimes是否分时线
     * stock_charge_type是否停牌分段
     * */
    public Bitmap setTimesList(StockMinuteBean bean, Boolean isTimes, int stock_charge_type) {
        this.isTimes = isTimes;
        timesList.clear();
        if (stock_charge_type != 1) {
            isSuspension = true;
        } else {
            isSuspension = false;
        }
        if (isTimes) {
            DEFAULT_LOGITUDE_NUM = 0;
            DATA_MAX_COUNT = 241;
        } else {
            DEFAULT_LOGITUDE_NUM = 4;
            DATA_MAX_COUNT = 302;
        }
        c = Calendar.getInstance();
        List<StockMinuteData> timesorFivesList = new ArrayList<>();
        SimpleDateFormat date_format_get_year = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Date tmp_date = null;
        List<Date> unsorted_date_list = new ArrayList<>();
        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (bean != null) {
            if (bean.getCode() == 0) {
                timesorFivesList.clear();
                timesorFivesList.addAll(bean.getData());
                if (!isTimes) {
                    for (int i = 0; i < bean.getData().size(); i++) {
                        try {
                            tmp_date = dateFormat.parse(bean.getData().get(i).getDate());
                            String only_date = date_format_get_year.format(tmp_date);
                            tmp_date = date_format_get_year.parse(only_date);
                            unsorted_date_list.add(tmp_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Collections.sort(unsorted_date_list);
            for (Date date : unsorted_date_list) {
                if (!dateList.contains(date_format_get_year.format(date).substring(5, 10))) {
                    dateList.add(date_format_get_year.format(date).substring(5, 10));
                }
            }
            //停牌处理
            if (isSuspension) {
                String m = null;
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                if (month < 10) {
                    m = String.valueOf("0" + month);
                } else {
                    m = String.valueOf(month);
                }
                String today = m + "-" + day;
                dateList.add(today);
            }
            if (timesorFivesList.size() > 0) {
                StockMinuteData fiveDayData = timesorFivesList.get(0);
                //分时和五日数据处理
                double weightedIndex = fiveDayData.getPrice();
                double nonWeightedIndex = fiveDayData.getAvg_price();
                this.closePrice = fiveDayData.getPrice() / (1 + (fiveDayData.getPrice_change_rate() / 100.00f));
                initialWeightedIndex = fiveDayData.getPrice() / (1 + (fiveDayData.getPrice_change_rate() / 100.00f));
                for (int i = 1; i < timesorFivesList.size(); i++) {
                    fiveDayData = timesorFivesList.get(i);
                    weightedIndex = fiveDayData.getPrice();
                    nonWeightedIndex = fiveDayData.getAvg_price();

                    uperHalfHigh = (float) (uperHalfHigh > Math
                            .abs(nonWeightedIndex - initialWeightedIndex) ? uperHalfHigh : Math
                            .abs(nonWeightedIndex - initialWeightedIndex));
                    uperHalfHigh = (float) (uperHalfHigh > Math.abs(weightedIndex - initialWeightedIndex) ? uperHalfHigh
                            : Math.abs(weightedIndex - initialWeightedIndex));
                }
                if (isTimes) {
                    timesList.addAll(timesorFivesList);
                    //找到最后一点位置
                    StockMinuteData MinuteDataLast = timesList.get(timesList.size() - 1);
                    int stock_hour = Integer.parseInt(MinuteDataLast.getDate().substring(11, 13));
                    int stock_minute = Integer.parseInt(MinuteDataLast.getDate().substring(14, 16));
                    lastPoint = getMinute(stock_hour, stock_minute);
                } else {
                    for (int i = 0; i < timesorFivesList.size(); i += 4) {
                        timesList.add(timesorFivesList.get(i));
                    }
                }

                for (int i = 0; i < timesList.size(); i++) {
                    StockMinuteData fenshiData1 = timesList.get(i);
                    list.add((float) fenshiData1.getVolume());
                }
                Collections.sort(list);
                lowerHigh = list.get(list.size() - 1);
            } else {
                this.closePrice = StockUtils.getColseprice();
            }
        }
        drawBitMap(canvas);
        return bitmap;
    }

    //时间转换点数
    private int getMinute(int hour, int minute) {
        int count = 0;
        switch (hour) {
            case 9:
                count = minute - 30;
                break;
            case 10:
                count = minute + 30;
                break;
            case 11:
                count = minute + 90;
                break;
            case 13:
                count = minute + 120;
                break;
            case 14:
                count = minute + 180;
                break;
            case 15:
                count = minute + 240;
                break;
            default:
                break;
        }
        return count;
    }


    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
    }

    public int getmLongtitudeColor() {
        return mLongtitudeColor;
    }

    public void setmLongtitudeColor(int mLongtitudeColor) {
        this.mLongtitudeColor = mLongtitudeColor;
    }

    public PathEffect getDashEffect() {
        return mDashEffect;
    }

    public void setDashEffect(PathEffect DashEffect) {
        this.mDashEffect = DashEffect;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int BorderColor) {
        this.mBorderColor = BorderColor;
    }

    public float getUperChartHeight() {
        return mUperChartHeight;
    }

    public void setUperChartHeight(float UperChartHeight) {
        this.mUperChartHeight = UperChartHeight;
    }

    public float getLowerChartHeight() {
        return mLowerChartHeight;
    }

    public void setLowerChartHeight(float LowerChartHeight) {
        this.mLowerChartHeight = LowerChartHeight;
    }

    public float getLongitudeSpacing() {
        return longitudeSpacing;
    }

    public float getLatitudeSpacing() {
        return latitudeSpacing;
    }

    public void setLatitudeSpacing(float latitudeSpacing) {
        this.latitudeSpacing = latitudeSpacing;
    }

    public void setIsFromStockDetail(boolean isFromStockDetail) {
        this.isFromStockDetail = isFromStockDetail;
    }

    public void setLongitudeNum(int longitudeNum) {
        this.longitudeNum = longitudeNum;
    }

    public void setUperLatitudeNum(int uperLatitudeNum) {
        this.uperLatitudeNum = uperLatitudeNum;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public int getmSpaceSize() {
        return mSpaceSize;
    }

    public void setmSpaceSize(int mSpaceSize) {
        this.mSpaceSize = mSpaceSize;
    }

    public float getDataspcing() {
        return this.dataSpacing;
    }

    public int getLastPoint() {
        return this.lastPoint;
    }

    public boolean isTimes() {
        return isTimes;
    }

    public float getUperBottom() {
        return this.uperBottom;
    }

    public float getUperHalfHigh() {
        return this.uperHalfHigh;
    }

    public float getUperRate() {
        return this.uperRate;
    }

    public double getInitialWeightedIndex() {
        return this.initialWeightedIndex;
    }

    public HashMap getMap() {
        return this.DetailMap;
    }

    public List getTimeList() {
        return this.timesList;
    }

    public void setShowDetail(boolean isFromDetail) {
        this.isFromDetail = isFromDetail;
    }

    public double getClosePrice() {
        return this.closePrice;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public int getmLatitudeColor() {
        return mLatitudeColor;
    }

    public void setmLatitudeColor(int mLatitudeColor) {
        this.mLatitudeColor = mLatitudeColor;
    }

    public static int getDefaultBackgroud() {
        return DEFAULT_BACKGROUD;
    }

    public int getmAxisTitleSize() {
        return mAxisTitleSize;
    }

    public void setmAxisTitleSize(int mAxisTitleSize) {
        this.mAxisTitleSize = mAxisTitleSize;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }
}
