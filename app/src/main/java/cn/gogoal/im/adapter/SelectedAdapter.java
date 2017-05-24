package cn.gogoal.im.adapter;

import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.ToolsSettingActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseItemDraggableAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;

/**
 * author wangjd on 2017/5/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :投研工具已选择
 */
public class SelectedAdapter extends BaseItemDraggableAdapter<ToolData.Tool, BaseViewHolder> {

    private int fullInnerItem;

    private RecyclerView contextView;

    private List<ToolData.Tool> datas;

    public SelectedAdapter(RecyclerView contextView, List<ToolData.Tool> datas) {
        super(R.layout.item_touyan_item, datas);

        this.contextView = contextView;
        this.datas = datas;

        fullInnerItem = AppDevice.getWidth(contextView.getContext()) / (AppDevice.isLowDpi() ? 3 : 4);

//        innerItem = AppDevice.isLowDpi() ?
//                (AppDevice.getWidth(context)-AppDevice.dp2px(context,30)) / 3 :
//                (AppDevice.getWidth(context)-AppDevice.dp2px(context,40)) / 4;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ToolData.Tool data, final int position) {

        TextView tvDesc = holder.getView(R.id.tv_touyan_item_text);
        tvDesc.setText(data.getDesc());
        tvDesc.setPadding(0, 0, 0, AppDevice.dp2px(contextView.getContext(), 10));

        final View itemView = holder.getView(R.id.item_touyan_item);
        ImageView itemIcon = holder.getView(R.id.img_touyan_item_icon);
        AppCompatImageView itemHot = holder.getView(R.id.img_touyan_operation);

        setViewHeight$Width(itemIcon, fullInnerItem / 3);
        setViewHeight$Width(itemHot, fullInnerItem / 4);

//        setViewHeight$Width(itemView,innerItem);

        itemHot.setImageResource(R.mipmap.img_tools_del);

        holder.setBackgroundColor(R.id.item_touyan_item, Color.parseColor("#f6f6f6"));

        Glide.with(contextView.getContext()).load(data.getIconUrl()).dontAnimate().dontTransform().into(itemIcon);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ToolsSettingActivity) contextView.getContext()).remooveSelected(data);
            }
        });

        ViewGroup.LayoutParams params = contextView.getLayoutParams();
        if (AppDevice.isLowDpi()) {
            if (datas.size() > 4) {
                params.height = fullInnerItem * 2;
            } else {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        } else {
            if (datas.size() > 9) {
                params.height = fullInnerItem * 3;
            } else {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
//        params.height = (datas.size() > (AppDevice.isLowDpi() ? 3 : 8)) ?
//                fullInnerItem * (AppDevice.isLowDpi() ? 2 : 3) : ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        contextView.setLayoutParams(params);

        contextView.requestLayout();
    }

    //动态设置item视图宽高
    private void setViewHeight$Width(View view, int width$height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width$height;
        params.height = width$height;
        view.setLayoutParams(params);
    }
}
