package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.drag.DefaultItemTouchHelpCallback;
import cn.gogoal.im.common.drag.DefaultItemTouchHelper;
import cn.gogoal.im.common.drag.DragAdapter;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :自选股排序
 */
public class EditMyStockActivity extends BaseActivity {

    @BindView(R.id.rv_edit_drag)
    RecyclerView rvEditDrag;

    @BindView(R.id.tv_drag_selector_all)
    TextView tvDragSelectorAll;

    @BindView(R.id.tv_drag_delete)
    TextView tvDragDelete;

    private SelectedListener listener;

    public void setOnSelectedListener(SelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 数据源
     */
    private ArrayList<MyStockData> myStockList = null;

    private ArrayList<MyStockData> myStockListNormal = null;

    /*
     * 选中结果
     */
//    private List<MyStockData> result = new ArrayList<>();

//    private List<String> resultFullCode = new ArrayList<>();
    /**
     * 数据适配器
     */
    private DragAdapter dragAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_mystock;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("自选股", true).addAction(new XTitle.TextAction(getString(R.string.complete)) {
            @Override
            public void actionClick(View view) {
                finish();
            }
        });

        BaseActivity.initRecycleView(rvEditDrag, 0);

        myStockList = (ArrayList<MyStockData>) getIntent().getSerializableExtra("my_stock_list");

        setData();

        dragAdapter = new DragAdapter(mContext, myStockList);
        dragAdapter.setOnItemCheckedChangeListener(onCheckedChangeListener);
        rvEditDrag.setAdapter(dragAdapter);

        DefaultItemTouchHelper itemTouchHelper = DefaultItemTouchHelper.init(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(rvEditDrag);
        dragAdapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);

        //选中数据集
        final LinkedHashSet<MyStockData> result = new LinkedHashSet<>();
        //选中数据集FullCode
        final List<String> fullCodeDatas = new ArrayList<>();

        this.setOnSelectedListener(new SelectedListener() {
            @Override
            public void add(MyStockData data) {
                result.add(data);
                KLog.e("添加：" + data.getStock_name());
                notifCountText(result.size());
            }

            @Override
            public void remove(MyStockData data) {
                result.remove(data);
                KLog.d("移除：" + data.getStock_name());
                notifCountText(result.size());
            }
        });

        //全选按钮
        tvDragSelectorAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectedAll(myStockList)) {
                    for (MyStockData da : myStockList) {
                        da.setCheck(false);
                        if (listener != null) {
                            listener.remove(da);
                        }
                    }
                } else {
                    for (MyStockData da : myStockList) {
                        da.setCheck(true);
                        if (listener != null) {
                            listener.add(da);
                        }
                    }
                }
                dragAdapter.notifyDataSetChanged();
            }

        });

        tvDragDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KLog.e(isSelectedAll(myStockList));

                if (isSelectedAll(myStockList)) {
                    HashMap<String, String> params = UserUtils.getTokenParams();
                    params.put("clear", "1");

                    new GGOKHTTP(params, GGOKHTTP.RESET_MYSTOCKS, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            KLog.e(responseInfo);
                            myStockList.clear();
                            StockUtils.clearLocalMyStock();
                            dragAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String msg) {
                            KLog.e(msg);
                        }
                    }).startGet();
                } else {
                    for (MyStockData data : result) {
                        fullCodeDatas.add(data.getSource() + data.getStock_code());
                        dragAdapter.removeItem(data);
                    }
                    result.clear();
                    notifCountText(0);
                    StockUtils.deleteMyStock(getActivity(),
                            ArrayUtils.mosaicListElement(fullCodeDatas), null);
                }
            }
        });

    }

    private void setData() {
        if (myStockListNormal == null) {
            myStockListNormal = new ArrayList<>();
        }
        myStockListNormal.clear();
        myStockListNormal.addAll(myStockList);
    }

    /**
     * 是否全选
     */
    private boolean isSelectedAll(List<MyStockData> myStockList) {
        for (MyStockData da : myStockList) {
            if (!da.isCheck()) {
                return false;
            }
        }
        return true;
    }

    private void notifCountText(int selectedCount) {
        if (selectedCount == myStockList.size()) {
            tvDragSelectorAll.setText("取消全选");
        } else {
            tvDragSelectorAll.setText("全选");
        }
        if (selectedCount > 0) {
            tvDragDelete.setText("删除(" + selectedCount + ")");
            tvDragDelete.setTextColor(getResColor(R.color.colorAccent));
        } else {
            tvDragDelete.setText("删除");
            tvDragDelete.setTextColor(getResColor(R.color.textColor_333333));
        }
    }

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener =
            new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
                @Override
                public void onSwiped(int adapterPosition) {
                    if (myStockList != null) {
                        myStockList.remove(adapterPosition);
                        dragAdapter.notifyItemRemoved(adapterPosition);
                    }
                }

                @Override
                public boolean onMove(int srcPosition, int targetPosition) {
                    if (myStockList != null) {
                        // 更换数据源中的数据Item的位置
                        Collections.swap(myStockList, srcPosition, targetPosition);
                        // 更新UI中的Item的位置，主要是给用户看到交互效果
                        dragAdapter.notifyItemMoved(srcPosition, targetPosition);

                        return true;
                    }
                    return false;
                }

                @Override
                public void onSelectedChanged(int fromPosition, int toPosition) {
                    KLog.e("fromPosition=" + fromPosition + ";toPosition=" + toPosition);
                    KLog.e("fromIndex=" + myStockListNormal.get(fromPosition).getStock_sort() +
                            ";toIndex=" + myStockListNormal.get(toPosition).getStock_sort());

                    HashMap<String, String> tokenParams = UserUtils.getTokenParams();
                    tokenParams.put("fromIndex", String.valueOf(myStockListNormal.get(fromPosition).getStock_sort()));
                    tokenParams.put("toIndex", String.valueOf(myStockListNormal.get(toPosition).getStock_sort()));

                    new GGOKHTTP(tokenParams, GGOKHTTP.STOCK_INDEX_SORT, new GGOKHTTP.GGHttpInterface() {
                        @Override
                        public void onSuccess(String responseInfo) {
                            KLog.e(responseInfo);
                            dragAdapter.notifyDataSetChanged();
                            setData();
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    }).startGet();
                }
            };


    private DragAdapter.OnCheckedChangeListener onCheckedChangeListener = new DragAdapter.OnCheckedChangeListener() {
        @Override
        public void onItemCheckedChange(CompoundButton view, int position, boolean checked) {
            if (checked) {
                if (listener != null) {
                    listener.add(dragAdapter.getStockData(position));
                }
            } else {
                if (listener != null) {
                    listener.remove(dragAdapter.getStockData(position));
                }
            }
        }
    };

    private interface SelectedListener {
        void add(MyStockData data);

        void remove(MyStockData data);
    }

    class SrockAsync extends AsyncTask<MyStockData, Void, Integer> {

        @Override
        protected Integer doInBackground(MyStockData... params) {
            for (MyStockData data : params) {
                KLog.e(data.getStock_name() + ";;" + data.getStock_sort());
            }
            return null;
        }
    }
}
