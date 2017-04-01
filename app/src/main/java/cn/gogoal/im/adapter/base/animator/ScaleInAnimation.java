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
public class ScaleInAnimation implements BaseAnimation {

    private static final float DEFAULT_SCALE_FROM = .5f;
    private final float mFrom;

    public ScaleInAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new ObjectAnimator[] { scaleX, scaleY };
    }
}