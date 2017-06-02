package cn.gogoal.im.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;

/**
 * Created by dave.
 * Date: 2017/5/26.
 * Desc: 录播主播介绍
 */
public class PlayIntroduceFragment extends BaseFragment {

    //主播头像
    @BindView(R.id.anchor_avatar)
    RoundedImageView anchor_avatar;
    //来自机构
    @BindView(R.id.anchor_name)
    TextView anchor_name;
    //主播称谓
    @BindView(R.id.anchor_position)
    TextView anchor_position;
    //主播介绍
    @BindView(R.id.anchor_achieve)
    TextView anchor_achieve;
    //视频图片
    @BindView(R.id.live_avatar)
    ImageView live_avatar;
    //视频介绍
    @BindView(R.id.live_achieve)
    TextView live_achieve;

    private String playInfo;

    public static PlayIntroduceFragment newInstance(String playInfo) {
        PlayIntroduceFragment fragment = new PlayIntroduceFragment();
        Bundle args = new Bundle();
        args.putString("playInfo", playInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_play_introduce;
    }

    @Override
    public void doBusiness(Context mContext) {

        playInfo = getArguments().getString("playInfo");
        JSONObject data = JSONObject.parseObject(playInfo);
        JSONObject anchor = data.getJSONObject("anchor");

        ImageDisplay.loadCircleImage(getContext(), anchor.getString("face_url"), anchor_avatar);

        anchor_name.setText(anchor.getString("anchor_name"));

        String organization = anchor.getString("organization");
        String position = anchor.getString("anchor_position");
        anchor_position.setText(organization != null ? organization : "--" + " | "
                + position != null ? position : "--");

        String anchor_introduction = anchor.getString("anchor_introduction");
        anchor_achieve.setText(anchor_introduction != null
                ? anchor_introduction : getString(R.string.play_introduction_null));

        ImageDisplay.loadImage(getContext(), data.getString("introduction_img") != null
                ? data.getString("introduction_img") : data.getString("video_img_url"), live_avatar);

        live_achieve.setText(anchor.getString("introduction") != null
                ? anchor.getString("introduction") : getString(R.string.play_introduction_null));
    }
}
