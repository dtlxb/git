package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.activity.ToolsListActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.bean.stock.Stock;
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

    private Context context;
    private DialogFragment dialog;
    private Stock stock;

    private int innerItem;

    public InvestmentResearchAdapter(
            Context context,
            List<ToolData.Tool> datas,
            DialogFragment dialog,
            Stock stock) {
        super(R.layout.item_touyan_item, datas);

        this.context = context;
        this.dialog = dialog;
        this.stock=stock==null?new Stock("000001","平安银行"):stock;

        innerItem = AppDevice.isLowDpi() ?
                AppDevice.getWidth(context) / 3 :
                AppDevice.getWidth(context) / 4;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ToolData.Tool data, int position) {
        View itemView = holder.getView(R.id.item_touyan_item);
        itemView.setBackgroundColor(Color.WHITE);

        TextView tvDesc = holder.getView(R.id.tv_touyan_item_text);
        holder.setVisible(R.id.img_touyan_operation, false);
        ImageView imgIcon = holder.getView(R.id.img_touyan_item_icon);

        setViewWidth$Height(itemView, innerItem, innerItem);

        itemView.setClickable(!data.isSimulatedArg());
        itemView.setEnabled(!data.isSimulatedArg());
        imgIcon.setVisibility(data.isSimulatedArg() ? View.INVISIBLE : View.VISIBLE);
        tvDesc.setVisibility(data.isSimulatedArg() ? View.INVISIBLE : View.VISIBLE);

        setViewWidth$Height(imgIcon, innerItem / 2, innerItem / 2);
        tvDesc.setText(data.getDesc());

        if (data.getIsClick() == 10086) {
            Glide.with(context).load(Uri.parse(data.getIconUrl())).into(imgIcon);
        } else {
            ImageDisplay.loadImage(context, data.getIconUrl(), imgIcon);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳原生类型
                if (data.getDesc().equalsIgnoreCase("行情")) {
                    ((MainActivity) context).changeItem(0);
                    ((MainActivity) context).mainStockFragment.changeItem(1);
                } else {
                    //TODO 跳网页类型
                    if (data.getIsClick() == 0) {
                        String[] params = {
                                data.getUrl() +
                                        "?code=" + data.getCode()+
                                        "&stock_code=" + stock.getStock_code() +
                                        "&stock_name=" + stock.getStock_name(),
                                data.getDesc()};
                        BannerUtils.getInstance(context, data.getType(), params).go();
                    } else if (data.getIsClick() == 10086) {//更多
                        context.startActivity(new Intent(context, ToolsListActivity.class));
                    } else {
                        UIHelper.toastInCenter(context, "该功能暂未开放使用");
                    }
                }
                if (dialog!=null){
                    dialog.dismiss();
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
