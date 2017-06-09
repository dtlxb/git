package cn.gogoal.im.ui.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.BaseIconText;
import cn.gogoal.im.common.AppDevice;

/**
 * Author：Bro0cL on 2016/12/26.
 */
public class PopuMoreMenu {

    private Activity mContext;

    private PopupWindow mPopupWindow;

    private RecyclerView mRecyclerView;

    private View content;

    private PopuMoreAdapter mAdapter;

    private List<BaseIconText<Integer,String>> menuItemList;

    private static final int DEFAULT_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private int popHeight = DEFAULT_HEIGHT;

    private int popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;

//    private boolean showIcon = true;
    private boolean dimBackground = true;
    private boolean needAnimationStyle = true;

    private static final int DEFAULT_ANIM_STYLE = R.style.TRM_ANIM_STYLE;
    private int animationStyle;

    private float alpha = 0.75f;

    public PopuMoreMenu(Activity context) {
        this.mContext = context;
        init();
    }

    private void init() {
        content = LayoutInflater.from(mContext).inflate(
                R.layout.trm_popup_menu,
                new LinearLayout(mContext),
                false);

        mRecyclerView = (RecyclerView) content.findViewById(R.id.trm_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        menuItemList = new ArrayList<>();
        mAdapter = new PopuMoreAdapter(this,menuItemList);
    }

    private PopupWindow getPopupWindow(){
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(content);
        mPopupWindow.setHeight(popHeight);
        mPopupWindow.setWidth(AppDevice.getWidth(mContext)/2);
        if (needAnimationStyle){
            mPopupWindow.setAnimationStyle(animationStyle <= 0 ? DEFAULT_ANIM_STYLE : animationStyle);
        }

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (dimBackground) {
                    setBackgroundAlpha(alpha, 1f, 300);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        return mPopupWindow;
    }

    public PopuMoreMenu setHeight(int height){
        if (height <= 0 && height != RecyclerView.LayoutParams.MATCH_PARENT
                && height != RecyclerView.LayoutParams.WRAP_CONTENT){
            this.popHeight = DEFAULT_HEIGHT;
        }else {
            this.popHeight = height;
        }
        return this;
    }

    public PopuMoreMenu setWidth(int width){
        if (width <= 0 && width != RecyclerView.LayoutParams.MATCH_PARENT){
            this.popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;
        }else {
            this.popWidth = width;
        }
        return this;
    }

//    /**
//     * 是否显示菜单图标
//     * @param show
//     * @return
//     */
//    public PopuMoreMenu showIcon(boolean show){
//        this.showIcon = show;
//        return this;
//    }

    /**
     * 添加单个菜单
     * @param item
     * @return
     */
    public PopuMoreMenu addMenuItem(BaseIconText<Integer,String> item){
        menuItemList.add(item);
        return this;
    }

    /**
     * 添加多个菜单
     * @param list
     * @return
     */
    public PopuMoreMenu addMenuList(List<BaseIconText<Integer,String>> list){
        menuItemList.addAll(list);
        return this;
    }

    /**
     * 是否让背景变暗
     * @param b
     * @return
     */
    public PopuMoreMenu dimBackground(boolean b){
        this.dimBackground = b;
        return this;
    }

    /**
     * 否是需要动画
     * @param need
     * @return
     */
    public PopuMoreMenu needAnimationStyle(boolean need){
        this.needAnimationStyle = need;
        return this;
    }

    /**
     * 设置动画
     * @param style
     * @return
     */
    public PopuMoreMenu setAnimationStyle(int style){
        this.animationStyle = style;
        return this;
    }

    public PopuMoreMenu setOnMenuItemClickListener(OnMenuItemClickListener listener){
        mAdapter.setOnMenuItemClickListener(listener);
        return this;
    }

    public PopuMoreMenu showAsDropDown(View anchor){
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    public PopuMoreMenu showAsDropDown(View anchor, int xoff, int yoff){
        if (mPopupWindow == null){
            getPopupWindow();
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
            if (dimBackground){
                setBackgroundAlpha(1f, alpha, 240);
            }
        }
        return this;
    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    public void dismiss(){
        if (mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }

    public interface OnMenuItemClickListener{
        void onMenuItemClick(int position);
    }
}
