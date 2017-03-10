package cn.gogoal.im.common;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class AnimationUtils {

    private static final AnimationUtils ourInstance = new AnimationUtils();

    public static AnimationUtils getInstance() {
        return ourInstance;
    }

    private AnimationUtils() {
    }

    //缩放动画
    public void scaleBigAndSmall(final View loveBtn, final int replace) {
        Animation anim1 = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(200);
        loveBtn.startAnimation(anim1);

        Animation anim2 = new ScaleAnimation(0.6f, 1f, 0.6f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(200);

        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (replace != -1)
                    loveBtn.setBackgroundResource(replace);
            }
            public void onAnimationEnd(Animation animation) {
            }
            public void onAnimationRepeat(Animation animation) {
            }
        });
        loveBtn.startAnimation(anim2);
    }


    public RotateAnimation setLoadingAnime(ImageView refreshBtn, @DrawableRes int rotateImage) {
        refreshBtn.setImageResource(rotateImage);
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(true);
        animation.setStartOffset(0);
        animation.setInterpolator(new LinearInterpolator());
        animation.startNow();
        refreshBtn.setAnimation(animation);
        return animation;
    }

    public void cancleLoadingAnime(Animation animation,ImageView refreshView, @DrawableRes int placeholderImage) {
        if (animation != null) {
            animation.cancel();
            refreshView.setImageResource(placeholderImage);
            refreshView.clearAnimation();
        }
    }
}
