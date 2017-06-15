package cn.gogoal.im.ui.stockviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import hply.com.niugu.bean.MALineBean;

/**
 * Created by huangxx on 2017/6/15.
 */

public class KChartsBitmap {
    // /////////////默认值////////////////
    public static final int DEFAULT_BACKGROUND = Color.WHITE;        //默认背景色
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
    private static int longitudeNum;                         //上表纬线数
    private static int uperLatitudeNum;                      //经线数
    private float longitudeSpacing;                            //经度的间隔
    private float latitudeSpacing;                             //纬度的间隔
    private DashPathEffect mRedDashPathEffect;                 //中间红色虚线
    private int mAxisTitleSize;                              //字体尺寸
    private int mSpaceSize;                                  //第一条线到顶部间距
    private int mSize;                                       //图形尺寸
    private boolean isFromDetail;                            //判断横竖屏
    private int useWidth;                                    //图形占有宽度

    // /////////////K线图////////////////
    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.YELLOW;     //默认Y轴字体颜色
    private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.RED;        //默认X轴字体颜色
    private final static int MIN_CANDLE_NUM = 20;                           //显示的最小Candle数
    private final static int MAX_CANDLE_NUM = 100;                          //默认显示的Candle数
    private final static int DEFAULT_CANDLE_NUM = 50;                       //默认显示的Candle数
    private final static int MIN_MOVE_DISTANCE = 15;                        //最小可识别的移动距离
    private double mCandleWidth;                                            //Candle宽度

    //OHLC数据
    private int mDataStartIndex;                   //显示的OHLC数据起始位置
    private int mShowDataNum;                       //显示的OHLC数据个数

    //当前数据的最大最小值
    private double mMaxPrice;
    private double mMinPrice;
    private List<MALineBean> MALineData;            // MA数据
    private String mTabTitle;

    //实心空心设置
    private Boolean hollow;
    private Paint hollowPaint;
    private List<Float> mChangeRate;
    private Paint lowerPaint;

    // 下部表的数据
    List<Float> mVolume;
    //均线设置
    private int dayK1;
    private int dayK2;
    private int dayK3;

    //数据解析
    private List<Map<String, Object>> mOHLCData;

    //view的宽高
    private int viewHeight;
    private int viewWidth;
    //线与字体的颜色
    private int red = Color.rgb(0xf2, 0x49, 0x57);
    private int green = Color.rgb(0x1d, 0xbf, 0x60);
    private int gray = Color.rgb(0x54, 0x69, 0x80);

    public KChartsBitmap(Integer width, Integer height) {
        this.viewWidth = width;
        this.viewHeight = height;
        init();
    }

    private void init() {
        mAxisTitleSize = DEFAULT_TITLE_SIZE;
        mSize = DEFAULT_SIZE;
        mBackGround = DEFAULT_BACKGROUND;
        mLongtitudeColor = DEFAULT_LONGTITUDE_COLOR;
        mLatitudeColor = DEFAULT_LAITUDE_COLOR;
        mDashEffect = DEFAULT_DASH_EFFECT;
        mBorderColor = DEFAULT_BORDER_COLOR;
        longitudeNum = DEFAULT_LOGITUDE_NUM;
        uperLatitudeNum = DEFAULT_UPER_LATITUDE_NUM;
        mRedDashPathEffect = new DashPathEffect(new float[]{3, 3, 3, 3}, 2);

        DEFAULT_LOGITUDE_NUM = 4;
        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndex = 0;
        mMaxPrice = -1;
        mMinPrice = -1;
        mTabTitle = "Volume";
        mChangeRate = new ArrayList<>();
        mOHLCData = new ArrayList<>();
        mVolume = new ArrayList<>();
        hollowPaint = new Paint();
        hollowPaint.setStyle(Paint.Style.STROKE);
        if (StringUtils.getHighsAndLows()) {
            hollowPaint.setColor(red);
        } else {
            hollowPaint.setColor(green);
        }
        lowerPaint = new Paint();
    }


