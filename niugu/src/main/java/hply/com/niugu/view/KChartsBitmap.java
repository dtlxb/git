package hply.com.niugu.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hply.com.niugu.StringUtils;
import hply.com.niugu.bean.KDJBean;
import hply.com.niugu.bean.MACDBean;
import hply.com.niugu.bean.MALineBean;
import hply.com.niugu.bean.RSIBean;
import hply.com.niugu.stock.Grid;

/**
 * Created by Lizn on 2015/10/15.
 */
public class KChartsBitmap extends Grid {
    private static final String TAG = "KChartsBitmap";
    /**
     * 触摸模式
     */
    private static int TOUCH_MODE;
    private final static int NONE = 0;
    private final static int DOWN = 1;
    private final static int MOVE = 2;
    private final static int ZOOM = 3;
    private final static int LONG_TOUCH = 4;

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
    private final static int MIN_CANDLE_NUM = 20;

    /**
     * 默认显示的Candle数
     */
    private final static int MAX_CANDLE_NUM = 100;

    /**
     * 默认显示的Candle数
     */
    private final static int DEFAULT_CANDLE_NUM = 50;

    /**
     * 最小可识别的移动距离
     */
    private final static int MIN_MOVE_DISTANCE = 15;
    private final SharedPreferences preferences;

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
//    private List<OHLCData> mOHLCData;

    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndext;

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

    /**
     * MA数据
     */
    private List<MALineBean> MALineData;

    private String mTabTitle;

    //实心空心设置
    private Boolean hollow;
    private Paint hollowpaint;
    private List<Float> mChangeRate;
    private Paint lowerPaint;

    // 下部表的数据
    List<Float> mVolume;
    MACDBean mMACDData;
    KDJBean mKDJData;
    RSIBean mRSIData;
    //均线设置
    private int dayk1;
    private int dayk2;
    private int dayk3;

    private Context context;
    //数据解析
    private List<Map<String, Object>> mOHLCData;

    public KChartsBitmap(Context context,Integer width, Integer height) {
        super(width, height);
        this.context=context;
        preferences = context.getSharedPreferences("gogoal_preferences", Context.MODE_PRIVATE);
        init();
    }

    private void init() {

        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndext = 0;
        showDetails = false;
        mMaxPrice = -1;
        mMinPrice = -1;
        mTabTitle = "Volume";
        mChangeRate = new ArrayList<>();

        mOHLCData = new ArrayList<Map<String, Object>>();
        mVolume = new ArrayList<>();
        mMACDData = new MACDBean(null);
        mKDJData = new KDJBean(null);
        mRSIData = new RSIBean(null);
        hollowpaint = new Paint();
        hollowpaint.setStyle(Paint.Style.STROKE);
        hollowpaint.setColor(red);
        lowerPaint = new Paint();
    }

    @Override
    protected void drawBitMap(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        super.drawBitMap(canvas);

        drawLines(canvas);

        drawXYPostion(canvas, height, width);
    }


    @Override
    public void drawXYPostion(Canvas canvas, int viewHeight, int viewWidth) {
        super.drawXYPostion(canvas, viewHeight, viewWidth);
        if (mOHLCData == null || mOHLCData.size() == 0)
            return;

        Map<String, Integer> leftPositionLengths = new HashMap<>();

        Paint tilte_paint = new Paint();
        tilte_paint.setTextSize(mAxisTitleSize);
        tilte_paint.setColor(gray);
        Rect r = new Rect();

        int fontHeight;

        // 左边的坐标
        String pos_max_price = StringUtils.save2Significand(mMaxPrice);
        tilte_paint.getTextBounds(pos_max_price, 0, pos_max_price.length(), r);
        leftPositionLengths.put("pos_max_price", r.width());

        String close_price = StringUtils.save2Significand((mMaxPrice + mMinPrice) / 2);
        tilte_paint.getTextBounds(close_price, 0, close_price.length(), r);
        leftPositionLengths.put("close_price", r.width());

        String pos_min_price = StringUtils.save2Significand(mMinPrice);
        tilte_paint.getTextBounds(pos_min_price, 0, pos_min_price.length(), r);
        leftPositionLengths.put("pos_min_price", r.width());

        //当前数据最大交易量
        float maxVolume = 0;
        for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < mOHLCData.size(); i++) {
            if ((float) mOHLCData.get(i).get("volume") > maxVolume) {
                maxVolume = (float) mOHLCData.get(i).get("volume");
            }
        }

//        String volume = String.format("%.2f", maxVolume / 10000 / 100);
//        tilte_paint.getTextBounds(volume, 0, volume.length(), r);
//        leftPositionLengths.put("volume", r.width());
//
//        String unit = "万手";
//        tilte_paint.getTextBounds(unit, 0, unit.length(), r);
//        leftPositionLengths.put("unit", r.width());

