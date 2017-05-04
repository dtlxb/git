package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.PlayerActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by dave.
 * Date: 2017/5/3.
 * Desc: description
 */
public class SocialRecordAdapter extends CommonAdapter<SocialRecordData, BaseViewHolder> {

    private Context mContext;

    public SocialRecordAdapter(Context mContext, List<SocialRecordData> data) {
        super(R.layout.item_social_live, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SocialRecordData data, int position) {

        String update_time = CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getUpdate_time());

        holder.setVisible(R.id.framePc, true);
        holder.setVisible(R.id.linearPc, true);
        holder.setVisible(R.id.btnPcOrder, false);

        ImageView imgPcCover = holder.getView(R.id.imgPcCover);
        ImageDisplay.loadNetImage(mContext, data.getVideo_img_url(), imgPcCover);

        holder.setText(R.id.textPcStatus, "回放 " + update_time);
        holder.setBackgroundRes(R.id.textPcStatus, R.drawable.shape_social_live_status_gray);
        holder.setImageResource(R.id.imgPcTime, R.mipmap.record_play_count);
        holder.setText(R.id.textPcTime, data.getPlay_base());

        holder.setText(R.id.textPcTitle, data.getVideo_name());
        ImageView imgPcIcon = holder.getView(R.id.imgPcIcon);
        ImageDisplay.loadCircleNetImage(mContext, data.getAnchor().getFace_url(), imgPcIcon);
        holder.setText(R.id.textCompanTitle, data.getAnchor().getOrganization() == null ? "--"
                : data.getAnchor().getOrganization() + " | " + data.getAnchor().getAnchor_position()
                == null ? "--" : data.getAnchor().getAnchor_position());

        holder.setOnClickListener(R.id.linearSocial, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("live_id", data.getVideo_id());
                mContext.startActivity(intent);
            }
        });
    }
}
