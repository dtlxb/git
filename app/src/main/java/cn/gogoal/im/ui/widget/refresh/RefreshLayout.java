package cn.gogoal.im.ui.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/7/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class RefreshLayout extends PtrFrameLayout {

    /**
     * headerView
     */
    RefreshHeader mHeaderView;

    public RefreshLayout(Context context) {
        super(context);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    /**
     * 初始化view
     */
    private void initView() {
        mHeaderView = new RefreshHeader(getContext());
        setHeaderView(mHeaderView);
        setRatioOfHeaderHeightToRefresh(0.8f);
        addPtrUIHandler(mHeaderView);
//        resistance
        setResistance(2.5f);
    }

}
