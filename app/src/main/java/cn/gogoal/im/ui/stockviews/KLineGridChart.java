package cn.gogoal.im.ui.stockviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

/**
 * Created by huangxx on 2017/6/15.
 */

public class KLineGridChart {
    ///////////////默认值////////////////
    public static final int DEFAULT_BACKGROUND = Color.WHITE;        //默认背景色
    public static final int DEFAULT_TITLE_SIZE = 30;                 //默认字体大小
    public static final int DEFAULT_SIZE = 4;                        //默认尺寸大小
    public static final int DEFAULT_LONGTITUDE_COLOR = Color.GRAY;   //默认经纬线颜色
    public static final int DEFAULT_LAITUDE_COLOR = Color.RED;       //默认经纬线颜色
    public static int DEFAULT_UPER_LATITUDE_NUM = 1;                 //默认上表纬线数
    public static int DEFAULT_LOGITUDE_NUM = 1;                      //默认经线数
    public static final int DEFAULT_BORDER_COLOR = Color.GRAY;       //默认边框的颜色
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{3, 3, 3,
            3}, 2);

    ///////////////属性////////////////
    private int mBackGround;                                //背景色
    private int mLatitudeColor;                             //经纬线颜色
    private int mLongtitudeColor;                           //经纬线颜色
    private PathEffect mDashEffect;                         //虚线效果
    private int mBorderColor;                               //边线色
    private float mUperChartHeight;                         //上表高度
    private float mLowerChartHeight;                        //下表高度
    private int leftMargin;                                 //左边坐标宽度
    private int rightMargin;                                //右边坐标宽度
    private static int longitudeNum;                        //上表纬线数
    private static int uperLatitudeNum;                     //经线数
    private float longitudeSpacing;                         //经度的间隔
    private float latitudeSpacing;                          //纬度的间隔
    private DashPathEffect mRedDashPathEffect;              //中间红色虚线
    private int mAxisTitleSize;                             //字体尺寸
    private int mSize;                                      //图形尺寸

    //view的宽高
    private int viewHeight;
    private int viewWidth;

    //横竖屏判断
    private boolean isFromDetail;

    public KLineGridChart(Integer width, Integer height) {
        this.viewWidth = width;
        this.viewHeight = height;
        init();
    }

    private void init() {
        isFromDetail = false;
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
    }

    public Bitmap drawBitMap() {

        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        setUperChartHeight((viewHeight * 7) / 10f);
        setLowerChartHeight((float) ((viewHeight * 2.1) / 10f));

        // 绘制边框
        drawBorders(canvas);

        // 绘制经线
        drawLongitudes(canvas);

        // 绘制纬线
        drawLatitudes(canvas);

        return bitmap;
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
        canvas.drawLine(leftMargin + mSize / 2, mSize / 2, viewWidth - rightMargin - mSize / 2, mSize / 2, paint);
        if (isFromDetail) {
            //left
            canvas.drawLine(leftMargin + mSize / 2, mSize / 2, leftMargin + mSize / 2, mUperChartHeight - mSize / 2, paint);
            //right
            canvas.drawLine(viewWidth - rightMargin - mSize / 2, mSize / 2, viewWidth - rightMargin - mSize / 2, mUperChartHeight - mSize / 2, paint);
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
            canvas.drawLine(longitudeSpacing * i + leftMargin + mSize / 2, mSize / 2, longitudeSpacing * i + leftMargin + mSize / 2,
                    mUperChartHeight - mSize / 2, paint);
            canvas.drawLine(mSize / 2 + longitudeSpacing * i + leftMargin, viewHeight - getLowerChartHeight() + mSize / 2, mSize / 2 + longitudeSpacing * i + leftMargin,
                    viewHeight - mSize / 2, paint);
        }

    }

    /**
     * 绘制纬线（只包含上表）
     */
    private void drawLatitudes(Canvas canvas) {

        latitudeSpacing = (mUperChartHeight - mSize) / (uperLatitudeNum + 1);

        Paint paint = new Paint();
        paint.setColor(mLatitudeColor);
        paint.setStrokeWidth(mSize / 2);
        paint.setPathEffect(mRedDashPathEffect);

        for (int i = 1; i <= uperLatitudeNum; i++) {
            canvas.drawLine(leftMargin + mSize / 2, mSize / 2 + latitudeSpacing * i, viewWidth - rightMargin - mSize / 2, mSize / 2 + latitudeSpacing * i, paint);
        }
    }


    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
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

    public void setShowDetail(boolean isFromDetail) {
        this.isFromDetail = isFromDetail;
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

    public static int getLongitudeNum() {
        return longitudeNum;
    }

    public static int getUperLatitudeNum() {
        return uperLatitudeNum;
    }

    public void setLongitudeSpacing(float longitudeSpacing) {
        this.longitudeSpacing = longitudeSpacing;
    }

    public static int getDefaultBackground() {
        return DEFAULT_BACKGROUND;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public static int getDefaultTitleSize() {
        return DEFAULT_TITLE_SIZE;
    }

    public int getmLongtitudeColor() {
        return mLongtitudeColor;
    }

    public void setmLongtitudeColor(int mLongtitudeColor) {
        this.mLongtitudeColor = mLongtitudeColor;
    }

    public int getmLatitudeColor() {
        return mLatitudeColor;
    }

    public void setmLatitudeColor(int mLatitudeColor) {
        this.mLatitudeColor = mLatitudeColor;
    }
}

