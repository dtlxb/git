package cn.gogoal.im.adapter.baseAdapter;

import android.view.View;

/**
 * {@link SimpleClickListener}
 **/
public abstract class OnItemChildLongClickListener extends SimpleClickListener {

    @Override
    public void onItemClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(CommonAdapter adapter, View view, int position) {
        onSimpleItemChildLongClick(adapter,view,position);
    }
    public abstract void onSimpleItemChildLongClick(CommonAdapter adapter, View view, int position);
}
