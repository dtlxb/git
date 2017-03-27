package com.hply.imagepicker.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;

import com.hply.imagepicker.R;

public class SuperCheckBox extends AppCompatCheckBox {

    public SuperCheckBox(Context context) {
        this(context,null,0);
    }

    public SuperCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, android.R.attr.checkboxStyle);
        init(context);
    }

    private void init(Context context) {
        this.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.selector_item_checked));
    }

    @Override
    public boolean performClick() {
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }
}
