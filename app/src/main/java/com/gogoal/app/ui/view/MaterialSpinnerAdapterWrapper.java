package com.gogoal.app.ui.view;

import android.content.Context;
import android.widget.ListAdapter;

/**
 * author wangjd on 2017/2/16 0016.
 * Staff_id 1375
 * phone 18930640263
 */
final class MaterialSpinnerAdapterWrapper extends MaterialSpinnerBaseAdapter {
    private final ListAdapter listAdapter;

    public MaterialSpinnerAdapterWrapper(Context context, ListAdapter toWrap) {
        super(context);
        this.listAdapter = toWrap;
    }

    public int getCount() {
        return this.listAdapter.getCount() - 1;
    }

    public Object getItem(int position) {
        return position >= this.getSelectedIndex()?this.listAdapter.getItem(position + 1):this.listAdapter.getItem(position);
    }

    public Object get(int position) {
        return this.listAdapter.getItem(position);
    }
}