        // 字体高度
        fontHeight = r.height();

        // 绘制左边Y轴的坐标
        canvas.drawText(pos_max_price, 0, fontHeight + mCircleSize + 3, tilte_paint);
//        canvas.drawText(close_price, 0, (mUperChartHeight + fontHeight) / 2 + mCircleSize, tilte_paint);
        canvas.drawText(pos_min_price, 0, mUperChartHeight - 3, tilte_paint);
//        canvas.drawText(volume, 0, viewHeight - mLowerChartHeight + fontHeight, tilte_paint);
//        canvas.drawText(unit, 0, viewHeight - 3, tilte_paint);

        //画X坐标
        String date;
        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndext).get("date")).substring(0, 10);
            tilte_paint.getTextBounds(date, 0, date.length(), r);
            canvas.drawText(
                    date,
                    width - r.width() - 5,
                    mUperChartHeight + mAxisTitleSize + 4,
                    tilte_paint);
        }

        if (mShowDataNum >= MIN_CANDLE_NUM) {
            date = ((String) mOHLCData.get(mDataStartIndext + mShowDataNum / 2).get("date")).substring(0, 10);
            tilte_paint.getTextBounds(date, 0, date.length(), r);
            canvas.drawText(
                    date,
                    (width - leftMargin - r.width()) / 2 + leftMargin,
                    mUperChartHeight + mAxisTitleSize + 4,
                    tilte_paint);
        }

        date = ((String) mOHLCData.get(mDataStartIndext + mShowDataNum - 1).get("date")).substring(0, 10);
        tilte_paint.getTextBounds(date, 0, date.length(), r);
        canvas.drawText(
                date,
                leftMargin,
                mUperChartHeight + mAxisTitleSize + 4,
                tilte_paint);
    }

    @Override
    protected void drawLines(Canvas canvas) {
        //蜡烛图
        drawUpperRegion(canvas);
        //下表
        drawLowerRegion(canvas);
    }

    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        Paint paint = new Paint();
        int viewWidth = this.width - leftMargin;
        double rate = (getUperChartHeight() - 4 - mCircleSize) / (mMaxPrice - mMinPrice);
        if (mOHLCData.size() >= MIN_CANDLE_NUM) {
            mCandleWidth = viewWidth * 1.00f / mShowDataNum;
        } else {
            mCandleWidth = viewWidth * 1.00f / MIN_CANDLE_NUM;
        }

        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            float open = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndext + i).get("open_price")) * rate + mCircleSize + 2);
            float close = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndext + i).get("close_price")) * rate + mCircleSize + 2);
            float high = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndext + i).get("high_price")) * rate + mCircleSize + 2);
            float low = (float) ((mMaxPrice - (float) mOHLCData.get(mDataStartIndext + i).get("low_price")) * rate + mCircleSize + 2);

            float left;
            float right;
            float startX;

            if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                left = (float) (viewWidth + 2 - mCandleWidth * (i + 1));
                right = (float) (viewWidth - 2 - mCandleWidth * i);
                startX = (float) (viewWidth - mCandleWidth * i - mCandleWidth / 2);
            } else {
                left = (float) (viewWidth + 2 - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i + 1));
                right = (float) (viewWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i));
                startX = (float) (viewWidth - mCandleWidth * (MIN_CANDLE_NUM - mOHLCData.size() + i) - mCandleWidth / 2);
            }

            if ((float) mOHLCData.get(mDataStartIndext + i).get("open_price") <= (float) mOHLCData.get(mDataStartIndext + i).get("close_price")) {
                    paint.setColor(red);
            } else {
                    paint.setColor(green);
            }

