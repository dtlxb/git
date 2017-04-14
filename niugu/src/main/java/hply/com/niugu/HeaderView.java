package hply.com.niugu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Lizn on 2015/11/20.
 */
public class HeaderView extends FrameLayout implements PtrUIHandler {
    private View rootView;
    private LinearLayout header_tips, header_stats, header_loading, header_toast;
    private TextView tv_tips, tv_prompt, tv_loading, tv_toast;
    private ImageView img_arrows, img_loading;

    private RotateAnimation mFlipAnimation, mReverseFlipAnimation, mRotateAnimation;
    private AnimationSet animationSet;
    private int mRotateAniTime = 100;

    private String date, dateKey;
    private boolean isToast;

    public HeaderView(Context context) {
        super(context);
        Init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        dateKey = context.getClass().getSimpleName() + "_refresh_date";

        isToast = false;

        rootView = inflate(getContext(), R.layout.header_view_layout, null);
        //header_tips
        header_tips = (LinearLayout) rootView.findViewById(R.id.header_tips);
        tv_tips = (TextView) rootView.findViewById(R.id.tv_tips);
        //header_stats
        header_stats = (LinearLayout) rootView.findViewById(R.id.header_stats);
        tv_prompt = (TextView) rootView.findViewById(R.id.tv_prompt);
        img_arrows = (ImageView) rootView.findViewById(R.id.img_arrows);
        //header_loading
        header_loading = (LinearLayout) rootView.findViewById(R.id.header_loading);
        img_loading = (ImageView) rootView.findViewById(R.id.img_loading);
        tv_loading = (TextView) rootView.findViewById(R.id.tv_loading);
        //header_toast
        header_toast = (LinearLayout) rootView.findViewById(R.id.header_toast);
        tv_toast = (TextView) rootView.findViewById(R.id.tv_toast);

        addView(rootView);

        buildAnimation();

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        date = formatter.format(curDate);
        date = getContext().getSharedPreferences("gogoal_preferences",Context.MODE_PRIVATE).getString(dateKey, date);

        String str = getResources().getString(R.string.pull_refresh) + " 最后更新 " + date;
        tv_prompt.setText(str);
    }

    private void buildAnimation() {
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(mRotateAniTime);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(mRotateAniTime);
        mReverseFlipAnimation.setFillAfter(true);

        mRotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(mRotateAniTime * 20);
        mRotateAnimation.setRepeatCount(3000);
        mRotateAnimation.setFillAfter(true);

        //弹框动画
        animationSet = new AnimationSet(false);
        Animation anim1 = new ScaleAnimation(0f, 1.1f, 0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(200);
        animationSet.addAnimation(anim1);

        Animation anim2 = new ScaleAnimation(1.1f, 1, 1.1f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(300);
        animationSet.addAnimation(anim2);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        recover();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss",Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        date = formatter.format(curDate);
        SharedPreferences preferences=HeaderView.this.getContext().getSharedPreferences("gogoal_preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(dateKey, date).apply();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                if (img_arrows != null) {
                    img_arrows.clearAnimation();
                    img_arrows.startAnimation(mReverseFlipAnimation);
                    if (date != null) {
                        String str = getResources().getString(R.string.pull_refresh) + " 最后更新 " + date;
                        tv_prompt.setText(str);
                    } else {
                        tv_prompt.setText(R.string.pull_refresh);
                    }
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                if (img_arrows != null) {
                    img_arrows.clearAnimation();
                    img_arrows.startAnimation(mFlipAnimation);
                    if (date != null) {
                        String str = getResources().getString(R.string.release_refresh) + " 最后更新 " + date;
                        tv_prompt.setText(str);
                    } else {
                        tv_prompt.setText(R.string.release_refresh);
                    }
                }
            }
        }
    }

    public void recover() {
        header_tips.setVisibility(GONE);
        header_stats.setVisibility(VISIBLE);
        header_loading.setVisibility(GONE);
        header_toast.setVisibility(GONE);

        img_arrows.clearAnimation();
        img_arrows.startAnimation(mReverseFlipAnimation);

        String str = getResources().getString(R.string.pull_refresh) + " 最后更新 " + date;
        tv_prompt.setText(str);

        img_loading.clearAnimation();
    }

    public void over() {
        if (isToast) {
            header_toast.setVisibility(VISIBLE);
            tv_toast.clearAnimation();
            tv_toast.startAnimation(animationSet);
        } else {
            header_tips.setVisibility(VISIBLE);
        }
        header_stats.setVisibility(GONE);
        header_loading.setVisibility(GONE);
    }

    public void loading() {
        header_tips.setVisibility(GONE);
        header_stats.setVisibility(GONE);
        header_loading.setVisibility(VISIBLE);

        img_loading.startAnimation(mRotateAnimation);
    }

    public void setFontColor(int color) {
        tv_tips.setTextColor(color);
        tv_prompt.setTextColor(color);
        tv_loading.setTextColor(color);
        tv_toast.setTextColor(color);
    }

    public void setPullImage(int resid) {
        img_arrows.setImageResource(resid);
    }

    public void setLoadingImage(int resid) {
        img_loading.setImageResource(resid);
    }

    public void setToastMessge(String str) {
        tv_toast.setText(str);
        isToast = true;
    }
}
