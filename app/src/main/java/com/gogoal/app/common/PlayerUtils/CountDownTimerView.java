package com.gogoal.app.common.PlayerUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogoal.app.R;
import com.gogoal.app.common.CalendarUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dave.
 * Date: 2017/2/21.
 * Desc: 倒计时
 */
public class CountDownTimerView extends LinearLayout {

    private LinearLayout liveTelecastTrailer;

    private TextView textLiveTrailer;

    // 天，十位
    private TextView textDaysDecade;
    // 天，个位
    private TextView textDaysUnit;
    // 小时，十位
    private TextView textHoursDecade;
    // 小时，个位
    private TextView textHoursUnit;
    // 分钟，十位
    private TextView textMinutesDecade;
    // 分钟，个位
    private TextView textMinutesUnit;
    // 秒，十位
    private TextView textSecondsDecade;
    // 秒，个位
    private TextView textSecondsUnit;

    private Context context;
    private Long day_decade;
    private Long day_unit;

    private Long hour_decade;
    private Long hour_unit;
    private Long min_decade;
    private Long min_unit;
    private Long sec_decade;
    private Long sec_unit;
    // 计时器
    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }
    };
    private int day = 0;
    private int hour = 0;
    private int min = 0;
    private int sec = 0;

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);


        liveTelecastTrailer = (LinearLayout) view.findViewById(R.id.liveTelecastTrailer);
        textLiveTrailer = (TextView) view.findViewById(R.id.textLiveTrailer);
        textDaysDecade = (TextView) view.findViewById(R.id.textDaysDecade);
        textDaysUnit = (TextView) view.findViewById(R.id.textDaysUnit);
        textHoursDecade = (TextView) view.findViewById(R.id.textHoursDecade);
        textHoursUnit = (TextView) view.findViewById(R.id.textHoursUnit);
        textMinutesDecade = (TextView) view.findViewById(R.id.textMinutesDecade);
        textMinutesUnit = (TextView) view.findViewById(R.id.textMinutesUnit);
        textSecondsDecade = (TextView) view.findViewById(R.id.textSecondsDecade);
        textSecondsUnit = (TextView) view.findViewById(R.id.textSecondsUnit);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 开始计时
     */
    public void start() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 停止计时
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 如果:sum = 12345678
    public void addTime(String start_time) {

        //当前时间
        Long cur_time = new Date().getTime();

        Date date = CalendarUtils.parseString2Date(start_time);

        Long sum = (date.getTime() - cur_time)/1000;

        // 先获取个秒数值
        Long sec = sum % 60;
        // 如果大于60秒，获取分钟。（秒数）
        Long sec_time = sum / 60;
        // 再获取分钟
        Long min = sec_time % 60;
        // 如果大于60分钟，获取小时（分钟数）。
        Long min_time = sec_time / 60;
        // 获取小时
        Long hour = min_time % 24;
        // 剩下的自然是天数
        Long day = min_time / 24;

        setTime(day, hour, min, sec);

    }

    /**
     * @param
     * @return void
     * @throws Exception
     * @throws
     * @Description: 设置倒计时的时长
     */
    public void setTime(Long day, Long hour, Long min, Long sec) {
        //这里的天数不写也行，我写365
        if (day >= 365 || hour >= 24 || min >= 60 || sec >= 60 || day < 0
                || hour < 0 || min < 0 || sec < 0) {
            throw new RuntimeException("Time format is error,please check out your code");
        }
        // day 的十位数
        day_decade = day / 10;
        // day的个位数,这里求余就行
        day_unit = day - day_decade * 10;

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;
        // 第个time 进行初始化
        timeClean();
    }

    private void timeClean() {
        textLiveTrailer.setText("距" + "08月28日 15:30" + "直播开始还有");

        textDaysDecade.setText(day_decade + "");
        textDaysUnit.setText(day_unit + "");
        textHoursDecade.setText(hour_decade + "");
        textHoursUnit.setText(hour_unit + "");
        textMinutesDecade.setText(min_decade + "");
        textMinutesUnit.setText(min_unit + "");
        textSecondsDecade.setText(sec_decade + "");
        textSecondsUnit.setText(sec_unit + "");
    }

    /**
     * @param
     * @return boolean
     * @throws
     * @Description: 倒计时
     */
    public Boolean countDown() {

        if (isCarry4Unit(textSecondsUnit)) {
            if (isCarry4Decade(textSecondsDecade)) {

                if (isCarry4Unit(textMinutesUnit)) {
                    if (isCarry4Decade(textMinutesDecade)) {

                        if (isDay4Unit(textHoursUnit)) {
                            if (isDay4Decade(textHoursDecade)) {

                                if (isDay4Unit(textDaysUnit)) {
                                    if (isDay4Decade(textDaysDecade)) {
                                        textDaysDecade.setText("0");
                                        textDaysUnit.setText("0");
                                        textHoursDecade.setText("0");
                                        textHoursUnit.setText("0");
                                        textMinutesDecade.setText("0");
                                        textMinutesUnit.setText("0");
                                        textSecondsDecade.setText("0");
                                        textSecondsUnit.setText("0");
                                        stop();
                                        liveTelecastTrailer.setVisibility(View.GONE);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @param
     * @return boolean
     * @throws
     * @Description: 变化十位，并判断是否需要进位
     */
    private boolean isCarry4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @param
     * @return boolean
     * @throws
     * @Description: 变化个位，并判断是否需要进位
     */
    private boolean isCarry4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @param
     * @return boolean
     * @throws
     * @Description: 变化十位，并判断是否需要进位
     */
    private boolean isDay4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 3;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @param
     * @return boolean
     * @throws
     * @Description: 变化个位，并判断是否需要进位
     */
    private boolean isDay4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 2;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }
    }
}
