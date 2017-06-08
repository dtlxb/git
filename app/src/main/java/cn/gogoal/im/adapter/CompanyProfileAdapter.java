package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ProfileData;
import cn.gogoal.im.common.copy.FtenUtils;

/**
 * Created by dave.
 * Date: 2017/6/8.
 * Desc: description
 */
public class CompanyProfileAdapter extends CommonAdapter<ProfileData, BaseViewHolder> {

    private List<ProfileData> listData;
    private Context context;

    public CompanyProfileAdapter(Context context, List<ProfileData> listData) {
        super(R.layout.item_company_profile, listData);
        this.listData = listData;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ProfileData profileData, int position) {
        if (position % 2 == 0) {
            holder.setBackgroundColor(R.id.linearProfile, ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.setBackgroundColor(R.id.linearProfile, ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.setText(R.id.textName, profileData.name);
        holder.setText(R.id.textContent, profileData.content != null ? profileData.content : "--");

        if (position == 0) {
            holder.setVisible(R.id.tv_sticky_header_view, true);
            holder.setText(R.id.tv_sticky_header_view, profileData.sticky);
            holder.itemView.setTag(FtenUtils.FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(profileData.sticky, listData.get(position - 1).sticky)) {
                holder.setVisible(R.id.tv_sticky_header_view, true);
                holder.setText(R.id.tv_sticky_header_view, profileData.sticky);
                holder.itemView.setTag(FtenUtils.HAS_STICKY_VIEW);
            } else {
                holder.setVisible(R.id.tv_sticky_header_view, false);
                holder.itemView.setTag(FtenUtils.NONE_STICKY_VIEW);
            }
        }

        holder.itemView.setContentDescription(profileData.sticky);
    }
}
