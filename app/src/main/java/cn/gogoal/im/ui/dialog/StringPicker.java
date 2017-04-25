package cn.gogoal.im.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/4/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :滚轮,两级联动底部弹窗
 */
public class StringPicker extends NumberPicker {
    public StringPicker(Context context) {
        super(context);
        setNumberPickerDividerColor(this);
    }

    public StringPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNumberPickerDividerColor(this);
    }

    public StringPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNumberPickerDividerColor(this);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            //这里修改字体的属性
            ((EditText) view).setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        }
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            switch (pf.getName()) {
                case "mSelectionDivider":
                    pf.setAccessible(true);
                    try {
                        //设置分割线的颜色值
                        pf.set(numberPicker, new ColorDrawable(0xffe0e0e0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "mSelectionDividerHeight":
                    pf.setAccessible(true);
                    try {
                        //设置分割线的高度
                        pf.set(numberPicker, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
//            if (pf.getName().equals("mSelectionDivider")) {
//                pf.setAccessible(true);
//                try {
//                    //设置分割线的颜色值
//                    pf.set(numberPicker, new ColorDrawable(0xff999999));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            if (pf.getName().equals("mSelectionDividerHeight")){
//                pf.setAccessible(true);
//                try {
//                    //设置分割线的颜色值
//                    pf.set(numberPicker, new ColorDrawable(0xff999999));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
        }
    }
}
