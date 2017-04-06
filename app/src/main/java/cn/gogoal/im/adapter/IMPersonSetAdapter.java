package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.UserUtils;

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
        layoutParams.width = AppDevice.getWidth(getmContext()) / 6;
        layoutParams.height = AppDevice.getWidth(getmContext()) / 4;
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.absoluteWhite));
        view.setLayoutParams(layoutParams);

        ImageView imageIcon = holder.getView(R.id.iv);
        TextView personName = holder.getView(R.id.tv);
        TextView squareManTag = holder.getView(R.id.square_man_tag);
        ViewGroup.LayoutParams viewParams = imageIcon.getLayoutParams();
        ViewGroup.LayoutParams nameParams = personName.getLayoutParams();
        viewParams.width = AppDevice.getWidth(getmContext()) / 8;
        viewParams.height = AppDevice.getWidth(getmContext()) / 8;
        nameParams.width = AppDevice.getWidth(getmContext()) / 8;
        imageIcon.setLayoutParams(viewParams);
        personName.setLayoutParams(nameParams);

        Object avatar = contactBean.getAvatar();

        personName.setText(contactBean.getNickname());
        if (avatar instanceof String) {
            holder.setImageUrl(R.id.iv, avatar.toString());
        } else if (avatar instanceof Integer) {
            holder.setImageResource(R.id.iv, (Integer) avatar);
        }
        if (String.valueOf(contactBean.getFriend_id()).equals(UserUtils.getUserAccountId())) {
            squareManTag.setVisibility(View.VISIBLE);
        } else {
            squareManTag.setVisibility(View.GONE);
        }
    }
}
