package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.MarketActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :标题title+Gird/list列表的Section列表
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
    protected void convertHead(final BaseViewHolder holder, final SectionTouYanData data) {
        holder.setText(R.id.tv_touyan_title_text, data.header);

        holder.setOnClickListener(R.id.item_touyan_title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(v.getContext(),data.header+";pos="+holder.getAdapterPosition());
            }
        });
    }


    @Override
    protected void convert(BaseViewHolder holder, SectionTouYanData data, final int position) {
        final TouYan.DataBean.Item item = data.t;

        AppCompatImageView imgview = holder.getView(R.id.img_touyan_item_icon);
        holder.setText(R.id.tv_touyan_item_text, item.getDesc());
        if (TextUtils.isEmpty(item.getIconUrl())){
            imgview.setImageDrawable(ContextCompat.getDrawable(context,android.R.color.transparent));
        }else {
            ImageDisplay.loadNetImage(context, item.getIconUrl(), imgview);
        }

        final ViewGroup.LayoutParams imageParams = imgview.getLayoutParams();
        imageParams.width = screenWidth / 11;
        imgview.setLayoutParams(imageParams);

        LinearLayout itemParent = holder.getView(R.id.item_touyan_item);
        ViewGroup.LayoutParams itemParentLayoutParams = itemParent.getLayoutParams();
        itemParentLayoutParams.width = (screenWidth - 3) / 4;
        itemParentLayoutParams.height = (screenWidth - 3) / 4;
        itemParent.setLayoutParams(itemParentLayoutParams);

        if (TextUtils.isEmpty(item.getDesc())){
            itemParent.setClickable(false);
            itemParent.setEnabled(false);
        }else {
            itemParent.setClickable(true);
            itemParent.setEnabled(true);
        }
        itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getDesc().equalsIgnoreCase("行情")) {
                    context.startActivity(new Intent(context, MarketActivity.class));
                } else {
                    UIHelper.toast(context, item.getDesc() + "," + position);
                }
            }
        });
    }
}