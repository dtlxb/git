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
 * description :债券.
 */
public class BondFragment extends BaseFragment {
    @BindView(R.id.xLayout)
    XLayout xLayout;

    @Override
    public int bindLayout() {
        return R.layout.fragment_bond;
    }

    @Override
    public void doBusiness(Context mContext) {
        commingSoon(xLayout);
    }

    private void commingSoon(XLayout xLayout){
        xLayout.setEmptyText(getString(R.string.str_coming_soon));
        xLayout.setEmptyImage(R.mipmap.img_dev_coming_soon);
        xLayout.setStatus(XLayout.Empty);
    }
}
