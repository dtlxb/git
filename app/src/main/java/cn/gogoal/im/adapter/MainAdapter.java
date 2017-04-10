package cn.gogoal.im.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.ChatRoomActivity;
import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.IMRegisterActivity;
import cn.gogoal.im.activity.stock.MarketActivity;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGAPI;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.permission.IPermissionListner;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :投研-主适配器.
 */
public class MainAdapter extends CommonAdapter<FoundData> {

    private Context context;
    public MainAdapter(Context context, int layoutId, List<FoundData> datas) {
        super(context, layoutId, datas);
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder holder, FoundData foundData, int position) {
        holder.setText(R.id.tv_title, foundData.getTitle());

        List<FoundData.ItemPojos> itemPojos = foundData.getItemPojos();
        if (itemPojos != null) {
            if (itemPojos.size() % 4 == 3) {
                FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos1);
            } else if (itemPojos.size() % 4 == 2) {
                FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos1);

                FoundData.ItemPojos itemPojos2 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos2);
            } else if (itemPojos.size() % 4 == 1) {
                FoundData.ItemPojos itemPojos1 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos1);

                FoundData.ItemPojos itemPojos2 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos2);

                FoundData.ItemPojos itemPojos3 = new FoundData.ItemPojos("", 0, "");
                itemPojos.add(itemPojos3);
            }

            GridAdapter gridAdapter = new GridAdapter(context,itemPojos, position);
            RecyclerView recyclerView = holder.getView(R.id.rv_grid);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerView.setAdapter(gridAdapter);
        }
    }

    private class GridAdapter extends CommonAdapter<FoundData.ItemPojos> {

        private int parentPosition;

        private Context context;
        private GridAdapter(Context context,List<FoundData.ItemPojos> datas, int parentPosition) {
            super(context, R.layout.item_grid_foundfragment, datas);
            this.context=context;
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(ViewHolder holder, final FoundData.ItemPojos itemPojos, final int position) {

            if (TextUtils.isEmpty(itemPojos.getUrl())) {
                holder.itemView.setEnabled(false);
            }

            AppDevice.setViewWidth$Height(holder.itemView,(AppDevice.getWidth(context) - 3) / 4,(AppDevice.getWidth(context) - 3) / 4);

            holder.setText(R.id.tv, itemPojos.getItemTextDescription());

            ImageView imageIcon = holder.getView(R.id.iv);
            ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
            viewParams.width = AppDevice.getWidth(context) / 10;
            viewParams.height = AppDevice.getWidth(context) / 10;
            imageIcon.setLayoutParams(viewParams);

            if (itemPojos.getIconRes() != 0) {
                ImageDisplay.loadResImage(context, itemPojos.getIconRes(), imageIcon);
            }

            final TypedValue typeValue = new TypedValue();

            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typeValue, true);

            holder.setOnClickListener(R.id.layout_grid, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemPojos.getUrl())) {//由于增加了headView,所以父Item是从1开始的
                        switch (parentPosition) {

                            //由于增加了headView,所以父Item是从1开始的
                            case 1:
                                gridItemClick_0(position, itemPojos);//我的活动
                                break;
                            case 2:
                                gridItemClick_1(position, itemPojos);//股票投研
                                break;
                            case 3:
                                gridItemClick_2(position, itemPojos);//测试-股票
                                break;
                        }
                    }
                }
            });
        }

        //我的活动
        private void gridItemClick_0(int position, FoundData.ItemPojos itemPojos) {
            Intent intent = new Intent(context, FunctionActivity.class);
            intent.putExtra("title", itemPojos.getItemTextDescription());
            switch (position) {
                case 0:
//                    intent.putExtra("function_url", itemPojos.getUrl());
                    intent.putExtra("function_url", GGAPI.WEB_URL + "/live/list");
                    context.startActivity(intent);
                    break;
                case 1:
                    intent.putExtra("function_url", GGAPI.WEB_URL + "/research");
                    context.startActivity(intent);
                    break;
                case 2:
                    BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new IPermissionListner() {
                        @Override
                        public void onUserAuthorize() {
                            context.startActivity(new Intent(context, ChatRoomActivity.class));
                        }

                        @Override
                        public void onRefusedAuthorize(List<String> deniedPermissions) {
                            UIHelper.toast(context, "录音权限被拒，录音无法使用");
                        }
                    });
                    break;
                case 3:
                    context.startActivity(new Intent(context, IMRegisterActivity.class));
                    break;
                case 4:
                    intent.putExtra("function_url", "file:///android_asset/demo.html");
                    context.startActivity(intent);
                    break;
            }
        }

        //股票投研
        private void gridItemClick_1(int position, FoundData.ItemPojos itemPojos) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(context, FunctionActivity.class);
                    intent.putExtra("title", itemPojos.getItemTextDescription());
                    //intent.putExtra("function_url", itemPojos.getItemTextDescription());
                    intent.putExtra("function_url", GGAPI.WEB_URL + "/report");
                    context.startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }

        //测试模块——股票
        private void gridItemClick_2(int position, FoundData.ItemPojos itemPojos) {
            switch (position) {
                case 0://行情
                    context.startActivity(new Intent(context, MarketActivity.class));
                    break;
                case 3:
                    Intent intent = new Intent(context, FunctionActivity.class);
                    intent.putExtra("title", itemPojos.getItemTextDescription());
                    //intent.putExtra("function_url", itemPojos.getItemTextDescription());
                    intent.putExtra("function_url", GGAPI.WEB_URL + "/text");
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
