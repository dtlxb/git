package cn.gogoal.im.fragment;

import android.Manifest;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChatRoomActivity;
import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.IMRegisterActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.wrapper.HeaderAndFooterWrapper;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.permission.IPermissionListner;

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

        //-----------------------添加测试--------------------------------

        FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("股票行情", R.mipmap.function_icon_news, "tag");
        FoundData.ItemPojos itemPojos2 = new FoundData.ItemPojos("自选股", R.mipmap.function_icon_news, "tag");
        FoundData.ItemPojos itemPojos3 = new FoundData.ItemPojos("个股详情", R.mipmap.function_icon_news, "tag");

        List<FoundData.ItemPojos> listTest = new ArrayList<>();
        listTest.add(itemPojos1);
        listTest.add(itemPojos2);
        listTest.add(itemPojos3);
        datas.add(new FoundData(listTest, "测试部分"));
        return datas;
    }

    private class MainAdapter extends CommonAdapter<FoundData> {

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

    private class GridAdapter extends CommonAdapter<FoundData.ItemPojos> {

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

            holder.setText(R.id.tv, itemPojos.getItemTextDescription());

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
                        switch (parentPosition) {

                            //由于增加了headView,所以父Item是从1开始的
                            case 1:
                                gridItemClick_0(position);//我的活动
                                break;
                            case 2:
                                gridItemClick_1(position);//股票投研
                                break;
                            case 3:
                                gridItemClick_2(position);//测试-股票
                                break;
                        }
                    }
                }
            });
        }

        //我的活动
        private void gridItemClick_0(int position) {
            switch (position) {
                case 0:
                    startActivity(new Intent(getContext(), FunctionActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(getContext(), IMRegisterActivity.class));
                    break;
                case 2:
                    BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new IPermissionListner() {
                        @Override
                        public void onUserAuthorize() {
                            startActivity(new Intent(getContext(), ChatRoomActivity.class));
                        }

                        @Override
                        public void onRefusedAuthorize(List<String> deniedPermissions) {
                            UIHelper.toast(getContext(), "录音权限被拒，录音无法使用");
                        }
                    });
                    break;
            }
        }

        //股票投研
        private void gridItemClick_1(int position) {

        }

        //测试模块——股票
        private void gridItemClick_2(int position) {

        }
    }

}
