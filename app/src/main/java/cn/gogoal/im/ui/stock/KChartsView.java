package cn.gogoal.im.ui.stock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.activity.copy.MessageHandlerList;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.ui.copy.GridChart;
import hply.com.niugu.ConstantUtils;
import hply.com.niugu.StringUtils;
import hply.com.niugu.bean.KDJBean;
import hply.com.niugu.bean.MACDBean;
import hply.com.niugu.bean.MALineBean;
import hply.com.niugu.bean.RSIBean;

public class KChartsView extends GridChart implements GridChart.OnTabClickListener {
    public static final String TAG = "KChartsView";

    public static final int KLINE_TYPE_DAY = 0;
    public static final int KLINE_TYPE_WEEK = 1;
    public static final int KLINE_TYPE_MONTH = 2;

    /**
     * 触摸模式
     */
    public static int TOUCH_MODE;
    public final static int NONE = 0;
    public final static int DOWN = 1;
    public final static int MOVE = 2;
    public final static int ZOOM = 3;
    public final static int LONG_TOUCH = 4;

    /**
     * 默认Y轴字体颜色
     **/
    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.YELLOW;

    /**
     * 默认X轴字体颜色
     **/
    private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.RED;

    /**
     * 显示的最小Candle数
     */
    private final static int MIN_CANDLE_NUM = 60;

    /**
     * 最大显示的Candle数
     */
    private final static int MAX_CANDLE_NUM = 150;

    /**
     * 默认显示的Candle数
     */
    private final static int DEFAULT_CANDLE_NUM = 100;

    /**
     * 最小可识别的移动距离
     */
    private final static int MIN_MOVE_DISTANCE = 15;


    /**
     * Candle宽度
     */
    private double mCandleWidth;

    /**
     * 触摸点
     */
    private float mStartX;
    private float mStartY;
    private float mPointY;

    /**
     * OHLC数据
     */
    private List<Map<String, Object>> mOHLCData;

    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndex;

    /**
     * 显示的OHLC数据个数
     */
    private int mShowDataNum;

    /**
     * 是否显示蜡烛详情
     */
    private boolean showDetails;

    /**
     * 当前数据的最大最小值
     */
    private double mMaxPrice;
    private double mMinPrice;
    private float maxVolume = 0;

    /**
     * MA数据
     */
    private List<MALineBean> MALineData;

    //均线设置
    private int dayk1;
    private int dayk2;
    private int dayk3;

    private String mTabTitle;

    /**
     * 下部表的数据
     */
    private List<Float> mVolume;
    private MACDBean mMACDData;
    private KDJBean mKDJData;
    private RSIBean mRSIData;

    /**
     * 长按时选中的股票下标
     */
    private int selectIndex;

    /**
     * 拉伸时用来计算滑动距离的变量
     */
    private float oldDist, newDist;

    /**
     * 监听飞滑 长按动作
     */
    private GestureDetector mGestureDetector;
    private int flingCount = 0;

    /**
     * K线图类型
     */
    private int chartsType;

    /**
     * 画笔
     */
    private Paint candlePaint, titlePaint, detailPaint, lowerPaint, leftTitlePaint;
    private Rect r;

    /**
     * 是否在刷新
     */
    private boolean isRefresh;
    /**
     * 蜡烛空实心设置
     */
    private Boolean hollow;
    private Paint hollowpaint;
    private List<Float> mChangeRate;

    //十字是否跟随
    private boolean follow;

    /**
     * 数据小于最小Candle数
     */
    private boolean dataMore;
    private double rate;
    private float volumeRate;

    public KChartsView(Context context) {
        super(context);
        init(context);
    }

    public KChartsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KChartsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        super.setOnTabClickListener(this);

        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndex = 0;
        showDetails = false;
        mMaxPrice = -1;
        mMinPrice = -1;
        mTabTitle = "Volume";
        mChangeRate = new ArrayList<>();

        mOHLCData = new ArrayList<>();
        mVolume = new ArrayList<>();
        mMACDData = new MACDBean(null);
        mKDJData = new KDJBean(null);
        mRSIData = new RSIBean(null);

        mGestureDetector = new GestureDetector(context, new KChartGestureDetectorListener());

        candlePaint = new Paint();
        titlePaint = new Paint();
        detailPaint = new Paint();
        lowerPaint = new Paint();
        leftTitlePaint = new Paint();
        r = new Rect();

