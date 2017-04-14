package cn.gogoal.im.common.drag;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.stock.MyStockData;

public class DragAdapter extends RecyclerView.Adapter<DragAdapter.MainContentViewHolder> {

    private Context context;

    /**
     * Item是否被选中监听
     */
    private OnCheckedChangeListener mOnCheckedChangeListener;

//    /**
//     * Item点击监听
//     */
//    private OnItemClickListener mItemOnClickListener;


    /**
     * 数据
     */
    private List<MyStockData> dataList = null;

    /**
     * Item拖拽滑动帮助
     */
    private ItemTouchHelper itemTouchHelper;

    public DragAdapter() {
    }

    public DragAdapter(Context context,List<MyStockData> dataList) {
        this.context=context;
        this.dataList = dataList;
    }

    public void notifyDataSetChanged(List<MyStockData> userInfos) {
        this.dataList = userInfos;
        super.notifyDataSetChanged();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    @Override
    public MainContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggable_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MainContentViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public MyStockData getData(int position) {
        return dataList.get(position);
    }

    public String getStockName(int position) {
        return dataList.get(position).getStock_name();
    }

    public String getStockCode(int position) {
        return dataList.get(position).getStock_code();
    }

    public MyStockData getStockData(int position) {
        return dataList.get(position);
    }

    public interface OnCheckedChangeListener {
        void onItemCheckedChange(CompoundButton view, int position, boolean checked);
    }

    class MainContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        private TextView mTvName;
        private ImageView mIvTouch;
        private AppCompatCheckBox mCbCheck;

        private MainContentViewHolder(View itemView) {
            super(itemView);
            mCbCheck = (AppCompatCheckBox) itemView.findViewById(R.id.checker);
            mTvName = (TextView) itemView.findViewById(R.id.tv_drag_stock_name);
            mIvTouch = (ImageView) itemView.findViewById(R.id.img_drag);

            mCbCheck.setOnClickListener(this);
            mIvTouch.setOnTouchListener(this);
        }

        public void setData() {
            MyStockData data = getData(getAdapterPosition());
            mTvName.setText(data.getStock_name());
            mCbCheck.setChecked(data.isCheck());
        }

        @Override
        public void onClick(View view) {
            if (view == mCbCheck && mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onItemCheckedChange(mCbCheck, getAdapterPosition(), mCbCheck.isChecked());
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view == mIvTouch)
                itemTouchHelper.startDrag(this);
            return false;
        }
    }

    /**
     * 获取数据集合
     */
    public List<MyStockData> getData() {
        return dataList;
    }

    /**
     * 删除指定数据条目
     */
    public void removeItem(MyStockData model) {
        removeItem(dataList.indexOf(model));
    }

    /**
     * 删除指定索引数据条目
     */
    public void removeItem(int position) {
        dataList.remove(position);
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
    public void addLastItem(MyStockData model) {
        addItem(dataList.size(), model);
    }

    /**
     * 在指定位置添加数据条目
     */
    public void addItem(int position, MyStockData model) {
        dataList.add(position, model);
        notifyItemInsertedWrapper(position);
    }
}
