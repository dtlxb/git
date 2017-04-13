package cn.gogoal.im.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.RelaterVideoData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGAPI;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.PlayerUtils.TextAndImage;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.widget.PopupWindowHelper;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

public class WatchBottomFragment extends BaseFragment {

    @BindView(R.id.functionBar)
    LinearLayout functionBar;

    @BindView(R.id.player_linear)
    LinearLayout player_linear;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    @BindView(R.id.send_text)
    TextView btnSend;

    @BindView(R.id.linearPlayerProfiles)
    LinearLayout linearPlayerProfiles;
    @BindView(R.id.linearPlayerRelaterVideo)
    LinearLayout linearPlayerRelaterVideo;
    @BindView(R.id.linearPlayerShotCut)
    LinearLayout linearPlayerShotCut;

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private List<RelaterVideoData> videoDatas;

    private RecorderUIClickListener mRecordUIClickListener;

    private String live_id;
    private JSONObject anchor;
    private int type;

    private PopupWindowHelper anchorHelper;
    private PopupWindowHelper anchorHelperLand;
    private PopupWindowHelper relaterHelper;

    public static final WatchBottomFragment newInstance(String live_id, String anchor) {
        WatchBottomFragment wbf = new WatchBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("live_id", live_id);
        bundle.putString("anchor", anchor);
        wbf.setArguments(bundle);
        return wbf;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_watch_bottom;
    }

