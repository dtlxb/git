package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.fragment.stock.BondFragment;
import cn.gogoal.im.fragment.stock.FundFragment;
import cn.gogoal.im.fragment.stock.HKFragment;
import cn.gogoal.im.fragment.stock.HuShenFragment;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :行情.
 */
public class NewMarketActivity extends BaseActivity {
    @BindView(R.id.search_market)
    SearchView searchMarket;

    @BindView(R.id.tablayout_market)
    TabLayout tablayoutMarket;

    @BindView(R.id.vp_market)
    ViewPager vpMarket;

    @BindArray(R.array.market_tab_top)
    String[] marketTabTitles;

    @Override
    public int bindLayout() {
        return R.layout.activity_new_market;
    }

    @Override
    public void doBusiness(Context mContext) {

        setMyTitle(getString(R.string.str_market),true);

        final List<Fragment> marketFragments = new ArrayList<>();

        marketFragments.add(new HuShenFragment());
        marketFragments.add(new HKFragment());
        marketFragments.add(new FundFragment());
        marketFragments.add(new BondFragment());

        vpMarket.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
    }
}
