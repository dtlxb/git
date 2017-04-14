package hply.com.niugu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import hply.com.niugu.R;


/**
 * 坐标轴使用的View
 *
 * @author Lizn
 */
public abstract class GridChart extends View {

    // ////////////默认值////////////////
    /**
     * 默认背景色
     */
    public static final int DEFAULT_BACKGROUD = R.color.white;

    /**
     * 默认XY轴字体大小
     **/
    public static final int DEFAULT_AXIS_TITLE_SIZE = 22;

    /**
     * 默认XY坐标轴颜色
     */
    public static final int DEFAULT_AXIS_COLOR = Color.GRAY;

    /**
     * 默认经纬线颜色
     */
    public static final int DEFAULT_LONGI_LAITUDE_COLOR = Color.GRAY;

    /**
     * 默认上表纬线数
     */
    public static int DEFAULT_UPER_LATITUDE_NUM = 1;

    /**
     * 默认经线数
     */
    public static int DEFAULT_LOGITUDE_NUM = 1;

    /**
     * 默认边框的颜色
     */
    public static final int DEFAULT_BORDER_COLOR = Color.GRAY;

    /**
     * 默认虚线效果
     */
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{3, 3, 3,
            3}, 1);

    protected static float UPER_CHART_BOTTOM;
    protected static float LOWER_CHART_TOP;
    // /////////////属性////////////////
    /**
     * 背景色
     */
    private int mBackGround;

    /**
     * 坐标轴XY颜色
     */
    private int mAxisColor;

    /**
     * 经纬线颜色
     */
    protected int mLongiLatitudeColor;

    /**
     * 虚线效果
     */
    protected PathEffect mDashEffect;

    /**
     * 边线色
     */
    protected int mBorderColor;

    /**
     * 上表高度
     */
    protected float mUperChartHeight;
    /**
     * 量能线竖屏宽度
     */
    protected int mLinePortraitSize;

    /**
     * 量能线横屏宽度
     */
    protected int mLineLandscapeSize;

    /**
     * 是否显示下表
     */
    private boolean showLowerChart;

    /**
     * 下表TabTitles
     */
    private String[] mLowerChartTabTitles;

    /**
     * 下表高度
     */
    protected float mLowerChartHeight;

    /**
     * 左边坐标文字的最大长度
     */
    protected int leftMargin;

    /**
     * 右边坐标文字的最大长度
     */
    protected int rightMargin;

    /**
     * 上表纬线数
     */
    protected static int longitudeNum;

    /**
     * 经线数
     */
    protected static int uperLatitudeNum;

    /**
     * 经度的间隔
     */
    private float longitudeSpacing;

    /**
     * 纬度的间隔
     */
    private float latitudeSpacing;

    /**
     * 显示股票详情
     */
    protected boolean isFromStockDetail;

    protected int red = Color.rgb(0xf2, 0x49, 0x57);
    protected int green = Color.rgb(0x1d, 0xbf, 0x60);
    protected int gray = Color.rgb(0x54, 0x69, 0x80);

    protected int viewHeight;
    protected int viewWidth;
    private boolean isSw480P;
    private boolean isSw720P;
    private boolean isSw1080P;
    protected int mAxisTitleSize;

    private OnTabClickListener mOnTabClickListener;


    public GridChart(Context context) {
        super(context);
        init();
    }

    public GridChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GridChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBackGround = DEFAULT_BACKGROUD;
        mAxisColor = DEFAULT_AXIS_COLOR;
        mAxisTitleSize = DEFAULT_AXIS_TITLE_SIZE;
        mLongiLatitudeColor = DEFAULT_LONGI_LAITUDE_COLOR;
        mDashEffect = DEFAULT_DASH_EFFECT;
        mBorderColor = DEFAULT_BORDER_COLOR;
        longitudeNum = DEFAULT_LOGITUDE_NUM;
        uperLatitudeNum = DEFAULT_UPER_LATITUDE_NUM;
        showLowerChart = true;
        mOnTabClickListener = null;
        mLinePortraitSize = 1;
        mLineLandscapeSize = 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundResource(mBackGround);

        viewHeight = getHeight();
        viewWidth = getWidth();

        setUperChartHeight((viewHeight * 7) / 10f);
        setLowerChartHeight((viewHeight * 2) / 10f);

        //字体大小判断
        if (isSw480P) {
            mAxisTitleSize = 15;
            mLinePortraitSize = 1;
            mLineLandscapeSize = 1;
        } else if (isSw720P) {
            mAxisTitleSize = 22;
            mLinePortraitSize = 1;
            mLineLandscapeSize = 2;
        } else if (isSw1080P) {
            mAxisTitleSize = 30;
            mLinePortraitSize = 2;
            mLineLandscapeSize = 4;
        } else {
            mAxisTitleSize = 36;
            mLinePortraitSize = 2;
            mLineLandscapeSize = 4;
        }
        // 绘制XY轴坐标
        drawXYPosition(canvas, viewHeight, viewWidth);

        longitudeSpacing = (viewWidth - leftMargin - rightMargin) / (longitudeNum + 1);

        latitudeSpacing = (mUperChartHeight - 4) / (uperLatitudeNum + 1);

        UPER_CHART_BOTTOM = 2 + latitudeSpacing * (uperLatitudeNum + 1);

        // 绘制边框
        drawBorders(canvas, null, viewHeight, viewWidth);

        // 绘制经线
        drawLongitudes(canvas, null, viewHeight);

        // 绘制纬线
        drawLatitudes(canvas, null, viewHeight, viewWidth);
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;


    }

    public interface OnTabClickListener {
        void onTabClick(int indext);
    }

    /**
     * 绘制XY轴坐标
     *
     * @param canvas
     * @param viewHeight
     * @param viewWidth
     */
    protected abstract void drawXYPosition(Canvas canvas, int viewHeight, int viewWidth);

    /**
     * 绘制边框
     *
     * @param canvas
     * @param paint
     * @param viewHeight
     * @param viewWidth
     */
    protected void drawBorders(Canvas canvas, Paint paint, int viewHeight, int viewWidth) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(mBorderColor);
            paint.setStrokeWidth(2);
        }
        // 画上表
        //up
        canvas.drawLine(leftMargin + 1, 1, viewWidth - rightMargin - 1, 1, paint);
        //left
        //canvas.drawLine(maxLeftPositionLength + 1, 1, maxLeftPositionLength + 1, mUpChartHeight - 1, paint);
        //bottom
        canvas.drawLine(leftMargin + 1, mUperChartHeight - 1, viewWidth - rightMargin - 1, mUperChartHeight - 1, paint);
        //right
        //canvas.drawLine(viewWidth - maxRightPositionLength - 1, 1, viewWidth - maxRightPositionLength - 1, mUpChartHeight - 1, paint);

        // 画下表
        //up
        //canvas.drawLine(maxLeftPositionLength + 1, viewHeight - mLowerChartHeight + 1, viewWidth - maxRightPositionLength - 1, viewHeight - mLowerChartHeight + 1, paint);
        //left
        //canvas.drawLine(maxLeftPositionLength + 1, viewHeight - mLowerChartHeight + 1, maxLeftPositionLength + 1, viewHeight - 1, paint);
        //bottom
        canvas.drawLine(leftMargin + 1, viewHeight - 1, viewWidth - rightMargin - 1, viewHeight - 1, paint);
        //right
        //canvas.drawLine(viewWidth - maxRightPositionLength - 1, viewHeight - mLowerChartHeight + 1, viewWidth - maxRightPositionLength - 1, viewHeight - 1, paint);
    }

    /**
     * 绘制经线
     *
     * @param canvas
     * @param paint
     * @param viewHeight
     */
    protected void drawLongitudes(Canvas canvas, Paint paint, int viewHeight) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(mLongiLatitudeColor);
            paint.setPathEffect(mDashEffect);
        }

        for (int i = 1; i <= longitudeNum; i++) {
            canvas.drawLine(longitudeSpacing * i + leftMargin, 2, longitudeSpacing * i + leftMargin,
                    mUperChartHeight - 2, paint);
            canvas.drawLine(longitudeSpacing * i + leftMargin, viewHeight - getLowerChartHeight(), longitudeSpacing * i + leftMargin,
                    viewHeight - 2, paint);
        }
    }

    /**
     * 绘制纬线（只包含上表）
     *
     * @param canvas
     * @param paint
     * @param viewHeight
     * @param viewWidth
     */
    protected void drawLatitudes(Canvas canvas, Paint paint, int viewHeight, int viewWidth) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(mLongiLatitudeColor);
            paint.setPathEffect(mDashEffect);
        }

        for (int i = 1; i <= uperLatitudeNum; i++) {
            canvas.drawLine(leftMargin + 1, 1 + latitudeSpacing * i, viewWidth - rightMargin - 1, 1 + latitudeSpacing * i, paint);
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas
     */
    protected abstract void drawLines(Canvas canvas);

    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
    }

    public int getAxisColor() {
        return mAxisColor;
    }

    public void setAxisColor(int AxisColor) {
        this.mAxisColor = AxisColor;
    }

    public int getLongiLatitudeColor() {
        return mLongiLatitudeColor;
    }

    public void setLongiLatitudeColor(int LongiLatitudeColor) {
        this.mLongiLatitudeColor = LongiLatitudeColor;
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

    public boolean isShowLowerChart() {
        return showLowerChart;
    }

    public void setShowLowerChart(boolean showLowerChart) {
        this.showLowerChart = showLowerChart;
    }

    public float getLowerChartHeight() {
        return mLowerChartHeight;
    }

    public void setLowerChartHeight(float LowerChartHeight) {
        this.mLowerChartHeight = LowerChartHeight;
    }

    public String[] getLowerChartTabTitles() {
        return mLowerChartTabTitles;
    }

    public void setLowerChartTabTitles(String[] LowerChartTabTitles) {
        this.mLowerChartTabTitles = LowerChartTabTitles;
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

    public void setIsSw1080P(boolean isSw1080P) {
        this.isSw1080P = isSw1080P;
    }

    public void setIsSw480P(boolean isSw480P) {
        this.isSw480P = isSw480P;
    }

    public void setIsSw720P(boolean isSw720P) {
        this.isSw720P = isSw720P;
    }
}