    @Override
    public void doBusiness(Context mContext) {

        live_id = getArguments().getString("live_id");
        anchor = JSONObject.parseObject(getArguments().getString("anchor"));

        if (anchor == null) {
            linearPlayerProfiles.setVisibility(View.GONE);
        } else {
            showAnchorProfiles();
            showAnchorProfilesLand();
            linearPlayerProfiles.setVisibility(View.VISIBLE);
        }


        getRelaterVideoInfo();

        //获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEtComment.getText().toString().equals("") || !mEtComment.getText().toString().matches(".*[^ ].*")) {
                    btnSend.setVisibility(View.GONE);
                } else {
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void setActivityRootView(View activityRootView) {
        this.activityRootView = activityRootView;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (type == 0) {
            linearPlayerShotCut.setVisibility(View.VISIBLE);
        } else {
            linearPlayerShotCut.setVisibility(View.GONE);
        }

        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    //键盘弹起
                    KLog.e("监听到软键盘弹起...");
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    //键盘关闭
                    KLog.e("监听到软键盘关闭...");
                    player_linear.setVisibility(View.GONE);
                    functionBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.imgPlayerChat, R.id.imgPlayerProfiles, R.id.imgPlayerRelaterVideo, R.id.imgPlayerShare,
            R.id.imgPlayerShotCut, R.id.imgPlayerClose, R.id.send_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlayerChat: //聊天
                showCommentEditUI();
                break;
            case R.id.imgPlayerProfiles:
                if (AppDevice.isLandscape(getContext())) {
                    anchorHelperLand.showFromRight(v);
                } else {
                    anchorHelper.showFromBottom(v);
                }
                break;
            case R.id.imgPlayerRelaterVideo:
                if (AppDevice.isLandscape(getContext())) {
                    relaterHelper.showFromRight(v);
                } else {
                    relaterHelper.showFromBottom(v);
                }
                break;
            case R.id.imgPlayerShare:
                DialogHelp.showShareDialog(getActivity(), GGAPI.WEB_URL + "/live/share/" + live_id, "http://g1.dfcfw.com/g2/201702/20170216133526.png", "分享", "第一次分享");

                break;
            case R.id.imgPlayerShotCut:
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onswitchCamera();
                }
                break;
            case R.id.imgPlayerClose:
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onFinish();
                }
                break;
            case R.id.send_text:
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onSendComment(mEtComment);
                }
                break;
        }
    }

    /**
     * 显示评论编辑器
     */
    private void showCommentEditUI() {
        player_linear.setVisibility(View.VISIBLE);
        functionBar.setVisibility(View.GONE);
        mEtComment.post(openKeyboardRunnable);
        mEtComment.requestFocus();
    }

    private Runnable openKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            showSoftKeyboard();
        }
    };

    /**
     * 显示软键盘
     */
    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtComment, 0);
    }

    private void showAnchorProfiles() {
        View anchorIntroduction = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anchor_introduction, null);
        anchorHelper = new PopupWindowHelper(anchorIntroduction);

        final ImageView anchor_avatar = (ImageView) anchorIntroduction.findViewById(R.id.anchor_avatar);
        TextView anchor_name = (TextView) anchorIntroduction.findViewById(R.id.anchor_name);
        TextView anchor_position = (TextView) anchorIntroduction.findViewById(R.id.anchor_position);
        final TextView anchor_achieve = (TextView) anchorIntroduction.findViewById(R.id.anchor_achieve);

        ImageDisplay.loadNetImage(getContext(), anchor.getString("face_url"), anchor_avatar);
        anchor_name.setText(anchor.getString("anchor_name"));
        anchor_position.setText(anchor.getString("organization") + " | " + anchor.getString("anchor_position"));

        if (anchor.getString("anchor_introduction") != null) {
            anchor_achieve.setText(anchor.getString("anchor_introduction"));

            final ViewTreeObserver observer = anchor_avatar.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    anchor_avatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int finalHeight = anchor_avatar.getMeasuredHeight();
                    int finalWidth = anchor_avatar.getMeasuredWidth();
                    TextAndImage.makeSpan(finalHeight, finalWidth, anchor_achieve);
                }
            });
        }
    }

    private void showAnchorProfilesLand() {
        View anchorIntroduction = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anchor_introduction_land, null);
        anchorHelperLand = new PopupWindowHelper(anchorIntroduction);

        final ImageView anchor_avatar = (ImageView) anchorIntroduction.findViewById(R.id.anchor_avatar);
        TextView anchor_name = (TextView) anchorIntroduction.findViewById(R.id.anchor_name);
        TextView anchor_position = (TextView) anchorIntroduction.findViewById(R.id.anchor_position);
        final TextView anchor_achieve = (TextView) anchorIntroduction.findViewById(R.id.anchor_achieve);

        ImageDisplay.loadNetImage(getContext(), anchor.getString("face_url"), anchor_avatar);
        anchor_name.setText(anchor.getString("anchor_name"));
        anchor_position.setText(anchor.getString("organization") + " | " + anchor.getString("anchor_position"));

        anchor_achieve.setText(anchor.getString("anchor_introduction"));

        final ViewTreeObserver observer = anchor_avatar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                anchor_avatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int finalHeight = anchor_avatar.getMeasuredHeight();
                int finalWidth = anchor_avatar.getMeasuredWidth();
                TextAndImage.makeSpan(finalHeight, finalWidth, anchor_achieve);
            }
        });
    }

    private void showRelaterVideo() {
        View relaterVideo = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_relater_video, null);
        relaterHelper = new PopupWindowHelper(relaterVideo);

        RecyclerView recy_relater = (RecyclerView) relaterVideo.findViewById(R.id.recy_relater);
        initRecycleView(recy_relater, null);

        recy_relater.setAdapter(new RelaterVideoAdapter(getContext(), videoDatas));
    }

    class RelaterVideoAdapter extends CommonAdapter<RelaterVideoData, BaseViewHolder> {

        public RelaterVideoAdapter(Context context, List<RelaterVideoData> list) {
            super(R.layout.item_relater_video, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, final RelaterVideoData data, int position) {

            holder.setAlpha(R.id.text_playback, (float) 0.5);
            if (data.getType() == 1) {
                holder.setVisible(R.id.relative_player, true);
            } else {
                holder.setVisible(R.id.relative_player, false);
            }
            ImageView relater_img = holder.getView(R.id.relater_img);
            ImageDisplay.loadNetImage(getActivity(), data.getVideo_img_url(), relater_img);
            holder.setText(R.id.relater_tittle, data.getVideo_name());
            holder.setText(R.id.relater_play_count, data.getPlay_base() + "次");
            ImageView relater_avatar = holder.getView(R.id.relater_avatar);
            ImageDisplay.loadCircleNetImage(getActivity(), data.getFace_url(), relater_avatar);
            holder.setText(R.id.relater_name, data.getAnchor_name());
            holder.setText(R.id.relater_content, data.getProgramme_name());
        }
    }

    /*
    * 获取直播相关视频
    * */
    private void getRelaterVideoInfo() {

        Map<String, String> param = new HashMap<>();
        param.put("video_id", live_id);
        param.put("video_type", "1");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    videoDatas = JSONObject.parseArray(String.valueOf(object.getJSONArray("data")), RelaterVideoData.class);
                    if (videoDatas == null) {
                        linearPlayerRelaterVideo.setVisibility(View.GONE);
                    } else {
                        linearPlayerRelaterVideo.setVisibility(View.VISIBLE);
                        showRelaterVideo();
                    }
                } else {
                    linearPlayerRelaterVideo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_RELATED_VIDEO, ggHttpInterface).startGet();
    }

    public void setRecordUIClickListener(RecorderUIClickListener listener) {
        this.mRecordUIClickListener = listener;
    }

    public interface RecorderUIClickListener {

        /**
         * switch comment
         */
        void onSendComment(EditText editText);

        /**
         * switch comment
         */
        int onswitchCamera();

        /**
         * 关闭页面
         */
        void onFinish();
    }
}


