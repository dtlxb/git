package cn.gogoal.im.adapter.base.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class AlphaInAnimation implements BaseAnimation {

    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;

    public AlphaInAnimation() {
        this(DEFAULT_ALPHA_FROM);
    }

    public AlphaInAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
    }
}
