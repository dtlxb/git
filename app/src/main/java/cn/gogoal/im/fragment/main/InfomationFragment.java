package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.fragment.infomation.InfoMyStockTabFragment;
import cn.gogoal.im.fragment.infomation.InfomationTabFragment;
import cn.gogoal.im.fragment.infomation.SevenBy24Fragment;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :资讯tab
 */
public class InfomationFragment extends BaseFragment {

    @BindView(R.id.tab_infomation)
    TabLayout tabInfomation;

    @BindView(R.id.vp_infomation)
    ViewPager vpInfomation;

    @BindArray(R.array.info_arrar)
    String[] infoArrays;

    @Override
    public int bindLayout() {
        return R.layout.fragment_infomation;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("资讯");

        final int[] tabTypes = {InfomationTabFragment.INFOMATION_TYPE_GET_ASK_NEWS,
                InfomationTabFragment.INFOMATION_TYPE_SUN_BUSINESS,
                InfomationTabFragment.INFOMATION_TYPE_PRIVATE_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_SKY_VIEW_POINT,
                InfomationTabFragment.INFOMATION_TYPE_POLICY_DYNAMICS};

        vpInfomation.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? new InfoMyStockTabFragment() : (position == 1 ? new SevenBy24Fragment() :
                        InfomationTabFragment.newInstance(tabTypes[position-2]));
            }

            @Override
            public int getCount() {
                return infoArrays.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return infoArrays[position];
            }
        });
        tabInfomation.setupWithViewPager(vpInfomation);
    }

}
