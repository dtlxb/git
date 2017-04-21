package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.FunctionActivity;
import cn.gogoal.im.activity.stock.MarketActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.SectionTouYanData;
import cn.gogoal.im.bean.TouYan;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;



/**
 * author wangjd on 2017/4/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :投研适配器
 */
public class SectionAdapter extends BaseSectionQuickAdapter<SectionTouYanData, BaseViewHolder> {
    private Context context;

    private int screenWidth;

    private int innerItem;

    public SectionAdapter(Context context, List<SectionTouYanData> data) {
        super(R.layout.item_touyan_item, R.layout.item_touyan_title, data);
        this.context = context;
        screenWidth = AppDevice.getWidth(context);

        innerItem = AppDevice.isLowDpi() ?
                (AppDevice.getWidth(context) - 2) / 3:
        (AppDevice.getWidth(context) - 3) / 4 ;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, final SectionTouYanData item) {
        holder.setText(R.id.tv_touyan_title_text, item.header);
    }


    @Override
    protected void convert(BaseViewHolder holder, SectionTouYanData data, final int position) {
        final TouYan.DataBean.Item item = data.t;

        //initView
        View itemView = holder.getView(R.id.item_touyan_item);
        ImageView itemIcon = holder.getView(R.id.img_touyan_item_icon);
        ImageView itemHot = holder.getView(R.id.img_touyan_item_hot);
        TextView itemTvDesc = holder.getView(R.id.tv_touyan_item_text);

        //init width and height
        setViewHeight$Width(itemView, innerItem);//3、4格情况适配
        setViewHeight$Width(itemIcon, innerItem / 3);
        setViewHeight$Width(itemHot, innerItem / 4);

        //模拟的空item处理
        if (data.isSimulatedArg()) {
            itemView.setClickable(false);
            itemView.setEnabled(false);
            itemHot.setVisibility(View.GONE);
            itemIcon.setImageDrawable(ContextCompat.getDrawable(context, android.R.color.transparent));
            holder.setText(R.id.tv_touyan_item_text, "");
        } else {
            itemView.setClickable(true);
            itemView.setEnabled(true);
            ImageDisplay.loadNetImage(context, item.getIconUrl(), itemIcon, true);
            itemTvDesc.setText(item.getDesc());

            if (TextUtils.isEmpty(item.getIconUrl()) || item.getShowHotFlag() == 0) {//不显示
                itemHot.setVisibility(View.GONE);
            } else {
                itemHot.setVisibility(View.VISIBLE);
            }

            itemTvDesc.setTextColor(item.getIsClick() == 0 ?
                    ContextCompat.getColor(context, R.color.textColor_333333) :
                    ContextCompat.getColor(context, R.color.textColor_999999));

            ImageDisplay.loadNetImage(context, item.getIconUrl(), itemIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //TODO 跳原生类型
                    if (item.getDesc().equalsIgnoreCase("行情")) {
                        context.startActivity(new Intent(context, MarketActivity.class));
                    } else {
                        //TODO 跳网页类型

                        if (item.getIsClick() == 0) {
                            UIHelper.toast(v.getContext(), "pos=" + position);
                            Intent intent = new Intent(v.getContext(), FunctionActivity.class);
                            intent.putExtra("function_url", item.getUrl());
                            intent.putExtra("title", item.getDesc());
                            context.startActivity(intent);
                        } else {
                            View dialogView = LayoutInflater.from(context).
                                    inflate(R.layout.dialog_touyan_coming_soon, new LinearLayout(context), false);

                            final AlertDialog dialog = DialogHelp.getWindoDialog(v.getContext(), dialogView, 3 * screenWidth / 4);

                            dialogView.findViewById(R.id.img_touyan_cancle).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
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

