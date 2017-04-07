package cn.gogoal.im.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

public class WatchBottomFragment extends BaseFragment {

    @BindView(R.id.iv_comment)
    ImageView mIvComment;

    @BindView(R.id.iv_beauty)
    ImageView mIvBeauty;

    @BindView(R.id.iv_camera)
    ImageView mIvCamera;

    @BindView(R.id.iv_flash)
    ImageView mIvFlash;

    @BindView(R.id.player_linear)
    LinearLayout player_linear;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    @BindView(R.id.send_text)
    TextView btnSend;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private RecorderUIClickListener mRecordUIClickListener;


    @Override
    public int bindLayout() {
        return R.layout.fragment_watch_bottom;
    }

    @Override
    public void doBusiness(Context mContext) {

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

    @Override
    public void onResume() {
        super.onResume();

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
                    mIvComment.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static final WatchBottomFragment newInstance() {
        return new WatchBottomFragment();
    }

    @OnClick({R.id.iv_comment, R.id.iv_camera, R.id.iv_beauty, R.id.iv_flash, R.id.send_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_comment: //聊天
                showCommentEditUI();
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

    public void setRecordUIClickListener(RecorderUIClickListener listener) {
        this.mRecordUIClickListener = listener;
    }

    /**
     * 隐藏推流时相关的操作UI（切换摄像头、美颜开关、闪光灯开关）
     */
    public void hideRecordView() {
        mIvBeauty.setVisibility(View.GONE);
        mIvCamera.setVisibility(View.GONE);
        mIvFlash.setVisibility(View.GONE);
        //mIvCallAnchor.setEnabled(true);
    }

    public interface RecorderUIClickListener {

        /**
         * switch comment
         */
        void onSendComment(EditText editText);

        /**
         * switch camera
         *
         * @return current camera id
         */
        int onSwitchCamera();

        /**
         * switch beauty
         *
         * @return true: beauty on , false: beauty off
         */
        boolean onBeautySwitch();

        /**
         * switch flash
         *
         * @return true: flash on, false: flash off;
         */
        boolean onFlashSwitch();
    }
}


