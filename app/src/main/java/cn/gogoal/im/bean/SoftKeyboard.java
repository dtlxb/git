package cn.gogoal.im.bean;

import android.graphics.drawable.Drawable;

/**
 * author wangjd on 2017/6/9 0009.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SoftKeyboard {
    private String num;
    private Drawable drawable;

    public SoftKeyboard(String num, Drawable drawable) {
        this.num = num;
        this.drawable = drawable;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
