package com.gogoal.app.common;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * author wangjd on 2017/2/13 0013.
 * Staff_id 1375
 * phone 18930640263
 */
public class ClickUtils implements View.OnClickListener{

    private OnSingleDoubleClickListener mCallback;

    private static final int DOUBLE_CLICK_TIME = 350;        //双击间隔时间500毫秒

    private boolean waitDoubleClick = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCallback.OnSingleClick((View) msg.obj);
        }
    };

    public ClickUtils(OnSingleDoubleClickListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onClick(final View view) {
        if (waitDoubleClick) {
            waitDoubleClick = false;        //与执行双击事件
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(DOUBLE_CLICK_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }    //等待双击时间，否则执行单击事件
                    if (!waitDoubleClick) {
                        //如果过了等待事件还是预执行双击状态，则视为单击
                        waitDoubleClick = true;
                        Message msg = handler.obtainMessage();
                        msg.obj = view;
                        handler.sendMessage(msg);
//                        view.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mCallback.OnSingleClick(view);
//                            }
//                        });
                    }
                }

            }.start();
        } else {
            waitDoubleClick = true;
            mCallback.OnDoubleClick(view);    //执行双击
        }
    }

    public interface OnSingleDoubleClickListener {
        void OnSingleClick(View v);

        void OnDoubleClick(View v);
    }

    public interface OnSuperClickListener extends View.OnClickListener{
        void onClick(View view);
        void onDoubleClick(View view);
    }
}
