package cn.gogoal.im.adapter.baseAdapter.listener;

import android.view.View;

import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;


/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {




    @Override
    public void onItemClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(CommonAdapter adapter, View view, int position) {
        onSimpleItemLongClick( adapter,  view,  position);
    }

    @Override
    public void onItemChildClick(CommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(CommonAdapter adapter, View view, int position) {
    }
    public abstract void onSimpleItemLongClick(CommonAdapter adapter, View view, int position);
}
