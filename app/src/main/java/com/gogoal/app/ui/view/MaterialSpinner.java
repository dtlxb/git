package com.gogoal.app.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gogoal.app.R;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * author wangjd on 2017/2/16 0016.
 * Staff_id 1375
 * phone 18930640263
 */
public class MaterialSpinner extends TextView {
    private MaterialSpinner.OnNothingSelectedListener onNothingSelectedListener;
    private MaterialSpinner.OnItemSelectedListener onItemSelectedListener;
    private MaterialSpinnerBaseAdapter adapter;
    private PopupWindow popupWindow;
    private ListView listView;
    private Drawable arrowDrawable;
    private boolean hideArrow;
    private boolean nothingSelected;
    private int selectedIndex;
    private int backgroundColor;
    private int arrowColor;
    private int textColor;
    private int numberOfItems;

    @ColorInt
    private static int darker(@ColorInt int color, @FloatRange(
            from = 0.0D,
            to = 1.0D
    ) float factor) {
        return Color.argb(Color.alpha(color), Math.max((int)((float)Color.red(color) * factor), 0), Math.max((int)((float)Color.green(color) * factor), 0), Math.max((int)((float)Color.blue(color) * factor), 0));
    }

    public MaterialSpinner(Context context) {
        super(context);
        this.init(context, (AttributeSet)null);
    }

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public MaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner);
        int defaultColor = this.getTextColors().getDefaultColor();
        this.backgroundColor = typedArray.getColor(R.styleable.MaterialSpinner_ms_background_color, -1);
        this.textColor = typedArray.getColor(R.styleable.MaterialSpinner_ms_text_color, defaultColor);
        this.arrowColor = typedArray.getColor(R.styleable.MaterialSpinner_ms_arrow_tint, this.textColor);
        this.hideArrow = typedArray.getBoolean(R.styleable.MaterialSpinner_ms_hide_arrow, false);
        typedArray.recycle();
        this.setGravity(8388627);
        boolean rtl = false;
        if(Build.VERSION.SDK_INT >= 17) {
            Configuration resources = this.getResources().getConfiguration();
            rtl = resources.getLayoutDirection() == 1;
            if(rtl) {
                this.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                this.setTextDirection(View.TEXT_DIRECTION_RTL);
            }
        }

        Resources resources1 = this.getResources();
        int right;
        int bottom;
        int top;
        int left = right = bottom = top = resources1.getDimensionPixelSize(R.dimen.ms__padding_top);
        if(rtl) {
            right = resources1.getDimensionPixelSize(R.dimen.ms__padding_left);
        } else {
            left = resources1.getDimensionPixelSize(R.dimen.ms__padding_left);
        }

        this.setClickable(true);
        this.setPadding(left, top, right, bottom);
        this.setBackgroundResource(R.drawable.ms__selector);
        if(!this.hideArrow) {
            this.arrowDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context,R.drawable.ms__arrow));
            DrawableCompat.setTint(this.arrowDrawable, this.arrowColor);
            if(rtl) {
                this.setCompoundDrawablesWithIntrinsicBounds(this.arrowDrawable, (Drawable)null, (Drawable)null, (Drawable)null);
            } else {
                this.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.arrowDrawable, (Drawable)null);
            }
        }

        this.listView = new ListView(context);
        this.listView.setId(this.getId());
        this.listView.setDivider((Drawable)null);
        this.listView.setItemsCanFocus(true);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= MaterialSpinner.this.selectedIndex && position < MaterialSpinner.this.adapter.getCount()) {
                    ++position;
                }

                MaterialSpinner.this.selectedIndex = position;
                MaterialSpinner.this.nothingSelected = false;
                Object item = MaterialSpinner.this.adapter.get(position);
                MaterialSpinner.this.adapter.notifyItemSelected(position);
                MaterialSpinner.this.setText(item.toString());
                MaterialSpinner.this.collapse();
                if(MaterialSpinner.this.onItemSelectedListener != null) {
                    MaterialSpinner.this.onItemSelectedListener.onItemSelected(MaterialSpinner.this, position, id, item);
                }

            }
        });
        this.popupWindow = new PopupWindow(context);
        this.popupWindow.setContentView(this.listView);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setFocusable(true);
        if(Build.VERSION.SDK_INT >= 21) {
            this.popupWindow.setElevation(16.0F);
            this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.ms__drawable));
        } else {
            this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.ms__drop_down_shadow));
        }

        if(this.backgroundColor != -1) {
            this.setBackgroundColor(this.backgroundColor);
        }

        if(this.textColor != defaultColor) {
            this.setTextColor(this.textColor);
        }

        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if(MaterialSpinner.this.nothingSelected && MaterialSpinner.this.onNothingSelectedListener != null) {
                    MaterialSpinner.this.onNothingSelectedListener.onNothingSelected(MaterialSpinner.this);
                }

                if(!MaterialSpinner.this.hideArrow) {
                    MaterialSpinner.this.animateArrow(false);
                }

            }
        });
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.popupWindow.setWidth(MeasureSpec.getSize(widthMeasureSpec));
        this.popupWindow.setHeight(-2);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(event.getAction() == 1) {
            if(!this.popupWindow.isShowing()) {
                this.expand();
            } else {
                this.collapse();
            }
        }

        return super.onTouchEvent(event);
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        Drawable background = this.getBackground();
        if(background instanceof StateListDrawable) {
            try {
                Method e = StateListDrawable.class.getDeclaredMethod("getStateDrawable", new Class[]{Integer.TYPE});
                if(!e.isAccessible()) {
                    e.setAccessible(true);
                }

                int[] colors = new int[]{darker(color, 0.85F), color};

                for(int i = 0; i < colors.length; ++i) {
                    ColorDrawable drawable = (ColorDrawable)e.invoke(background, new Object[]{Integer.valueOf(i)});
                    drawable.setColor(colors[i]);
                }
            } catch (Exception var7) {
                Log.e("MaterialSpinner", "Error setting background color", var7);
            }
        } else if(background != null) {
            background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }

        this.popupWindow.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public void setTextColor(int color) {
        this.textColor = color;
        super.setTextColor(color);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", super.onSaveInstanceState());
        bundle.putInt("selected_index", this.selectedIndex);
        if(this.popupWindow != null) {
            bundle.putBoolean("is_popup_showing", this.popupWindow.isShowing());
            this.collapse();
        } else {
            bundle.putBoolean("is_popup_showing", false);
        }

        return bundle;
    }

    public void onRestoreInstanceState(Parcelable savedState) {
        if(savedState instanceof Bundle) {
            Bundle bundle = (Bundle)savedState;
            this.selectedIndex = bundle.getInt("selected_index");
            if(this.adapter != null) {
                this.setText(this.adapter.get(this.selectedIndex).toString());
                this.adapter.notifyItemSelected(this.selectedIndex);
            }

            if(bundle.getBoolean("is_popup_showing") && this.popupWindow != null) {
                this.post(new Runnable() {
                    public void run() {
                        MaterialSpinner.this.expand();
                    }
                });
            }

            savedState = bundle.getParcelable("state");
        }

        super.onRestoreInstanceState(savedState);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int position) {
        if(this.adapter != null) {
            if(position < 0 || position > this.adapter.getCount()) {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }

            this.adapter.notifyItemSelected(position);
            this.selectedIndex = position;
            this.setText(this.adapter.get(position).toString());
        }

    }

    public void setOnItemSelectedListener(@Nullable MaterialSpinner.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnNothingSelectedListener(@Nullable MaterialSpinner.OnNothingSelectedListener onNothingSelectedListener) {
        this.onNothingSelectedListener = onNothingSelectedListener;
    }

    public <T> void setItems(@NonNull List<T> items) {
        this.numberOfItems = items.size();
        this.adapter = (new MaterialSpinnerAdapter(this.getContext(), items)).setTextColor(this.textColor);
        this.setAdapterInternal(this.adapter);
    }

    public <T> void setItems(@NonNull T... items) {
        this.setItems(Arrays.asList(items));
    }

    public void setAdapter(@NonNull ListAdapter adapter) {
        this.adapter = new MaterialSpinnerAdapterWrapper(this.getContext(), adapter);
        this.setAdapterInternal(this.adapter);
    }

    private void setAdapterInternal(@NonNull MaterialSpinnerBaseAdapter adapter) {
        this.listView.setAdapter(adapter);
        if(this.selectedIndex >= this.numberOfItems) {
            this.selectedIndex = 0;
        }

        this.setText(adapter.get(this.selectedIndex).toString());
    }

    public void expand() {
        if(!this.hideArrow) {
            this.animateArrow(true);
        }

        this.nothingSelected = true;
        if(Build.VERSION.SDK_INT >= 23) {
            this.popupWindow.setOverlapAnchor(false);
            this.popupWindow.showAsDropDown(this);
        } else {
            int[] location = new int[2];
            this.getLocationOnScreen(location);
            int x = location[0];
            int y = this.getHeight() + location[1];
            this.popupWindow.showAtLocation(this, 8388659, x, y);
        }

    }

    public void collapse() {
        if(!this.hideArrow) {
            this.animateArrow(false);
        }

        this.popupWindow.dismiss();
    }

    public void setArrowColor(@ColorInt int color) {
        this.arrowColor = color;
        if(this.arrowDrawable != null) {
            DrawableCompat.setTint(this.arrowDrawable, this.arrowColor);
        }

    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp?0:10000;
        int end = shouldRotateUp?10000:0;
        ObjectAnimator animator = ObjectAnimator.ofInt(this.arrowDrawable, "level", new int[]{start, end});
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.start();
    }

    public interface OnNothingSelectedListener {
        void onNothingSelected(MaterialSpinner var1);
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(MaterialSpinner var1, int var2, long var3, T var5);
    }
}
