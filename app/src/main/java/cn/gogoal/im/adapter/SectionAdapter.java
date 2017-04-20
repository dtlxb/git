package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.stock.MarketActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :投研适配器
 */
public class SectionAdapter extends BaseSectionQuickAdapter<SectionTouYanData, BaseViewHolder> {
    private Context context;

    private int screenWidth;

    public SectionAdapter(Context context, List<SectionTouYanData> data) {
        super(R.layout.item_touyan_item, R.layout.item_touyan_title, data);
        this.context = context;
        screenWidth = AppDevice.getWidth(context);
    }

    @Override
    protected void convertHead(BaseViewHolder holder, final SectionTouYanData item) {
        holder.setText(R.id.tv_touyan_title_text, item.header);
    }


    @Override
    protected void convert(BaseViewHolder holder, SectionTouYanData data, final int position) {
        final TouYan.DataBean.Item item = data.t;

        View itemView = holder.getView(R.id.item_touyan_item);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = (screenWidth - 3) / 4;
        params.height = (screenWidth - 3) / 4;
        itemView.setLayoutParams(params);
        if (TextUtils.isEmpty(item.getDesc())){
            itemView.setClickable(false);
            itemView.setEnabled(false);
        }else {
            itemView.setClickable(true);
            itemView.setEnabled(true);
        }

        ImageView itemIcon = holder.getView(R.id.img_touyan_item_icon);

        if (TextUtils.isEmpty(item.getIconUrl())){
            itemIcon.setImageDrawable(ContextCompat.getDrawable(context,android.R.color.transparent));
        }else {
            ImageDisplay.loadNetImage(context, item.getIconUrl(), itemIcon,true);
        }

        final LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) itemIcon.getLayoutParams();
        imageParams.width = screenWidth / 11;
        imageParams.height = screenWidth / 11;
        itemIcon.setLayoutParams(imageParams);

        ImageDisplay.loadNetImage(context,item.getIconUrl(),itemIcon);
        holder.setText(R.id.tv_touyan_item_text, item.getDesc());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getDesc().equalsIgnoreCase("行情")) {
                    context.startActivity(new Intent(context, MarketActivity.class));
                } else {
                    Intent intent = new Intent(v.getContext(), FunctionActivity.class);
                    intent.putExtra("function_url",item.getUrl());
                    intent.putExtra("title",item.getDesc());
                    context.startActivity(intent);
                }
            }
        });
    }
}

