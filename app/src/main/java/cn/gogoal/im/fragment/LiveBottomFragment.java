package cn.gogoal.im.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;


public class LiveBottomFragment extends BaseFragment {

    @BindView(R.id.iv_beauty)
    ImageView mIvBeauty;
    @BindView(R.id.iv_camera)
    ImageView mIvCamera;
    @BindView(R.id.iv_flash)
    ImageView mIvFlash;

    private RecorderUIClickListener mUIClickListener;

    @Override
    public int bindLayout() {
        return R.layout.fragment_live_bottom;
    }

    @Override
    public void doBusiness(Context mContext) {
        mIvBeauty.setActivated(true);
    }

    public static final LiveBottomFragment newInstance() {
        return new LiveBottomFragment();
    }

    @OnClick({R.id.iv_camera, R.id.iv_beauty, R.id.iv_flash})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                if (mUIClickListener != null) {
                    mUIClickListener.onSwitchCamera();
                }
                break;
            case R.id.iv_beauty:
                if (mUIClickListener != null) {
                    mIvBeauty.setActivated(mUIClickListener.onBeautySwitch());
                }
                break;
            case R.id.iv_flash:
                if (mUIClickListener != null) {
                    mUIClickListener.onFlashSwitch();
                }
                break;
        }
    }

    public void setRecorderUIClickListener(RecorderUIClickListener listener) {
        this.mUIClickListener = listener;
    }

    public void setBeautyUI(boolean beautyOn) {
        if (mIvBeauty != null) {
            mIvBeauty.setActivated(beautyOn);
        }
    }

    public interface RecorderUIClickListener {
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
