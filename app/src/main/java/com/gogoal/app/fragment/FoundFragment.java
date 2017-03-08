package com.gogoal.app.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gogoal.app.R;
import com.gogoal.app.activity.ChatRoomActivity;
import com.gogoal.app.activity.FunctionActivity;
import com.gogoal.app.activity.IMRegisterActivity;
import com.gogoal.app.adapter.recycleviewAdapterHelper.CommonAdapter;
import com.gogoal.app.adapter.recycleviewAdapterHelper.base.ViewHolder;
import com.gogoal.app.adapter.recycleviewAdapterHelper.wrapper.HeaderAndFooterWrapper;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.bean.FoundData;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.ImageUtils.ImageDisplay;
import com.gogoal.app.common.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * 应用
 */
public class FoundFragment extends BaseFragment {

    @BindView(R.id.rv_found)
    RecyclerView recyclerView;

    @BindArray(R.array.found_fun0)
    String[] functionArr0;

    @BindArray(R.array.found_fun1)
    String[] functionArr1;

    public FoundFragment() {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_found;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle(R.string.title_found);

        initRecycleView(recyclerView, R.drawable.shape_divider_10dp);

        List<FoundData> datas = getFunctionDatas();//模拟数据

//      KLog.e(JSONObject.toJSON(datas).toString());

        HeaderAndFooterWrapper wraper = addHeadImage(datas);

        recyclerView.setAdapter(wraper);
    }

    @NonNull
    private HeaderAndFooterWrapper addHeadImage(List<FoundData> datas) {
        HeaderAndFooterWrapper wraper = new HeaderAndFooterWrapper(new MainAdapter(getContext(), R.layout.item_foundfragment, datas));
        ImageView imageHead = new ImageView(getContext());
        imageHead.setAdjustViewBounds(true);

        LinearLayout.LayoutParams headParams = new LinearLayout.LayoutParams(AppDevice.getWidth(getContext()),
                AppDevice.getWidth(getContext()) * 120 / 375);

        imageHead.setLayoutParams(headParams);
        imageHead.setImageResource(R.mipmap.image_found_top_ad);
        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(getContext(), "单页广告图");
            }
        });
        wraper.addHeaderView(imageHead);
        return wraper;
    }

    /**
     * 模拟数据，后续可以简单修改变成服务端请求数据
     */
    private List<FoundData> getFunctionDatas() {
        String[] titles = getResources().getStringArray(R.array.found_title);

        int[][] iconArray = {{R.mipmap.function_icon_onlive, R.mipmap.function_icon_report,
                R.mipmap.function_icon_school, R.mipmap.function_icon_1q},
                {R.mipmap.function_icon_improtant_report, R.mipmap.function_icon_title, R.mipmap.function_icon_news}};

        String[][] iconDescription = {functionArr0, functionArr1};

        List<FoundData> datas = new ArrayList<>();

        for (int i = 0; i < iconArray.length; i++) {

            List<FoundData.ItemPojos> itemPojoses = new ArrayList<>();

            for (int j = 0; j < iconArray[i].length; j++) {
                itemPojoses.add(new FoundData.ItemPojos(iconDescription[i][j], iconArray[i][j], "https://m.baidu.com"));
            }

            datas.add(new FoundData(itemPojoses, titles[i]));
        }

        return datas;
    }

    class MainAdapter extends CommonAdapter<FoundData> {

        private MainAdapter(Context context, int layoutId, List<FoundData> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, FoundData foundData, int position) {
            holder.setText(R.id.tv_title, foundData.getTitle());

            List<FoundData.ItemPojos> itemPojos = foundData.getItemPojos();
            if (itemPojos != null) {
                if (itemPojos.size() % 3 == 2) {
                    FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("", 0, "");
                    itemPojos.add(itemPojos1);
                } else if (itemPojos.size() % 3 == 1) {
                    FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("", 0, "");
                    itemPojos.add(itemPojos1);

                    FoundData.ItemPojos itemPojos2 = new FoundData.ItemPojos("", 0, "");
                    itemPojos.add(itemPojos2);
                }

                GridAdapter gridAdapter = new GridAdapter(itemPojos, position);
                RecyclerView recyclerView = holder.getView(R.id.rv_grid);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                recyclerView.setAdapter(gridAdapter);
            }
        }
    }

    class GridAdapter extends CommonAdapter<FoundData.ItemPojos> {

        private int parentPosition;

        private GridAdapter(List<FoundData.ItemPojos> datas, int parentPosition) {
            super(getContext(), R.layout.item_grid_foundfragment, datas);
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(ViewHolder holder, final FoundData.ItemPojos itemPojos, final int position) {

            final View view = holder.getView(R.id.layout_grid);

            if (TextUtils.isEmpty(itemPojos.getUrl())) {
                view.setEnabled(false);
            }

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

            layoutParams.width = (AppDevice.getWidth(getContext()) - AppDevice.dp2px(getContext(), 4)) / 3;

            layoutParams.height = (AppDevice.getWidth(getContext()) - AppDevice.dp2px(getContext(), 4)) / 3;

            view.setLayoutParams(layoutParams);

            holder.setText(R.id.tv, itemPojos.getIconDescription());

            ImageView imageIcon = holder.getView(R.id.iv);
            ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
            viewParams.width = AppDevice.getWidth(getContext()) / 9;
            viewParams.height = AppDevice.getWidth(getContext()) / 9;
            imageIcon.setLayoutParams(viewParams);

            ImageDisplay.loadResImage(getContext(), itemPojos.getIconRes(), imageIcon);

            final TypedValue typeValue = new TypedValue();

            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typeValue, true);

            holder.setOnClickListener(R.id.layout_grid, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemPojos.getUrl())) {//由于增加了headView,所以父Item是从1开始的
                        if (parentPosition == 1 && position == 0) {
                            Intent intent = new Intent(getContext(), FunctionActivity.class);
                            intent.putExtra("title", itemPojos.getIconDescription());
                            intent.putExtra("type", 1);
                            startActivity(intent);
                        } else if (parentPosition == 1 && position == 1) {
                            startActivity(new Intent(getContext(), IMRegisterActivity.class));
                        } else if (parentPosition == 1 && position == 2) {
                            startActivity(new Intent(getContext(), ChatRoomActivity.class));
                        } else if (parentPosition == 2 && position == 0) {
                            Intent intent = new Intent(getContext(), FunctionActivity.class);
                            intent.putExtra("title", itemPojos.getIconDescription());
                            intent.putExtra("type", 2);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), itemPojos.getIconDescription(), Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getContext(), FunctionActivity.class);
//                            intent.putExtra("function_url", itemPojos.getUrl());
//                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

}