//            if ((i + 1) < mOHLCData.size()) {
//                if (entity.getClose_price() >= mOHLCData.get(i + 1).getClose_price())
//                    paint.setColor(red);
//                else
//                    paint.setColor(green);
//            }

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
                        canvas.drawRect(left, close, right, open, hollowpaint);
                    } else {
                        canvas.drawRect(left, close, right, open, paint);
                    }
                    canvas.drawLine(startX, high, startX, low, paint);
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
            MAPaint.setTextSize(mAxisTitleSize);
            MAPaint.setStrokeWidth(3);

//            canvas.drawText(
//                    lineEntity.getTitle() + ":" + new DecimalFormat("#.##").format(lineEntity.getMaxValue(mDataStartIndext, mShowDataNum)),
//                    MATitleWidth * j + leftMargin,
//                    DEFAULT_AXIS_TITLE_SIZE,
//                    paint);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {

                if (lineEntity.getLineData().get(mDataStartIndext + i) == 0 && mDataStartIndext + i >= lineEntity.getLineData().size() - 30)
                    continue;

                if (i != 0) {
                    if (mShowDataNum < MIN_CANDLE_NUM) {
                        canvas.drawLine(
                                startX + leftMargin,
                                startY + 1,
                                (float) (viewWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f) + leftMargin,
                                (float) ((mMaxPrice - lineEntity.getLineData()
                                        .get(mDataStartIndext + i)) * rate + 1 + mCircleSize),
                                MAPaint);

                    } else {
                        canvas.drawLine(
                                startX + leftMargin,
                                startY + 1,
                                (float) (viewWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f) + leftMargin,
                                (float) ((mMaxPrice - lineEntity.getLineData()
                                        .get(mDataStartIndext + i)) * rate + 1 + mCircleSize),
                                MAPaint);
                    }
                }
                if (mShowDataNum < MIN_CANDLE_NUM) {
                    startX = (float) (viewWidth - 2 - mCandleWidth * (MIN_CANDLE_NUM - mShowDataNum + i) - mCandleWidth * 0.5f);
                } else {
                    startX = (float) (viewWidth - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                }
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate) + mCircleSize;
            }
        }
    }

    private void drawLowerRegion(Canvas canvas) {
        float lowertop = this.height - mLowerChartHeight + 2;
        float lowerHight = this.height - lowertop - 4;
        float viewWidth = this.width;

        Paint magentaPaint = new Paint();
        magentaPaint.setColor(Color.MAGENTA);

        Paint textPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(mAxisTitleSize);

        if (mTabTitle.trim().equalsIgnoreCase("Volume")) {
            double low = mVolume.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < mVolume.size(); i++) {
                low = low < mVolume.get(i) ? low : mVolume.get(i);
                high = high > mVolume.get(i) ? high : mVolume.get(i);
            }

            rate = lowerHight / (high - low);

            float zero = (float) (high * rate) + lowertop;
            if (zero < lowertop) {
                zero = lowertop;
            }

            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < mVolume.size(); i++) {
                // 绘制矩形
                if (mVolume.get(i) > 0) {
//                    if (i > 0) {
//                        if (mVolume.get(i) > mVolume.get(i - 1)) {
//                            paint.setColor(red);
//                        } else {
//                            paint.setColor(green);
//                        }
//                    } else if (i == 0) {
//                        paint.setColor(red);
//                    }

                    if (!hollow) {
                        if (mChangeRate.get(i) < 0) {
                                lowerPaint.setColor(green);
                        } else {
                                lowerPaint.setColor(red);
                        }
                    } else {
                            lowerPaint.setColor(green);
                    }

                    float top = (float) ((high - mVolume.get(i)) * rate) + lowertop;
                    if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                        if (hollow) {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndext),
                                        zero + 2,
                                        viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext),
                                        zero + 2,
                                        mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                            } else {
                                canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndext)),
                                        top + 2,
                                        viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext),
                                        zero + 2,
                                        mChangeRate.get(i) > 0 ? hollowpaint : lowerPaint);
                            }
                        } else {
                            if (zero - top <= 0.3f) {
                                canvas.drawLine(viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndext),
                                        zero + 2,
                                        viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext),
                                        zero + 2,
                                        lowerPaint);
                            } else {
                                canvas.drawRect((viewWidth + 2 - (float) mCandleWidth * (i + 1 - mDataStartIndext)),
                                        top + 2,
                                        viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext),
                                        zero + 2,
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
    }

    private void setCurrentData() {
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }

        if (mShowDataNum > mOHLCData.size()) {
            mDataStartIndext = 0;
        } else if (mShowDataNum + mDataStartIndext > mOHLCData.size()) {
            mDataStartIndext = mOHLCData.size() - mShowDataNum;
        }
        mMinPrice = (float) mOHLCData.get(mDataStartIndext).get("low_price");
        mMaxPrice = (float) mOHLCData.get(mDataStartIndext).get("high_price");
        for (int i = mDataStartIndext + 1; i < mOHLCData.size()
                && i < mShowDataNum + mDataStartIndext; i++) {
            mMinPrice = mMinPrice < (float) mOHLCData.get(i).get("low_price") ? mMinPrice : (float) mOHLCData.get(i).get("low_price");
            mMaxPrice = mMaxPrice > (float) mOHLCData.get(i).get("high_price") ? mMaxPrice : (float) mOHLCData.get(i).get("high_price");
            mVolume.add((Float) mOHLCData.get(i).get("volume"));
        }

        for (MALineBean lineEntity : MALineData) {
            for (int i = mDataStartIndext; i < lineEntity.getLineData().size()
                    && i < mShowDataNum + mDataStartIndext; i++) {

                if (lineEntity.getLineData().get(i) != 0)
                    mMinPrice = mMinPrice < lineEntity.getLineData().get(i) ? mMinPrice : lineEntity
                            .getLineData().get(i);

                mMaxPrice = mMaxPrice > lineEntity.getLineData().get(i) ? mMaxPrice : lineEntity
                        .getLineData().get(i);
            }
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
            if (days == dayk1 && (Float) ohlcData.get("avg_price_" + dayk1) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk1));
            } else if (days == dayk2 && (Float) ohlcData.get("avg_price_" + dayk2) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk2));
            } else if (days == dayk3 && (Float) ohlcData.get("avg_price_" + dayk3) != 0.0) {
                MAValues.add((Float) ohlcData.get("avg_price_" + dayk3));
            } else {

            }
        }

        return MAValues;
    }

    public List<Map<String, Object>> getOHLCData() {
        return mOHLCData;
    }

    public Bitmap setOHLCData(List<Map<String, Object>> OHLCData) {
        mShowDataNum = DEFAULT_CANDLE_NUM;
        DEFAULT_LOGITUDE_NUM = 4;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        if (OHLCData == null || OHLCData.size() <= 0) {
            drawBitMap(canvas);
            return bitmap;
        }
        this.mOHLCData = OHLCData;
        hollow = getBoolean("hollow", false);
        dayk1 = getInt("tv_ln1", 5);
        dayk2 = getInt("tv_ln2", 10);
        dayk3 = getInt("tv_ln3", 20);

        for (Map<String, Object> data : mOHLCData) {
            mVolume.add((Float) data.get("volume"));
            mChangeRate.add((Float) data.get("price_change_rate"));
        }

        initMALineData();

//        mMACDData = new MACDBean(mOHLCData);
//        mKDJData = new KDJBean(mOHLCData);
//        mRSIData = new RSIBean(mOHLCData);

        setCurrentData();

        drawBitMap(canvas);

        return bitmap;
    }

    private boolean getBoolean(String hollow, boolean b) {
        return preferences.getBoolean(hollow,b);
    }

    private int getInt(String key,int value){
        return preferences.getInt(key,value);
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
}
