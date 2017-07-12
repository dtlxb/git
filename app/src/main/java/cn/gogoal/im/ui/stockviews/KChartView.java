package cn.gogoal.im.ui.stockviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import hply.com.niugu.bean.MALineBean;

/**
 * Created by huangxx on 2017/6/15.
 */

public class KChartView extends View {
    public static final String TAG = "KChartsView";

    ///////////////边框绘制////////////////
    private KLineGridChart mGridChart;
    private Bitmap bitmap = null;
    private float mUperChartHeight;                          //上表高度
    private float mLowerChartHeight;                         //下表高度
    private int leftMargin;                                  //左边坐标宽度
    private int rightMargin;                                 //右边坐标宽度
    private int mAxisTitleSize;                              //字体尺寸
    private int mSize;                                       //图形尺寸
    //view的宽高
    private int viewHeight;
    private int viewWidth;

    ///////////////K线属性////////////////
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

    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.YELLOW;     //默认Y轴字体颜色
    private final static int MIN_CANDLE_NUM = 60;                   //显示的最小Candle数
    private final static int MAX_CANDLE_NUM = 150;                  //最大显示的Candle数
    private final static int DEFAULT_CANDLE_NUM = 100;              //默认显示的Candle数
    private final static int MIN_MOVE_DISTANCE = 15;                //最小可识别的移动距离
    private double mCandleWidth;//Candle宽度

    //触摸点
    private float mStartX;
    private float mStartY;
    private float mPointY;
    private float mPointX;

    private int startWidth;                                     //右边起点位置
    private int useWidth;                                       //图形占有宽度
    private int mDataStartIndex;                                //显示的OHLC数据起始位置
    private int mShowDataNum;                                   //显示的OHLC数据个数
    private boolean showDetails;                                //是否显示蜡烛详情
    private List<MALineBean> MALineData;                        //MA数据
    private String mTabTitle;
    private List<Float> mVolume;                                //下部表的数据
    private int selectIndex;                                    //长按时选中的股票下标
    private boolean isRefresh;                                  //是否在刷新
    private float oldDist, newDist;                             //拉伸时用来计算滑动距离的变量
    private int chartsType;                                     //K线图类型
    private Paint candlePaint, titlePaint, detailPaint, lowerPaint, leftTitlePaint, linePaint;//画笔

    //数据小于最小Candle数
    private boolean dataMore;
    private double rate;
    private float volumeRate;


    //监听飞滑 长按动作
    private GestureDetector mGestureDetector;
    private int flingCount = 0;

    //蜡烛空实心设置
    private Boolean hollow;
    private Paint hollowPaint;
    private List<Float> mChangeRate;
    private boolean follow;

    //均线设置
    private int dayK1;
    private int dayK2;
    private int dayK3;

    //线与字体的颜色
    private int red = Color.rgb(0xf2, 0x49, 0x57);
    private int green = Color.rgb(0x1d, 0xbf, 0x60);
    private int gray = Color.rgb(0x54, 0x69, 0x80);

    //OHLC数据
    private List<Map<String, Object>> mOHLCData;

    //当前数据的最大最小值
    private double mMaxPrice;
    private double mMinPrice;
    private float maxVolume = 0;
    private int k_animation_tag;

    public KChartView(Context context) {
        super(context);
        init(context);
    }

    public KChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mGridChart = null;

        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndex = 0;
        showDetails = false;
        mMaxPrice = -1;
        mMinPrice = -1;
        mTabTitle = "Volume";
        mChangeRate = new ArrayList<>();
        k_animation_tag = DEFAULT_CANDLE_NUM - 1;

        mOHLCData = new ArrayList<>();
        mVolume = new ArrayList<>();

        mGestureDetector = new GestureDetector(context, new KChartGestureDetectorListener());

        candlePaint = new Paint();
        titlePaint = new Paint();
        detailPaint = new Paint();
        lowerPaint = new Paint();
        leftTitlePaint = new Paint();
        linePaint = new Paint();

        isRefresh = true;
        dataMore = true;
        hollowPaint = new Paint();
        hollowPaint.setStyle(Paint.Style.STROKE);
        if (StringUtils.getHighsAndLows()) {
            hollowPaint.setColor(red);
        } else {
            hollowPaint.setColor(green);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        useWidth = viewWidth - leftMargin - rightMargin - mSize;
        startWidth = viewWidth - rightMargin - mSize / 2;

        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }

