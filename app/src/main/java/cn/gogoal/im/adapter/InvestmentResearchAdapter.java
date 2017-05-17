package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.BannerUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/5/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class InvestmentResearchAdapter extends CommonAdapter<ToolData.Tool, BaseViewHolder> {

    private int innerItem;

    private Context context;

    public InvestmentResearchAdapter(Context context, List<ToolData.Tool> datas) {
        super(R.layout.item_touyan_item, datas);
        this.context = context;
        innerItem = AppDevice.isLowDpi() ?
                AppDevice.getWidth(context) / 3 :
                AppDevice.getWidth(context) / 4;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ToolData.Tool data, int position) {
        View itemView = holder.getView(R.id.item_touyan_item);
        TextView tvDesc = holder.getView(R.id.tv_touyan_item_text);
        ImageView imgHot = holder.getView(R.id.img_touyan_operation);
        ImageView imgIcon = holder.getView(R.id.img_touyan_item_icon);

        setViewWidth$Height(itemView, innerItem, innerItem);

        itemView.setClickable(!data.isSimulatedArg());
        itemView.setEnabled(!data.isSimulatedArg());
        imgIcon.setVisibility(data.isSimulatedArg() ? View.INVISIBLE : View.VISIBLE);
        tvDesc.setVisibility(data.isSimulatedArg() ? View.INVISIBLE : View.VISIBLE);

//        tvDesc.setTextColor(data.getType()==10000?(data.getIsClick() == 0 ?
//                getResColor(R.color.textColor_333333) :
//                getResColor(R.color.textColor_999999)):getResColor(R.color.textColor_333333));

        if (data.isSimulatedArg()) {//模拟数据
            imgHot.setVisibility(View.INVISIBLE);
        } else {
            imgHot.setVisibility(View.VISIBLE);

            setViewWidth$Height(imgHot, 107 * innerItem / 500, 107 * innerItem / 500);
            setViewWidth$Height(imgIcon, innerItem / 3, innerItem / 3);
            tvDesc.setText(data.getDesc());
            imgHot.setPadding(0, 0, 0, 0);
            imgHot.setImageResource(R.mipmap.img_hot);
            imgHot.setVisibility(data.getShowHotFlag() == 0 ? View.VISIBLE : View.INVISIBLE);
            ImageDisplay.loadImage(context,data.getIconUrl(),imgIcon);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳原生类型
                if (data.getDesc().equalsIgnoreCase("行情")) {
                    ((MainActivity) context).changeItem(1);
                    ((MainActivity) context).mainStockFragment.changeItem(1);
                } else {
                    //TODO 跳网页类型
                    if (data.getIsClick() == 0) {
                        String[] params={data.getUrl(),data.getTitle()};
                        BannerUtils.getInstance(context,data.getType(),params).go();
                    } else {
//                        new ComingSoonDialog().show(((MainActivity) context).getSupportFragmentManager());
                        UIHelper.toastInCenter(context,"该功能暂未开放使用");
//                        MessageFullScreen.newInstance("该功能暂未开放使用")
//                                .show(((MainActivity) context).getSupportFragmentManager());
                    }
                }
            }
        });
    }

    private void setViewWidth$Height(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (null != params) {
            params.height = height;
            params.width = width;
            view.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams framelayoutParams = new FrameLayout.LayoutParams(width, height);
            view.setLayoutParams(framelayoutParams);
        }
    }

}
