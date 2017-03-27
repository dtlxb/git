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

    /**
     * 获取数据集合
     */
    public List<T> getData() {
        return mDatas;
    }

    /**
     * 删除指定数据条目
     */
    public void removeItem(T model) {
        removeItem(mDatas.indexOf(model));
    }

    /**
     * 删除指定索引数据条目
     */
    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoveWrapper(position);
    }

    /**
     * 数据移除刷新
     */
    public final void notifyItemRemoveWrapper(int position) {
        notifyItemRemoved(position);
    }

    /**
     * 数据添加刷新
     */
    public final void notifyItemInsertedWrapper(int position) {
        notifyItemInserted(position);
    }

    /**
     * 在集合末尾添加数据条目
     */
    public void addLastItem(T model) {
        addItem(mDatas.size(), model);
    }

    /**
     * 在指定位置添加数据条目
     */
    public void addItem(int position, T model) {
        mDatas.add(position, model);
        notifyItemInsertedWrapper(position);
    }


    protected abstract void convert(ViewHolder holder, T data, int position);


}
