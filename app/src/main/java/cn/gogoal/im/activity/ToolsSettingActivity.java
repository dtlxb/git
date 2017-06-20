package cn.gogoal.im.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.XDividerItemDecoration;
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

    @BindView(R.id.tv_touyan_setting_tips)
    TextView tvTips;

    private SectionAdapter adapterAllData;

    private List<SectionToolsData> dataAll;
    private List<ToolData.Tool> dataOriginal = new ArrayList<>();

    private List<ToolData.Tool> dataSelected;
    private SelectedAdapter selectedAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_tools_setting;
    }

    @Override
    public void doBusiness(final Context mContext) {
        SpannableString spannableString = new SpannableString("我的工具(按住拖动调整排序)");
        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.textColor_333333)), 0, 8,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResColor(R.color.textColor_999999)), 8,
                spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tvTips.setText(spannableString);


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
                setChoose();
            }
        });

        dataSelected = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(rvSelected, dataSelected);

        List<ToolData.Tool> intentDatas = getIntent().getParcelableArrayListExtra("selected_tools");
        if (intentDatas != null && !intentDatas.isEmpty()) {
            dataSelected.addAll(intentDatas);
            selectedAdapter.notifyDataSetChanged();
        }

//        投研首页常用工具（按住拖动调整排序）
        ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(selectedAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rvSelected);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        selectedAdapter.disableSwipeItem();
        selectedAdapter.enableDragItem(mItemTouchHelper);
        rvSelected.setLayoutManager(new GridLayoutManager(mContext, AppDevice.isLowDpi() ? 3 : 4));
        rvSelected.setAdapter(selectedAdapter);

        rvSelected.addItemDecoration(new SelectedDivider());

        rvSelected.setItemAnimator(new NoAlphaItemAnimator());

        rvSelected.setPadding(AppDevice.dp2px(mContext,4),0,AppDevice.dp2px(mContext,4),0);

        selectedAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                viewHolder.itemView.setBackgroundColor(getResColor(R.color.textColor_999999));

                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                holder.setTextColor(R.id.tv_touyan_item_text, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
//                KLog.e("move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                viewHolder.itemView.setBackgroundResource(R.drawable.shape_line_bottom_right);

                holder.setTextResColor(R.id.tv_touyan_item_text, R.color.textColor_666666);

                selectedAdapter.notifyDataSetChanged();
            }
        });

        //=========================================================

        rvAll.setLayoutManager(new StaggeredGridLayoutManager(AppDevice.isLowDpi() ? 3 : 4,
                StaggeredGridLayoutManager.VERTICAL));
        dataAll = new ArrayList<>();
        adapterAllData = new SectionAdapter(ToolsSettingActivity.this, dataAll);
        rvAll.setAdapter(adapterAllData);
        getTouYan();
    }

    private void setChoose() {

        String builderShow = "";
        for (ToolData.Tool tool : dataSelected) {
            builderShow += tool.getId() + ";";
        }

        String builderHide = getHideIdString();

        Map<String, String> map = new HashMap<>();
        map.put("showid", builderShow.length() > 0 ? (builderShow.substring(0, builderShow.length() - 1)) : "");
        map.put("nshowid", builderHide.length() > 0 ? (builderHide.substring(0, builderHide.length() - 1)) : "");
        map.put("token", UserUtils.getToken());
        new GGOKHTTP(map, GGOKHTTP.GET_EDITE_USERCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getBooleanValue("success")) {
                        finish();
                    } else {
                        UIHelper.toast(ToolsSettingActivity.this, data.getString("msg"));
                    }
                } else {
                    UIHelper.toastResponseError(ToolsSettingActivity.this, responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    /**
     * ☆☆☆
     */
    private String getHideIdString() {

        String builderHide = "";

        List<ToolData.Tool> result = new ArrayList<>();
        result.clear();
        result.addAll(dataOriginal);
        result.removeAll(dataSelected);

        if (result.isEmpty()) {
            return "";
        } else {
            for (ToolData.Tool tool : result) {
                builderHide += tool.getId() + ";";
            }
        }

        return builderHide;
    }

    public void getTouYan() {
        Map<String, String> map = new HashMap<>();
        map.put("token", UserUtils.getToken());

        new GGOKHTTP(map, GGOKHTTP.GET_USERCOLUMN, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    List<ToolData> toolDatas = JSONObject.parseObject(responseInfo, ToolBean.class).getData();
                    for (int i = 0; i < toolDatas.size(); i++) {
                        ToolData toolData = toolDatas.get(i);
                        dataAll.add(new SectionToolsData(true, toolData.getTitle()));
                        List<ToolData.Tool> itemList = toolData.getDatas();
                        for (ToolData.Tool item : itemList) {
                            item.setSimulatedArg(false);
                            dataOriginal.add(item);
                            dataAll.add(new SectionToolsData(item, itemList.size()));
                        }

                        addSpace(itemList);
                    }

                    tvTips.setVisibility(dataSelected.size() == 0 ? View.GONE : View.VISIBLE);
                    adapterAllData.notifyDataSetChanged();

                } else if (code == 1001) {
                    UIHelper.toast(ToolsSettingActivity.this, "你没有符合权限的工具可以使用", Toast.LENGTH_LONG);
                } else {
                    UIHelper.toast(ToolsSettingActivity.this, "请求出错", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(ToolsSettingActivity.this, msg);
            }
        }).startGet();

    }

    private void addSpace(List<ToolData.Tool> itemList) {
        //模拟填充空数据
        ToolData.Tool clone = itemList.get(0).clone();
        clone.setSimulatedArg(true);
        clone.setSimulatedArg(true);
        SectionToolsData spaceItemData = new SectionToolsData(clone, 0);
        if (AppDevice.isLowDpi()) {
            switch (itemList.size() % 3) {
                case 0:
                    //不添加
                    break;
                case 1:
                    dataAll.add(spaceItemData);
                    dataAll.add(spaceItemData);
                    break;
                case 2:
                    dataAll.add(spaceItemData);
                    break;
            }
        } else {
            switch (itemList.size() % 4) {
                case 0:
                    //不添加
                    break;
                case 1:
                    dataAll.add(spaceItemData);
                    dataAll.add(spaceItemData);
                    dataAll.add(spaceItemData);
                    break;
                case 2:
                    dataAll.add(spaceItemData);
                    dataAll.add(spaceItemData);
                    break;
                case 3:
                    dataAll.add(spaceItemData);
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

        for (SectionToolsData d : dataAll) {
            if (d.t == tool) {
                d.t.setIsShow(1);
                if (!rvAll.isComputingLayout()) {
                    adapterAllData.notifyItemChanged(dataAll.indexOf(d));
                } else {
                    KLog.e("操作出错");
                }
//                adapterAllData.notifyDataSetChanged();
                break;
            }
        }

        tvTips.setVisibility(View.VISIBLE);

        rvSelected.setVisibility(View.VISIBLE);
    }

    /**
     * 删减
     */
    public void remooveSelected(ToolData.Tool tool) {

        for (SectionToolsData d : dataAll) {
            if (!d.isHeader && d.t.getId() == tool.getId()) {
                d.t.setIsShow(0);
                adapterAllData.notifyDataSetChanged();
                break;
            }
        }

        dataSelected.remove(tool);

        selectedAdapter.notifyDataSetChanged();

        tvTips.setVisibility(dataSelected.size() > 0 ? View.VISIBLE : View.GONE);

        rvSelected.setVisibility(dataSelected.size() > 0 ? View.VISIBLE : View.GONE);

    }

    private class SelectedDivider extends XDividerItemDecoration {

        private boolean isLowDpi;

        private SelectedDivider() {
            super(ToolsSettingActivity.this,
                    4,
                    Color.WHITE);
            isLowDpi=AppDevice.isLowDpi();
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] show = new boolean[4];
            show[2] = (itemPosition + 1) % (isLowDpi ? 3 : 4) != 0;
            show[3] = true;
            return show;
        }
    }

}
