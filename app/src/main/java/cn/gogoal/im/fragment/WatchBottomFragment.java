package cn.gogoal.im.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.roundimage.roundImage.RoundedImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.linkUtils.PlayDataStatistics;
import cn.gogoal.im.ui.widget.PopupWindowHelper;

public class WatchBottomFragment extends BaseFragment {

    @BindView(R.id.functionBar)
    RelativeLayout functionBar;

    @BindView(R.id.player_linear)
    LinearLayout player_linear;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    @BindView(R.id.send_text)
    TextView btnSend;

    @BindView(R.id.linearPlayerChat)
    LinearLayout linearPlayerChat;
    @BindView(R.id.linearPlayerProfiles)
    LinearLayout linearPlayerProfiles;
    @BindView(R.id.linearPlayerShotCut)
    LinearLayout linearPlayerShotCut;
    @BindView(R.id.linearPlayerFullScreen)
    LinearLayout linearPlayerFullScreen;

    /*//Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;*/

    private RecorderUIClickListener mRecordUIClickListener;

    private String live_id;
    private JSONObject anchor;
    private String introduction_img;
    private String introduction;
    private String video_img_url;
    private int type;

    private PopupWindowHelper anchorHelper;
    private PopupWindowHelper anchorHelperLand;

    public static final WatchBottomFragment newInstance(
            String live_id, String anchor, String introduction_img, String introduction, String video_img_url) {
        WatchBottomFragment wbf = new WatchBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("live_id", live_id);
        bundle.putString("anchor", anchor);
        bundle.putString("introduction_img", introduction_img);
        bundle.putString("introduction", introduction);
        bundle.putString("video_img_url", video_img_url);
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
        introduction_img = getArguments().getString("introduction_img");
        introduction = getArguments().getString("introduction");
        video_img_url = getArguments().getString("video_img_url");

        if (anchor == null) {
            linearPlayerProfiles.setVisibility(View.GONE);
        } else {
            showAnchorProfiles();
            showAnchorProfilesLand();
            linearPlayerProfiles.setVisibility(View.VISIBLE);
        }

        /*//获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;*/

        mEtComment.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);

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
        //this.activityRootView = activityRootView;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 显示推流时相关的操作UI（切换摄像头）
     */
    public void showCameraView() {
        linearPlayerShotCut.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏推流时相关的操作UI（切换摄像头）
     */
    public void hideCameraView() {
        linearPlayerShotCut.setVisibility(View.GONE);
    }

    /**
     * 显示推流时相关的操作UI（切换横竖屏）
     */
    public void showFullScreen() {
        linearPlayerFullScreen.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏推流时相关的操作UI（切换横竖屏）
     */
    public void hideFullScreen() {
        linearPlayerFullScreen.setVisibility(View.GONE);
    }

    /**
     * 显示推流时相关的操作UI（显隐聊天）
     */
    public void showChatView() {
        linearPlayerChat.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏推流时相关的操作UI（显隐聊天）
     */
    public void hideChatView() {
        linearPlayerChat.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (type == 0) {
            linearPlayerShotCut.setVisibility(View.VISIBLE);
            linearPlayerFullScreen.setVisibility(View.GONE);
        } else {
            linearPlayerShotCut.setVisibility(View.GONE);
            linearPlayerFullScreen.setVisibility(View.VISIBLE);
        }

        /*//添加layout大小发生改变监听器
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
        });*/
    }

    @OnClick({R.id.imgPlayerChat, R.id.linearPlayerFullScreen, R.id.imgPlayerProfiles,
            R.id.imgPlayerShare, R.id.imgPlayerShotCut, R.id.imgPlayerClose, R.id.send_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlayerChat: //聊天
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onOpenComment();
                }
                break;
            case R.id.linearPlayerFullScreen: //全屏
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onSwitchFullScreen();
                }
                break;
            case R.id.imgPlayerProfiles: //主播介绍
                if (AppDevice.isLandscape(getContext())) {
                    anchorHelperLand.showFromRight(v);
                } else {
                    anchorHelper.showFromBottom(v);
                }
                break;
            case R.id.imgPlayerShare: //分享
                PlayDataStatistics.getStatisticalData(getContext(), "1", live_id, "2", "2");
                DialogHelp.showShareDialog(getActivity(), AppConst.GG_LIVE_SHARE + live_id + "?live", "http://g1.dfcfw.com/g2/201702/20170216133526.png", "分享", "第一次分享");
                break;
            case R.id.imgPlayerShotCut: //切摄像头
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onSwitchCamera();
                }
                break;
            case R.id.imgPlayerClose: //关闭
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
    public void showCommentEditUI() {
        player_linear.setVisibility(View.VISIBLE);
        functionBar.setVisibility(View.GONE);
        mEtComment.post(openKeyboardRunnable);
        mEtComment.requestFocus();
    }

    /**
     * 隐藏评论编辑器
     */
    public void hideCommentEditUI() {
        hideSoftKeyboard();
        mEtComment.clearFocus();
        player_linear.setVisibility(View.GONE);
        functionBar.setVisibility(View.VISIBLE);
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

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtComment.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mOnGlobalLayoutListener.hasShowInputMethod = false;
    }

    private InputMethodUIListener mOnGlobalLayoutListener = new InputMethodUIListener();

    /**
     * 通过对比RootView的height和windowVisibleDisplayFrame的bottom来计算root view的不可见高度
     * 如果不可见高度是0说明键盘处于隐藏的状态，反之属于弹出的状态
     */
    class InputMethodUIListener implements ViewTreeObserver.OnGlobalLayoutListener {
        boolean hasShowInputMethod = false;

        @Override
        public void onGlobalLayout() {
            Rect rootRect = new Rect();
            View view = getActivity().getWindow().getDecorView();
            view.getWindowVisibleDisplayFrame(rootRect);

            int rootInvisibleHeight = view.getRootView().getHeight() - rootRect.bottom;

            if (hasShowInputMethod && rootInvisibleHeight == 0) {//软件盘隐藏
                hideCommentEditUI();
                hasShowInputMethod = false;
            } else if (rootInvisibleHeight > 0) {
                hasShowInputMethod = true;
            }
        }
    }

    private void showAnchorProfiles() {
        View anchorIntroduction = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anchor_introduction, null);
        anchorHelper = new PopupWindowHelper(anchorIntroduction);

        final RoundedImageView anchor_avatar = (RoundedImageView) anchorIntroduction.findViewById(R.id.anchor_avatar);
        TextView anchor_name = (TextView) anchorIntroduction.findViewById(R.id.anchor_name);
        TextView anchor_position = (TextView) anchorIntroduction.findViewById(R.id.anchor_position);
        final TextView anchor_achieve = (TextView) anchorIntroduction.findViewById(R.id.anchor_achieve);
        ImageView live_avatar = (ImageView) anchorIntroduction.findViewById(R.id.live_avatar);
        TextView live_achieve = (TextView) anchorIntroduction.findViewById(R.id.live_achieve);

        ImageDisplay.loadCircleImage(getContext(), anchor.getString("face_url"), anchor_avatar);
        anchor_name.setText(anchor.getString("anchor_name"));

        String organization = anchor.getString("organization");
        String position = anchor.getString("anchor_position");
        anchor_position.setText(organization != null ? organization : "--" + " | "
                + position != null ? position : "--");

        String anchor_introduction = anchor.getString("anchor_introduction");
        anchor_achieve.setText(anchor_introduction != null ? anchor_introduction
                : getString(R.string.play_introduction_null));

        ImageDisplay.loadImage(getContext(), introduction_img != null ? introduction_img : video_img_url, live_avatar);

        live_achieve.setText(introduction != null ? introduction
                : getString(R.string.play_introduction_null));
    }

    private void showAnchorProfilesLand() {
        View anchorIntroduction = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anchor_introduction_land, null);
        anchorHelperLand = new PopupWindowHelper(anchorIntroduction);

        final RoundedImageView anchor_avatar = (RoundedImageView) anchorIntroduction.findViewById(R.id.anchor_avatar);
        TextView anchor_name = (TextView) anchorIntroduction.findViewById(R.id.anchor_name);
        TextView anchor_position = (TextView) anchorIntroduction.findViewById(R.id.anchor_position);
        final TextView anchor_achieve = (TextView) anchorIntroduction.findViewById(R.id.anchor_achieve);
        ImageView live_avatar = (ImageView) anchorIntroduction.findViewById(R.id.live_avatar);
        TextView live_achieve = (TextView) anchorIntroduction.findViewById(R.id.live_achieve);

        ImageDisplay.loadCircleImage(getContext(), anchor.getString("face_url"), anchor_avatar);
        anchor_name.setText(anchor.getString("anchor_name"));

        String organization = anchor.getString("organization");
        String position = anchor.getString("anchor_position");
        anchor_position.setText(organization != null ? organization : "--" + " | "
                + position != null ? position : "--");

        String anchor_introduction = anchor.getString("anchor_introduction");
        anchor_achieve.setText(anchor_introduction != null ? anchor_introduction
                : getString(R.string.play_introduction_null));

        ImageDisplay.loadImage(getContext(), introduction_img != null ? introduction_img : video_img_url, live_avatar);

        live_achieve.setText(introduction != null ? introduction
                : getString(R.string.play_introduction_null));
    }

    public void setRecordUIClickListener(RecorderUIClickListener listener) {
        this.mRecordUIClickListener = listener;
    }

    public interface RecorderUIClickListener {

        /**
         * open comment
         */
        void onOpenComment();

        /**
         * switch comment
         */
        void onSendComment(EditText editText);

        /**
         * switch fullscreen
         */
        void onSwitchFullScreen();

        /**
         * switch comment
         */
        int onSwitchCamera();

        /**
         * 关闭页面
         */
        void onFinish();
    }
}


