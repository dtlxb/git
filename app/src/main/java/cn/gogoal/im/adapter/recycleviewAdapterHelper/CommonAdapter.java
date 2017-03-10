package cn.gogoal.im.adapter.recycleviewAdapterHelper;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ItemViewDelegate;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T data, int position) {
                CommonAdapter.this.convert(holder, data, position);
            }
        });
    }

    public Context getmContext() {
        return mContext;
    }

    protected abstract void convert(ViewHolder holder, T data, int position);


}
