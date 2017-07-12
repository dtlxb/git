package cn.gogoal.im.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.SocialLiveData;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.common.linkUtils.PlayDataStatistics;
import cn.gogoal.im.ui.dialog.InviteAuthDialog;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: description
 */
public class SocialLiveAdapter extends CommonAdapter<SocialLiveData, BaseViewHolder> {

    private FragmentActivity mContext;

    public SocialLiveAdapter(FragmentActivity mContext, List<SocialLiveData> data) {
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
            ImageDisplay.loadImage(mContext, data.getLive_large_img(), imgPcCover);

            //预约
            final CheckBox btnPcOrder = holder.getView(R.id.btnPcOrder);

            //直播状态
            if (data.getLive_status() == 1) {
                //直播中
                btnPcOrder.setVisibility(View.GONE);

                holder.setText(R.id.textPcStatus, "直播中 " + live_time_start);
                holder.setBackgroundRes(R.id.textPcStatus, R.drawable.shape_social_live_status_red);
                holder.setImageResource(R.id.imgPcTime, R.mipmap.social_online_num);
                getOnlineCount(data.getRoom_id(), (TextView) holder.getView(R.id.textPcTime));
            } else {
                //预告
                btnPcOrder.setVisibility(View.VISIBLE);

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
                    btnPcOrder.setVisibility(View.GONE);

                    holder.setText(R.id.textPcStatus, "准备中 " + live_time_start);
                    holder.setText(R.id.textPcTime, "即将开始");
                }

                if (data.getOrder_status() == 0) {
                    btnPcOrder.setChecked(false);
                    btnPcOrder.setText("预约");
                    btnPcOrder.setTextColor(ContextCompat.getColor(mContext, R.color.social_order_text));
                    btnPcOrder.setBackgroundResource(R.drawable.bg_social_live_order_red);
                } else if (data.getOrder_status() == 1) {
                    btnPcOrder.setChecked(true);
                    btnPcOrder.setText("已预约");
                    btnPcOrder.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_666666));
                    btnPcOrder.setBackgroundResource(R.drawable.bg_social_live_order_black);
                } else {
                    btnPcOrder.setVisibility(View.GONE);
                }

                btnPcOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                        if (check) {
                            setOrderLive(data.getLive_id(), btnPcOrder);
                        } else {

                        }
                    }
                });
            }

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

        } else {
            //phone
            holder.setVisible(R.id.linearPhone, true);
            holder.setVisible(R.id.framePhone, true);
            holder.setVisible(R.id.framePc, false);
            holder.setVisible(R.id.linearPc, false);

            RoundedImageView imgAnchorAvatar = holder.getView(R.id.imgAnchorAvatar);
            ImageDisplay.loadCircleImage(mContext, data.getAnchor().getFace_url(), imgAnchorAvatar);
            holder.setText(R.id.textAnchorName, data.getAnchor().getAnchor_name());
            holder.setText(R.id.textAnchorTitle, data.getAnchor().getOrganization() == null ? "--"
                    : data.getAnchor().getOrganization() + " | " + data.getAnchor().getAnchor_position()
                    == null ? "--" : data.getAnchor().getAnchor_position());

            //预约
            final CheckBox btnPhoneOrder = holder.getView(R.id.btnPhoneOrder);

            ImageView imgPhoneCover = holder.getView(R.id.imgPhoneCover);
            ImageDisplay.loadImage(mContext, data.getLive_large_img(), imgPhoneCover);

            //直播状态
            if (data.getLive_status() == 1) {
                //直播中
                btnPhoneOrder.setVisibility(View.GONE);

                holder.setText(R.id.textPhoneStatus, "直播中 " + live_time_start);
                holder.setBackgroundRes(R.id.textPhoneStatus, R.drawable.shape_social_live_status_red);
                holder.setImageResource(R.id.imgPhoneTime, R.mipmap.social_online_num);
                getOnlineCount(data.getRoom_id(), (TextView) holder.getView(R.id.textPhoneTime));
            } else {
                //预告
                btnPhoneOrder.setVisibility(View.VISIBLE);

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
                    btnPhoneOrder.setVisibility(View.GONE);

                    holder.setText(R.id.textPhoneStatus, "准备中 " + live_time_start);
                    holder.setText(R.id.textPhoneTime, "即将开始");
                }

                if (data.getOrder_status() == 0) {
                    btnPhoneOrder.setChecked(false);
                    btnPhoneOrder.setText("预约");
                    btnPhoneOrder.setTextColor(ContextCompat.getColor(mContext, R.color.social_order_text));
                    btnPhoneOrder.setBackgroundResource(R.drawable.bg_social_live_order_red);
                } else if (data.getOrder_status() == 1) {
                    btnPhoneOrder.setChecked(true);
                    btnPhoneOrder.setText("已预约");
                    btnPhoneOrder.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_666666));
                    btnPhoneOrder.setBackgroundResource(R.drawable.bg_social_live_order_black);
                } else {
                    btnPhoneOrder.setVisibility(View.GONE);
                }

                btnPhoneOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                        if (check) {
                            setOrderLive(data.getLive_id(), btnPhoneOrder);
                        } else {

                        }
                    }
                });
            }
            holder.setText(R.id.textPhoneTitle, data.getVideo_name());

            if (data.getAuth() == 1) {
                holder.setVisible(R.id.textPhoneInvite, true);
            } else {
                holder.setVisible(R.id.textPhoneInvite, false);
            }
        }

        holder.setOnClickListener(R.id.linearSocial, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getAuth() == 1) {
                    setInviteAuth(data.getLive_id());
                } else {
                    PlayDataStatistics.enterLiveAuthorize(false, mContext, data.getLive_id(), false);
                }
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

    /*
    * 预约
    * */
    private void setOrderLive(String video_id, final CheckBox btnOrder) {

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("video_id", video_id);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 100) {
                        btnOrder.setChecked(true);
                        btnOrder.setText("已预约");
                        btnOrder.setTextColor(ContextCompat.getColor(mContext, R.color.textColor_666666));
                        btnOrder.setBackgroundResource(R.drawable.bg_social_live_order_black);
                    } else {
                        UIHelper.toast(mContext, "预约失败");
                    }
                } else {
                    UIHelper.toast(mContext, "预约失败");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(mContext, "预约失败");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ORDER_LIVE, ggHttpInterface).startGet();
    }

    /**
     * 邀约
     */
    private void setInviteAuth(final String invite_id) {

        String identifies = SPTools.getString(invite_id, null);

        if (identifies == null) {
            InviteAuthDialog.newInstance("live", invite_id).show(mContext.getSupportFragmentManager());
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
                        PlayDataStatistics.enterLiveAuthorize(false, mContext, invite_id, false);
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
