package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.VirtualLayoutManager;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

/**
 * author wangjd on 2017/6/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :基于阿里vlayout的行情
 */
public class MarketFragment2 extends BaseFragment {

    @BindView(R.id.rv_market)
    RecyclerView rvMarket;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public int bindLayout() {
        return R.layout.fragment_market2;
    }

    @Override
    public void doBusiness(final Context mContext) {
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
        rvMarket.setLayoutManager(layoutManager);

        final List<LayoutHelper> helpers = new LinkedList<>();

    }
}