        k_animation_tag -= 2;
        if (k_animation_tag >= -2) {
            invalidate();
        }

        //绘制表格
        if (mGridChart != null && bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

        //绘制坐标
        drawXYPosition(canvas);

        //蜡烛图
        drawUpperRegion(canvas);

        //下表柱体
        drawLowerRegion(canvas);

        //手势交互
        drawCandleDetails(canvas);

    }

    /*
    * 手势交互
    * */
    private void drawCandleDetails(Canvas canvas) {
        String sPrice = "";
        float dataY = 0;
        float close_price = 0;
        float open_price = 0;
        float volume;
        float detail_startWidth;
        if (showDetails) {

            // 绘制点击线条及详情区域
            if (dataMore) {
                selectIndex = (int) ((startWidth - mStartX) / mCandleWidth + mDataStartIndex);
            } else {
                selectIndex = ((int) ((startWidth - mStartX) / mCandleWidth) - MIN_CANDLE_NUM + mShowDataNum);
                selectIndex = selectIndex < 0 ? 0 : selectIndex;
                selectIndex = selectIndex > mShowDataNum - 1 ? mShowDataNum - 1 : selectIndex;
            }

            if (follow) {
                if (selectIndex < mOHLCData.size() && selectIndex > -1) {
                    dataY = (float) ((mMaxPrice - (float) mOHLCData.get(selectIndex).get("close_price")) * rate + 2);
                    close_price = (float) mOHLCData.get(selectIndex).get("close_price");
                    open_price = (float) mOHLCData.get(selectIndex).get("open_price");
                    sPrice = String.format("%.2f", close_price);
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
                    sPrice = String.format("%.2f", close_price);
                } else if (dataY <= viewHeight && dataY >= viewHeight - mLowerChartHeight + 1) {
                    volume = maxVolume - ((dataY - (viewHeight - mLowerChartHeight + 2)) / volumeRate);
                    close_price = volume / 1000000;
                    if (volume > maxVolume) {
                        close_price = maxVolume / 1000000;
                    }
                    if (close_price < 0) {
                        close_price = 0;
                    }
                    sPrice = String.format("%.2f", close_price);
                }
            }

            leftTitlePaint.setTextSize(mAxisTitleSize);
            if (follow) {
                if (open_price <= close_price) {
                    if (StringUtils.getHighsAndLows()) {
                        leftTitlePaint.setColor(red);
                    } else {
                        leftTitlePaint.setColor(green);
                    }
                } else {
                    if (StringUtils.getHighsAndLows()) {
                        leftTitlePaint.setColor(green);
                    } else {
                        leftTitlePaint.setColor(red);
                    }
                }
            } else {
                leftTitlePaint.setColor(gray);
            }

            leftTitlePaint.setAlpha(200);
            leftTitlePaint.setAntiAlias(true);
            Paint.FontMetrics fontMetrics = leftTitlePaint.getFontMetrics();
            float fontHeight = fontMetrics.descent - fontMetrics.ascent;

            detailPaint.setColor(Color.BLACK);
            detailPaint.setAlpha(150);
            detailPaint.setStrokeWidth(mSize);

            //找到离触碰位置最近的点
            if (dataMore) {
                detail_startWidth = startWidth;
            } else {
                detail_startWidth = (float) (startWidth - (MIN_CANDLE_NUM - mShowDataNum) * mCandleWidth);
            }
            if (selectIndex > mDataStartIndex) {
                if (mStartX >= detail_startWidth - ((selectIndex - mDataStartIndex + 1) * mCandleWidth + mCandleWidth / 2) &&
                        mStartX <= detail_startWidth - (selectIndex - mDataStartIndex - 1) * mCandleWidth + mCandleWidth / 2) {
                    mPointX = (float) (detail_startWidth - ((selectIndex - mDataStartIndex) * mCandleWidth + mCandleWidth / 2));
                }
            } else if (selectIndex == mDataStartIndex) {
                mPointX = (float) (detail_startWidth - mCandleWidth / 2);
            }

            //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISPLAY_XCHART_K_DATA, mOHLCData, selectIndex);
            HashMap<String, Object> map = new HashMap<>();
            map.put("mohlcd_data", mOHLCData);
            map.put("select_index", selectIndex);
            BaseMessage baseMessage = new BaseMessage("Stock_KLine", map);
            AppManager.getInstance().sendMessage("Stock_KLineData", baseMessage);

            if (dataMore) {
                if ((mPointX > leftMargin + mSize / 2 && mPointX < startWidth - mSize / 2)) {
                    if (follow) {
                        canvas.drawLine(leftMargin + mSize / 2, dataY, startWidth, dataY, detailPaint);
                        drawSideDetail(canvas, dataY, fontHeight, sPrice);
                    } else {
                        if ((mPointY >= mSize / 2 && mPointY <= mUperChartHeight)
                                || (mPointY <= viewHeight - mSize / 2 && mPointY >= viewHeight - mLowerChartHeight + mSize / 2)) {
                            canvas.drawLine(leftMargin + mSize / 2, dataY, startWidth, dataY, detailPaint);
                            drawSideDetail(canvas, dataY, fontHeight, sPrice);
                        }
                    }
                    canvas.drawLine(mPointX, mSize / 2, mPointX, mUperChartHeight - mSize / 2,
                            detailPaint);
                    canvas.drawLine(mPointX, viewHeight - mLowerChartHeight + mSize / 2, mPointX, viewHeight, detailPaint);
                } else {
                    //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                    sendDissChartMsg();
                    TOUCH_MODE = NONE;
                    postInvalidate();
                }
            } else {
                if (mPointX > leftMargin + mSize / 2 && mPointX < (mShowDataNum * mCandleWidth) + leftMargin + mSize / 2) {
                    if (follow) {
                        canvas.drawLine(leftMargin + mSize / 2, dataY, startWidth, dataY, detailPaint);
                        drawSideDetail(canvas, dataY, fontHeight, sPrice);
                    } else {
                        if ((mPointY >= mSize / 2 && mPointY <= mUperChartHeight)
                                || (mPointY <= viewHeight - mSize / 2 && mPointY >= viewHeight - mLowerChartHeight + mSize / 2)) {
                            canvas.drawLine(leftMargin + mSize / 2, dataY, startWidth, dataY, detailPaint);
                            drawSideDetail(canvas, dataY, fontHeight, sPrice);
                        }
                    }
                    canvas.drawLine(mPointX, mSize / 2, mPointX, mUperChartHeight - mSize / 2,
                            detailPaint);
                    canvas.drawLine(mPointX, viewHeight - mLowerChartHeight + mSize / 2, mPointX, viewHeight, detailPaint);
                } else {
                    //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                    sendDissChartMsg();
                    TOUCH_MODE = NONE;
                    postInvalidate();
                }
            }
        }
    }

