package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * author wangjd on 2017/3/3 0003.
 * Staff_id 1375
 * phone 18930640263
 */
public class SwitchImageView extends AppCompatImageView {

    private int state;

    private OnSwitchListener listener;

    public void setOnSwitchListener(OnSwitchListener listener) {
        this.listener = listener;
    }

    public SwitchImageView(Context context) {
        this(context,null,0);
    }

    public SwitchImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        state=0;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state==0){
                    state=1;
                    if (listener!=null){
                        listener.onSwitch(v,state);
                    }
                }else {
                    state=0;
                    if (listener!=null){
                        listener.onSwitch(v,state);
                    }
                }
            }
        });
    }

    public interface OnSwitchListener {
        void onSwitch(View view,int state);//0,1
    }

    public void setState(boolean checked){
        state=checked?0:1;
        if (listener!=null){
            listener.onSwitch(this,state);
        }
    }
}
