package cn.gogoal.im.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.PlayerActivity;
import cn.gogoal.im.activity.WatchLiveActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.LiveListItemBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.ui.dialog.InviteAuthDialog;

/**
 * author wangjd on 2017/5/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :直播、录播视频列表适配器
 */
public class LiveListAdapter extends CommonAdapter<LiveListItemBean, BaseViewHolder> {

    private int imgBgSize;

    private RequestOptions options;

    private FragmentActivity context;

    public LiveListAdapter(FragmentActivity context, List<LiveListItemBean> data) {
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

    @SuppressLint("SetTextI18n")

    @Override
    protected void convert(BaseViewHolder holder, final LiveListItemBean data, int position) {

        int liveStatus = data.getLive_status();


        ImageView ivLiveBg = holder.getView(R.id.iv_live_list_bg);
        TextView tvStartTime = holder.getView(R.id.tv_item_live_list_start_time);
        TextView tvStartTimeDistance = holder.getView(R.id.tv_item_live_list_count_down);
        TextView tvLiveListOrder = holder.getView(R.id.tv_live_list_order);//最复杂的按钮

        ViewGroup.LayoutParams params = ivLiveBg.getLayoutParams();
        params.width = imgBgSize;
        params.height = 11 * imgBgSize / 20;
        ivLiveBg.setLayoutParams(params);

        Glide.with(context)
                .load(data.getLiveImgBg())
                .apply(options)
                .into(ivLiveBg);

        holder.setText(R.id.tv_tv_item_live_list_title, data.getTitle());

        holder.setImageUrl(context, R.id.iv_live_list_avatar, data.getAnchorAvatar());

        holder.setText(R.id.tv_item_live_list_anchor_name, data.getAnchorName());

        holder.setText(R.id.tv_item_live_list_programme_name, data.getProgramme_name());

        holder.setVisible(R.id.tv_item_live_list_count_down,
                data.getLive_status() != -1);

        Drawable leftDrawable = ContextCompat.getDrawable(context, data.getLive_status() == 0 ? R.mipmap.social_time : R.mipmap.social_online_num);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        tvStartTimeDistance.setCompoundDrawables(leftDrawable, null, null, null);

        tvLiveListOrder.setBackgroundResource(liveStatus == -1 ? android.R.color.transparent : R.drawable.bg_social_live_order_red);

        tvLiveListOrder.setVisibility(
                data.getLive_status() == -1 ?View.VISIBLE:
                        (data.getLive_status()==1?View.VISIBLE:
                                (CalendarUtils.isPassNow(data.getStartTime())?View.GONE:
                                        View.VISIBLE)));

        Drawable orderDrawable=ContextCompat.getDrawable(context,R.mipmap.img_social_online_num_gray);
        orderDrawable.setBounds(0,0,orderDrawable.getMinimumWidth(),orderDrawable.getMinimumHeight());
        tvLiveListOrder.setCompoundDrawables(data.getLive_status()==-1?orderDrawable:null,null,null,null);
        tvLiveListOrder.setCompoundDrawablePadding(AppDevice.dp2px(context, 2));
        tvLiveListOrder.setText(data.getLive_status() == -1 ? data.getPlayerCount() : (data.isNeedOrder() ? "预约" : "已预约"));
        tvLiveListOrder.setTextColor(data.getLive_status() == -1 ?
                getResColor(R.color.textColor_999999) : getResColor(R.color.colorPrimary));

        if (liveStatus == 0) {           //预告
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_yellow);

            tvStartTime.setText((CalendarUtils.isPassNow(data.getStartTime()) ? "准备中 " : "预告中 ") +
                    CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getStartTime()));
            tvStartTimeDistance.setText($(data.getStartTime()));

        } else if (liveStatus == -1) {   //录播
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_gray);
            tvStartTime.setText("回放 " + CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getStartTime()));

        } else {                                    //直播中
            tvStartTime.setBackgroundResource(R.drawable.shape_social_live_status_red);
            tvStartTime.setText("直播中 " + CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getStartTime()));

            tvStartTimeDistance.setText(data.getPlayerCount());


        }

        holder.setVisible(R.id.tv_live_invite, !data.isHavePermissions());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isHavePermissions()) {//不需要邀约
                    Intent intent = new Intent(context, data.getLive_status()==-1?PlayerActivity.class:WatchLiveActivity.class);
                    intent.putExtra("live_id", data.getLive_id());
                    context.startActivity(intent);
                } else {//需要邀约
                    setInviteAuth(data.getLive_id());
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
            InviteAuthDialog.newInstance("live", invite_id).show(context.getSupportFragmentManager());
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("video_id", invite_id);
        param.put("identifies", identifies);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 101) {
                        Intent intent = new Intent(context, WatchLiveActivity.class);
                        intent.putExtra("live_id", invite_id);
                        context.startActivity(intent);
                    } else {
                        InviteAuthDialog.newInstance("video", invite_id).show(context.getSupportFragmentManager());
                    }
                } else {
                    InviteAuthDialog.newInstance("video", invite_id).show(context.getSupportFragmentManager());
                }
            }

            @Override
            public void onFailure(String msg) {
                InviteAuthDialog.newInstance("video", invite_id).show(context.getSupportFragmentManager());
            }
        };
        new GGOKHTTP(param, GGOKHTTP.VALIDATE_IDENTIFIES, ggHttpInterface).startGet();
    }

    private String $(String startTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date parse = format.parse(startTime, new ParsePosition(0));

        long distance = parse.getTime() - System.currentTimeMillis();


        if (distance > 86400 * 1000) {
            return "距离开始 " + ((distance) / (86400 * 1000)) + "天";
        } else if (distance > 3600 * 1000) {
            return "距离开始 " + (distance / (3600 * 1000)) + "小时";
        } else {
            return "即将开始";
        }
    }
}
