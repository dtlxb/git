package cn.gogoal.im.common.PlayerUtils;

import android.os.CountDownTimer;

public class MyDownTimer extends CountDownTimer {

    private Runner run;
    private long second;
    private long cache;

    public MyDownTimer(long sec, Runner runner) {
        super(sec * 1002, 1000);
        run = runner;
        second = sec - 1;
        cache = sec - 1;
    }

    public MyDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {

        if (run != null) {
            run.run(second);
            second--;
        }
    }

    @Override
    public void onFinish() {

        if (run != null) {
            run.finish();
            second = cache;
        }
    }

    public interface Runner {
        void run(long sec);
        void finish();
    }
}
