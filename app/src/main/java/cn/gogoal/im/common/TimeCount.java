package cn.gogoal.im.common;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :倒计时
 */
public class TimeCount extends CountDownTimer {

    private TextView executantView;//执行者

    public TimeCount(long millisInFuture, long countDownInterval, TextView executantView) {
        //参数依次为总时长,和计时的时间间隔
        super(millisInFuture, countDownInterval);
        this.executantView = executantView;
    }

    @Override
    public void onFinish() {
        //计时完毕时触发
        executantView.setText("重新发送");
        executantView.setEnabled(true);
        executantView.setTextColor(ContextCompat.getColor(
                executantView.getContext(), R.color.colorPrimary));
        executantView.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //计时过程显示
        executantView.setEnabled(false);
        executantView.setClickable(false);
        executantView.setTextColor(ContextCompat.getColor(
                executantView.getContext(), android.R.color.white));

        executantView.setText("已发送 (" + millisUntilFinished / 1000 + ")");
    }
}