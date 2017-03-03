package com.gogoal.app.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gogoal.app.common.AppDevice;

/**
 * author wangjd on 2017/3/2 0002.
 * Staff_id 1375
 * phone 18930640263
 */
public class VoiceView extends SelectorButton {

    private float moveRange;//向上移动，取消录音 阈值

    public VoiceView(Context context) {
        this(context, null, 0);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //化部分
    private void init(Context context) {
        moveRange = AppDevice.getWidth(context) / 5;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下，1.录音，2.显示中间动画 3.设置状态
                break;
            case MotionEvent.ACTION_MOVE://手指移动，1.左右到控件边缘取消录音，向上移动到阈值的时候也取消录音 2.修改中间动画
                break;
            case MotionEvent.ACTION_UP://抬起手指
                break;
            default://其他情况，比如录音过程突然锁屏
                break;
        }

        return super.onTouchEvent(event);
    }
}