        isRefresh = true;
        dataMore = true;
        hollowpaint = new Paint();
        hollowpaint.setStyle(Paint.Style.STROKE);
        hollowpaint.setColor(red);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        drawLines(canvas);
        drawCandleDetails(canvas);
    }

    @Override
    protected void drawXYPosition(Canvas canvas, int viewHeight, int viewWidth) {
        if (mOHLCData == null || mOHLCData.size() == 0)
            return;

        Map<String, Integer> leftPositionLengths = new HashMap<>();

        titlePaint.setTextSize(mAxisTitleSize);
        titlePaint.setColor(gray);

        // 左边的坐标
        String pos_max_price = StringUtils.save2Significand(mMaxPrice);
        titlePaint.getTextBounds(pos_max_price, 0, pos_max_price.length(), r);
        leftPositionLengths.put("pos_max_price", r.width());

        String close_price = StringUtils.save2Significand((mMaxPrice + mMinPrice) / 2);
        titlePaint.getTextBounds(close_price, 0, close_price.length(), r);
        leftPositionLengths.put("close_price", r.width());

        String pos_min_price = StringUtils.save2Significand(mMinPrice);
        titlePaint.getTextBounds(pos_min_price, 0, pos_min_price.length(), r);
        leftPositionLengths.put("pos_min_price", r.width());

        //当前数据最大交易量
        for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mOHLCData.size(); i++) {
            if ((float) mOHLCData.get(i).get("volume") > maxVolume) {
                maxVolume = (float) mOHLCData.get(i).get("volume");
            }
        }

        String volume = StringUtils.save2Significand(maxVolume / 1000000);
        titlePaint.getTextBounds(volume, 0, volume.length(), r);
        leftPositionLengths.put("volume", r.width());

        String unit = "万手";
        titlePaint.getTextBounds(unit, 0, unit.length(), r);
        leftPositionLengths.put("unit", r.width());

        // 计算左边最长坐标长度
        for (Integer length : leftPositionLengths.values()) {
            if (length > leftMargin)
                leftMargin = length;
        }

        //字体高度（字体高度与大小的值有偏差）
        int fontHeight = r.height();

        // 绘制左边Y轴的坐标
        canvas.drawText(pos_max_price, leftMargin - leftPositionLengths.get("pos_max_price"), fontHeight + 3, titlePaint);
        canvas.drawText(close_price, leftMargin - leftPositionLengths.get("close_price"), (mUperChartHeight + fontHeight) / 2, titlePaint);
        canvas.drawText(pos_min_price, leftMargin - leftPositionLengths.get("pos_min_price"), mUperChartHeight - 3, titlePaint);
        canvas.drawText(volume, leftMargin - leftPositionLengths.get("volume"), viewHeight - mLowerChartHeight + fontHeight, titlePaint);
        canvas.drawText(unit, leftMargin - leftPositionLengths.get("unit"), viewHeight - 3, titlePaint);

        //画X坐标
        String date;
        if (dataMore) {
            date = ((String) mOHLCData.get(mDataStartIndex).get("date")).substring(0, 10);
            titlePaint.getTextBounds(date, 0, date.length(), r);
            canvas.drawText(
                    date,
                    getWidth() - r.width() - 5,
                    mUperChartHeight + DEFAULT_AXIS_TITLE_SIZE,
                    titlePaint);
        }

        if (dataMore) {
            date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum / 2).get("date")).substring(0, 10);
            titlePaint.getTextBounds(date, 0, date.length(), r);
            canvas.drawText(
                    date,
                    (getWidth() - leftMargin - r.width()) / 2 + leftMargin,
                    mUperChartHeight + DEFAULT_AXIS_TITLE_SIZE,
                    titlePaint);
        }

        date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum - 1).get("date")).substring(0, 10);
        titlePaint.getTextBounds(date, 0, date.length(), r);
        canvas.drawText(
                date,
                leftMargin,
                mUperChartHeight + DEFAULT_AXIS_TITLE_SIZE,
                titlePaint);
    }

    @Override
    protected void drawLines(Canvas canvas) {
        //蜡烛图
        drawUpperRegion(canvas);
        //下表
        drawLowerRegion(canvas);

        k_animation_tag -= 2;
        if (k_animation_tag >= -2) invalidate();
    }

    private void drawCandleDetails(Canvas canvas) {
        String sPrice = "";
        float dataY = 0;
        float close_price = 0;
        float open_price = 0;
        float volume = 0;
        Rect r = new Rect();
        Map<String, Integer> rValues = new HashMap<>();
        if (showDetails) {
            float height = getHeight();

            // 绘制点击线条及详情区域
            if (dataMore) {
                selectIndex = (int) ((getWidth() - mStartX) / mCandleWidth + mDataStartIndex);
            } else {
                selectIndex = ((int) ((getWidth() - mStartX) / mCandleWidth) - MIN_CANDLE_NUM + mShowDataNum);
                selectIndex = selectIndex < 0 ? 0 : selectIndex;
                selectIndex = selectIndex > mShowDataNum - 1 ? mShowDataNum - 1 : selectIndex;
            }
            if (follow) {
                if (selectIndex < mOHLCData.size() && selectIndex > -1) {
                    dataY = (float) ((mMaxPrice - (float) mOHLCData.get(selectIndex).get("close_price")) * rate + 2);
                    close_price = (float) mOHLCData.get(selectIndex).get("close_price");
                    open_price = (float) mOHLCData.get(selectIndex).get("open_price");
                    sPrice = StringUtils.save2Significand(close_price);
                }
            } else {
                dataY = mPointY;
                if (dataY < mUperChartHeight && dataY > 0) {
                    close_price = (float) (mMaxPrice - (dataY - 2) / rate);
                    if (close_price > mMaxPrice) {
                        close_price = (float) mMaxPrice;
                    } else if (close_price < mMinPrice) {
                        close_price = (float) mMinPrice;
                    }
                    sPrice = StringUtils.save2Significand(close_price);
                } else if (dataY <= height && dataY >= height - mLowerChartHeight + 1) {
                    volume = maxVolume - ((dataY - (height - mLowerChartHeight + 2)) / volumeRate);
                    close_price = volume / 1000000;
                    if (volume > maxVolume) {
                        close_price = maxVolume / 1000000;
                    }
                    if (close_price < 0) {
                        close_price = 0;
                    }
                    sPrice = StringUtils.save2Significand(close_price);
                }
            }

            leftTitlePaint.setTextSize(mAxisTitleSize);
            if (follow) {
                if (open_price <= close_price) {
                    leftTitlePaint.setColor(red);
                } else {
                    leftTitlePaint.setColor(green);
                }
            } else {
                leftTitlePaint.setColor(gray);
            }

            leftTitlePaint.setAlpha(200);
            leftTitlePaint.setAntiAlias(true);

            leftTitlePaint.getTextBounds(sPrice, 0, sPrice.length(), r);
            rValues.put("sPriceW", r.width());
            rValues.put("sPriceH", r.height());

            detailPaint.setColor(Color.BLACK);
            detailPaint.setAlpha(150);
            detailPaint.setStrokeWidth(3);

            float w, h;
            h = rValues.get("sPriceH");
            MessageHandlerList.sendMessage(StockDetailChartsActivity.class, 6 , mOHLCData, selectIndex);
            if (dataMore) {
                if ((mStartX > leftMargin && mStartX < getWidth())) {
                    if (follow) {
                        canvas.drawLine(leftMargin, dataY, getWidth(), dataY, detailPaint);
                    } else {
                        if (!(mPointY > mUperChartHeight && mPointY < height - mLowerChartHeight)) {
                            canvas.drawLine(leftMargin, dataY, getWidth(), dataY, detailPaint);
                        }
                    }
                    canvas.drawLine(mStartX, 0, mStartX, mUperChartHeight,
                            detailPaint);
                    canvas.drawLine(mStartX, height - mLowerChartHeight, mStartX, height, detailPaint);
                    if (dataY >= (h / 2) + 5 && dataY < height - (h / 2) - 5) {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        dataY - (h / 2) - 5,
                                        leftMargin,
                                        dataY + (h / 2) + 5),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);

                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                dataY + (h / 2),
                                leftTitlePaint);
                    } else if (dataY >= height - (h / 2) - 5) {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        height - h - 10,
                                        leftMargin,
                                        height),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);

                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                height - 5,
                                leftTitlePaint);
                    } else {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        0,
                                        leftMargin,
                                        h + 10),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                h + 5,
                                leftTitlePaint);

                    }
                } else {
                    MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                    TOUCH_MODE = NONE;
                    postInvalidate();
                }
            } else {
                if ((mStartX > leftMargin && mStartX < (mShowDataNum * mCandleWidth) + leftMargin)) {
                    if (follow) {
                        canvas.drawLine(leftMargin, dataY, getWidth(), dataY, detailPaint);
                    } else {
                        if (!(mPointY > mUperChartHeight && mPointY < height - mLowerChartHeight)) {
                            canvas.drawLine(leftMargin, dataY, getWidth(), dataY, detailPaint);
                        }
                    }
                    canvas.drawLine(mStartX, 0, mStartX, mUperChartHeight,
                            detailPaint);
                    canvas.drawLine(mStartX, height - mLowerChartHeight, mStartX, height, detailPaint);
                    if (dataY >= (h / 2) + 5 && dataY < height - (h / 2) - 5) {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        dataY - (h / 2) - 5,
                                        leftMargin,
                                        dataY + (h / 2) + 5),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);

                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                dataY + (h / 2),
                                leftTitlePaint);
                    } else if (dataY >= height - (h / 2) - 5) {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        height - h - 10,
                                        leftMargin,
                                        height),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);

                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                height - 5,
                                leftTitlePaint);
                    } else {
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawRoundRect(
                                new RectF(
                                        leftMargin - w,
                                        0,
                                        leftMargin,
                                        h + 10),
                                h / 3,
                                h / 3,
                                leftTitlePaint);

                        leftTitlePaint.setColor(Color.WHITE);
                        leftTitlePaint.setAlpha(255);
                        w = rValues.get("sPriceW");
                        h = rValues.get("sPriceH");
                        canvas.drawText(
                                sPrice,
                                leftMargin - w - 1,
                                h + 5,
                                leftTitlePaint);
                    }
                } else {
                    MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                    TOUCH_MODE = NONE;
                    postInvalidate();
                }
            }
        }
    }

    private int k_animation_tag = DEFAULT_CANDLE_NUM - 1;

    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        candlePaint.setColor(gray);
        int viewWidth = getWidth() - leftMargin;
        rate = (getUperChartHeight() - 4) / (mMaxPrice - mMinPrice);

        if (dataMore) {
            mCandleWidth = viewWidth * 1.00f / mShowDataNum;
        } else {
            mCandleWidth = viewWidth * 1.00f / MIN_CANDLE_NUM;
        }

        for (int i = mShowDataNum - 1; i >= 0 && mDataStartIndex + i < mOHLCData.size(); i--) {
            if (k_animation_tag <= i) {

                float open = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("open_price")) * rate + 2);
                float close = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) * rate + 2);
                float high = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("high_price")) * rate + 2);
                float low = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("low_price")) * rate + 2);

                float left;
                float right;
                float startX;

                if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                    left = (float) (getWidth() + 2 - mCandleWidth * (i + 1));
                    right = (float) (getWidth() - 2 - mCandleWidth * i);
                    startX = (float) (getWidth() - mCandleWidth * i - mCandleWidth / 2);
                } else {
                    left = (float) (getWidth() + 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i + 1));
                    right = (float) (getWidth() - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i));
                    startX = (float) (getWidth() - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth / 2);
                }

                if ((float) mOHLCData.get(mDataStartIndex + i).get("open_price") <= (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) {
                        candlePaint.setColor(red);
                } else {
                        candlePaint.setColor(green);
                }

//            if ((mDataStartIndex + i + 1) < mOHLCData.size()) {
//                if (entity.getClose_price() >= mOHLCData.get(mDataStartIndex + i + 1).getClose_price())
//                    candlePaint.setColor(red);
//                else
//                    candlePaint.setColor(green);
//            }

                if (open < close) {
                    if (Math.abs(open - close) <= 1f) {
                        canvas.drawLine(left, open, right, open, candlePaint);
                        canvas.drawLine(startX, high, startX, low, candlePaint);
                    } else {
                        canvas.drawRect(left, open, right, close, candlePaint);
                        canvas.drawLine(startX, high, startX, low, candlePaint);
                    }
                } else if (open == close) {
                    canvas.drawLine(left, open, right, open, candlePaint);
                    canvas.drawLine(startX, high, startX, low, candlePaint);
                } else {
                    if (Math.abs(open - close) <= 1f) {
                        canvas.drawLine(left, close, right, close, candlePaint);
                        canvas.drawLine(startX, high, startX, low, candlePaint);
                    } else {
                        if (hollow) {
                            canvas.drawRect(left, close, right, open, hollowpaint);
                        } else {
                            canvas.drawRect(left, close, right, open, candlePaint);
                        }
                        canvas.drawLine(startX, high, startX, low, candlePaint);
                    }
                }
            }
        }

        // 绘制上部曲线图及上部分MA值
        float MATitleWidth = viewWidth / 6;
        for (int j = 0; j < MALineData.size(); j++) {
            MALineBean lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint MAPaint = new Paint();
            MAPaint.setColor(lineEntity.getLineColor());
            MAPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            MAPaint.setStrokeWidth(3);

            if (selectIndex < mOHLCData.size() && selectIndex >= 0) {
                if (TOUCH_MODE == LONG_TOUCH) {
                    canvas.drawText(
                            lineEntity.getTitle() + ":" + new DecimalFormat("#.##").format(lineEntity.getLineData().get(selectIndex)),
                            (mStartX < (getWidth() + leftMargin) / 2 || mStartX == (getWidth() + leftMargin) / 2) ? (2 + viewWidth / 2 + MATitleWidth * j + leftMargin) : (MATitleWidth * j + leftMargin),
                            DEFAULT_AXIS_TITLE_SIZE,
                            MAPaint);
                } else {
                    canvas.drawText(
                            lineEntity.getTitle() + ":" + new DecimalFormat("#.##").format(lineEntity.getLineData().get(mDataStartIndex)),
                            MATitleWidth * j + leftMargin,
                            DEFAULT_AXIS_TITLE_SIZE,
                            MAPaint);
                }
            }

            for (int i = mShowDataNum - 1; i >= 0
                    && mDataStartIndex + i < lineEntity.getLineData().size(); i--) {
                if (k_animation_tag <= i) {
                    if (lineEntity.getLineData().get(mDataStartIndex + i) == 0
                            && mDataStartIndex + i >= lineEntity.getLineData().size() - 30)
                        continue;

                    if (i != mShowDataNum - 1 && startX != 0 && startY != 0) {
                        if (dataMore) {
                            canvas.drawLine(
                                    startX + leftMargin,
                                    startY + 1,
                                    (float) (viewWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f) + leftMargin,
                                    (float) ((mMaxPrice - lineEntity.getLineData()
                                            .get(mDataStartIndex + i)) * rate + 1),
                                    MAPaint);
                        } else {
                            canvas.drawLine(
                                    startX + leftMargin,
                                    startY + 1,
                                    (float) (viewWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f) + leftMargin,
                                    (float) ((mMaxPrice - lineEntity.getLineData()
                                            .get(mDataStartIndex + i)) * rate + 1),
                                    MAPaint);
                        }
                    }

                    if (mShowDataNum < MIN_CANDLE_NUM) {
                        startX = (float) (viewWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f);
                    } else {
                        startX = (float) (viewWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                    }
                    if (lineEntity.getLineData().get(mDataStartIndex + i) > 0) {
                        startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndex + i)) * rate);
                    }
                }
            }
        }


    }

    private void drawLowerRegion(Canvas canvas) {
        float lowerTop = getHeight() - mLowerChartHeight + 2;
        float lowerHeight = getHeight() - lowerTop - 4;
        float viewWidth = getWidth();

        if (mTabTitle.trim().equalsIgnoreCase("Volume")) {
            float low = mVolume.get(mDataStartIndex);
            float high = low;
            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mVolume.size(); i++) {
                low = low < mVolume.get(i) ? low : mVolume.get(i);
                high = high > mVolume.get(i) ? high : mVolume.get(i);
            }

            volumeRate = lowerHeight / (high - low);

            float zero = high * volumeRate + lowerTop;
            if (zero < lowerTop) {
                zero = lowerTop;
            }

            for (int i = mDataStartIndex + mShowDataNum - 1; i >= mDataStartIndex && i < mVolume.size(); i--) {
                if (k_animation_tag <= i) {

                    // 绘制矩形
                    if (mVolume.get(i) > 0) {
                        if (!hollow) {
                            if (mChangeRate.get(i) < 0) {
                                    lowerPaint.setColor(green);
                            } else {
                                    lowerPaint.setColor(red);
                            }
                        } else {
                                lowerPaint.setColor(green);
                        }

                        float top = ((high - mVolume.get(i)) * volumeRate) + lowerTop;
                        if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                            if (hollow) {
                                if (zero - top <= 0.3f) {
                                    canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex),
                                            zero + 1,
                                            mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                                } else {
                                    canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex),
                                            zero + 1,
                                            mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                                }
                            } else {
                                if (zero - top <= 0.3f) {
                                    canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex),
                                            zero + 1,
                                            lowerPaint);
                                } else {
                                    canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndex)),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndex),
                                            zero + 1,
                                            lowerPaint);
                                }
                            }
                        } else {
                            if (hollow) {
                                if (zero - top <= 0.3f) {
                                    canvas.drawLine(viewWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1),
                                            zero + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i),
                                            zero + 1,
                                            mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                                } else {
                                    canvas.drawRect(viewWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i),
                                            zero + 1,
                                            mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                                }
                            } else {
                                if (zero - top <= 0.3f) {
                                    canvas.drawLine(viewWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1),
                                            zero + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i),
                                            zero + 1,
                                            lowerPaint);
                                } else {
                                    canvas.drawRect(viewWidth + 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1),
                                            top + 1,
                                            viewWidth - 2 - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i),
                                            zero + 1,
                                            lowerPaint);
                                }
                            }
                        }
                    }
                }
            }
