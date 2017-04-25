package cn.gogoal.im.fragment.stock;

import android.content.Context;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :港股.
 */
public class HKFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        commingSoon(xLayout);
    }

    private void commingSoon(XLayout xLayout) {
        xLayout.setEmptyText(getString(R.string.str_coming_soon));
        xLayout.setEmptyImage(R.mipmap.img_dev_coming_soon);
        xLayout.setStatus(XLayout.Empty);

    }
}
