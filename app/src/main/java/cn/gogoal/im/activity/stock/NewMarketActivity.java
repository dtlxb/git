package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.stock.BondFragment;
import cn.gogoal.im.fragment.stock.FundFragment;
import cn.gogoal.im.fragment.stock.HKFragment;
import cn.gogoal.im.fragment.stock.HuShenFragment;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.fragment.stock.HuShenFragment.REFRESH_TYPE_SWIPEREFRESH;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :行情.
 */
public class NewMarketActivity extends BaseActivity {
    @BindView(R.id.search_market)
    AppCompatTextView searchMarket;

    @BindView(R.id.tablayout_market)
    TabLayout tablayoutMarket;

    @BindView(R.id.vp_market)
    ViewPager vpMarket;

    @BindArray(R.array.market_tab_top)
    String[] marketTabTitles;
    private ArrayList<Fragment> marketFragments;

    @Override
    public int bindLayout() {
        return R.layout.activity_new_market;
    }

    @Override
    public void doBusiness(final Context mContext) {

        final HuShenFragment huShenFragment = new HuShenFragment();
        HKFragment hkFragment = new HKFragment();
        FundFragment fundFragment = new FundFragment();
        BondFragment bondFragment = new BondFragment();

        XTitle.ImageAction refreshAction = new XTitle.ImageAction(getResDrawable(R.drawable.ic_refresh_black_24dp)) {
            @Override
            public void actionClick(View view) {
                UIHelper.toast(mContext, "刷新");
                huShenFragment.setRefreshType(REFRESH_TYPE_SWIPEREFRESH);
            }
        };

        setMyTitle(getString(R.string.str_market),true).addAction(refreshAction);

        marketFragments = new ArrayList<>();

        marketFragments.add(huShenFragment);
        marketFragments.add(hkFragment);
        marketFragments.add(fundFragment);
        marketFragments.add(bondFragment);

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
