package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.ToolsSettingActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.SectionToolsData;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;


/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :投研适配器
 */
public class SectionAdapter extends BaseSectionQuickAdapter<SectionToolsData, BaseViewHolder> {

    private Context context;

    private int innerItem;

    public SectionAdapter(Context context, List<SectionToolsData> data) {
        super(R.layout.item_touyan_item, R.layout.item_touyan_title, data);
        this.context = context;

        innerItem = AppDevice.isLowDpi() ?
                AppDevice.getWidth(context) / 3 :
                AppDevice.getWidth(context) / 4;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, final SectionToolsData item) {
        holder.setVisible(R.id.view_parent_divider,holder.getAdapterPosition()!=0);
        holder.setText(R.id.tv_touyan_title_text, item.header);
    }


    @Override
    protected void convert(BaseViewHolder holder, final SectionToolsData data, final int position) {
        final ToolData.Tool item = data.t;

//        holder.setVisible(R.id.rdb_check,true);

        //initView
        final View itemView = holder.getView(R.id.item_touyan_item);
        ImageView itemIcon = holder.getView(R.id.img_touyan_item_icon);
        final AppCompatImageView itemHot = holder.getView(R.id.img_touyan_operation);
        TextView itemTvDesc = holder.getView(R.id.tv_touyan_item_text);

        //init width and height
        setViewHeight$Width(itemIcon, innerItem / 3);
        setViewHeight$Width(itemHot, innerItem / 4);
        setViewHeight$Width(itemView, innerItem);

        //模拟的空item处理
        if (item.isSimulatedArg()) {
            itemView.setClickable(false);
            itemView.setEnabled(false);
            itemHot.setVisibility(View.INVISIBLE);
            itemTvDesc.setVisibility(View.INVISIBLE);
            itemIcon.setVisibility(View.INVISIBLE);

        } else {
            itemHot.setVisibility(View.VISIBLE);
            itemTvDesc.setVisibility(View.VISIBLE);
            itemIcon.setVisibility(View.VISIBLE);

            itemView.setClickable(item.getIsShow()!=1);
            itemView.setEnabled(item.getIsShow()!=1);
            itemHot.setImageResource(item.getIsShow() == 1 ? R.mipmap.img_tools_added : R.mipmap.img_tools_add);

            ImageDisplay.loadImage(context, item.getIconUrl(), itemIcon);

            itemTvDesc.setText(item.getDesc());
            ImageDisplay.loadImage(context, item.getIconUrl(), itemIcon);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ((ToolsSettingActivity) context).addSelected(item);
                   itemView.setEnabled(false);
                   itemView.setClickable(false);
               }
           });
        }
    }

    //动态设置item视图宽高
    private void setViewHeight$Width(View view, int width$height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width$height;
        params.height = width$height;
        view.setLayoutParams(params);
    }
}

