package cn.gogoal.im.fragment;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

public class WatchBottomFragment extends BaseFragment {

    @BindView(R.id.iv_comment)
    ImageView mIvComment;

    @BindView(R.id.iv_call_anchor)
    ImageView mIvCallAnchor;

    @BindView(R.id.iv_beauty)
    ImageView mIvBeauty;

    @BindView(R.id.iv_camera)
    ImageView mIvCamera;

    @BindView(R.id.iv_flash)
    ImageView mIvFlash;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    private LiveBottomFragment.RecorderUIClickListener mRecordUIClickListener;


    @Override
    public int bindLayout() {
        return R.layout.fragment_watch_bottom;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    public static final WatchBottomFragment newInstance() {
        return new WatchBottomFragment();
    }

    @OnClick({R.id.iv_comment, R.id.iv_call_anchor, R.id.iv_camera, R.id.iv_beauty, R.id.iv_flash})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_comment: //聊天
                showCommentEditUI();
                break;
            case R.id.iv_call_anchor: //连麦
                break;
            case R.id.iv_camera:
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onSwitchCamera();
                }
                break;
            case R.id.iv_beauty:
                if (mRecordUIClickListener != null) {
                    mIvBeauty.setActivated(mRecordUIClickListener.onBeautySwitch());
                }
                break;
            case R.id.iv_flash:
                if (mRecordUIClickListener != null) {
                    mRecordUIClickListener.onFlashSwitch();
                }
                break;
        }
    }

    /**
     * 显示评论编辑器
     */
    private void showCommentEditUI() {
        mEtComment.setVisibility(View.VISIBLE);
        mIvComment.setVisibility(View.GONE);
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

    public void setRecordUIClickListener(LiveBottomFragment.RecorderUIClickListener listener) {
        this.mRecordUIClickListener = listener;
    }

    /**
     * 隐藏推流时相关的操作UI（切换摄像头、美颜开关、闪光灯开关）
     */
    public void hideRecordView() {
        mIvBeauty.setVisibility(View.GONE);
        mIvCamera.setVisibility(View.GONE);
        mIvFlash.setVisibility(View.GONE);
        mIvCallAnchor.setEnabled(true);
    }
}


