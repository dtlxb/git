package com.gogoal.app.ui.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gogoal.app.R;

/**
 * author wangjd on 2017/2/16 0016.
 * Staff_id 1375
 * phone 18930640263
 */
public abstract class MaterialSpinnerBaseAdapter<T> extends BaseAdapter {
    private final Context context;
    private int selectedIndex;
    private int textColor;

    public MaterialSpinnerBaseAdapter(Context context) {
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.ms__list_item, parent, false);
            textView = (TextView)convertView.findViewById(R.id.tv_tinted_spinner);
            textView.setTextColor(this.textColor);
            if(Build.VERSION.SDK_INT >= 17) {
                Configuration config = this.context.getResources().getConfiguration();
                if(config.getLayoutDirection() == 1) {
                    textView.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }

            convertView.setTag(new MaterialSpinnerBaseAdapter.ViewHolder(textView));
        } else {
            textView = ((MaterialSpinnerBaseAdapter.ViewHolder)convertView.getTag()).textView;
        }

        textView.setText(this.getItem(position).toString());
        return convertView;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void notifyItemSelected(int index) {
        this.selectedIndex = index;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public abstract T getItem(int var1);

    public abstract int getCount();

    public abstract T get(int var1);

    protected MaterialSpinnerBaseAdapter<T> setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    private static class ViewHolder {
        private TextView textView;

        private ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
