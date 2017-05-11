package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.SectionAdapter;
import cn.gogoal.im.adapter.SelectedAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.OnItemDragListener;
import cn.gogoal.im.adapter.baseAdapter.callback.ItemDragAndSwipeCallback;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.SectionToolsData;
import cn.gogoal.im.bean.ToolBean;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.NoAlphaItemAnimator;

/**
 * author wangjd on 2017/5/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :小工具设置
 */
public class ToolsSettingActivity extends BaseActivity {
    @BindView(R.id.rv_selected)
    RecyclerView rvSelected;

    @BindView(R.id.rv_all)
    RecyclerView rvAll;

    @BindView(R.id.title_bar)
    XTitle xTitle;

    private SectionAdapter adapterAllData;
    private List<SectionToolsData> dataAll;

    private List<ToolData.Tool> dataSelected;
    private SelectedAdapter selectedAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_tools_setting;
    }

    @Override
    public void doBusiness(final Context mContext) {
        xTitle.setVisibility(View.VISIBLE);
        xTitle.setTitle(R.string.title_found)
                .setLeftText("\u3000取消")
                .setLeftTextColor(getResColor(R.color.colorPrimary))
                .setActionTextColor(getResColor(R.color.colorPrimary))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).addAction(new XTitle.TextAction("确定") {
            @Override
            public void actionClick(View view) {
                //TODO 完成
            }
        });

        dataSelected = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(mContext, dataSelected);
//        投研首页常用工具（按住拖动调整排序）
        ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(selectedAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rvSelected);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        selectedAdapter.disableSwipeItem();
        selectedAdapter.enableDragItem(mItemTouchHelper);
        GridLayoutManager selectdLayoutManager = new GridLayoutManager(mContext, AppDevice.isLowDpi() ? 3 : 4);
        rvSelected.setLayoutManager(selectdLayoutManager);
        rvSelected.setAdapter(selectedAdapter);
        rvSelected.setNestedScrollingEnabled(false);
        rvSelected.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(AppDevice.dp2px(mContext,5),AppDevice.dp2px(mContext,5),
                        AppDevice.dp2px(mContext,5),AppDevice.dp2px(mContext,5));
            }
        });

//        TextView headView = new TextView(mContext);
//        headView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
//        headView.setPadding(AppDevice.dp2px(mContext,15),AppDevice.dp2px(mContext,8),0,AppDevice.dp2px(mContext,8));
//        headView.setGravity(Gravity.CENTER_VERTICAL);

//        headView.setText("投研首页常用工具（按住拖动调整排序）");
//        selectedAdapter.addHeaderView(headView);

        rvSelected.setItemAnimator(new NoAlphaItemAnimator());

        selectedAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                KLog.e("move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                KLog.e("drag end");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                selectedAdapter.notifyDataSetChanged();
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        });

        rvSelected.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                KLog.e("height=" + v.getHeight() + ";size=" + rvSelected.getAdapter().getItemCount());
                if (rvSelected.getAdapter().getItemCount() > 2 * (AppDevice.isLowDpi() ? 3 : 4) &&
                        rvSelected.getAdapter().getItemCount() < 3 * (AppDevice.isLowDpi() ? 3 : 4)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, v.getHeight());
                    rvSelected.setLayoutParams(params);
                    rvSelected.removeOnLayoutChangeListener(this);
                }
            }
        });

        rvSelected.setNestedScrollingEnabled(false);
        rvSelected.setHasFixedSize(true);
        //=========================================================
        rvAll.setNestedScrollingEnabled(false);
        rvAll.setHasFixedSize(true);

        rvAll.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        dataAll = new ArrayList<>();
        adapterAllData = new SectionAdapter(ToolsSettingActivity.this, dataAll);
        rvAll.setAdapter(adapterAllData);
        getTouYan();
    }

    public void getTouYan() {
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());

        new GGOKHTTP(map, GGOKHTTP.GET_USERCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                FileUtil.writeRequestResponse(responseInfo, "投研工具");
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    List<ToolData> toolDatas = JSONObject.parseObject(responseInfo, ToolBean.class).getData();
                    for (int i = 0; i < toolDatas.size(); i++) {
                        ToolData toolData = toolDatas.get(i);
                        dataAll.add(new SectionToolsData(true, toolData.getTitle()));
                        List<ToolData.Tool> itemList = toolData.getDatas();
                        for (ToolData.Tool item : itemList) {
                            dataAll.add(new SectionToolsData(item, false));
                            if (item.getIsShow() == 1) {
                                dataSelected.add(item);
                            }
                        }

                        //模拟填充空数据
                        creatSpaceItem(itemList);
                    }
                    adapterAllData.notifyDataSetChanged();
                    selectedAdapter.notifyDataSetChanged();
                } else if (code == 1001) {

                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
            }
        }).startGet();

    }

    private void creatSpaceItem(List<ToolData.Tool> itemList) {
        ToolData.Tool tool = itemList.get(0).clone();
        if (AppDevice.isLowDpi()) {
            switch (itemList.size() % 3) {
                case 1:
                    dataAll.add(new SectionToolsData(tool, true));
                    dataAll.add(new SectionToolsData(tool, true));
                    break;
                case 2:
                    dataAll.add(new SectionToolsData(tool, true));
                    break;
            }
        } else {
            switch (itemList.size() % 4) {
                case 1:
                    dataAll.add(new SectionToolsData(tool, true));
                    dataAll.add(new SectionToolsData(tool, true));
                    dataAll.add(new SectionToolsData(tool, true));
                    break;
                case 2:
                    dataAll.add(new SectionToolsData(tool, true));
                    dataAll.add(new SectionToolsData(tool, true));
                    break;
                case 3:
                    dataAll.add(new SectionToolsData(tool, true));
                    break;
            }
        }
    }

    /**
     * 增加
     */
    public void addSelected(ToolData.Tool tool) {
        dataSelected.add(tool);
//        selectedAdapter.notifyItemInserted(dataSelected.size());
        rvSelected.smoothScrollToPosition(dataSelected.size() - 1);
        selectedAdapter.notifyDataSetChanged();
    }
}
