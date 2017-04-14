package cn.gogoal.im.adapter.baseAdapter;

import android.view.View;

/**
 * {@link SimpleClickListener}
 */
public abstract class OnItemClickListener extends SimpleClickListener {

    @Override
    public void onItemClick(CommonAdapter adapter, View view, int position) {
        onSimpleItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(CommonAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemClick(CommonAdapter adapter, View view, int position);
}
