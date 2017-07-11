package cn.gogoal.im.common.drag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hply.imagepicker.view.SuperCheckBox;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

public class DragAdapter extends RecyclerView.Adapter<DragAdapter.MainContentViewHolder> {

    private Context context;

    /**
     * Item是否被选中监听
     */
    private OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * 数据
     */
    private List<MyStockData> dataList;

    /**
     * Item拖拽滑动帮助
     */
    private ItemTouchHelper itemTouchHelper;

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

    public void setOnItemCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    @Override
    public MainContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggable_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final MainContentViewHolder holder, final int position) {
        holder.setData();
        holder.mIv2Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStockData remove = dataList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                dataList.add(0,remove);
                notifyDataSetChanged();

                zhiDing(dataList.get(position).getStock_sort());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
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

    private void zhiDing(int stockSort){
        HashMap<String, String> tokenParams = UserUtils.getTokenParams();
        tokenParams.put("fromIndex", String.valueOf(stockSort));
        tokenParams.put("toIndex", String.valueOf(dataList.get(0).getStock_sort()));

        cn.gogoal.im.common.StringUtils.map2ggParameter(tokenParams);

        new GGOKHTTP(tokenParams, GGOKHTTP.STOCK_INDEX_SORT, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                DragAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    class MainContentViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvName;
        private ImageView mIvTouch;
        private ImageView mIv2Top;
        private SuperCheckBox mCbCheck;

        private MainContentViewHolder(View itemView) {
            super(itemView);
            mCbCheck = (SuperCheckBox) itemView.findViewById(R.id.checker);
            mTvName = (TextView) itemView.findViewById(R.id.tv_drag_stock_name);
            mIvTouch = (ImageView) itemView.findViewById(R.id.img_drag);
            mIv2Top = (ImageView) itemView.findViewById(R.id.img_2_top);

            mIvTouch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        itemTouchHelper.startDrag(MainContentViewHolder.this);
                    return false;
                }
            });

            mCbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mOnCheckedChangeListener != null){
                        mOnCheckedChangeListener.onItemCheckedChange(
                                mCbCheck,
                                getAdapterPosition(),
                                isChecked);
                        getData(getAdapterPosition()).setCheck(mCbCheck.isChecked());
                    }
                }
            });

            mTvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCbCheck.performClick();
                }
            });
        }

        public void setData() {
            MyStockData data = getData(getAdapterPosition());
            mTvName.setText(data.getStock_name());
            mCbCheck.setChecked(data.isCheck());
        }

    }

    public MyStockData getData(int position) {
        return dataList.get(position);
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
