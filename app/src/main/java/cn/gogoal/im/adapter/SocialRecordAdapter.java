package cn.gogoal.im.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.PlayerActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SocialRecordData;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.ui.dialog.InviteAuthDialog;

/**
 * Created by dave.
 * Date: 2017/5/3.
 * Desc: description
 */
public class SocialRecordAdapter extends CommonAdapter<SocialRecordData, BaseViewHolder> {

    private FragmentActivity mContext;

    public SocialRecordAdapter(FragmentActivity mContext, List<SocialRecordData> data) {
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
        ImageDisplay.loadImage(mContext, data.getVideo_img_url(), imgPcCover);

        holder.setText(R.id.textPcStatus, "回放 " + update_time);
        holder.setBackgroundRes(R.id.textPcStatus, R.drawable.shape_social_live_status_gray);
        holder.setImageResource(R.id.imgPcTime, R.mipmap.record_play_count);
        holder.setText(R.id.textPcTime, data.getPlay_base());

        holder.setText(R.id.textPcTitle, data.getVideo_name());
        RoundedImageView imgPcIcon = holder.getView(R.id.imgPcIcon);
        ImageDisplay.loadCircleImage(mContext, data.getAnchor().getFace_url(), imgPcIcon);
        holder.setText(R.id.textCompanTitle, data.getAnchor().getOrganization() == null ? "--"
                : data.getAnchor().getOrganization() + " | " + data.getAnchor().getAnchor_position()
                == null ? "--" : data.getAnchor().getAnchor_position());

        if (data.getAuth() == 1) {
            holder.setVisible(R.id.textPcInvite, true);
        } else {
            holder.setVisible(R.id.textPcInvite, false);
        }

        holder.setOnClickListener(R.id.linearSocial, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getAuth() == 1) {
                    setInviteAuth(data.getVideo_id());
                } else {
                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    intent.putExtra("live_id", data.getVideo_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * 邀约
     */
    private void setInviteAuth(final String invite_id) {

        String identifies = SPTools.getString(invite_id, null);


        if (identifies == null) {
            InviteAuthDialog.newInstance("video", invite_id).show(mContext.getSupportFragmentManager());
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("video_id", invite_id);
        param.put("identifies", identifies);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 101) {
                        Intent intent = new Intent(mContext, PlayerActivity.class);
                        intent.putExtra("live_id", invite_id);
                        mContext.startActivity(intent);
                    } else {
                        InviteAuthDialog.newInstance("video", invite_id).show(mContext.getSupportFragmentManager());
                    }
                } else {
                    InviteAuthDialog.newInstance("video", invite_id).show(mContext.getSupportFragmentManager());
                }
            }

            @Override
            public void onFailure(String msg) {
                InviteAuthDialog.newInstance("video", invite_id).show(mContext.getSupportFragmentManager());
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VALIDATE_IDENTIFIES, ggHttpInterface).startGet();
    }
}
