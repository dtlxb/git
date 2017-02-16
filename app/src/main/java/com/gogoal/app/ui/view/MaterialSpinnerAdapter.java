package com.gogoal.app.ui.view;

import android.content.Context;

import java.util.List;

/**
 * author wangjd on 2017/2/16 0016.
 * Staff_id 1375
 * phone 18930640263
 */
public class MaterialSpinnerAdapter<T> extends MaterialSpinnerBaseAdapter {
    private final List<T> items;

    public MaterialSpinnerAdapter(Context context, List<T> items) {
        super(context);
        this.items = items;
    }

    public int getCount() {
        return this.items.size() - 1;
    }

    public T getItem(int position) {
        return position >= this.getSelectedIndex()?this.items.get(position + 1):this.items.get(position);
    }

    public T get(int position) {
        return this.items.get(position);
    }
}
