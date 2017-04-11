package cn.gogoal.im.adapter.baseAdapter.listener;

import android.view.View;

import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;


/**
 * Created by AllenCoder on 2016/8/03.
 *
 *
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 */
public interface OnItemClickListener{
    void onItemClick(CommonAdapter adapter, View view, int position);
}
