package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.FoundData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by huangxx on 2017/3/14.
 */

public class ChatFunctionAdapter extends CommonAdapter<FoundData.ItemPojos,BaseViewHolder> {

    private Context mContex;

    public ChatFunctionAdapter(Context context, List<FoundData.ItemPojos> datas) {
        super(context, R.layout.item_chat_function, datas);
        this.mContex = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, FoundData.ItemPojos data, int position) {
        View view = holder.getView(R.id.layout_grid);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        layoutParams.width = (AppDevice.getWidth(mContext) - AppDevice.dp2px(mContext, 4)) / 4;

        layoutParams.height = (AppDevice.dp2px(mContext, 220)) / 2;

        view.setLayoutParams(layoutParams);

        holder.setText(R.id.tv, data.getItemTextDescription());
        ImageDisplay.loadResImage(mContex, data.getIconRes(), (ImageView) holder.getView(R.id.iv));
    }
}
