package hply.com.niugu.stock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hply.com.niugu.StringUtils;


/**
 * Created by huangxx on 2017/valentine's day.
 */
public class TimesFivesBitmap extends Grid {
    private int DATA_MAX_COUNT;
    private Map<String, Integer> leftPositionLengths = new HashMap<>();
    private Map<String, Integer> rightPositionLengths = new HashMap<>();
    private List<StockMinuteData> timesList = new ArrayList<StockMinuteData>();
    private List<Float> list = new ArrayList<Float>();

    public HashMap<String, StockMinuteData> DetailMap = new HashMap<String, StockMinuteData>();
    private StockMinuteData fenshiData;
    private double closePrice;
    private float uperBottom;
    private float uperHeight;
    private float lowerBottom;
    private float lowerHeight;
    private float dataSpacing;

    private double initialWeightedIndex;
    private float uperHalfHigh;
    private float lowerHigh;
    private float uperRate;
    private float lowerRate;
    private List<String> dateList = new ArrayList<String>();

    private Paint tilte_paint;
    private Paint paintTop;
    private Paint paintShadow;
    private Rect r;
    private float uperBlueX;
    private Paint paintBottom;
    private DashPathEffect mDashPathEffect;
    //横竖屏判断
    private boolean isFromDetail;

    //区分分时图和五日图
    private boolean isTimes;
    //停牌处理
    private boolean isSuspension;
    private SimpleDateFormat dateFormat;
    private Calendar c;
    private int lastPoint = 0;

    public TimesFivesBitmap(Integer width, Integer height) {
        super(width, height);
        init();
    }

    private void init() {
        super.setShowLowerChart(true);
        super.setUperChartHeight((height * 7) / 10f);
        super.setLowerChartHeight((height * 2) / 10f);
        uperBlueX = 0;
        isFromDetail = false;
        initialWeightedIndex = 0;
        tilte_paint = new Paint();
        paintTop = new Paint();
        paintShadow = new Paint();
        paintBottom = new Paint();
        mDashPathEffect = new DashPathEffect(new float[]{3, 3, 3, 3}, 2);
        r = new Rect();

    }

    @Override
    protected void drawBitMap(Canvas canvas) {
        super.drawBitMap(canvas);

        uperBottom = getUperChartHeight() - 1;
        uperHeight = getUperChartHeight() - mCircleSize - 2;
        lowerHeight = getLowerChartHeight() - 2;
        lowerBottom = height - 1;
        uperRate = uperHeight / uperHalfHigh / 2.0f;
        lowerRate = lowerHeight / lowerHigh;
        dataSpacing = (width - leftMargin - rightMargin) * 1.00f / (DATA_MAX_COUNT - 1);

        //绘制蓝线、黄线、量能线
        drawLines(canvas);

        //绘制Y轴下的坐标值
        drawXtitle(canvas);

        //停牌状态图表处理
        drawStopLines(canvas);
    }

