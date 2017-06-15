package cn.gogoal.im.ui.viewDot;

import android.support.annotation.Nullable;

import cn.gogoal.im.ui.viewDot.animation.AnimationManager;
import cn.gogoal.im.ui.viewDot.animation.controller.ValueController;
import cn.gogoal.im.ui.viewDot.draw.DrawManager;
import cn.gogoal.im.ui.viewDot.draw.data.Indicator;
import cn.gogoal.im.ui.viewDot.animation.data.Value;

public class IndicatorManager implements ValueController.UpdateListener {

    private DrawManager drawManager;
    private AnimationManager animationManager;
    private Listener listener;

    interface Listener {
        void onIndicatorUpdated();
    }

    IndicatorManager(@Nullable Listener listener) {
        this.listener = listener;
        this.drawManager = new DrawManager();
        this.animationManager = new AnimationManager(drawManager.indicator(), this);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator() {
        return drawManager.indicator();
    }

    public DrawManager drawer() {
        return drawManager;
    }

    @Override
    public void onValueUpdated(@Nullable Value value) {
        drawManager.updateValue(value);
        if (listener != null) {
            listener.onIndicatorUpdated();
        }
    }
}
