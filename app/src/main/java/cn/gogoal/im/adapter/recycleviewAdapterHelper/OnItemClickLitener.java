package cn.gogoal.im.adapter.recycleviewAdapterHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author wangjd on 2017/3/20 0020.
 * Staff_id 1375
 * phone 18930640263
 * description :RecycleView适配器的item短按和长按调用的接口.
 */
public interface OnItemClickLitener {

    void onItemClick(RecyclerView.ViewHolder holder,View view, int position);

    boolean onItemLongClick(RecyclerView.ViewHolder holder,View view, int position);
}
