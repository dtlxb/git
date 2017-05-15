package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.UFileImageHelper;

/**
 * Created by huangxx on 2017/3/20.
 */

public class IMPersonSetAdapter extends CommonAdapter<ContactBean, BaseViewHolder> {

    private Context context;
    private String squareCreater;
    private int chatTytpe;

    public IMPersonSetAdapter(int chatTytpe, Context context, int layoutId, String squareCreater, List<ContactBean> datas) {
        super(layoutId, datas);
        this.context = context;
        this.chatTytpe = chatTytpe;
        this.squareCreater = squareCreater;
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactBean contactBean, int position) {

        final View view = holder.getView(R.id.layout_grid);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = AppDevice.getWidth(context) / 6;
        layoutParams.height = AppDevice.getWidth(context) / 4;
        view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        view.setLayoutParams(layoutParams);

        ImageView imageIcon = holder.getView(R.id.iv);

        TextView personName = holder.getView(R.id.tv);
        TextView squareManTag = holder.getView(R.id.square_man_tag);
        ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
        ViewGroup.LayoutParams nameParams = personName.getLayoutParams();
        viewParams.width = AppDevice.getWidth(context) / 8;
        viewParams.height = AppDevice.getWidth(context) / 8;
        nameParams.width = AppDevice.getWidth(context) / 8;
        imageIcon.setLayoutParams(viewParams);
        personName.setLayoutParams(nameParams);

        Object avatar = contactBean.getAvatar();

        personName.setText(contactBean.getNickname());
        if (avatar instanceof String) {
            ImageDisplay.loadRoundedRectangleImage(
                    mContext,
                    UFileImageHelper.load(avatar.toString()).compress(5).get(),
                    imageIcon
            );
        } else if (avatar instanceof Integer) {
            imageIcon.setImageResource((Integer) avatar);
        }
        if ((String.valueOf(contactBean.getFriend_id()).equals(squareCreater) && chatTytpe == 1002)) {
            squareManTag.setVisibility(View.VISIBLE);
        } else {
            squareManTag.setVisibility(View.GONE);
        }
    }
}
