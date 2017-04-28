package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;

/**
 * author wangjd on 2017/4/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :行情Fragment
 */
public class MarketFragment extends BaseFragment {

    @BindView(R.id.tablayout_market)
    TabLayout tablayoutMarket;

    @BindView(R.id.vp_market)
    ViewPager vpMarket;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindArray(R.array.market_tab_top)
    String[] marketTabTitles;

    private ArrayList<Fragment> marketFragments;

    @Override
    public int bindLayout() {
        return R.layout.fragment_market;
    }

    @Override
    public void doBusiness(final Context mContext) {

        HuShenFragment huShenFragment = new HuShenFragment();
        HKFragment hkFragment = new HKFragment();
        FundFragment fundFragment = new FundFragment();
        BondFragment bondFragment = new BondFragment();


        marketFragments = new ArrayList<>();

        marketFragments.add(huShenFragment);
        marketFragments.add(hkFragment);
        marketFragments.add(fundFragment);
        marketFragments.add(bondFragment);

        vpMarket.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return marketFragments.get(position);
            }

            @Override
            public int getCount() {
                return marketFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return marketTabTitles[position];
            }
        });
        vpMarket.setOffscreenPageLimit(3);
        tablayoutMarket.setupWithViewPager(vpMarket);
        tablayoutMarket.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appBarLayout.setExpanded(true,true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
