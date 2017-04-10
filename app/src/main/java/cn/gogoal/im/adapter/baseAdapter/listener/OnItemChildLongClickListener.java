package cn.gogoal.im.adapter.baseAdapter.listener;

import android.view.View;

import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;


/**
 * Created by AllenCoder on 2016/8/03.
 * A convenience class to extend when you only want to OnItemChildLongClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
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