    private void drawStopLines(Canvas canvas) {
        if (isSuspension) {
            //绘制停牌阴影部分
            Path stop_shadow_path = new Path();
            paintShadow.setStyle(Paint.Style.FILL);//实心矩形框
            paintShadow.setColor(Color.argb(0x47, 0x98, 0xfc, 0xff));
            //绘制停牌分时线
            float endStopX1;
            float endStopX2;
            float endStopY;
            paintTop.setStyle(Paint.Style.STROKE);
            paintTop.setColor(Color.rgb(0xfd, 0xc1, 0x01));
            paintTop.setStrokeWidth(3);
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
                endStopY = (float) (uperBottom - (StringUtils.getDouble(fenshiData.getPrice()) + uperHalfHigh - initialWeightedIndex) * uperRate);
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

    private void drawXtitle(Canvas canvas) {
        tilte_paint.setTextSize(mAxisTitleSize);
        tilte_paint.setColor(gray);
        int fontHeight = r.height();
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
            canvas.drawText(beginTime, leftMargin, (height - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
            canvas.drawText(middleTime, (width - leftMargin - rightMargin - middleTimeSize) / 2 + leftMargin, (height - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
            canvas.drawText(overTime, width - rightMargin - overTimeSize - 2, (height - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
        } else {
            for (int i = 0; i < dateList.size(); i++) {
                String beginTime = dateList.get(i);
                tilte_paint.getTextBounds(beginTime, 0, beginTime.length(), r);
                int beginTimeSize = r.width();
                canvas.drawText(beginTime, leftMargin + getLongitudeSpacing() / 2 + getLongitudeSpacing() * i - beginTimeSize / 2, (height - mUperChartHeight - mLowerChartHeight + fontHeight) / 2 + mUperChartHeight, tilte_paint);
            }
        }
    }

    //绘制上表中间红线
    @Override
    protected void drawLatitudes(Canvas canvas, Paint paint, int viewHeight, int viewWidth) {
        super.drawLatitudes(canvas, paint, viewHeight, viewWidth);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setPathEffect(mDashPathEffect);
        super.drawLatitudes(canvas, paint, viewHeight, viewWidth);
    }

    //绘制x轴的左右侧坐标值
    @Override
    public void drawXYPostion(Canvas canvas, int viewHeight, int viewWidth) {
        super.drawXYPostion(canvas, viewHeight, viewWidth);
        int fontHeight;
        String unit = "";
        String volume = "";

        tilte_paint.setTextSize(mAxisTitleSize);
        tilte_paint.setColor(gray);

        //绘制左边价格
        String pos_max_price = "";
        if (isTimes && isSuspension) {
            pos_max_price = StringUtils.save2Significand(1.01 * closePrice);
        } else {
            pos_max_price = StringUtils.save2Significand(initialWeightedIndex + uperHalfHigh);
        }
        tilte_paint.getTextBounds(pos_max_price, 0, pos_max_price.length(), r);
        leftPositionLengths.put("pos_max_price", r.width());

        String close_price = StringUtils.save2Significand(closePrice);
        tilte_paint.getTextBounds(close_price, 0, close_price.length(), r);
        leftPositionLengths.put("close_price", r.width());

        String pos_min_price = "";
        if (isTimes && isSuspension) {
            pos_min_price = StringUtils.save2Significand(0.99 * closePrice);
        } else {
            pos_min_price = StringUtils.save2Significand(initialWeightedIndex - uperHalfHigh);
        }
        tilte_paint.getTextBounds(pos_min_price, 0, pos_min_price.length(), r);
        leftPositionLengths.put("pos_min_price", r.width());

        if (isFromDetail) {
            if ((lowerHigh / 10000 / 100) < 1) {
                unit = "手";
                volume = String.valueOf((int) (lowerHigh / 100));
            } else {
                unit = "万手";
                volume = StringUtils.save2Significand(lowerHigh / 10000 / 100);
            }
            tilte_paint.getTextBounds(volume, 0, volume.length(), r);
            leftPositionLengths.put("volume", r.width());

            tilte_paint.getTextBounds(unit, 0, unit.length(), r);
            leftPositionLengths.put("unit", r.width());
        }

        //绘制右边率
        String pos_max_rate = "";
        if (isTimes && isSuspension) {
            pos_max_rate = StringUtils.save2Significand(1.00) + "%";
        } else {
            String twoPointData = StringUtils.save2Significand(uperHalfHigh / initialWeightedIndex * 100);
            pos_max_rate = twoPointData + "%";
        }
        tilte_paint.getTextBounds(pos_max_rate, 0, pos_max_rate.length(), r);
        rightPositionLengths.put("pos_max_rate", r.width());

        String close_rate = StringUtils.save2Significand(0.00) + "%";
        tilte_paint.getTextBounds(close_rate, 0, close_rate.length(), r);
        rightPositionLengths.put("close_rate", r.width());

        String pos_min_rate = "";
        if (isTimes && isSuspension) {
            pos_min_rate = StringUtils.save2Significand(-1.00) + "%";
        } else {
            String twoPointData = StringUtils.save2Significand(-uperHalfHigh / initialWeightedIndex * 100);
//            int anInt = Integer.parseInt(twoPointData);
            pos_min_rate = twoPointData + "%";
        }
        tilte_paint.getTextBounds(pos_min_rate, 0, pos_min_rate.length(), r);
        rightPositionLengths.put("pos_min_rate", r.width());

        fontHeight = r.height();
        //margin+5，使左右游标显示完全
        if (isFromDetail) {
            for (Integer length : leftPositionLengths.values()) {
                if (length > leftMargin) {
                    leftMargin = length + 5;
                }
            }
            for (Integer length : rightPositionLengths.values()) {
                if (length > rightMargin)
                    rightMargin = length + 5;
            }
            canvas.drawText(pos_max_price, leftMargin - leftPositionLengths.get("pos_max_price"), fontHeight + mCircleSize, tilte_paint);
            canvas.drawText(close_price, leftMargin - leftPositionLengths.get("close_price"), (mUperChartHeight + fontHeight) / 2, tilte_paint);
            canvas.drawText(pos_min_price, leftMargin - leftPositionLengths.get("pos_min_price"), mUperChartHeight, tilte_paint);
            canvas.drawText(volume, leftMargin - leftPositionLengths.get("volume"), viewHeight - mLowerChartHeight + fontHeight, tilte_paint);
            canvas.drawText(unit, leftMargin - leftPositionLengths.get("unit"), viewHeight - 3, tilte_paint);
            canvas.drawText(pos_max_rate, viewWidth - rightMargin - 2, fontHeight + mCircleSize, tilte_paint);
            canvas.drawText(close_rate, viewWidth - rightMargin - 2, (mUperChartHeight + fontHeight) / 2, tilte_paint);
            canvas.drawText(pos_min_rate, viewWidth - rightMargin - 2, mUperChartHeight, tilte_paint);
        } else {
            // 绘制左边Y轴的坐标
            canvas.drawText(pos_max_price, 0, fontHeight + 5 + mCircleSize, tilte_paint);
            canvas.drawText(pos_min_price, 0, mUperChartHeight - 5, tilte_paint);
            // 绘制右边Y轴的坐标
            // TODO: 2016/9/22 0022
//          解决NAN
            canvas.drawText(pos_max_rate.substring(0, 3).toLowerCase().equals("nan") ? "0.00%" : pos_max_rate, viewWidth - rightPositionLengths.get("pos_max_rate") - 2, fontHeight + 5 + mCircleSize, tilte_paint);
            canvas.drawText(pos_min_rate.substring(0, 3).toLowerCase().equals("nan") ? "0.00%" : pos_min_rate, viewWidth - rightPositionLengths.get("pos_min_rate") - 2, mUperChartHeight - 5, tilte_paint);
        }
    }


    @Override
    protected void drawLines(Canvas canvas) {
        paintTop.setStyle(Paint.Style.STROKE);
        DetailMap.clear();
        paintTop.setStrokeWidth(3);
        paintShadow.setStyle(Paint.Style.FILL);//实心矩形框
        paintShadow.setColor(Color.argb(0x47, 0x98, 0xfc, 0xff));
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
            float endFirstBlueY = (float) (uperBottom - (StringUtils.getDouble(MinuteDataFirst.getPrice()) + uperHalfHigh - initialWeightedIndex) * uperRate);
            float endFirstYellowY = (float) (uperBottom - (StringUtils.getDouble(MinuteDataFirst.getAvg_price()) + uperHalfHigh - initialWeightedIndex) * uperRate);

            time_line_path.moveTo(endBlueFirstX, endFirstBlueY);
            avg_line_path.moveTo(endBlueFirstX, endFirstYellowY);
            time_shadow_path.moveTo(endBlueFirstX, endFirstBlueY);
            endBlueMidStartX = endBlueFirstX;
        }

        //找到其它点绘制图形
        for (int i = 0; i < timesList.size(); i++) {
            cut++;
            paintTop.setStrokeWidth(3);
            fenshiData = timesList.get(i);
            int stock_hour = Integer.parseInt(fenshiData.getDate().substring(11, 13));
            int stock_minute = Integer.parseInt(fenshiData.getDate().substring(14, 16));
            int position = getMinute(stock_hour, stock_minute);
            if (isTimes) {
                uperBlueX = dataSpacing * position + leftMargin;
            } else {
                uperBlueX = dataSpacing * i + leftMargin;
            }
            float valueSp = (uperBlueX - leftMargin) / dataSpacing;
            DetailMap.put(StringUtils.saveSignificand(valueSp,1), fenshiData);
            float endBlueY = (float) (uperBottom - (StringUtils.getDouble(DetailMap.get(StringUtils.saveSignificand(valueSp,1)).getPrice())
                    + uperHalfHigh - initialWeightedIndex)
                    * uperRate);
            if (i == timesList.size() - 1) {
                endBlueLastX = uperBlueX;
            }
            float endYelloY = (float) (uperBottom - (StringUtils.getDouble(DetailMap.get(StringUtils.saveSignificand(valueSp,1)).getAvg_price()) + uperHalfHigh - initialWeightedIndex)
                    * uperRate);
            //五日图绘制
            if (!isTimes) {
                time_line_path.lineTo(uperBlueX, endBlueY);
                avg_line_path.lineTo(uperBlueX, endYelloY);
                time_shadow_path.lineTo(uperBlueX, endBlueY);

                paintTop.setColor(Color.rgb(0x47, 0x98, 0xfc));
                canvas.drawPath(time_line_path, paintTop);
                paintTop.setColor(Color.rgb(0xfd, 0xc1, 0x01));
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

                    paintTop.setColor(Color.rgb(0x47, 0x98, 0xfc));
                    canvas.drawPath(time_line_path, paintTop);
                    paintTop.setColor(Color.rgb(0xfd, 0xc1, 0x01));
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
                paintTop.setStrokeWidth((float) (mLineLandscapeSize * 1.5));
            } else {
                paintTop.setStrokeWidth(mLineLandscapeSize);
            }
            if (isFromDetail) {
                paintBottom.setStrokeWidth(mLineLandscapeSize);
            } else {
                paintBottom.setStrokeWidth(mLinePortraitSize);
            }
            long buy = fenshiData.getVolume();
            if (i == 0) {
                paintBottom.setColor(Color.rgb(0xf1, 0x4a, 0x56));
            } else if (StringUtils.getDouble(fenshiData.getPrice()) >= StringUtils.getDouble(timesList.get(i - 1).getPrice())) {
                paintBottom.setColor(Color.rgb(0xf1, 0x4a, 0x56));
            } else if (fenshiData.getPrice() == timesList.get(i - 1).getPrice()) {
                paintBottom.setColor(Color.rgb(0x1d, 0xbf, 0x60));
            } else {
                paintBottom.setColor(Color.rgb(0x01, 0xb7, 0x0a));
            }
            canvas.drawLine(uperBlueX + 2, lowerBottom, uperBlueX + 2, lowerBottom - buy * lowerRate, paintBottom);
        }
        time_shadow_path.lineTo(endBlueLastX, mUperChartHeight);
        time_shadow_path.lineTo(endBlueMidStartX, mUperChartHeight);
        time_shadow_path.close();
        canvas.drawPath(time_shadow_path, paintShadow);
    }

    //带入数据
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
        List<StockMinuteData> timesorFivesList = new ArrayList<StockMinuteData>();
        SimpleDateFormat date_format_get_year = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Date tmp_date = null;
        List<Date> unsorted_date_list = new ArrayList<>();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        if (bean != null) {
            if (bean.getCode() == 0) {
                timesorFivesList.clear();
                timesorFivesList.addAll(bean.getData());
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
            if (timesorFivesList != null && timesorFivesList.size() > 0) {
                StockMinuteData fiveDayData = timesorFivesList.get(0);
                //分时和五日数据处理
                double weightedIndex = StringUtils.getDouble(fiveDayData.getPrice());
                double nonWeightedIndex = StringUtils.getDouble(fiveDayData.getAvg_price());
                this.closePrice = StringUtils.getDouble(fiveDayData.getPrice()) / (1 +
                        (StringUtils.getDouble(fiveDayData.getPrice_change_rate()) / 100.00f));
                initialWeightedIndex = StringUtils.getDouble(fiveDayData.getPrice()) / (1 +
                        (StringUtils.getDouble(fiveDayData.getPrice_change_rate()) / 100.00f));

                for (int i = 1; i < timesorFivesList.size(); i++) {
                    fiveDayData = timesorFivesList.get(i);
                    weightedIndex = StringUtils.getDouble(fiveDayData.getPrice());
                    nonWeightedIndex = StringUtils.getDouble(fiveDayData.getAvg_price());

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

    public float getDataspcing() {
        return this.dataSpacing;
    }

    public int getLastPoint() {
        return this.lastPoint;
    }

    public boolean isTimes() {
        return isTimes;
    }

    public float getMargin() {
        return this.leftMargin;
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

    public int getMaxisTitleSize() {
        return this.mAxisTitleSize;
    }

    public int getCircleSize() {
        return this.mCircleSize;
    }

    public HashMap getMap() {
        return this.DetailMap;
    }

    public List<StockMinuteData> getTimeList() {
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
}

