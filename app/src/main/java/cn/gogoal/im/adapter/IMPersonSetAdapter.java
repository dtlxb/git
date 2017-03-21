package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppDevice;

/**
 * Created by huangxx on 2017/3/20.
 */

public class IMPersonSetAdapter extends CommonAdapter<ContactBean> {

    public IMPersonSetAdapter(Context context, int layoutId, List<ContactBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ContactBean contactBean, int position) {

        final View view = holder.getView(R.id.layout_grid);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = AppDevice.getWidth(getContext()) / 5;
        layoutParams.height = AppDevice.getWidth(getContext()) / 4;
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.absoluteWhite));
        view.setLayoutParams(layoutParams);

        ImageView imageIcon = holder.getView(R.id.iv);
        ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
        viewParams.width = AppDevice.getWidth(getContext()) / 8;
        viewParams.height = AppDevice.getWidth(getContext()) / 8;
        imageIcon.setLayoutParams(viewParams);

        Object avatar = contactBean.getAvatar();
        holder.setText(R.id.tv, contactBean.getNickname());

        if (avatar instanceof String) {
            holder.setImageUrl(R.id.iv, avatar.toString());
        } else if (avatar instanceof Integer) {
            holder.setImageResource(R.id.iv, (Integer) avatar);
        }
    }
}
