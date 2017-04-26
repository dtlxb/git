package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.WatchLiveActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: description
 */
public class SocialLiveAdapter extends CommonAdapter<SocialLiveData, BaseViewHolder> {

    private Context mContext;

    public SocialLiveAdapter(Context mContext, List<SocialLiveData> data) {
        super(R.layout.item_social_live, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SocialLiveData data, int position) {

        long timeHour = 60 * 60;
        String live_time_start = CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getLive_time_start());

        if (data.getLive_source() == 2) {
            //pc
            holder.setVisible(R.id.linearPhone, false);
            holder.setVisible(R.id.framePhone, false);
            holder.setVisible(R.id.framePc, true);
            holder.setVisible(R.id.linearPc, true);

            ImageView imgPcCover = holder.getView(R.id.imgPcCover);
            ImageDisplay.loadNetImage(mContext, data.getLive_large_img(), imgPcCover);
            //直播状态
            if (data.getLive_status() == 1) {
                //直播中
                holder.setText(R.id.textPcStatus, "直播中 " + live_time_start);
                holder.setBackgroundRes(R.id.textPcStatus, R.drawable.shape_social_live_status_red);
                holder.setImageResource(R.id.imgPcTime, R.mipmap.social_online_num);
                getOnlineCount(data.getRoom_id(), (TextView) holder.getView(R.id.textPcTime));
            } else {
                //预告
                holder.setBackgroundRes(R.id.textPcStatus, R.drawable.shape_social_live_status_yellow);
                holder.setImageResource(R.id.imgPcTime, R.mipmap.social_time);

                if (data.getLaunch_time() > 0) {
                    //预告
                    holder.setText(R.id.textPcStatus, "预告 " + live_time_start);
                    if (data.getLaunch_time() >= timeHour) {
                        holder.setText(R.id.textPcTime, "距离开始" + (int) Math.floor(data.getLaunch_time() / timeHour) + "小时");
                    } else {
                        holder.setText(R.id.textPcTime, "即将开始");
                    }
                } else {
                    //准备中
                    holder.setText(R.id.textPcStatus, "准备中 " + live_time_start);
                    holder.setText(R.id.textPcTime, "即将开始");
                }
            }

            holder.setText(R.id.textPcTitle, data.getVideo_name());
            ImageView imgPcIcon = holder.getView(R.id.imgPcIcon);
            ImageDisplay.loadCircleNetImage(mContext, data.getAnchor().getFace_url(), imgPcIcon);
            holder.setText(R.id.textCompanTitle, data.getAnchor().getOrganization() + " | "
                    + data.getAnchor().getAnchor_position());

        } else {
            //phone
            holder.setVisible(R.id.linearPhone, true);
            holder.setVisible(R.id.framePhone, true);
            holder.setVisible(R.id.framePc, false);
            holder.setVisible(R.id.linearPc, false);

            ImageView imgAnchorAvatar = holder.getView(R.id.imgAnchorAvatar);
            ImageDisplay.loadCircleNetImage(mContext, data.getAnchor().getFace_url(), imgAnchorAvatar);
            holder.setText(R.id.textAnchorName, data.getAnchor().getAnchor_name());
            holder.setText(R.id.textAnchorTitle, data.getAnchor().getOrganization() + " | "
                    + data.getAnchor().getAnchor_position());

            ImageView imgPhoneCover = holder.getView(R.id.imgPhoneCover);
            ImageDisplay.loadNetImage(mContext, data.getLive_large_img(), imgPhoneCover);
            //直播状态
            if (data.getLive_status() == 1) {
                //直播中
                holder.setText(R.id.textPhoneStatus, "直播中 " + live_time_start);
                holder.setBackgroundRes(R.id.textPhoneStatus, R.drawable.shape_social_live_status_red);
                holder.setImageResource(R.id.imgPhoneTime, R.mipmap.social_online_num);
                getOnlineCount(data.getRoom_id(), (TextView) holder.getView(R.id.textPhoneTime));
            } else {
                //预告
                holder.setBackgroundRes(R.id.textPhoneStatus, R.drawable.shape_social_live_status_yellow);
                holder.setImageResource(R.id.imgPhoneTime, R.mipmap.social_time);

                if (data.getLaunch_time() > 0) {
                    //预告
                    holder.setText(R.id.textPhoneStatus, "预告 " + live_time_start);
                    if (data.getLaunch_time() >= timeHour) {
                        holder.setText(R.id.textPhoneTime, "距离开始" + (int) Math.floor(data.getLaunch_time() / timeHour) + "小时");
                    } else {
                        holder.setText(R.id.textPhoneTime, "即将开始");
                    }
                } else {
                    //准备中
                    holder.setText(R.id.textPhoneStatus, "准备中 " + live_time_start);
                    holder.setText(R.id.textPhoneTime, "即将开始");
                }
            }
            holder.setText(R.id.textPhoneTitle, data.getVideo_name());
        }

        holder.setOnClickListener(R.id.linearSocial, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WatchLiveActivity.class);
                intent.putExtra("live_id", data.getLive_id());
                mContext.startActivity(intent);
            }
        });
    }

    /*
    * 获取直播在线人数
    * */
    private void getOnlineCount(String room_id, final TextView textOnlineNumber) {

        Map<String, String> param = new HashMap<>();
        param.put("conv_id", room_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    textOnlineNumber.setText(data.getString("result"));
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_ONLINE_COUNT, ggHttpInterface).startGet();
    }
}