//            textPaint.setColor(Color.GRAY);
//            String text = "5.8";
//            canvas.drawText(text, maxLeftPositionLength == 0 ? 2 : (2 * DEFAULT_AXIS_TITLE_SIZE), lowertop
//                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
//            text = "万手";
//            canvas.drawText(text, maxLeftPositionLength == 0 ? 2 : (2 * DEFAULT_AXIS_TITLE_SIZE), getHeight() - 10, textPaint);
        }
        // 下部表的数据
        // MACDData mMACDData;
        // KDJData mKDJData;
        // RSIData mRSIData;
//        Paint whitePaint = new Paint();
//        whitePaint.setColor(Color.WHITE);
//        Paint yellowPaint = new Paint();
//        yellowPaint.setColor(Color.YELLOW);
//        Paint magentaPaint = new Paint();
//        magentaPaint.setColor(Color.MAGENTA);
//
//        Paint textPaint = new Paint();
//        textPaint.setColor(Color.GRAY);
//        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
//
//        if (mTabTitle.trim().equalsIgnoreCase("MACD")) {
//            List<Double> MACD = mMACDData.getMACD();
//            List<Double> DEA = mMACDData.getDEA();
//            List<Double> DIF = mMACDData.getDIF();
//
//            double low = DEA.get(mDataStartIndex);
//            double high = low;
//            double rate = 0.0;
//            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < MACD.size(); i++) {
//                low = low < MACD.get(i) ? low : MACD.get(i);
//                low = low < DEA.get(i) ? low : DEA.get(i);
//                low = low < DIF.get(i) ? low : DIF.get(i);
//
//                high = high > MACD.get(i) ? high : MACD.get(i);
//                high = high > DEA.get(i) ? high : DEA.get(i);
//                high = high > DIF.get(i) ? high : DIF.get(i);
//            }
//            rate = lowerHight / (high - low);
//
//            float median = (float) ((high + low) / 2);
//
//            Paint paint = new Paint();
//            float zero = lowerHight / 2 + lowertop;
//
//            // 绘制双线
//            float dea = 0.0f;
//            float dif = 0.0f;
//            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < MACD.size(); i++) {
//                // 绘制矩形
//                if (MACD.get(i) >= 0.0) {
//                    paint.setColor(Color.RED);
//                    float top = (float) ((high - MACD.get(i)) * rate) + lowertop;
//                    if (zero - top < 0.55f) {
//                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                * (i + 1 - mDataStartIndex), zero, viewWidth - 2
//                                - (float) mCandleWidth * (i - mDataStartIndex), zero, paint);
//                    } else {
//                    canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
//                            * (i + 1 - mDataStartIndex), top, viewWidth - 2
//                            - (float) mCandleWidth * (i - mDataStartIndex), zero, paint);
//                    }
//                } else {
//                    paint.setColor(Color.GREEN);
//                    float bottom = (float) ((high - MACD.get(i)) * rate) + lowertop;
//
//                    if (bottom - zero < 0.55f) {
//                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                * (i + 1 - mDataStartIndex), zero, viewWidth - 2
//                                - (float) mCandleWidth * (i - mDataStartIndex), zero, paint);
//                    } else {
//                    canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
//                            * (i + 1 - mDataStartIndex), zero, viewWidth - 2
//                            - (float) mCandleWidth * (i - mDataStartIndex), bottom, paint);
//                    }
//                }
//
//                if (i != mDataStartIndex) {
//                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                    * (i + 1 - mDataStartIndex) + (float) mCandleWidth / 2,
//                            (float) ((high - DEA.get(i)) * rate) + lowertop, viewWidth - 2
//                                    - (float) mCandleWidth * (i - mDataStartIndex)
//                                    + (float) mCandleWidth / 2, dea, whitePaint);
//
//                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                    * (i + 1 - mDataStartIndex) + (float) mCandleWidth / 2,
//                            (float) ((high - DIF.get(i)) * rate) + lowertop, viewWidth - 2
//                                    - (float) mCandleWidth * (i - mDataStartIndex)
//                                    + (float) mCandleWidth / 2, dif, yellowPaint);
//                }
//
//                dea = (float) ((high - DEA.get(i)) * rate) + lowertop;
//                dif = (float) ((high - DIF.get(i)) * rate) + lowertop;
//            }
//
//            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
//                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
//            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
//                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
//            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
//                    textPaint);
//
//        } else if (mTabTitle.trim().equalsIgnoreCase("KDJ")) {
//            List<Double> Ks = mKDJData.getK();
//            List<Double> Ds = mKDJData.getD();
//            List<Double> Js = mKDJData.getJ();
//
//            double low = Ks.get(mDataStartIndex);
//            double high = low;
//            double rate = 0.0;
//            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < Ks.size(); i++) {
//                low = low < Ks.get(i) ? low : Ks.get(i);
//                low = low < Ds.get(i) ? low : Ds.get(i);
//                low = low < Js.get(i) ? low : Js.get(i);
//
//                high = high > Ks.get(i) ? high : Ks.get(i);
//                high = high > Ds.get(i) ? high : Ds.get(i);
//                high = high > Js.get(i) ? high : Js.get(i);
//            }
//            rate = lowerHight / (high - low);
//
//            // 绘制白、黄、紫线
//            float k = 0.0f;
//            float d = 0.0f;
//            float j = 0.0f;
//            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < Ks.size(); i++) {
//
//                if (i != mDataStartIndex) {
//                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                    * (i + 1 - mDataStartIndex) + (float) mCandleWidth / 2,
//                            (float) ((high - Ks.get(i)) * rate) + lowertop, viewWidth - 2
//                                    - (float) mCandleWidth * (i - mDataStartIndex)
//                                    + (float) mCandleWidth / 2, k, whitePaint);
//
//                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                    * (i + 1 - mDataStartIndex) + (float) mCandleWidth / 2,
//                            (float) ((high - Ds.get(i)) * rate) + lowertop, viewWidth - 2
//                                    - (float) mCandleWidth * (i - mDataStartIndex)
//                                    + (float) mCandleWidth / 2, d, yellowPaint);
//
//                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
//                                    * (i + 1 - mDataStartIndex) + (float) mCandleWidth / 2,
//                            (float) ((high - Js.get(i)) * rate) + lowertop, viewWidth - 2
//                                    - (float) mCandleWidth * (i - mDataStartIndex)
//                                    + (float) mCandleWidth / 2, j, magentaPaint);
//                }
//                k = (float) ((high - Ks.get(i)) * rate) + lowertop;
//                d = (float) ((high - Ds.get(i)) * rate) + lowertop;
//                j = (float) ((high - Js.get(i)) * rate) + lowertop;
//            }
//
//            canvas.drawText(new DecimalFormat("#.##").format(high), 2, lowertop
//                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
//            canvas.drawText(new DecimalFormat("#.##").format((high + low) / 2), 2, lowertop
//                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
//            canvas.drawText(new DecimalFormat("#.##").format(low), 2, lowertop + lowerHight,
//                    textPaint);
//
//        } else if (mTabTitle.trim().equalsIgnoreCase("RSI")) {
//
//        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MALineData == null) {
            return true;
        }

        if (TOUCH_MODE != LONG_TOUCH && event.getPointerCount() == 1) {
            mGestureDetector.onTouchEvent(event);
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 设置触摸模式
            case MotionEvent.ACTION_DOWN:
                TOUCH_MODE = DOWN;
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                TOUCH_MODE = NONE;
                showDetails = false;
                MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                TOUCH_MODE = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mOHLCData == null || mOHLCData.size() <= 0) {
                    return true;
                }

                if (TOUCH_MODE == LONG_TOUCH) {
                    showDetails = true;
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                    mPointY = event.getY();

                    if (!(mStartX < leftMargin + 1 || mStartX > getWidth() - 1)) {
                        postInvalidate();
                    }
                } else {
                    showDetails = false;
                    if (TOUCH_MODE == MOVE && event.getPointerCount() == 1) {
                        float horizontalSpacing = event.getRawX() - mStartX;
                        if (Math.abs(horizontalSpacing) < MIN_MOVE_DISTANCE) {
                            return true;
                        }
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        if (horizontalSpacing < 0) {
                            mDataStartIndex -= 3;
                            if (mDataStartIndex < 0) {
                                mDataStartIndex = 0;
                            }
                        } else if (horizontalSpacing > 0) {
                            mDataStartIndex += 3;
                        }

                        if (mDataStartIndex + mShowDataNum > mOHLCData.size() && isRefresh) {
                            isRefresh = false;
                            Message msg = Message.obtain();
                            msg.arg1 = chartsType;
                            msg.what = ConstantUtils.REFRESH_KLINE_DATA;
                            MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessage(msg);
                        }

                        setCurrentData();
                        postInvalidate();
                    } else if (TOUCH_MODE == ZOOM & event.getPointerCount() > 1) {
                        newDist = spacing(event);
                        if (newDist - oldDist > 0) {
                            zoomOut();
                        } else if (oldDist - newDist > 0) {
                            zoomIn();
                        }

                        oldDist = newDist;
                        setCurrentData();
                        postInvalidate();
                    } else if (TOUCH_MODE == DOWN) {
                        setTouchMode(event);
                    }
                }
                break;
        }
        return true;
    }

    private void setCurrentData() {
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }

        if (mShowDataNum > mOHLCData.size()) {
            mDataStartIndex = 0;
        } else if (mShowDataNum + mDataStartIndex > mOHLCData.size()) {
            mDataStartIndex = mOHLCData.size() - mShowDataNum;
        }

        dataMore = mShowDataNum >= MIN_CANDLE_NUM;

        mMinPrice = (float) mOHLCData.get(mDataStartIndex).get("low_price");
        mMaxPrice = (float) mOHLCData.get(mDataStartIndex).get("high_price");


        for (int i = mDataStartIndex + 1; i < mOHLCData.size()
                && i < mShowDataNum + mDataStartIndex; i++) {
            mMinPrice = mMinPrice < (float) mOHLCData.get(i).get("low_price") ? mMinPrice : (float) mOHLCData.get(i).get("low_price");
            mMaxPrice = mMaxPrice > (float) mOHLCData.get(i).get("high_price") ? mMaxPrice : (float) mOHLCData.get(i).get("high_price");
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

    private void zoomIn() {
        mShowDataNum += 3;
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
        }
        mShowDataNum = mShowDataNum > MAX_CANDLE_NUM ? MAX_CANDLE_NUM : mShowDataNum;
    }

    private void zoomOut() {
        mShowDataNum -= 3;
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }
    }

    private void setTouchMode(MotionEvent event) {
        float daltX = Math.abs(event.getRawX() - mStartX);
        float daltY = Math.abs(event.getRawY() - mStartY);
        if (Math.sqrt(daltX * daltX + daltY * daltY) > MIN_MOVE_DISTANCE) {
            if (event.getPointerCount() > 1) {
                TOUCH_MODE = ZOOM;
            } else {
                TOUCH_MODE = MOVE;
            }

            mStartX = event.getRawX();
            mStartY = event.getRawY();
        }
    }

    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Float> initMA(List<Map<String, Object>> entityList, int days) {
        if (days < 0 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Float> MAValues = new ArrayList<Float>();

        for (Map<String, Object> ohlcData : entityList) {
            if (days == dayk1) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk1));
            } else if (days == dayk2) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk2));
            } else if (days == dayk3) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk3));
            } else {

            }
        }

        return MAValues;
    }

    public List<Map<String, Object>> getOHLCData() {
        return mOHLCData;
    }

    public void setOHLCData(List<Map<String, Object>> OHLCData, boolean is_need_animation) {
        if (OHLCData == null || OHLCData.size() <= 0) {
            return;
        }
        if (is_need_animation) {
            k_animation_tag = DEFAULT_CANDLE_NUM;
        }
        this.mOHLCData = OHLCData;
        follow = getBoolean("follow", true);
        hollow = getBoolean("hollow", false);
        dayk1 = getInt("tv_ln1", 5);
        dayk2 = getInt("tv_ln2", 10);
        dayk3 = getInt("tv_ln3", 20);
        for (Map<String, Object> data : mOHLCData) {
            mVolume.add((Float) data.get("volume"));
            mChangeRate.add((Float) data.get("price_change_rate"));
        }

        if (OHLCData.size() < MIN_CANDLE_NUM) {
            k_animation_tag = OHLCData.size();
        }

        initMALineData();
//        mMACDData = new MACDBean(mOHLCData);
//        mKDJData = new KDJBean(mOHLCData);
//        mRSIData = new RSIBean(mOHLCData);

        setCurrentData();
        postInvalidate();
    }

    private boolean getBoolean(String key,boolean defvalue){
        SharedPreferences preferences = getContext().getSharedPreferences("gogoal_preferences", Context.MODE_PRIVATE);
        return preferences.getBoolean(key,defvalue);
    }
    
    private int getInt(String key,int defvalue){
        SharedPreferences preferences = getContext().getSharedPreferences("gogoal_preferences", Context.MODE_PRIVATE);
        return preferences.getInt(key,defvalue);
    }
    
    private void initMALineData() {
        MALineBean MA_First = new MALineBean();
        MA_First.setTitle("MA" + dayk1);
        MA_First.setLineColor(Color.rgb(0xff, 0xbf, 0x00));
        MA_First.setLineData(initMA(mOHLCData, dayk1));

        MALineBean MA_Second = new MALineBean();
        MA_Second.setTitle("MA" + dayk2);
        MA_Second.setLineColor(Color.rgb(0x05, 0x6c, 0xfc));
        MA_Second.setLineData(initMA(mOHLCData, dayk2));

        MALineBean MA_Third = new MALineBean();
        MA_Third.setTitle("MA" + dayk3);
        MA_Third.setLineColor(Color.rgb(0xff, 0x59, 0xc9));
        MA_Third.setLineData(initMA(mOHLCData, dayk3));

        MALineData = new ArrayList<MALineBean>();
        MALineData.add(MA_First);
        MALineData.add(MA_Second);
        MALineData.add(MA_Third);
    }

    public void onTabClick(int index) {
        String[] titles = getLowerChartTabTitles();
        mTabTitle = titles[index];
        postInvalidate();
    }

    private float spacing(MotionEvent event) {
//        float sqrt = 0;
        float x = 0;
        try {
            x = Math.abs(event.getX(0) - event.getX(1));
//            float y = event.getY(0) - event.getY(1);
//            sqrt = (float) Math.sqrt(x * x + y * y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    class KChartGestureDetectorListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        //Touch了还没有滑动时触发
        @Override
        public void onShowPress(MotionEvent motionEvent) {
            if (TOUCH_MODE != ZOOM) {
                TOUCH_MODE = LONG_TOUCH;
            }
        }

        //Touch down后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        //Touch了不移动一直Touch down时触发
        @Override
        public void onLongPress(MotionEvent motionEvent) {
//                if (TOUCH_MODE != ZOOM) {
//                TOUCH_MODE = LONG_TOUCH;
//            }
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if (motionEvent1.getAction() == MotionEvent.ACTION_UP) {
                TOUCH_MODE = NONE;
                Message msg = Message.obtain();
                msg.obj = v;
                msg.arg1 = chartsType;
                msg.what = ConstantUtils.XCHART_FLING;
                MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessage(msg);
            }
            return false;
        }
    }

    public void displayChartTouchUp(float v) {
        flingCount++;
        if (v < -1000) {
            mDataStartIndex -= 10;
            if (mDataStartIndex < 0) {
                mDataStartIndex = 0;
            }
        } else if (v > 1000) {
            mDataStartIndex += 10;
        }

        setCurrentData();
        postInvalidate();

        if (flingCount < 10) {
            Message msg = Message.obtain();
            msg.obj = v;
            msg.arg1 = chartsType;
            msg.what = ConstantUtils.XCHART_FLING;
            MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessageDelayed(msg, 50);
        } else {
            flingCount = 0;
        }
    }

    public void setChartsType(int chartsType) {
        this.chartsType = chartsType;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
