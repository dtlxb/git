package cn.gogoal.im.adapter.base.animator;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.base.LoadMoreView;

/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}