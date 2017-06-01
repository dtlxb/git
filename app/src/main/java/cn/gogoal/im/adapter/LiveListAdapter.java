package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.LiveListItemBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :直播、录播视频列表适配器
 */
public class LiveListAdapter extends CommonAdapter<LiveListItemBean, BaseViewHolder> {

    private int imgBgSize;

    private RequestOptions options;

    private Context context;

    public LiveListAdapter(Context context, List<LiveListItemBean> data) {
        super(R.layout.item_live_list, data);
        imgBgSize = AppDevice.getWidth(context) - AppDevice.dp2px(context, 14);
        this.context = context;

        options = RequestOptions.skipMemoryCacheOf(false);
        options.dontAnimate();
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.placeholder(R.mipmap.image_placeholder);
        options.error(R.mipmap.image_placeholder);
        options.centerCrop();
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveListItemBean data, int position) {

        ImageView ivLiveBg = holder.getView(R.id.iv_live_list_bg);
        TextView tvStartTime = holder.getView(R.id.tv_item_live_list_start_time);

        ViewGroup.LayoutParams params = ivLiveBg.getLayoutParams();
        params.width = imgBgSize;
        params.height = 11 * imgBgSize / 20;
        ivLiveBg.setLayoutParams(params);

        Glide.with(context)
                .load(data.getImgBg())
                .apply(options)
                .into(ivLiveBg);

        holder.setText(R.id.tv_tv_item_live_list_title, data.getTitle());

        holder.setImageUrl(context, R.id.iv_live_list_avatar, data.getAnchorAvatar());

        holder.setText(R.id.tv_item_live_list_anchor_name, data.getAnchorName());

        holder.setText(R.id.tv_item_live_list_programme_name, data.getProgramme_name());

        holder.setVisible(R.id.tv_item_live_list_count_down,
                data.getLive_status() != -1);

        if (data.getLive_status() == 0) {           //预告
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_yellow);
            tvStartTime.setText("预告中 " + CalendarUtils.formatDate("MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", data.getStartTime()));
            holder.setText(R.id.tv_item_live_list_count_down,"距离开始");
        } else if (data.getLive_status() == -1) {   //录播
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_gray);
            tvStartTime.setText("回放 " + CalendarUtils.formatDate("MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", data.getStartTime()));
        } else {                                    //直播中
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_red);
            tvStartTime.setText("直播中 " + CalendarUtils.formatDate("MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", data.getStartTime()));
        }

    }
}