    private void drawBitMap(Canvas canvas) {

        setUperChartHeight((viewHeight * 7) / 10f);
        setLowerChartHeight((float) ((viewHeight * 2.1) / 10f));
        useWidth = viewWidth - leftMargin - rightMargin;

        // 绘制边框
        drawBorders(canvas);

        // 绘制经线
        drawLongitudes(canvas);

        // 绘制纬线
        drawLatitudes(canvas);

        //蜡烛图
        drawUpperRegion(canvas);

        //下表柱体
        drawLowerRegion(canvas);

        //绘制标注
        drawXYPostion(canvas);
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

    /*
    * 蜡烛图
    * */
    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        Paint paint = new Paint();
        paint.setStrokeWidth(mSize);
        double rate = (getUperChartHeight() - mSize / 2 - mSpaceSize) / (mMaxPrice - mMinPrice);
        if (mOHLCData.size() >= MIN_CANDLE_NUM) {
            mCandleWidth = useWidth * 1.00f / mShowDataNum;
        } else {
            mCandleWidth = useWidth * 1.00f / MIN_CANDLE_NUM;
        }

        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mOHLCData.size(); i++) {
            float open = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("open_price")) * rate + mSpaceSize + mSize / 2);
            float close = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) * rate + mSpaceSize + mSize / 2);
            float high = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("high_price")) * rate + mSpaceSize + mSize / 2);
            float low = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("low_price")) * rate + mSpaceSize + mSize / 2);

            float left;
            float right;
            float startX;

            if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                left = (float) (useWidth + mSize / 2 - mCandleWidth * (i + 1) + leftMargin);
                right = (float) (useWidth - mSize / 2 - mCandleWidth * i + leftMargin);
                startX = (float) (useWidth - mCandleWidth * i - mCandleWidth / 2 + leftMargin);
            } else {
                left = (float) (useWidth + mSize / 2 - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin);
                right = (float) (useWidth - mSize / 2 - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin);
                startX = (float) (useWidth - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) - mCandleWidth / 2 + leftMargin);
            }

            if ((float) mOHLCData.get(mDataStartIndex + i).get("open_price") <= (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) {
                if (StringUtils.getHighsAndLows()) {
                    paint.setColor(red);
                } else {
                    paint.setColor(green);
                }
            } else {
                if (StringUtils.getHighsAndLows()) {
                    paint.setColor(green);
                } else {
                    paint.setColor(red);
                }
            }

            if (open < close) {
                if (Math.abs(open - close) <= 1f) {
                    canvas.drawLine(left, open, right, open, paint);
                    canvas.drawLine(startX, high, startX, low, paint);
                } else {
                    canvas.drawRect(left, open, right, close, paint);
                    canvas.drawLine(startX, high, startX, low, paint);
                }
            } else if (open == close) {
                canvas.drawLine(left, open, right, open, paint);
                canvas.drawLine(startX, high, startX, low, paint);
            } else {
                if (Math.abs(open - close) <= 1f) {
                    canvas.drawLine(left, close, right, close, paint);
                    canvas.drawLine(startX, high, startX, low, paint);
                } else {
                    if (hollow) {
                        canvas.drawRect(left, close, right, open, hollowPaint);
                    } else {
                        canvas.drawRect(left, close, right, open, paint);
                    }
                    canvas.drawLine(startX, high, startX, close, paint);
                    canvas.drawLine(startX, open, startX, low, paint);
                }
            }
        }

        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MALineData.size(); j++) {
            MALineBean lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint MAPaint = new Paint();
            MAPaint.setColor(lineEntity.getLineColor());
            MAPaint.setTextSize(mAxisTitleSize);
            MAPaint.setStrokeWidth(mSize);
            MAPaint.setAntiAlias(true);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndex + i < lineEntity.getLineData().size(); i++) {

                if (lineEntity.getLineData().get(mDataStartIndex + i) == 0 && mDataStartIndex + i >= lineEntity.getLineData().size() - 30)
                    continue;

                if (i != 0) {
                    if (mShowDataNum < MIN_CANDLE_NUM) {
                        canvas.drawLine(
                                startX + leftMargin,
                                startY + 1,
                                (float) (useWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f) + leftMargin,
                                (float) ((mMaxPrice - lineEntity.getLineData()
                                        .get(mDataStartIndex + i)) * rate + 1 + mSpaceSize),
                                MAPaint);

                    } else {
                        canvas.drawLine(
                                startX + leftMargin,
                                startY + 1,
                                (float) (useWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f) + leftMargin,
                                (float) ((mMaxPrice - lineEntity.getLineData()
                                        .get(mDataStartIndex + i)) * rate + 1 + mSpaceSize),
                                MAPaint);
                    }
                }
                if (mShowDataNum < MIN_CANDLE_NUM) {
                    startX = (float) (useWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f);
                } else {
                    startX = (float) (useWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                }
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndex + i)) * rate) + mSpaceSize;
            }
        }
    }

    /*
     * 下表柱体
     * */
    private void drawLowerRegion(Canvas canvas) {
        float lowerTop = viewHeight - mLowerChartHeight;
        float lowerHeight = viewHeight - lowerTop - mSize;

        Paint magentaPaint = new Paint();
        magentaPaint.setColor(Color.MAGENTA);

        Paint textPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(mAxisTitleSize);

        if (mTabTitle.trim().equalsIgnoreCase("Volume")) {
            double low = mVolume.get(mDataStartIndex);
            double high = low;
            double rate;
            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mVolume.size(); i++) {
                low = low < mVolume.get(i) ? low : mVolume.get(i);
                high = high > mVolume.get(i) ? high : mVolume.get(i);
            }

            rate = lowerHeight / (high - low);

            float zero = (float) (high * rate) + lowerTop;
            if (zero < lowerTop) {
                zero = lowerTop;
            }

            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mVolume.size(); i++) {
                // 绘制矩形
                if (mVolume.get(i) > 0) {
                    if (!hollow) {
                        if (mChangeRate.get(i) < 0) {
                            if (StringUtils.getHighsAndLows()) {
                                lowerPaint.setColor(green);
                            } else {
                                lowerPaint.setColor(red);
                            }
                        } else {
                            if (StringUtils.getHighsAndLows()) {
                                lowerPaint.setColor(red);
                            } else {
                                lowerPaint.setColor(green);
                            }
                        }
                    } else {
                        if (StringUtils.getHighsAndLows()) {
                            lowerPaint.setColor(green);
                        } else {
                            lowerPaint.setColor(red);
                        }
                    }

                    float top = (float) ((high - mVolume.get(i)) * rate) + lowerTop;
                    if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                        if (hollow) {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        useWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            } else {
                                canvas.drawRect((useWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)) + leftMargin,
                                        top + 2,
                                        useWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            }
                        } else {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        useWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        lowerPaint);
                            } else {
                                canvas.drawRect((useWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)) + leftMargin,
                                        top + 2,
                                        useWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero + 2,
                                        lowerPaint);
                            }
                        }
                    } else {
                        if (hollow) {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        zero + 1,
                                        useWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero + 1,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            } else {
                                canvas.drawRect(useWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        top + 1,
                                        useWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero + 1,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            }
                        } else {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        zero + 1,
                                        useWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero + 1,
                                        lowerPaint);
                            } else {
                                canvas.drawRect(useWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        top + 1,
                                        useWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero + 1,
                                        lowerPaint);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * 绘制标注
     * */
    private void drawXYPostion(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() == 0)
            return;

        Paint title_paint = new Paint();
        title_paint.setTextSize(mAxisTitleSize);
        title_paint.setColor(gray);
        title_paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = title_paint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        // 左边的坐标
        String pos_max_price = String.format("%.2f", mMaxPrice);

        String close_price = String.format("%.2f", (mMaxPrice + mMinPrice) / 2);

        String pos_min_price = String.format("%.2f", mMinPrice);

        //当前数据最大交易量
        float maxVolume = 0;
        for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mOHLCData.size(); i++) {
            if ((float) mOHLCData.get(i).get("volume") > maxVolume) {
                maxVolume = (float) mOHLCData.get(i).get("volume");
            }
        }

        // 绘制左边Y轴的坐标
        canvas.drawText(pos_max_price, leftMargin, fontHeight + 2 * mSize, title_paint);
        canvas.drawText(pos_min_price, leftMargin, mUperChartHeight - 3 * mSize, title_paint);

        //画X坐标
        String date;
        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndex).get("date")).substring(0, 10);

            canvas.drawText(
                    date,
                    viewWidth - title_paint.measureText(date) - rightMargin,
                    (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                    title_paint);
        }

        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum / 2).get("date")).substring(0, 10);
            canvas.drawText(
                    date,
                    (useWidth - title_paint.measureText(date)) / 2 + leftMargin,
                    (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                    title_paint);
        }

        //最左边
        date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum - 1).get("date")).substring(0, 10);
        canvas.drawText(
                date,
                leftMargin,
                (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                title_paint);
    }

    /*
     * 当前数据
     * */
    private void setCurrentData() {
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }

        if (mShowDataNum > mOHLCData.size()) {
            mDataStartIndex = 0;
        } else if (mShowDataNum + mDataStartIndex > mOHLCData.size()) {
            mDataStartIndex = mOHLCData.size() - mShowDataNum;
        }
        mMinPrice = (float) mOHLCData.get(mDataStartIndex).get("low_price");
        mMaxPrice = (float) mOHLCData.get(mDataStartIndex).get("high_price");
        for (int i = mDataStartIndex + 1; i < mOHLCData.size()
                && i < mShowDataNum + mDataStartIndex; i++) {
            mMinPrice = mMinPrice < (float) mOHLCData.get(i).get("low_price") ? mMinPrice : (float) mOHLCData.get(i).get("low_price");
            mMaxPrice = mMaxPrice > (float) mOHLCData.get(i).get("high_price") ? mMaxPrice : (float) mOHLCData.get(i).get("high_price");
            mVolume.add((Float) mOHLCData.get(i).get("volume"));
        }

        for (MALineBean lineEntity : MALineData) {
            for (int i = mDataStartIndex; i < lineEntity.getLineData().size()
                    && i < mShowDataNum + mDataStartIndex; i++) {

                if (lineEntity.getLineData().get(i) != 0)
                    mMinPrice = mMinPrice < lineEntity.getLineData().get(i) ? mMinPrice : lineEntity
                            .getLineData().get(i);

                mMaxPrice = mMaxPrice > lineEntity.getLineData().get(i) ? mMaxPrice : lineEntity
                        .getLineData().get(i);
            }
        }
    }

    /**
     * 初始化MA值
     */
    private List<Float> initMA(List<Map<String, Object>> entityList, int days) {
        if (days < 0 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Float> MAValues = new ArrayList<Float>();

        for (Map<String, Object> ohlcData : entityList) {
            if (days == dayK1 && (Float) ohlcData.get("avg_price_" + dayK1) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK1));
            } else if (days == dayK2 && (Float) ohlcData.get("avg_price_" + dayK2) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK2));
            } else if (days == dayK3 && (Float) ohlcData.get("avg_price_" + dayK3) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK3));
            } else {

            }
        }

        return MAValues;
    }

    public Bitmap setOHLCData(List<Map<String, Object>> OHLCData) {

        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        if (OHLCData == null || OHLCData.size() <= 0) {
            drawBitMap(canvas);
            return bitmap;
        }
        this.mOHLCData = OHLCData;
        hollow = SPTools.getBoolean("hollow", false);
        dayK1 = SPTools.getInt("tv_ln1", 5);
        dayK2 = SPTools.getInt("tv_ln2", 10);
        dayK3 = SPTools.getInt("tv_ln3", 20);

        for (Map<String, Object> data : mOHLCData) {
            mVolume.add((Float) data.get("volume"));
            mChangeRate.add((Float) data.get("price_change_rate"));
        }

        initMALineData();

        setCurrentData();

        drawBitMap(canvas);

        return bitmap;
    }

    private void initMALineData() {
        MALineBean MA_First = new MALineBean();
        MA_First.setTitle("MA" + dayK1);
        MA_First.setLineColor(Color.rgb(0xff, 0xbf, 0x00));
        MA_First.setLineData(initMA(mOHLCData, dayK1));

        MALineBean MA_Second = new MALineBean();
        MA_Second.setTitle("MA" + dayK2);
        MA_Second.setLineColor(Color.rgb(0x05, 0x6c, 0xfc));
        MA_Second.setLineData(initMA(mOHLCData, dayK2));

        MALineBean MA_Third = new MALineBean();
        MA_Third.setTitle("MA" + dayK3);
        MA_Third.setLineColor(Color.rgb(0xff, 0x59, 0xc9));
        MA_Third.setLineData(initMA(mOHLCData, dayK3));

        MALineData = new ArrayList<MALineBean>();
        MALineData.add(MA_First);
        MALineData.add(MA_Second);
        MALineData.add(MA_Third);
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

    public void setShowDetail(boolean isFromDetail) {
        this.isFromDetail = isFromDetail;
    }

    public int getmLatitudeColor() {
        return mLatitudeColor;
    }

    public void setmLatitudeColor(int mLatitudeColor) {
        this.mLatitudeColor = mLatitudeColor;
    }

    public static int getDefaultBackground() {
        return DEFAULT_BACKGROUND;
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