    /*
    * 绘制坐标
    * */
    private void drawXYPosition(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() == 0)
            return;

        titlePaint.setTextSize(mAxisTitleSize);
        titlePaint.setColor(gray);
        titlePaint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        // 左边的坐标
        String pos_max_price = String.format("%.2f", mMaxPrice);
        String close_price = String.format("%.2f", (mMaxPrice + mMinPrice) / 2);
        String pos_min_price = String.format("%.2f", mMinPrice);

        // 绘制左边Y轴的坐标
        canvas.drawText(pos_max_price, leftMargin - titlePaint.measureText(pos_max_price) - mSize, fontHeight * 2 / 3, titlePaint);
        canvas.drawText(close_price, leftMargin - titlePaint.measureText(close_price) - mSize, mUperChartHeight / 2 + fontHeight * 1 / 3, titlePaint);
        canvas.drawText(pos_min_price, leftMargin - titlePaint.measureText(pos_min_price) - mSize, mUperChartHeight, titlePaint);

        //当前数据最大交易量
        for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mOHLCData.size(); i++) {
            if ((float) mOHLCData.get(i).get("volume") > maxVolume) {
                maxVolume = (float) mOHLCData.get(i).get("volume");
            }
        }

        String volume = String.format("%.2f", maxVolume / 1000000);
        String unit = "万手";

        canvas.drawText(volume, leftMargin - titlePaint.measureText(volume) - mSize, viewHeight - mLowerChartHeight + fontHeight * 2 / 3, titlePaint);
        canvas.drawText(unit, leftMargin - titlePaint.measureText(unit) - mSize, viewHeight - mSize, titlePaint);

        //画X坐标
        String date = "";

        //最右边
        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndex).get("date")).substring(0, 10);
            canvas.drawText(
                    date,
                    viewWidth - titlePaint.measureText(date) - rightMargin,
                    (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                    titlePaint);
        }

        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum / 2).get("date")).substring(0, 10);
            canvas.drawText(
                    date,
                    (useWidth - titlePaint.measureText(date)) / 2 + leftMargin,
                    (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                    titlePaint);
        }

        //最左边
        date = ((String) mOHLCData.get(mDataStartIndex + mShowDataNum - 1).get("date")).substring(0, 10);
        canvas.drawText(
                date,
                leftMargin,
                (viewHeight - mUperChartHeight - mLowerChartHeight) / 2 + mUperChartHeight + fontHeight / 3,
                titlePaint);
    }

    /*
    * 蜡烛图
    * */
    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        candlePaint.setStrokeWidth(mSize / 2);
        hollowPaint.setStrokeWidth(mSize / 2);
        linePaint.setStrokeWidth(mSize);
        rate = (mUperChartHeight - mSize) / (mMaxPrice - mMinPrice);

        if (mOHLCData.size() >= MIN_CANDLE_NUM) {
            mCandleWidth = useWidth * 1.00f / mShowDataNum;
        } else {
            mCandleWidth = useWidth * 1.00f / MIN_CANDLE_NUM;
        }

        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mOHLCData.size(); i++) {
            if (k_animation_tag <= i) {
                float open = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("open_price")) * rate + mSize / 2);
                float close = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) * rate + mSize / 2);
                float high = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("high_price")) * rate + mSize / 2);
                float low = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndex + i).get("low_price")) * rate + mSize / 2);

                float left;
                float right;
                float startX;

                if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                    left = (float) (useWidth + mSize - mCandleWidth * (i + 1) + leftMargin);
                    right = (float) (useWidth - mCandleWidth * i + leftMargin);
                    startX = (float) (useWidth - mCandleWidth * i - mCandleWidth / 2 + leftMargin + mSize / 2);
                } else {
                    left = (float) (useWidth + mSize - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin);
                    right = (float) (useWidth - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin);
                    startX = (float) (useWidth - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) - mCandleWidth / 2 + leftMargin + mSize / 2);
                }

                if ((float) mOHLCData.get(mDataStartIndex + i).get("open_price") <= (float) mOHLCData.get(mDataStartIndex + i).get("close_price")) {
                    if (StringUtils.getHighsAndLows()) {
                        candlePaint.setColor(red);
                        linePaint.setColor(red);
                    } else {
                        candlePaint.setColor(green);
                        linePaint.setColor(green);
                    }
                } else {
                    if (StringUtils.getHighsAndLows()) {
                        candlePaint.setColor(green);
                        linePaint.setColor(green);
                    } else {
                        candlePaint.setColor(red);
                        linePaint.setColor(red);
                    }
                }

                if (open < close) {
                    if (Math.abs(open - close) <= 1f) {
                        canvas.drawLine(left, open, right, open, linePaint);
                        canvas.drawLine(startX, high, startX, low, linePaint);
                    } else {
                        canvas.drawRect(left, open, right, close, candlePaint);
                        canvas.drawLine(startX, high, startX, low, linePaint);
                    }
                } else if (open == close) {
                    canvas.drawLine(left, open, right, open, linePaint);
                    canvas.drawLine(startX, high, startX, low, linePaint);
                } else {
                    if (Math.abs(open - close) <= 1f) {
                        canvas.drawLine(left, close, right, close, linePaint);
                        canvas.drawLine(startX, high, startX, low, linePaint);
                    } else {
                        if (hollow) {
                            canvas.drawRect(left, close, right, open, hollowPaint);
                        } else {
                            canvas.drawRect(left, close, right, open, candlePaint);
                        }
                        canvas.drawLine(startX, high, startX, close, linePaint);
                        canvas.drawLine(startX, open, startX, low, linePaint);
                    }
                }

            }
        }

        // 绘制上部曲线图及上部分MA值
        float font_moveWidth = 0;
        float font_staticWidth = 0;
        for (int j = 0; j < MALineData.size(); j++) {
            MALineBean lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            String fontMove = "";
            String fontStatic = "";
            Paint MAPaint = new Paint();
            MAPaint.setColor(lineEntity.getLineColor());
            MAPaint.setTextSize(mAxisTitleSize);
            MAPaint.setAlpha(166);
            MAPaint.setStrokeWidth(mSize);
            MAPaint.setAntiAlias(true);

            //绘值
            if (selectIndex < mOHLCData.size() && selectIndex >= 0) {
                if (TOUCH_MODE == LONG_TOUCH) {
                    fontMove = lineEntity.getTitle() + ":" + new DecimalFormat("#.##").format(lineEntity.getLineData().get(selectIndex));
                    canvas.drawText(
                            fontMove,
                            (mStartX < useWidth / 2 + leftMargin || mStartX == viewWidth / 2 + leftMargin) ?
                                    (useWidth / 2 + leftMargin + (leftMargin / 2) * (j + 1) + font_moveWidth)
                                    : (leftMargin + (leftMargin / 2) * (j + 1) + font_moveWidth),
                            mAxisTitleSize,
                            MAPaint);
                } else {
                    fontStatic = lineEntity.getTitle() + ":" + new DecimalFormat("#.##").format(lineEntity.getLineData().get(mDataStartIndex));
                    canvas.drawText(
                            fontStatic,
                            leftMargin + (leftMargin / 2) * (j + 1) + font_staticWidth,
                            mAxisTitleSize,
                            MAPaint);
                }
                font_moveWidth += MAPaint.measureText(fontMove);
                font_staticWidth += MAPaint.measureText(fontStatic);
            }

            //绘线
            MAPaint.setAlpha(255);
            for (int i = mShowDataNum - 1; i >= 0
                    && mDataStartIndex + i < lineEntity.getLineData().size(); i--) {
                if (k_animation_tag <= i) {
                    if (lineEntity.getLineData().get(mDataStartIndex + i) == 0
                            && mDataStartIndex + i >= lineEntity.getLineData().size() - 30)
                        continue;

                    if (i != mShowDataNum - 1 && startX != 0 && startY != 0) {
                        if (dataMore) {
                            canvas.drawLine(
                                    startX + leftMargin + mSize / 2,
                                    startY + 1,
                                    (float) (useWidth - mCandleWidth * i - mCandleWidth / 2) + leftMargin,
                                    (float) ((mMaxPrice - lineEntity.getLineData()
                                            .get(mDataStartIndex + i)) * rate + 1),
                                    MAPaint);
                        } else {
                            canvas.drawLine(
                                    startX + leftMargin + mSize / 2,
                                    startY + 1,
                                    (float) (useWidth - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth / 2) + leftMargin,
                                    (float) ((mMaxPrice - lineEntity.getLineData()
                                            .get(mDataStartIndex + i)) * rate + 1),
                                    MAPaint);
                        }
                    }

                    if (mShowDataNum < MIN_CANDLE_NUM) {
                        startX = (float) (useWidth - mSize / 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth / 2);
                    } else {
                        startX = (float) (useWidth - mSize / 2 - mCandleWidth * i - mCandleWidth / 2);
                    }
                    if (lineEntity.getLineData().get(mDataStartIndex + i) > 0) {
                        startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndex + i)) * rate);
                    }
                }
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

        lowerPaint.setStrokeWidth(mSize / 2);

        if (mTabTitle.trim().equalsIgnoreCase("Volume")) {
            float low = mVolume.get(mDataStartIndex);
            float high = low;
            for (int i = mDataStartIndex; i < mDataStartIndex + mShowDataNum && i < mVolume.size(); i++) {
                low = low < mVolume.get(i) ? low : mVolume.get(i);
                high = high > mVolume.get(i) ? high : mVolume.get(i);
            }
            volumeRate = lowerHeight / (high - low);

            float zero = viewHeight - mSize / 2;

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

                    float top = ((high - mVolume.get(i)) * volumeRate) + lowerTop + mSize / 2;
                    if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                        if (hollow) {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + mSize - (float) mCandleWidth * (i + 1 - mDataStartIndex) + leftMargin,
                                        zero,
                                        useWidth - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            } else {
                                canvas.drawRect((useWidth + mSize - (float) mCandleWidth * (i + 1 - mDataStartIndex)) + leftMargin,
                                        top,
                                        useWidth - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            }
                        } else {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + mSize - (float) mCandleWidth * (i + 1 - mDataStartIndex) + leftMargin,
                                        zero,
                                        useWidth - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero,
                                        lowerPaint);
                            } else {
                                canvas.drawRect((useWidth + mSize - (float) mCandleWidth * (i + 1 - mDataStartIndex)) + leftMargin,
                                        top,
                                        useWidth - (float) mCandleWidth * (i - mDataStartIndex) + leftMargin,
                                        zero,
                                        lowerPaint);
                            }
                        }
                    } else {
                        if (hollow) {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + mSize - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        zero,
                                        useWidth - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            } else {
                                canvas.drawRect(useWidth + mSize - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        top,
                                        useWidth - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero,
                                        mChangeRate.get(i) > 0 ? hollowPaint : lowerPaint);
                            }
                        } else {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(useWidth + mSize - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        zero,
                                        useWidth - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero,
                                        lowerPaint);
                            } else {
                                canvas.drawRect(useWidth + mSize - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1) + leftMargin,
                                        top,
                                        useWidth - (float) mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) + leftMargin,
                                        zero,
                                        lowerPaint);
                            }
                        }
                    }
                }
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
            if (days == dayK1) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK1));
            } else if (days == dayK2) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK2));
            } else if (days == dayK3) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayK3));
            } else {

            }
        }

        return MAValues;
    }

    /**
     * 返回框架图
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }


    /**
     * 设置数据源
     */
    public void setOHLCData(List<Map<String, Object>> OHLCData, boolean is_need_animation) {

        if (OHLCData == null || OHLCData.size() <= 0) {
            return;
        }
        if (is_need_animation) {
            k_animation_tag = DEFAULT_CANDLE_NUM;
        }
        this.mOHLCData = OHLCData;
        follow = SPTools.getBoolean("follow", true);
        hollow = SPTools.getBoolean("hollow", false);
        dayK1 = SPTools.getInt("tv_ln1", 5);
        dayK2 = SPTools.getInt("tv_ln2", 10);
        dayK3 = SPTools.getInt("tv_ln3", 20);

        for (Map<String, Object> data : mOHLCData) {
            mVolume.add((Float) data.get("volume"));
            mChangeRate.add((Float) data.get("price_change_rate"));
        }

        if (OHLCData.size() < MIN_CANDLE_NUM) {
            k_animation_tag = OHLCData.size();
        }

        initMALineData();

        setCurrentData();

        invalidate();
    }

    /*
    * K线设置
    * */
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

        MALineData = new ArrayList<>();
        MALineData.add(MA_First);
        MALineData.add(MA_Second);
        MALineData.add(MA_Third);
    }

    /**
     * 框架图数据
     */
    public void setBitmapData(KLineGridChart KLineGridChart) {
        this.mGridChart = KLineGridChart;
        mUperChartHeight = mGridChart.getUperChartHeight();
        mLowerChartHeight = mGridChart.getLowerChartHeight();
        leftMargin = mGridChart.getLeftMargin();
        rightMargin = mGridChart.getRightMargin();
        mAxisTitleSize = mGridChart.getmAxisTitleSize();
        mSize = mGridChart.getmSize();
        viewHeight = mGridChart.getViewHeight();
        viewWidth = mGridChart.getViewWidth();
    }

    /*
    * 间距
    * */
    private float spacing(MotionEvent event) {
        float x = 0;
        try {
            x = Math.abs(event.getX(0) - event.getX(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    /*
    * 滑动事件
    * */
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
            if (TOUCH_MODE != ZOOM) {
                TOUCH_MODE = LONG_TOUCH;
            }
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if (motionEvent1.getAction() == MotionEvent.ACTION_UP) {
                TOUCH_MODE = NONE;
                /*Message msg = Message.obtain();
                msg.obj = v;
                msg.arg1 = chartsType;
                msg.what = ConstantUtils.XCHART_FLING;
                MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessage(msg);*/
                HashMap<String, Object> map = new HashMap<>();
                map.put("charts_type", chartsType);
                map.put("fling_speed", v);
                BaseMessage baseMessage = new BaseMessage("Fling_Info", map);
                AppManager.getInstance().sendMessage("Fling_Speed", baseMessage);
            }
            return false;
        }
    }

    /*
    * 触控事件
    * */
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
                //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_XCHART_DATA, 0);
                sendDissChartMsg();
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

                    if (!(mStartX < leftMargin + 1 || mStartX > startWidth - 1)) {
                        postInvalidate();
                    }
                } else {
                    showDetails = false;
                    if (TOUCH_MODE == MOVE & event.getPointerCount() == 1) {
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
                            /*Message msg = Message.obtain();
                            msg.arg1 = chartsType;
                            msg.what = ConstantUtils.REFRESH_KLINE_DATA;
                            MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessage(msg);*/

                            BaseMessage baseMessage = new BaseMessage();
                            baseMessage.setType(chartsType);
                            AppManager.getInstance().sendMessage("Refresh_Info", baseMessage);
                        }

                        setCurrentData();
                        postInvalidate();
                    } else if (TOUCH_MODE == ZOOM && event.getPointerCount() > 1) {
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

    /*
    * 放大
    * */
    private void zoomIn() {
        mShowDataNum += 3;
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
        }
        mShowDataNum = mShowDataNum > MAX_CANDLE_NUM ? MAX_CANDLE_NUM : mShowDataNum;
    }

    /*
    * 缩小
    * */
    private void zoomOut() {
        mShowDataNum -= 3;
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }
    }

    /*
    * 缩放事件
    * */
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

    /*
    * 绘制最左边详情
    * */
    private void drawSideDetail(Canvas canvas, float dataY, float fontHeight, String sPrice) {
        if (dataY >= fontHeight / 2 && dataY < viewHeight - fontHeight / 2) {
            canvas.drawRoundRect(
                    new RectF(
                            0,
                            dataY - (fontHeight / 2),
                            leftMargin,
                            dataY + (fontHeight / 2)),
                    fontHeight / 3,
                    fontHeight / 3,
                    leftTitlePaint);

            leftTitlePaint.setColor(Color.WHITE);
            leftTitlePaint.setAlpha(255);

            canvas.drawText(
                    sPrice,
                    (leftMargin - leftTitlePaint.measureText(sPrice)) / 2,
                    dataY + fontHeight / 3,
                    leftTitlePaint);
        } else if (dataY >= viewHeight - fontHeight / 2) {
            canvas.drawRoundRect(
                    new RectF(
                            0,
                            viewHeight - fontHeight,
                            leftMargin,
                            viewHeight),
                    fontHeight / 3,
                    fontHeight / 3,
                    leftTitlePaint);

            leftTitlePaint.setColor(Color.WHITE);
            leftTitlePaint.setAlpha(255);

            canvas.drawText(
                    sPrice,
                    (leftMargin - leftTitlePaint.measureText(sPrice)) / 2,
                    viewHeight - fontHeight / 6,
                    leftTitlePaint);
        } else {
            canvas.drawRoundRect(
                    new RectF(
                            0,
                            0,
                            leftMargin,
                            fontHeight),
                    fontHeight / 3,
                    fontHeight / 3,
                    leftTitlePaint);

            leftTitlePaint.setColor(Color.WHITE);
            leftTitlePaint.setAlpha(255);

            canvas.drawText(
                    sPrice,
                    (leftMargin - leftTitlePaint.measureText(sPrice)) / 2,
                    fontHeight * 6 / 7,
                    leftTitlePaint);
        }
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

        dataMore = mShowDataNum >= MIN_CANDLE_NUM;

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
            /*Message msg = Message.obtain();
            msg.obj = v;
            msg.arg1 = chartsType;
            msg.what = ConstantUtils.XCHART_FLING;

            MessageHandlerList.getHandler(StockDetailChartsActivity.class.getName()).sendMessageDelayed(msg, 50);*/
            HashMap<String, Object> map = new HashMap<>();
            map.put("charts_type", chartsType);
            map.put("fling_speed", v);
            BaseMessage baseMessage = new BaseMessage("Fling_Info", map);
            AppManager.getInstance().sendMessage("Fling_Speed", baseMessage);
        } else {
            flingCount = 0;
        }
    }

    private void sendDissChartMsg() {
        AppManager.getInstance().sendMessage("Dismiss_Chart");
    }

    public void setChartsType(int chartsType) {
        this.chartsType = chartsType;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}

