package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.common.drag.DefaultItemTouchHelpCallback;
import cn.gogoal.im.common.drag.DefaultItemTouchHelper;
import cn.gogoal.im.common.drag.DragAdapter;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class EditMyStockActivity extends BaseActivity {

    @BindView(R.id.rv_edit_drag)
    RecyclerView rvEditDrag;

    @BindView(R.id.tv_drag_selector_all)
    TextView tvDragSelectorAll;

    @BindView(R.id.tv_drag_delete)
    TextView tvDragDelete;

    /**
     * 数据源
     */
    private List<MyStockData> myStockList = new ArrayList<>();

    /*
     * 结果
     */
    private List<MyStockData> result = new ArrayList<>();

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
                List<String> code = new ArrayList<>();
                List<MyStockData> resultDatas = dragAdapter.getData();
                for (MyStockData data : resultDatas) {
                    code.add(data.getStock_code());
                }
                KLog.e("剩下："+code.toString());
            }
        });

        BaseActivity.initRecycleView(rvEditDrag,0);

        myStockList.addAll((ArrayList<MyStockData>) getIntent().getSerializableExtra("my_stock_edit"));

        dragAdapter = new DragAdapter(mContext, myStockList);
        dragAdapter.setOnCheckedChangeListener(onCheckedChangeListener);
        rvEditDrag.setAdapter(dragAdapter);

        // 把ItemTouchHelper和itemTouchHelper绑定
        /*
      滑动拖拽的帮助类
     */
        DefaultItemTouchHelper itemTouchHelper = DefaultItemTouchHelper.init(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(rvEditDrag);

        dragAdapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);

        tvDragSelectorAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < myStockList.size(); i++) {
                    if (myStockList.get(i).isCheck()) {
                        myStockList.get(i).setCheck(false);
                        result.remove(dragAdapter.getStockData(i));

                    } else {
                        myStockList.get(i).setCheck(true);
                        result.add(dragAdapter.getData(i));
                    }
                    dragAdapter.notifyItemChanged(i);
                    notifCountText(result.size());
                }
            }
        });

        tvDragDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MyStockData data : result) {
                    dragAdapter.removeItem(data);
                }
                dragAdapter.notifyDataSetChanged();
                result.clear();
                notifCountText(0);
            }
        });
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
            };


    private DragAdapter.OnCheckedChangeListener onCheckedChangeListener = new DragAdapter.OnCheckedChangeListener() {
        @Override
        public void onItemCheckedChange(CompoundButton view, int position, boolean checked) {
//            UIHelper.toast(getActivity(), dragAdapter.getName(position) + (checked ? "选中" : "被反选"));
            if (checked) {
                result.add(dragAdapter.getStockData(position));
            } else {
                result.remove(dragAdapter.getStockData(position));
            }
            notifCountText(result.size());
        }
    };

}
