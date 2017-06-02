package cn.gogoal.im.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * Author wangjd on 2017/6/2 0002.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==描述==
 */
public class ViewBanner extends ViewFlipper {
    //interval 必须大于 animDuration，否则视图将会重叠
    private int interval = 2500;//Item翻页时间间隔
    private int animDuration = 500;//Item动画执行时间
    private Animation animIn, animOut;//进出动画

    public ViewBanner(Context context) {
        this(context, null);
    }

    public ViewBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        setFlipInterval(interval);
//        animIn = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_in);
//        animIn.setDuration(animDuration);
        setInAnimation(animIn);
//        animOut = AnimationUtils.loadAnimation(getContext(), R.anim.top_out);
//        animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    public <E extends View,T> void setMarqueeFactory(ViewBannerFactory<E, T> factory) {
        factory.setAttachedToMarqueeView(this);
        removeAllViews();
        List<E> mViews = factory.getMarqueeViews();
        if (mViews != null) {
            for (int i = 0; i < mViews.size(); i++) {
                addView(mViews.get(i));
            }
        }
    }

    public void setInterval(int interval) {
        this.interval = interval;
        setFlipInterval(interval);
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
        animIn.setDuration(animDuration);
        setInAnimation(animIn);
        animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    public void setAnimInAndOut(Animation animIn, Animation animOut) {
        this.animIn = animIn;
        this.animOut = animOut;
        setInAnimation(animIn);
        setOutAnimation(animOut);
    }

    public void setAnimInAndOut(int animInId, int animOutID) {
        animIn = AnimationUtils.loadAnimation(getContext(), animInId);
        animOut = AnimationUtils.loadAnimation(getContext(), animOutID);
        animIn.setDuration(animDuration);
        animOut.setDuration(animDuration);
        setInAnimation(animIn);
        setOutAnimation(animOut);

    }

    public Animation getAnimIn() {
        return animIn;
    }

    public Animation getAnimOut() {
        return animOut;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopFlipping();
    }
}
