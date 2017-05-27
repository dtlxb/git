package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.PlayerActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.RelaterVideoData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.UIHelper;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

/**
 * Created by dave.
 * Date: 2017/5/26.
 * Desc: 录播相关视频
 */
public class PlayRelatedFragment extends BaseFragment {

    @BindView(R.id.recy_relater)
    RecyclerView recy_relater;
    @BindView(R.id.linearShowError)
    LinearLayout linearShowError;

    private RelaterVideoAdapter adapter;

    private String live_id;

    public static PlayRelatedFragment newInstance(String live_id) {
        PlayRelatedFragment fragment = new PlayRelatedFragment();
        Bundle args = new Bundle();
        args.putString("live_id", live_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_play_related;
    }

    @Override
    public void doBusiness(Context mContext) {

        live_id = getArguments().getString("live_id");

        initRecycleView(recy_relater, null);

        getRelaterVideoInfo();
    }

    /*
    * 获取直播相关视频
    * */
    private void getRelaterVideoInfo() {

        Map<String, String> param = new HashMap<>();
        param.put("video_id", live_id);
        param.put("video_type", "2");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    recy_relater.setVisibility(View.VISIBLE);
                    linearShowError.setVisibility(View.GONE);

                    List<RelaterVideoData> videoDatas = JSONObject.parseArray(String.valueOf(object.getJSONArray("data")), RelaterVideoData.class);
                    adapter = new RelaterVideoAdapter(getContext(), videoDatas);
                    recy_relater.setAdapter(adapter);
                } else {
                    recy_relater.setVisibility(View.GONE);
                    linearShowError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RELATED_VIDEO, ggHttpInterface).startGet();
    }

    class RelaterVideoAdapter extends CommonAdapter<RelaterVideoData, BaseViewHolder> {

        public RelaterVideoAdapter(Context context, List<RelaterVideoData> list) {
            super(R.layout.item_relater_video, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, final RelaterVideoData data, int position) {

            /*holder.setAlpha(R.id.text_playback, (float) 0.5);
            if (data.getType() == 1) {
                holder.setVisible(R.id.relative_player, true);
            } else {
                holder.setVisible(R.id.relative_player, false);
            }*/

            ImageView relater_img = holder.getView(R.id.relater_img);
            ImageDisplay.loadImage(getContext(), data.getVideo_img_url(), relater_img);
            holder.setText(R.id.relater_tittle, data.getVideo_name());
            holder.setText(R.id.relater_play_count, data.getPlay_base() + "次");
            RoundedImageView relater_avatar = holder.getView(R.id.relater_avatar);
            ImageDisplay.loadCircleImage(getContext(), data.getFace_url(), relater_avatar);
            holder.setText(R.id.relater_name, data.getAnchor_name());
            holder.setText(R.id.relater_content, data.getProgramme_name());

            holder.setOnClickListener(R.id.linearRelaterVideo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), PlayerActivity.class);
                    intent.putExtra("live_id", data.getVideo_id());
                    startActivity(intent);
                }
            });
        }
    }
}
