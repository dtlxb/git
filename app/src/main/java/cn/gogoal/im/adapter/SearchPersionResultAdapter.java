package cn.gogoal.im.adapter;

import android.view.View;

import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.ContactBean;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/6/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :搜人适配器
 */
public class SearchPersionResultAdapter extends CommonAdapter<ContactBean,BaseViewHolder> {

    public SearchPersionResultAdapter(List<ContactBean> datas) {
        super(R.layout.item_search_type_persion, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ContactBean data, int position) {
        holder.getView(R.id.btn_search_group_add).setVisibility(View.GONE);

        RoundedImageView imageView = holder.getView(R.id.item_user_avatar);
        holder.setText(R.id.item_tv_search_result_name, data.getNickname());
        holder.setText(R.id.item_tv_search_result_intro, data.getDuty());
        try {
            ImageDisplay.loadRoundedRectangleImage(mContext,
                    data.getAvatar(),
                    imageView);
        } catch (Exception e) {
        }

        UIHelper.setRippBg(holder.itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2PersionDetail(v.getContext(),
                        data.getUserId());
            }
        });
    }
}
