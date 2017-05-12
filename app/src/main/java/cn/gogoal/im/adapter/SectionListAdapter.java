package cn.gogoal.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.ToolsListActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.SectionToolsData;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.ui.dialog.AdvisersDialog;


/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :工具中心
 */
public class SectionListAdapter extends BaseSectionQuickAdapter<SectionToolsData, BaseViewHolder> {

    private Context context;

    private String[] status = {"获取开通", "已开通", "试用中", "免费"};
    // 数组index和isOpen对应，0 获取开通 1 已开通   2 试用中 3 免费

    public SectionListAdapter(Context context, List<SectionToolsData> data) {
        super(R.layout.item_tools_center, R.layout.item_touyan_title, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, final SectionToolsData item) {
        holder.setVisible(R.id.title_flag, true);
        View titleView = holder.getView(R.id.item_touyan_title);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) titleView.getLayoutParams();
        if (holder.getAdapterPosition() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(0, AppDevice.dp2px(context, 20), 0, 0);
        }
        titleView.setLayoutParams(params);
        holder.setText(R.id.tv_touyan_title_text, item.header);
    }


    @Override
    protected void convert(BaseViewHolder holder, final SectionToolsData data, final int position) {
        final ToolData.Tool tool = data.t;

        holder.setText(R.id.tv_item_tools_title, tool.getDesc());
        holder.setText(R.id.tv_item_tools_introduce, tool.getIntroduce());

        Glide.with(context)
                .load(tool.getIconUrl())
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .into((ImageView) holder.getView(R.id.img_logo));

        TextView tvOperation = holder.getView(R.id.tv_item_tools_operation);
        if (tool.getIsOpen() >= 0 && tool.getIsOpen() < status.length) {
            tvOperation.setText(status[tool.getIsOpen()]);
        } else {
            tvOperation.setText("未知类型");
        }

        tvOperation.setBackgroundResource(tool.getIsOpen() == 0 ?
                R.drawable.shape_add_friend : android.R.color.transparent);
        tvOperation.setTextColor(tool.getIsOpen() == 0 ? Color.WHITE : getResColor(R.color.textColor_333333));
        tvOperation.setFocusable(tool.getIsOpen() == 0);
        tvOperation.setClickable(tool.getIsOpen() == 0);
        tvOperation.setEnabled(tool.getIsOpen() == 0);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2WebActivity(context,
                        AppConst.WEB_URL + "/column/info/" + tool.getId(), tool.getDesc());
            }
        });

        tvOperation.setOnClickListener(tool.getIsOpen() == 0 ? new DialogClickListener():null);

    }

    private class DialogClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            new AdvisersDialog().show(((ToolsListActivity) context).getSupportFragmentManager());
        }
    }

}

